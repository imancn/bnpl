package com.iman.bnpl.domain.list.business.data.repository

import com.iman.bnpl.domain.list.business.data.model.FavoriteBusinessListEntity
import org.springframework.data.mongodb.repository.MongoRepository

interface FavoriteBusinessListRepository : MongoRepository<FavoriteBusinessListEntity, String> {
    fun findAllByUserId(userId: String): List<FavoriteBusinessListEntity>
}