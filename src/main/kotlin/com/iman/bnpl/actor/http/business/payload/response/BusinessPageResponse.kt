package com.iman.bnpl.actor.http.business.payload.response

import com.iman.bnpl.actor.shared.model.*
import com.iman.bnpl.application.shared.enums.BusinessMode
import com.iman.bnpl.application.shared.enums.Category
import com.iman.bnpl.domain.bnpl.data.model.BnplEntity
import com.iman.bnpl.domain.business.data.model.BusinessEntity

data class BusinessPageResponse(
    val businessId: String?,
    val name: String,
    val logo: ImageDto?,
    val thumbnail: ImageDto?,
    val businessModes: List<BusinessMode>,
    val bnplList: List<BnplLogoDto>,
    val category: Category?,
    val address: AddressDto?,
    val phoneNumber: String?,
    val branches: List<BranchSummaryResponse>?,
    val images: List<ImageDto>?,
    val websiteInfo: LinkDto?,
    val workHours: WorkHoursDto?
){
    constructor(businessEntity: BusinessEntity, bnplList: List<BnplEntity>): this(
        businessId = businessEntity.id,
        name = businessEntity.name,
        logo = businessEntity.logoUrl?.let { ImageDto(it) },
        thumbnail = businessEntity.thumbnailUrl?.let { ImageDto(it) },
        businessModes = businessEntity.businessModes,
        bnplList = bnplList.map { BnplLogoDto(it) },
        category = businessEntity.category,
        address = businessEntity.address?.let { AddressDto(it) },
        phoneNumber = businessEntity.phoneNumber,
        branches = null, //Todo: Must be implement later
        images = businessEntity.images?.let { it.map { image -> ImageDto(image) } },
        websiteInfo = businessEntity.websiteInfo?.let { LinkDto(it) },
        workHours = businessEntity.workHours?.let{ WorkHoursDto(it) }
    )
}