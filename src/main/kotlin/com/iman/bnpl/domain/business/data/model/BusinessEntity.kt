package com.iman.bnpl.domain.business.data.model

import com.iman.bnpl.application.shared.enums.BusinessMode
import com.iman.bnpl.application.shared.enums.Category
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.TypeAlias
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "businesses")
@TypeAlias("businesses")
data class BusinessEntity(
    @Id
    var id: String?,
    var name: String,
    var logo: Image?,
    var thumbnail: Image?,
    var businessModes: List<BusinessMode>,
    var bnplIds: List<String>,
    var category: Category,
    var address: Address?,
    var phoneNumbers: List<String>?,
    var images: List<Image>?,
    var websiteInfo: Link?,
    var workHours: WorkHours?
)