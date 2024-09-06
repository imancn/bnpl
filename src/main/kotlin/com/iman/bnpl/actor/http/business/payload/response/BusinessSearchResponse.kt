package com.iman.bnpl.actor.http.business.payload.response

import com.iman.bnpl.actor.shared.model.BnplLogoDto
import com.iman.bnpl.actor.shared.model.ImageDto
import com.iman.bnpl.domain.business.data.model.BusinessEntity
import com.iman.bnpl.application.shared.enums.BusinessMode
import com.iman.bnpl.domain.bnpl.data.model.BnplEntity

data class BusinessSearchResponse(
    val businessId: String?,
    val name: String,
    val logo: ImageDto?,
    val thumbnail: ImageDto?,
    val businessModes: List<BusinessMode>,
    val bnplList: List<BnplLogoDto>?
) {
    constructor(businessEntity: BusinessEntity, bnplList: List<BnplEntity>) : this(
        businessId = businessEntity.businessId,
        name = businessEntity.name,
        logo = businessEntity.logoUrl?.let { ImageDto(it) },
        thumbnail = businessEntity.thumbnailUrl?.let { ImageDto(it) },
        businessModes = businessEntity.businessModes,
        bnplList = bnplList.map { BnplLogoDto(it) }
    )
}
