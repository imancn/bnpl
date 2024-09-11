package com.iman.bnpl.domain.list.business.service

import com.iman.bnpl.actor.http.list.business.payload.FavoriteBusinessOfListResponse
import com.iman.bnpl.actor.http.list.business.payload.FavoriteBusinessListResponse
import com.iman.bnpl.actor.shared.model.BnplLogoDto
import com.iman.bnpl.actor.shared.model.ImageDto
import com.iman.bnpl.application.advice.AccessDeniedException
import com.iman.bnpl.application.advice.NotFoundException
import com.iman.bnpl.application.shared.util.Auth
import com.iman.bnpl.domain.bnpl.service.BnplService
import com.iman.bnpl.domain.business.data.repository.BusinessRepository
import com.iman.bnpl.domain.list.business.data.model.FavoriteBusinessListEntity
import com.iman.bnpl.domain.list.business.data.repository.FavoriteBusinessListRepository
import org.springframework.stereotype.Service
import kotlin.jvm.optionals.getOrNull

@Service
class FavoriteBusinessListService(
    private val favoriteBusinessListRepository: FavoriteBusinessListRepository,
    private val businessRepository: BusinessRepository,
    private val bnplService: BnplService
) {
    
    fun getAllFavoriteBusinessLists(): List<FavoriteBusinessListResponse> {
        val favoriteLists = favoriteBusinessListRepository.findAllByUserId(Auth.userId())
        return favoriteLists.map { list ->
            FavoriteBusinessListResponse(
                favoriteBusinessListId = list.id ?: "",
                name = list.name,
                count = list.businessIds.size,
                thumbnail = list.businessIds.lastOrNull()?.let { businessId ->
                    businessRepository.findById(businessId).getOrNull()?.thumbnail?.let { ImageDto(it) }
                }
            )
        }
    }
    
    fun addFavoriteBusinessList(name: String): FavoriteBusinessListResponse {
        val newList = FavoriteBusinessListEntity(
            id = null,
            userId = Auth.userId(),
            name = name,
            businessIds = emptyList()
        )
        return FavoriteBusinessListResponse(favoriteBusinessListRepository.save(newList))
    }
    
    fun editFavoriteBusinessList(id: String, name: String): FavoriteBusinessListResponse {
        val list = getFavoriteBusinessList(id)
        return FavoriteBusinessListResponse(
            favoriteBusinessListRepository.save(list.also { it.name = name })
        )
    }
    
    fun removeFavoriteBusinessList(id: String) {
        getFavoriteBusinessList(id)
        favoriteBusinessListRepository.deleteById(id)
    }
    
    fun getFavoriteBusinessesOfList(listId: String): List<FavoriteBusinessOfListResponse> {
        val list = getFavoriteBusinessList(listId)
        return list.businessIds.map { businessId ->
            businessRepository.findById(businessId).map { business ->
                FavoriteBusinessOfListResponse(
                    businessId = business.id ?: "",
                    title = business.name,
                    logoUrl = business.logo?.url ?: "",
                    thumbnailUrl = business.thumbnail?.url ?: "",
                    businessModes = business.businessModes,
                    bnplList = bnplService.getBnplsByIds(business.bnplIds).map { BnplLogoDto(it) }
                )
            }.orElseThrow {
                throw NotFoundException("Business not found")
            }
        }
    }
    
    fun addBusinessToList(listId: String, businessId: String) {
        val list = getFavoriteBusinessList(listId)
        if (!businessRepository.existsById(businessId)) {
            throw NotFoundException("Business not found")
        }
        list.businessIds += businessId
        favoriteBusinessListRepository.save(list)
    }
    
    fun removeBusinessFromList(listId: String, businessId: String) {
        val list = getFavoriteBusinessList(listId)
        if (!list.businessIds.contains(businessId)) {
            throw NotFoundException("Business not found in this list")
        }
        list.businessIds -= businessId
        favoriteBusinessListRepository.save(list)
    }
    
    private fun getFavoriteBusinessList(listId: String): FavoriteBusinessListEntity {
        val list = favoriteBusinessListRepository.findById(listId).orElseThrow {
            throw NotFoundException("Favorite business list not found")
        }
        if (list.userId != Auth.userId()) {
            throw AccessDeniedException("You don't access to this list")
        }
        return list
    }
}
