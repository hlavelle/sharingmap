package com.sharingmap.category

import org.springframework.web.bind.annotation.*

@RestController
class CategoryController(private val categoryService: CategoryService) {

    @GetMapping("/categories/{id}")
    fun getCategoryById(@PathVariable id: Long): CategoryEntity {
        return categoryService.getCategoryById(id)
    }

    @GetMapping("/categories/all")
    fun getAllCategories(): List<CategoryEntity> {
        return categoryService.getAllCategories()
    }

    @PostMapping("/categories/create")
    fun createCategory(@RequestBody category: CategoryEntity) {
        categoryService.createCategory(category)
    }

    @DeleteMapping("/categories/delete/{id}")
    fun deleteCategory(@PathVariable id: Long) {
        categoryService.deleteCategory(id)
    }

    @PutMapping("/categories/update/{id}")
    fun updateCategory(@PathVariable id: Long, @RequestBody category: CategoryEntity) {
        categoryService.updateCategory(id, category)
    }
}