package com.iman.bnpl.actor.http.user.payload

import com.fasterxml.jackson.annotation.JsonCreator
import jakarta.validation.constraints.NotBlank

class UpdateUserProfileRequest @JsonCreator constructor(
    @field:NotBlank(message = "First Name must not be blank")
    var firstName: String,
    
    @field:NotBlank(message = "Last Name must not be blank")
    var lastName: String,
)
