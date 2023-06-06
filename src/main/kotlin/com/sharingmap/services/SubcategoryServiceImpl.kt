package com.sharingmap.services

import com.sharingmap.entities.CategoryEntity
import com.sharingmap.entities.SubcategoryEntity
import com.sharingmap.repositories.CategoryRepository
import com.sharingmap.repositories.SubcategoryRepository
import org.springframework.stereotype.Service

@Service
class SubcategoryServiceImpl(private val subcategoryRepository: SubcategoryRepository) : SubcategoryService {

    override fun getSubcategoryById(id: Long): SubcategoryEntity = subcategoryRepository.findById(id).get()

    override fun getAllSubcategories(): List<SubcategoryEntity> = subcategoryRepository.findAll().toList()

    override fun createSubcategory(subcategory: SubcategoryEntity) {
        subcategoryRepository.save(subcategory)
    }

    override fun deleteSubcategory(id: Long) {
        subcategoryRepository.deleteById(id)
    }

    override fun updateSubcategory(id: Long, subcategory: SubcategoryEntity) {
        var newSubcategory = subcategoryRepository.findById(id).get()
        newSubcategory.name = subcategory.name
        newSubcategory.description = subcategory.description
        newSubcategory.imageUrl = subcategory.imageUrl
        subcategoryRepository.save(newSubcategory)
    }

}