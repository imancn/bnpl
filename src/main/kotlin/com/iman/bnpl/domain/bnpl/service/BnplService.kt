package com.iman.bnpl.domain.bnpl.service

import com.iman.bnpl.domain.bnpl.data.model.BnplEntity
import com.iman.bnpl.domain.bnpl.data.repository.BnplRepository
import org.springframework.stereotype.Service
import kotlin.NoSuchElementException

@Service
class BnplService(private val bnplRepository: BnplRepository) {

    fun createBnpl(bnpl: BnplEntity): BnplEntity {
        return bnplRepository.save(bnpl)
    }

    fun getAllBnpls(): List<BnplEntity> {
        return bnplRepository.findAll()
    }

    fun getBnplById(id: String): BnplEntity {
        return bnplRepository.findById(id).orElseThrow { NoSuchElementException("BNPL does not exist") }
    }

    fun updateBnpl(bnpl: BnplEntity): BnplEntity {
        if (bnplRepository.existsById(bnpl.id ?: "")) {
            return bnplRepository.save(bnpl)
        } else {
            throw NoSuchElementException("BNPL does not exist")
        }
    }

    fun deleteBnpl(id: String) {
        if (bnplRepository.existsById(id)) {
            bnplRepository.deleteById(id)
        } else {
            throw NoSuchElementException("BNPL does not exist")
        }
    }
    
    fun getBnplsByIds(bnplIds: List<String>): List<BnplEntity> {
        return bnplRepository.findAllById(bnplIds)
    }
}