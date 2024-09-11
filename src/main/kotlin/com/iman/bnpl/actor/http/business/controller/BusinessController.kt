package com.iman.bnpl.actor.http.business.controller

import com.iman.bnpl.actor.http.business.payload.response.BusinessPageResponse
import com.iman.bnpl.actor.http.business.payload.response.BusinessSearchItemResponse
import com.iman.bnpl.actor.http.business.payload.response.BusinessSearchResponse
import com.iman.bnpl.application.shared.enums.BusinessMode
import com.iman.bnpl.domain.business.service.BusinessService
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("api/")
class BusinessController(private val businessService: BusinessService) {
    
    @GetMapping("/public/v1/businesses")
    fun searchBusinesses(
        @RequestParam categoryId: Long?,
        @RequestParam searchTerm: String?,
        @RequestParam businessTypes: List<BusinessMode>?,
        @RequestParam bnplIds: List<String>?,
        @RequestParam pageSize: Int?,
        @RequestParam pageNumber: Int?
    ): BusinessSearchResponse {
        val pageable = PageRequest.of(pageNumber ?: 0, pageSize ?: 10)
        return businessService.searchBusinesses(categoryId, searchTerm, businessTypes, bnplIds, pageable)
    }
    
    @GetMapping("/public/v1/businesses/{businessId}")
    fun getBusinessById(@PathVariable businessId: String): BusinessPageResponse? {
        return businessService.getBusinessById(businessId)
    }
}