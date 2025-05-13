package com.sharingmap.category

import org.springframework.stereotype.Service
import java.util.NoSuchElementException

@Service
class CategoryServiceImpl(private val categoryRepository: CategoryRepository) : CategoryService {

    override fun getCategoryById(id: Long): CategoryEntity {
        return categoryRepository.findById(id).orElseThrow { NoSuchElementException("Category not found with ID: $id") }
    }

    override fun getAllCategories(): List<CategoryEntity> =
        categoryRepository.findAll().sortedWith(
            compareBy<CategoryEntity> { it.position ?: 100 }
        )

    override fun createCategory(category: CategoryEntity) {
        categoryRepository.save(category)
    }

    override fun deleteCategory(id: Long) {
        categoryRepository.deleteById(id)
    }

    override fun updateCategory(id: Long, category: CategoryEntity) {
        val newCategory = categoryRepository.findById(id).get()
        newCategory.name = category.name
        newCategory.description = category.description
        categoryRepository.save(newCategory)
    }

}