package com.iman.bnpl.domain.bnpl.data.model

import org.springframework.data.annotation.Id
import org.springframework.data.annotation.TypeAlias
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "bnpls")
@TypeAlias("bnpls")
data class BnplEntity(
    @Id
    var id: String?,
    @Indexed(unique = true)
    var index: Long,
    var name: String,
    var logoUrl: String,
    var description: String?,
)
