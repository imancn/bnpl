package com.iman.bnpl.domain.business.data.repository

import com.iman.bnpl.domain.business.data.model.BusinessEntity
import com.iman.bnpl.application.shared.enums.BusinessMode
import com.iman.bnpl.application.shared.enums.BusinessCategory
import org.springframework.stereotype.Repository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.domain.PageImpl

@Repository
class CustomBusinessRepositoryImpl(private val mongoTemplate: MongoTemplate) : CustomBusinessRepository {

    override fun searchBusinesses(
        category: BusinessCategory?,
        searchTerm: String?,
        businessTypes: List<BusinessMode>?,
        bnplIds: List<String>?,
        pageable: Pageable
    ): Page<BusinessEntity> {
        val query = Query()

        category?.let {
            query.addCriteria(Criteria.where("category").`is`(it))
        }

        searchTerm?.let {
            query.addCriteria(Criteria.where("name").regex(it, "i"))
        }

        businessTypes?.let {
            query.addCriteria(Criteria.where("businessModes").`in`(it))
        }

        bnplIds?.let {
            query.addCriteria(Criteria.where("bnplIds").`in`(it))
        }

        query.with(pageable)

        val businesses = mongoTemplate.find(query, BusinessEntity::class.java)
        return PageImpl(businesses, pageable, mongoTemplate.count(query, BusinessEntity::class.java))
    }
}
