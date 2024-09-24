package com.iman.bnpl.actor.http.business.payload.response

import com.iman.bnpl.actor.shared.model.*
import com.iman.bnpl.application.shared.enums.BusinessMode
import com.iman.bnpl.application.shared.enums.BusinessCategory
import com.iman.bnpl.domain.bnpl.data.model.BnplEntity
import com.iman.bnpl.domain.branch.data.model.BusinessBranchEntity
import com.iman.bnpl.domain.business.data.model.BusinessEntity

data class BusinessPageResponse(
    val businessId: String?,
    val name: String,
    val logo: ImageDto?,
    val thumbnail: ImageDto?,
    val businessModes: List<BusinessMode>,
    val bnplList: List<BnplLogoDto>,
    val category: BusinessCategory?,
    val address: AddressDto?,
    val phoneNumbers: List<String>?,
    val branches: List<BranchSummaryResponse>?,
    val images: List<ImageDto>?,
    val websiteInfo: LinkDto?,
    val workHours: WorkHoursDto?
){
    constructor(businessEntity: BusinessEntity, bnplList: List<BnplEntity>, businessBranches: List<BusinessBranchEntity>): this(
        businessId = businessEntity.id,
        name = businessEntity.name,
        logo = businessEntity.logo?.let { ImageDto(it) },
        thumbnail = businessEntity.thumbnail?.let { ImageDto(it) },
        businessModes = businessEntity.businessModes,
        bnplList = bnplList.map { BnplLogoDto(it) },
        category = businessEntity.category,
        address = businessEntity.address?.let { AddressDto(it) },
        phoneNumbers = businessEntity.phoneNumbers?.map { "+98".plus(it) },
        branches = businessBranches.map {
            BranchSummaryResponse(businessEntity, it)
        },
        images = businessEntity.images?.let { it.map { image -> ImageDto(image) } },
        websiteInfo = businessEntity.websiteInfo?.let { LinkDto(it) },
        workHours = businessEntity.workHours?.let{ WorkHoursDto(it) }
    )
}