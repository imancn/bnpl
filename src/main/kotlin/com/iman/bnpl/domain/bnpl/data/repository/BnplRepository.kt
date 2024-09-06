package com.iman.bnpl.domain.bnpl.data.repository

import com.iman.bnpl.domain.bnpl.data.model.BnplEntity
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface BnplRepository : MongoRepository<BnplEntity, String>