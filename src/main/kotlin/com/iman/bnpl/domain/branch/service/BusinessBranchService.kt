package com.iman.bnpl.domain.branch.service

import com.iman.bnpl.actor.http.branch.payload.BusinessBranchPageResponse
import com.iman.bnpl.actor.http.branch.payload.BusinessBranchSearchItemResponse
import com.iman.bnpl.actor.http.branch.payload.BusinessBranchSearchResponse
import com.iman.bnpl.application.advice.NotFoundException
import com.iman.bnpl.domain.bnpl.service.BnplService
import com.iman.bnpl.domain.branch.data.repository.BusinessBranchRepository
import com.iman.bnpl.domain.business.data.repository.BusinessRepository
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service

@Service
class BusinessBranchService(
    private val bnplService: BnplService,
    private val businessRepository: BusinessRepository,
    private val businessBranchRepository: BusinessBranchRepository
) {
    fun searchBusinessBranches(businessId: String, searchTerm: String?, pageable: PageRequest): BusinessBranchSearchResponse {
        val business = businessRepository.findById(businessId).orElseThrow {
            NotFoundException("Business does not exist")
        }
        val businessBranches = businessBranchRepository.searchBusinesses(
            businessId, searchTerm, pageable
        )
        return BusinessBranchSearchResponse(
            businessName = business.name,
            page = businessBranches.map {
                BusinessBranchSearchItemResponse(
                    it, business
                )
            }
        )
    }
    
    fun getBusinessBranchById(businessBranchId: String): BusinessBranchPageResponse? {
        val businessBranch = businessBranchRepository.findById(businessBranchId).orElseThrow {
            NotFoundException("Business Branch does not exist")
        }
        val business = businessRepository.findById(businessBranch.businessId ?: "").orElseThrow {
            NotFoundException("Business does not exist")
        }
        val bnplList = bnplService.getBnplsByIds(business.bnplIds)
        val otherBranches = businessBranchRepository.findAll(PageRequest.of(0, 10)).toList()
        return BusinessBranchPageResponse(businessBranch, business, bnplList, otherBranches)
    }
}