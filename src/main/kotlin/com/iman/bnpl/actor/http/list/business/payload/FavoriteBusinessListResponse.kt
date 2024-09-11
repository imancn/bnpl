package com.iman.bnpl.actor.http.list.business.payload

import com.iman.bnpl.actor.shared.model.ImageDto
import com.iman.bnpl.domain.list.business.data.model.FavoriteBusinessListEntity

data class FavoriteBusinessListResponse(
    val favoriteBusinessListId: String?,
    val name: String,
    val count: Int,
    val thumbnail: ImageDto?
) {
    constructor(favoriteBusinessListEntity: FavoriteBusinessListEntity, thumbnail: ImageDto? = null) : this(
        favoriteBusinessListEntity.id,
        favoriteBusinessListEntity.name,
        favoriteBusinessListEntity.businessIds.count(),
        thumbnail
    )
}