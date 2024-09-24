package com.iman.bnpl.actor.http.bnpl

import com.iman.bnpl.domain.bnpl.service.BnplService
import com.iman.bnpl.domain.bnpl.data.model.BnplEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api")
class BnplController(private val bnplService: BnplService) {

    @PostMapping("/admin/v1/bnpls")
    fun createBnpl(@RequestBody bnpl: BnplEntity): BnplEntity {
        return bnplService.createBnpl(bnpl)
    }

    @GetMapping("/public/v1/bnpls")
    fun getAllBnpls(): List<BnplEntity> {
        return bnplService.getAllBnpls()
    }

    @GetMapping("/admin/v1/bnpls/{id}")
    fun getBnplById(@PathVariable id: String): BnplEntity {
        return bnplService.getBnplById(id)
    }

    @PutMapping("/admin/v1/bnpls")
    fun updateBnpl(@RequestBody bnpl: BnplEntity): BnplEntity {
        return bnplService.updateBnpl(bnpl)
    }

    @DeleteMapping("/admin/v1/bnpls/{id}")
    fun deleteBnpl(@PathVariable id: String) {
        bnplService.deleteBnpl(id)
    }
}
