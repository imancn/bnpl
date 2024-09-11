package com.iman.bnpl.actor.http.branch.payload

import com.iman.bnpl.actor.shared.model.ImageDto
import com.iman.bnpl.domain.branch.data.model.BusinessBranchEntity
import com.iman.bnpl.domain.business.data.model.BusinessEntity

data class BusinessBranchSearchItemResponse(
    val businessId: String?, val businessBranchId: String?, val name: String, val logo: ImageDto?
) {
    constructor(
        businessBranchEntity: BusinessBranchEntity,
        businessEntity: BusinessEntity
    ) : this(businessId = businessEntity.id,
        businessBranchId = businessBranchEntity.id,
        name = businessBranchEntity.name,
        logo = businessEntity.logo?.let { ImageDto(it) })
}
