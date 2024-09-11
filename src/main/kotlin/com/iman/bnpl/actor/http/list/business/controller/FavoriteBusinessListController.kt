package com.iman.bnpl.actor.http.list.business.controller

import com.iman.bnpl.actor.http.list.business.payload.FavoriteBusinessOfListResponse
import com.iman.bnpl.actor.http.list.business.payload.FavoriteBusinessListResponse
import com.iman.bnpl.domain.list.business.service.FavoriteBusinessListService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/customer/v1/favorite-business-lists")
class FavoriteBusinessListController(
    private val favoriteBusinessListService: FavoriteBusinessListService
) {
    
    @GetMapping
    fun getFavoriteBusinessLists(): List<FavoriteBusinessListResponse> {
        return favoriteBusinessListService.getAllFavoriteBusinessLists()
    }
    
    @PostMapping("/add")
    fun addFavoriteBusinessList(@RequestParam name: String): FavoriteBusinessListResponse {
        return favoriteBusinessListService.addFavoriteBusinessList(name)
    }
    
    @PostMapping("/edit")
    fun editFavoriteBusinessList(
        @RequestParam favoriteBusinessListId: String,
        @RequestParam name: String
    ): FavoriteBusinessListResponse {
        return favoriteBusinessListService.editFavoriteBusinessList(favoriteBusinessListId, name)
    }
    
    @PostMapping("/remove")
    fun removeFavoriteBusinessList(
        @RequestParam favoriteBusinessListId: String
    ) {
        favoriteBusinessListService.removeFavoriteBusinessList(favoriteBusinessListId)
    }
    
    @GetMapping("/businesses")
    fun getFavoriteBusinessesOfList(
        @RequestParam favoriteBusinessListId: String
    ): List<FavoriteBusinessOfListResponse> {
        return favoriteBusinessListService.getFavoriteBusinessesOfList(favoriteBusinessListId)
    }
    
    @PostMapping("/businesses/add")
    fun addBusinessToList(
        @RequestParam favoriteBusinessListId: String,
        @RequestParam businessId: String
    ) {
        favoriteBusinessListService.addBusinessToList(favoriteBusinessListId, businessId)
    }
    
    @PostMapping("/businesses/remove")
    fun removeBusinessFromList(
        @RequestParam favoriteBusinessListId: String,
        @RequestParam businessId: String
    ) {
        favoriteBusinessListService.removeBusinessFromList(favoriteBusinessListId, businessId)
    }
}
