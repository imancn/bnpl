package com.iman.bnpl.actor.http.bnpl

import com.iman.bnpl.domain.bnpl.service.BnplService
import com.iman.bnpl.domain.bnpl.data.model.BnplEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/admin/v1/bnpls")
class BnplController(private val bnplService: BnplService) {

    @PostMapping
    fun createBnpl(@RequestBody bnpl: BnplEntity): BnplEntity {
        return bnplService.createBnpl(bnpl)
    }

    @GetMapping
    fun getAllBnpls(): List<BnplEntity> {
        return bnplService.getAllBnpls()
    }

    @GetMapping("/{id}")
    fun getBnplById(@PathVariable id: String): BnplEntity {
        return bnplService.getBnplById(id)
    }

    @PutMapping("/{id}")
    fun updateBnpl(@RequestBody bnpl: BnplEntity): BnplEntity {
        return bnplService.updateBnpl(bnpl)
    }

    @DeleteMapping("/{id}")
    fun deleteBnpl(@PathVariable id: String) {
        bnplService.deleteBnpl(id)
    }
}
