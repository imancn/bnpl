package com.iman.bnpl.domain.list.business.data.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "favorite_business_lists")
data class FavoriteBusinessListEntity(
    @Id
    var id: String?,
    var userId: String,
    var name: String,
    var businessIds: List<String>
)