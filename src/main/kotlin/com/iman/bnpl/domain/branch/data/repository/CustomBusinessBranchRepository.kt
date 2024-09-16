package com.iman.bnpl.domain.branch.data.repository

import com.iman.bnpl.domain.branch.data.model.BusinessBranchEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface CustomBusinessBranchRepository {
    fun searchBusinessBranches(
        businessId: String, searchTerm: String?, pageable: Pageable
    ): Page<BusinessBranchEntity>
}