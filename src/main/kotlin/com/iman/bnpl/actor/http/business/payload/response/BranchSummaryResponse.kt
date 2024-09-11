package com.iman.bnpl.actor.http.business.payload.response

import com.iman.bnpl.actor.shared.model.ImageDto
import com.iman.bnpl.domain.branch.data.model.BusinessBranchEntity
import com.iman.bnpl.domain.business.data.model.BusinessEntity

data class BranchSummaryResponse(val businessId: String, val branchId: String, val name: String, val logo: ImageDto?) {
    constructor(business: BusinessEntity, branch: BusinessBranchEntity) : this(
        business.id ?: "",
        branch.id ?: "",
        branch.name,
        business.logo?.let { ImageDto(it) }
    )
}