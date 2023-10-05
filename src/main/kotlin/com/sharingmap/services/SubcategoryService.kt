package com.sharingmap.services

import com.sharingmap.entities.SubcategoryEntity

interface SubcategoryService {
    abstract fun getSubcategoryById(id: Long): SubcategoryEntity
    abstract fun getAllSubcategories(): List<SubcategoryEntity>
    abstract fun createSubcategory(subcategory: SubcategoryEntity)
    abstract fun deleteSubcategory(id: Long)
    abstract fun updateSubcategory(category: SubcategoryEntity)
}