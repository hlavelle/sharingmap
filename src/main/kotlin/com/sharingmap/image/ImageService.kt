package com.sharingmap.image

import aws.sdk.kotlin.services.s3.S3Client
import aws.sdk.kotlin.services.s3.model.PutObjectRequest
import aws.sdk.kotlin.services.s3.presigners.presign
import aws.smithy.kotlin.runtime.net.Url
import kotlinx.coroutines.runBlocking
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.dao.DataAccessException
import org.springframework.transaction.TransactionException
import java.util.*
import kotlin.time.Duration.Companion.hours

@ConfigurationProperties(prefix = "cloud.storage")
abstract class ImageService<E> {
    private val regionConfig = "ru-central1"
    private val bucketConfig = "sharing-map-test"
    private val endpointUrlConfig = "storage.yandexcloud.net"

    fun getPresignedUrls(objectId: UUID, count: Int): List<String> {
        val result: MutableList<String> = mutableListOf()
        val s3Client = runBlocking { S3Client.fromEnvironment { region = regionConfig; endpointUrl =  Url.parse(endpointUrlConfig)}}
        for (i in 0 until count) {
            try {
                val savedImageId = saveImageAndGetId(objectId)
                val unsignedRequest = runBlocking {
                    PutObjectRequest {
                    bucket = bucketConfig
                    key = getImagePath(objectId, savedImageId);
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

    fun setImageUploaded(imageId: UUID): Boolean {
        return true;
    }

    fun generateImageUrl(image: E): String {
        return bucketConfig + "." + endpointUrlConfig + "/" + getImagePath(image)
    }

    abstract fun saveImageAndGetId(objectId: UUID): UUID
    abstract fun getImages(objectId: UUID): List<E>
    abstract fun getImagePath(image: E): String
    abstract fun toImageDto(image: E): ImageDto
    abstract fun getImagePath(itemId: UUID, imageId: UUID): String

}
