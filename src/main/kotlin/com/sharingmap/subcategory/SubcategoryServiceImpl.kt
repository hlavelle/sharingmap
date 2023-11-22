package com.sharingmap.subcategory

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

    override fun updateSubcategory(subcategory: SubcategoryEntity) {
        val subcategoryId = subcategory.id
        subcategoryId?.let {
            subcategoryRepository.findById(subcategoryId)
                .orElseThrow { NoSuchElementException("Subcategory with id $subcategoryId not found") }
            subcategoryRepository.save(subcategory)
        } ?: {
            throw NoSuchElementException("please define item id for id")
        }
    }

}