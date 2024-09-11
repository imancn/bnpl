package com.iman.bnpl.domain.branch.data.model

import com.iman.bnpl.domain.business.data.model.Address
import com.iman.bnpl.domain.business.data.model.WorkHours
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.TypeAlias
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "business_branches")
@TypeAlias("business_branches")
data class BusinessBranchEntity(
    @Id
    var id: String?,
    var businessId: String?,
    var name: String,
    var address: Address?,
    var phoneNumber: String?,
    var workHours: WorkHours?
)