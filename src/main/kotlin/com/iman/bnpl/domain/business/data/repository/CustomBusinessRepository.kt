package com.iman.bnpl.domain.business.data.repository

import com.iman.bnpl.domain.business.data.model.BusinessEntity
import com.iman.bnpl.application.shared.enums.BusinessMode
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface CustomBusinessRepository {
    fun searchBusinesses(
        categoryId: Long?,
        searchTerm: String?,
        businessTypes: List<BusinessMode>?,
        bnplIds: List<String>?,
        pageable: Pageable
    ): Page<BusinessEntity>
}