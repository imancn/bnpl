package com.iman.bnpl.domain.business.service

import com.iman.bnpl.actor.http.business.payload.response.BusinessPageResponse
import com.iman.bnpl.actor.http.business.payload.response.BusinessSearchItemResponse
import com.iman.bnpl.actor.http.business.payload.response.BusinessSearchResponse
import com.iman.bnpl.application.advice.NotFoundException
import com.iman.bnpl.application.shared.enums.BusinessMode
import com.iman.bnpl.domain.bnpl.service.BnplService
import com.iman.bnpl.domain.branch.data.repository.BusinessBranchRepository
import com.iman.bnpl.domain.business.data.repository.BusinessRepository
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class BusinessService(
    private val businessRepository: BusinessRepository,
    private val bnplService: BnplService,
    private val businessBranchRepository: BusinessBranchRepository
) {
    fun searchBusinesses(
        categoryId: Long?,
        searchTerm: String?,
        businessTypes: List<BusinessMode>?,
        bnplIds: List<String>?,
        pageable: Pageable
    ): BusinessSearchResponse {
        return BusinessSearchResponse(
            businessRepository.searchBusinesses(categoryId, searchTerm, businessTypes, bnplIds, pageable).map {
                BusinessSearchItemResponse(
                    businessEntity = it,
                    bnplList = bnplService.getBnplsByIds(it.bnplIds)
                )
            }
        )
    }

    fun getBusinessById(businessId: String): BusinessPageResponse {
        val business = businessRepository.findById(businessId).orElseThrow {
            NotFoundException("Business does not exist")
        }
        val bnplList = bnplService.getBnplsByIds(business.bnplIds)
        val businessBranches = businessBranchRepository.findAll(PageRequest.of(0, 10)).toList()
        return BusinessPageResponse(business, bnplList, businessBranches)
    }
}
