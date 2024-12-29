package com.iman.bnpl.actor.http.list.business.payload

import com.iman.bnpl.actor.shared.model.dto.BnplLogoDto
import com.iman.bnpl.application.shared.enums.BusinessMode

data class FavoriteBusinessOfListResponse(
    val businessId: String,
    val title: String,
    val logoUrl: String,
    val thumbnailUrl: String,
    val businessModes: List<BusinessMode>,
    val bnplList: List<BnplLogoDto>
)