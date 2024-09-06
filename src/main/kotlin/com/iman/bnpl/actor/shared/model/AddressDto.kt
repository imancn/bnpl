package com.iman.bnpl.actor.shared.model

import com.iman.bnpl.domain.business.data.model.Address

data class AddressDto(val full: String?, val short: String?, val lat: Double?, val lng: Double?) {
    constructor(address: Address) : this(
        full = address.full,
        short = address.short,
        lat = address.lat,
        lng = address.lng
    )
}