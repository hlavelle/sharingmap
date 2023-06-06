package com.sharingmap.controllers

import com.sharingmap.entities.CategoryEntity
import com.sharingmap.services.CategoryService
import org.springframework.web.bind.annotation.*

@RestController
class CategoryController(private val categoryService: CategoryService) {

    @GetMapping("/categories/{id}")
    fun getCategoryById(@PathVariable id: Long): CategoryEntity {
        return categoryService.getCategoryById(id)
    }

    @GetMapping("/categories")
    fun getAllCategories(): List<CategoryEntity> {
        return categoryService.getAllCategories()
    }

    @PostMapping("/categories")
    fun createCategory(@RequestBody category: CategoryEntity) {
        categoryService.createCategory(category)
    }

    @DeleteMapping("/categories/{id}")
    fun deleteCategory(@PathVariable id: Long) {
        categoryService.deleteCategory(id)
    }

    @PutMapping("/categories/{id}")
    fun updateCategory(@PathVariable id: Long, @RequestBody category: CategoryEntity) {
        categoryService.updateCategory(id, category)
    }
}