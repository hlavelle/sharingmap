package com.sharingmap.image

import aws.smithy.kotlin.runtime.net.Url
import org.springframework.stereotype.Service
import java.util.*
import aws.sdk.kotlin.services.s3.presigners.*
import aws.sdk.kotlin.services.s3.S3Client
import aws.sdk.kotlin.services.s3.model.PutObjectRequest
import com.sharingmap.item.ItemEntity
import com.sharingmap.item.State
import kotlinx.coroutines.runBlocking
import org.springframework.dao.DataAccessException
import org.springframework.transaction.TransactionException
import kotlin.time.Duration.Companion.hours

@Service
class ItemImageServiceImpl(private val imageRepository: ItemImageRepository) : ItemImageService {
    val REGION = "ru-central1"
    val BUCKET = "sharing-map-test"
    val ENDPOINT_URL = Url.parse("storage.yandexcloud.net")

    override fun getPresignedUrls(itemId: UUID, count: Int): List<String> {
        var result: MutableList<String> = mutableListOf()
        val s3Client = runBlocking { S3Client.fromEnvironment { region = REGION; endpointUrl = ENDPOINT_URL}}
        for (i in 0 until count) {
            val newImage = ItemImageEntity(entity = ItemEntity(id = itemId, state = State.ACTIVE))

            try {
                val savedImage = imageRepository.save(newImage)

                val unsignedRequest = runBlocking {PutObjectRequest {
                    bucket = BUCKET
                    key = "$itemId/${savedImage.id}";
                }.presign(s3Client.config, 1.hours) }
                result.add(unsignedRequest.url.toString())

            } catch (ex: DataAccessException) {
                throw RuntimeException("Error occurred while creating the item.", ex)
            } catch (ex: TransactionException) {
                throw RuntimeException("Transaction failed while creating the item.", ex)
            } catch (ex: Exception) {
                throw RuntimeException("Unprocessed exception", ex)
            }
        }
        return result
    }

    override fun getItemImages(itemId: UUID): List<ItemImageEntity> {
        return imageRepository.findAllByEntityId(itemId)
    }

    override fun setImageUploaded(imageId: UUID): Boolean {
//        val result = imageRepository.setImageUploaded(imageId)
//        return result == 1
        return true
    }
}

