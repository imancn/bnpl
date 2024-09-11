package com.iman.bnpl.actor.http.branch.payload

import org.springframework.data.domain.Page

data class BusinessBranchSearchResponse(
    val businessName: String,
    val page: Page<BusinessBranchSearchItemResponse>,
)