package com.iman.bnpl.actor.shared.model.dto

import com.iman.bnpl.domain.business.data.model.Link

data class LinkDto(val url: String, val title: String) {
    constructor(link: Link) : this(
        url = link.url,
        title = link.title
    )
}