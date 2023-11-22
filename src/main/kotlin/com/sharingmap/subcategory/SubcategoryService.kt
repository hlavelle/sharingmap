package com.sharingmap.subcategory

interface SubcategoryService {
    abstract fun getSubcategoryById(id: Long): SubcategoryEntity
    abstract fun getAllSubcategories(): List<SubcategoryEntity>
    abstract fun createSubcategory(subcategory: SubcategoryEntity)
    abstract fun deleteSubcategory(id: Long)
    abstract fun updateSubcategory(category: SubcategoryEntity)
}