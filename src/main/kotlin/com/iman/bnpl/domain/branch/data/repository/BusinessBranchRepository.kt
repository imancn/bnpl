package com.iman.bnpl.domain.branch.data.repository

import com.iman.bnpl.domain.branch.data.model.BusinessBranchEntity
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface BusinessBranchRepository : MongoRepository<BusinessBranchEntity, String>, CustomBusinessBranchRepository