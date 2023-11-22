package com.sharingmap.category

import com.sharingmap.category.CategoryEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CategoryRepository : JpaRepository<CategoryEntity, Long> {
}