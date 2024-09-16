package com.iman.bnpl.actor.http.branch.controller

import com.iman.bnpl.actor.http.branch.payload.BusinessBranchPageResponse
import com.iman.bnpl.actor.http.branch.payload.BusinessBranchSearchResponse
import com.iman.bnpl.domain.branch.service.BusinessBranchService
import org.springframework.data.domain.PageRequest
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("api/public/v1/business-branches/")
class PublicBusinessBranchController(private val businessBranchService: BusinessBranchService) {
    
    @GetMapping
    fun searchBusinesses(
        @RequestParam businessId: String,
        @RequestParam searchTerm: String?,
        @RequestParam pageSize: Int?,
        @RequestParam pageNumber: Int?
    ): BusinessBranchSearchResponse {
        val pageable = PageRequest.of(pageNumber ?: 0, pageSize ?: 10)
        return businessBranchService.searchBusinessBranches(businessId, searchTerm, pageable)
    }
    
    @GetMapping("{businessBranchId}")
    fun getBusinessBranchById(@PathVariable businessBranchId: String): BusinessBranchPageResponse? {
        return businessBranchService.getBusinessBranchById(businessBranchId)
    }
}