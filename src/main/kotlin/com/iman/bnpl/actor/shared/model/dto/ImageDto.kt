package com.iman.bnpl.actor.shared.model.dto

import com.iman.bnpl.domain.business.data.model.Image

data class ImageDto(val url: String, val title: String?) {
    constructor(image: Image) : this(
        url = image.url,
        title = image.title
    )
}