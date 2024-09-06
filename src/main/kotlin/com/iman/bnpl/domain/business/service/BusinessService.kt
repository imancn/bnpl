package com.iman.bnpl.domain.business.service

import com.iman.bnpl.actor.http.business.payload.response.BusinessPageResponse
import com.iman.bnpl.actor.http.business.payload.response.BusinessSearchResponse
import com.iman.bnpl.application.shared.enums.BusinessMode
import com.iman.bnpl.domain.bnpl.service.BnplService
import com.iman.bnpl.domain.business.data.repository.BusinessRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class BusinessService(
    private val businessRepository: BusinessRepository,
    private val bnplService: BnplService
) {
    
    fun searchBusinesses(
        categoryId: Long?,
        searchTerm: String?,
        businessTypes: List<BusinessMode>?,
        bnplIds: List<String>?,
        pageable: Pageable
    ): Page<BusinessSearchResponse> {
        return businessRepository.searchBusinesses(categoryId, searchTerm, businessTypes, bnplIds, pageable).map {
            BusinessSearchResponse(
                businessEntity = it,
                bnplList = bnplService.getBnplsByIds(it.bnplIds)
            )
        }
    }

    fun getBusinessById(businessId: String): BusinessPageResponse {
        val business = businessRepository.findById(businessId).orElseThrow {
            NoSuchElementException("Business does not exist")
        }
        val bnplList = bnplService.getBnplsByIds(business.bnplIds)
        return BusinessPageResponse(business, bnplList)
    }
}
