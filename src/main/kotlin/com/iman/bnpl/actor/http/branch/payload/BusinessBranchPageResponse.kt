package com.iman.bnpl.actor.http.branch.payload

import com.iman.bnpl.actor.shared.model.*
import com.iman.bnpl.application.shared.enums.BusinessMode
import com.iman.bnpl.application.shared.enums.Category
import com.iman.bnpl.domain.bnpl.data.model.BnplEntity
import com.iman.bnpl.domain.branch.data.model.BusinessBranchEntity
import com.iman.bnpl.domain.business.data.model.BusinessEntity

data class BusinessBranchPageResponse(
    val businessId: String?,
    val businessBranchId: String?,
    val businessName: String?,
    val businessBranchName: String,
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
    constructor(businessBranchEntity: BusinessBranchEntity, businessEntity: BusinessEntity, bnplList: List<BnplEntity>): this(
        businessId = businessEntity.id,
        businessBranchId = businessBranchEntity.id,
        businessName = businessEntity.name,
        businessBranchName = businessBranchEntity.name,
        logo = businessEntity.logo?.let { ImageDto(it) },
        thumbnail = businessEntity.thumbnail?.let { ImageDto(it) },
        businessModes = businessEntity.businessModes,
        bnplList = bnplList.map { BnplLogoDto(it) },
        category = businessEntity.category,
        address = businessBranchEntity.address?.let { AddressDto(it) } ?: businessEntity.address?.let { AddressDto(it) },
        phoneNumber = businessBranchEntity.phoneNumber ?: businessEntity.phoneNumber,
        branches = null, //Todo: Must be implement later
        images = businessEntity.images?.let { it.map { image -> ImageDto(image) } },
        websiteInfo = businessEntity.websiteInfo?.let { LinkDto(it) },
        workHours = businessBranchEntity.workHours?.let{ WorkHoursDto(it) } ?: businessEntity.workHours?.let{ WorkHoursDto(it) }
    )
}