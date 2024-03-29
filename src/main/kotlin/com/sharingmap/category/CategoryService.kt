package com.sharingmap.category

import com.sharingmap.category.CategoryEntity

interface CategoryService {
    abstract fun getCategoryById(id: Long): CategoryEntity
    abstract fun getAllCategories(): List<CategoryEntity>
    abstract fun createCategory(category: CategoryEntity)
    abstract fun deleteCategory(id: Long)
    abstract fun updateCategory(id: Long, category: CategoryEntity)
}