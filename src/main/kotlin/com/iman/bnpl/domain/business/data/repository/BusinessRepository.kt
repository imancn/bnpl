package com.iman.bnpl.domain.business.data.repository

import com.iman.bnpl.domain.business.data.model.BusinessEntity
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface BusinessRepository : MongoRepository<BusinessEntity, String>, CustomBusinessRepository