package com.sharingmap.image

import com.sharingmap.user.UserEntity
import com.sharingmap.user.UserService
import jakarta.validation.constraints.Min
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.*


@RestController
class ImageController (
    private val imageService: ImageService<ItemImageEntity>,
    private val userImageService: ImageService<UserImageEntity>,
    private val userService: UserService
){
    @GetMapping("/{itemId}/images")
    fun getImagesByItem(@PathVariable(value = "itemId") @Min(1) itemId: UUID): ResponseEntity<Any> {
        return try {

            val items = imageService.getImages(itemId)
            if (items.isEmpty()) {
                val errorResponse = mapOf("error" to "No images are found for item ID: $itemId")
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse)
            } else {
                ResponseEntity.ok(items)
            }
        } catch (ex: NoSuchElementException) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        }
    }

    @GetMapping("/{itemId}/image/urls")
    fun getImagesUrls(
        @PathVariable(value = "itemId") itemId: UUID,
        @RequestParam(value = "count", defaultValue = "1") @Min(1) count: Int,
    ): ResponseEntity<Any> {
        return try {
            val urls = imageService.getPresignedUrls(itemId, count)
            if (urls.isEmpty()) {
                val errorResponse = mapOf("error" to "Failed to get urls from s3")
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse)
            } else {
                ResponseEntity.ok(urls)
            }
        } catch (ex: NoSuchElementException) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        }
    }

    @GetMapping("/{imageId}/image/success")
    fun getImagesUrls(
        @PathVariable(value = "imageId") imageId: UUID,
    ): ResponseEntity<Any> {
        val result = imageService.setImageUploaded(imageId)
        if (!result) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("failed to save $imageId");
        }
        return ResponseEntity.ok(null)
    }


    @GetMapping("/user/image/urls")
    fun getUserImagesUrl(): ResponseEntity<Any> {
        return try {
            if (!SecurityContextHolder.getContext().authentication.isAuthenticated) {
                ResponseEntity.badRequest()
            }
            val user = SecurityContextHolder.getContext().authentication.principal as UserEntity
            if (user.id == null) {
                ResponseEntity.notFound()
            }
            val urls = user.id?.let { userImageService.getPresignedUrls(it, 1) }
            val url = urls?.first()
            if (url?.isEmpty() == true) {
                val errorResponse = mapOf("error" to "Failed to get urls from s3")
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse)
            } else {
                val result: MutableList<String> = mutableListOf()
                result.add(url!!)
                ResponseEntity.ok(result)
            }
        } catch (ex: NoSuchElementException) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        }
    }

}