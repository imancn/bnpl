package com.iman.bnpl.actor.http.business.controller

import com.iman.bnpl.actor.http.business.payload.response.BusinessPageResponse
import com.iman.bnpl.actor.http.business.payload.response.BusinessSearchResponse
import com.iman.bnpl.application.shared.enums.BusinessMode
import com.iman.bnpl.application.shared.enums.BusinessCategory
import com.iman.bnpl.domain.business.service.BusinessService
import org.springframework.data.domain.PageRequest
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("api/public/v1/businesses")
class PublicBusinessController(private val businessService: BusinessService) {
    
    @GetMapping
    fun searchBusinesses(
        @RequestParam category: BusinessCategory?,
        @RequestParam searchTerm: String?,
        @RequestParam businessTypes: List<BusinessMode>?,
        @RequestParam bnplIds: List<String>?,
        @RequestParam pageSize: Int?,
        @RequestParam pageNumber: Int?
    ): BusinessSearchResponse {
        val pageable = PageRequest.of(pageNumber ?: 0, pageSize ?: 10)
        return businessService.searchBusinesses(category, searchTerm, businessTypes, bnplIds, pageable)
    }
    
    @GetMapping("/{businessId}")
    fun getBusinessById(@PathVariable businessId: String): BusinessPageResponse? {
        return businessService.getBusinessById(businessId)
    }
}