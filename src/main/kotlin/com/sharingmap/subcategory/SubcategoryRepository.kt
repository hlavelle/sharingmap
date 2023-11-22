package com.sharingmap.subcategory

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SubcategoryRepository : JpaRepository<SubcategoryEntity, Long> {
}