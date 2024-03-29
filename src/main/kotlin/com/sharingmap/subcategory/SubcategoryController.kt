package com.sharingmap.subcategory

import org.springframework.web.bind.annotation.*

@RestController
class SubcategoryController(private val subcategoryService: SubcategoryService) {

    @GetMapping("/subcategories/{id}")
    fun getSubcategoryById(@PathVariable id: Long): SubcategoryEntity {
        return subcategoryService.getSubcategoryById(id)
    }

    @GetMapping("/subcategories/all")
    fun getAllSubcategories(): List<SubcategoryEntity> {
        return subcategoryService.getAllSubcategories()
    }

    @PostMapping("/subcategories/create")
    fun createSubcategory(@RequestBody subcategory: SubcategoryEntity) {
        subcategoryService.createSubcategory(subcategory)
    }

    @DeleteMapping("/subcategories/delete/{id}")
    fun deleteSubcategory(@PathVariable id: Long) {
        subcategoryService.deleteSubcategory(id)
    }

    @PutMapping("/subcategories/update")
    fun updateSubcategory(@RequestBody subcategory: SubcategoryEntity) {
        subcategoryService.updateSubcategory(subcategory)
    }
}