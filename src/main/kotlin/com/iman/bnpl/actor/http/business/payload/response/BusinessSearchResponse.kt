package com.iman.bnpl.actor.http.business.payload.response

import org.springframework.data.domain.Page

data class BusinessSearchResponse(
    val page: Page<BusinessSearchItemResponse>,
)
