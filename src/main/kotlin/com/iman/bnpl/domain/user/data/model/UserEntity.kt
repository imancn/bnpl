package com.iman.bnpl.domain.user.data.model

import org.springframework.data.annotation.Id
import org.springframework.data.annotation.TypeAlias
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "users")
@TypeAlias("users")
data class UserEntity(
    @Id
    var id: String?,
    var fullName: String,
    var phoneNumber: String,
    var password: String?,
    var roles: Set<Role> = HashSet(),
    var deleted: Boolean = false
)