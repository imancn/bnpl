package com.iman.bnpl.domain.bnpl.service

import com.iman.bnpl.application.advice.NotFoundException
import com.iman.bnpl.domain.bnpl.data.model.BnplEntity
import com.iman.bnpl.domain.bnpl.data.repository.BnplRepository
import org.springframework.stereotype.Service

@Service
class BnplService(private val bnplRepository: BnplRepository) {

    fun createBnpl(bnpl: BnplEntity): BnplEntity {
        return bnplRepository.save(bnpl.also { it.id = null })
    }

    fun getAllBnpls(): List<BnplEntity> {
        return bnplRepository.findAll().sortedBy { it.index }
    }

    fun getBnplById(id: String): BnplEntity {
        return bnplRepository.findById(id).orElseThrow { NotFoundException("BNPL does not exist") }
    }

    fun updateBnpl(bnpl: BnplEntity): BnplEntity {
        if (bnplRepository.existsById(bnpl.id ?: "")) {
            return bnplRepository.save(bnpl)
        } else {
            throw NotFoundException("BNPL does not exist")
        }
    }

    fun deleteBnpl(id: String) {
        if (bnplRepository.existsById(id)) {
            bnplRepository.deleteById(id)
        } else {
            throw NotFoundException("BNPL does not exist")
        }
    }
    
    fun getBnplsByIds(bnplIds: List<String>): List<BnplEntity> {
        return bnplRepository.findAllById(bnplIds)
    }
    
    fun findByOrders(indexes: List<Long>): List<BnplEntity> {
        return bnplRepository.findByIndexIn(indexes)
    }
}