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
    val id: String?,
    val name: String,
    val logo: Image?,
    val thumbnail: Image?,
    val businessModes: List<BusinessMode>,
    val bnplIds: List<String>,
    val category: Category,
    val address: Address?,
    val phoneNumber: String?,
    val images: List<Image>?,
    val websiteInfo: Link?,
    val workHours: WorkHours?
)