package com.iman.bnpl.domain.branch.data.repository

import com.iman.bnpl.domain.branch.data.model.BusinessBranchEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Repository

@Repository
class CustomBusinessBranchRepositoryImpl(private val mongoTemplate: MongoTemplate) : CustomBusinessBranchRepository {
    
    override fun searchBusinessBranches(
        businessId: String, searchTerm: String?, pageable: Pageable
    ): Page<BusinessBranchEntity> {
        val query = Query()
        
        query.addCriteria(Criteria.where("businessId").`is`(businessId))
        
        searchTerm?.let {
            query.addCriteria(Criteria.where("name").regex(it, "i"))
        }
        
        query.with(pageable)
        
        val businesses = mongoTemplate.find(query, BusinessBranchEntity::class.java)
        return PageImpl(businesses, pageable, mongoTemplate.count(query, BusinessBranchEntity::class.java))
    }
}
