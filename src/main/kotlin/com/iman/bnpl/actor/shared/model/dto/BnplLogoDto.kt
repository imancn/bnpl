package com.iman.bnpl.actor.shared.model.dto

import com.iman.bnpl.domain.bnpl.data.model.BnplEntity

data class BnplLogoDto(val name: String, val logoUrl: String){
    constructor(bnpl: BnplEntity): this(
        name = bnpl.name,
        logoUrl = bnpl.logoUrl
    )
}