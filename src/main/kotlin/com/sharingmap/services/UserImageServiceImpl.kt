package com.sharingmap.services

import aws.smithy.kotlin.runtime.net.Url
import com.sharingmap.repositories.*
import org.springframework.stereotype.Service
import java.util.*
import aws.sdk.kotlin.services.s3.presigners.*
import aws.sdk.kotlin.services.s3.S3Client
import aws.sdk.kotlin.services.s3.model.PutObjectRequest
import com.sharingmap.entities.*
import kotlinx.coroutines.runBlocking
import org.springframework.dao.DataAccessException
import org.springframework.transaction.TransactionException
import kotlin.time.Duration.Companion.hours

@Service
class UserImageServiceImpl(private val imageRepository: UserImageRepository) : UserImageService {
    val REGION = "ru-central1"
    val BUCKET = "sharing-map-test"
    val ENDPOINT_URL = Url.parse("storage.yandexcloud.net")

    override fun getPresignedUrlAndReplace(objectId: UUID): String? {
        val s3Client = runBlocking { S3Client.fromEnvironment { region = REGION; endpointUrl = ENDPOINT_URL } }
        try {
            val oldImages = imageRepository.findAllByEntityId(objectId)
            if (oldImages.size > 1) {
                return null;
            }
            val newImage = UserImageEntity(
                id = objectId,
                entity = UserEntity(id = objectId, username = "", password = "", email = "", role = Role.ROLE_USER)
            )
            if (oldImages.size == 0) {
                imageRepository.save(newImage)
            }
            val unsignedRequest = runBlocking {
                PutObjectRequest {
                    bucket = BUCKET
                    key = "$objectId/$objectId";
                }.presign(s3Client.config, 1.hours)
            }
            return unsignedRequest.url.toString()
        } catch (ex: DataAccessException) {
            throw RuntimeException("Error occurred while creating the item.", ex)
        } catch (ex: TransactionException) {
            throw RuntimeException("Transaction failed while creating the item.", ex)
        } catch (ex: Exception) {
            throw RuntimeException("Unprocessed exception", ex)
        }
    }
    override fun setImageUploaded(imageId: UUID): Boolean {
//        val result = imageRepository.setImageUploaded(imageId)
//        return result == 1
        return true
    }
}
