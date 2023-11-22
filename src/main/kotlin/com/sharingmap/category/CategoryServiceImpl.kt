package com.sharingmap.category

import org.springframework.stereotype.Service

@Service
class CategoryServiceImpl(private val categoryRepository: CategoryRepository) : CategoryService {

    override fun getCategoryById(id: Long): CategoryEntity = categoryRepository.findById(id).get()

    override fun getAllCategories(): List<CategoryEntity> = categoryRepository.findAll().toList()

    override fun createCategory(category: CategoryEntity) {
        categoryRepository.save(category)
    }

    override fun deleteCategory(id: Long) {
        categoryRepository.deleteById(id)
    }

    override fun updateCategory(id: Long, category: CategoryEntity) {
        var newCategory = categoryRepository.findById(id).get()
        newCategory.name = category.name
        newCategory.description = category.description
        newCategory.imageUrl = category.imageUrl
        categoryRepository.save(newCategory)
    }

}