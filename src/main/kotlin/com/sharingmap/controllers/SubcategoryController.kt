package com.sharingmap.controllers

import com.sharingmap.entities.CategoryEntity
import com.sharingmap.entities.SubcategoryEntity
import com.sharingmap.services.CategoryService
import com.sharingmap.services.SubcategoryService
import org.springframework.web.bind.annotation.*

@RestController
class SubcategoryController(private val subcategoryService: SubcategoryService) {

    @GetMapping("/subcategories/{id}")
    fun getSubcategoryById(@PathVariable id: Long): SubcategoryEntity {
        return subcategoryService.getSubcategoryById(id)
    }

    @GetMapping("/subcategories")
    fun getAllSubcategories(): List<SubcategoryEntity> {
        return subcategoryService.getAllSubcategories()
    }

    @PostMapping("/subcategories")
    fun createSubcategory(@RequestBody subcategory: SubcategoryEntity) {
        subcategoryService.createSubcategory(subcategory)
    }

    @DeleteMapping("/subcategories/{id}")
    fun deleteSubcategory(@PathVariable id: Long) {
        subcategoryService.deleteSubcategory(id)
    }

    @PutMapping("/subcategories/{id}")
    fun updateSubcategory(@PathVariable id: Long, @RequestBody subcategory: SubcategoryEntity) {
        subcategoryService.updateSubcategory(id, subcategory)
    }
}