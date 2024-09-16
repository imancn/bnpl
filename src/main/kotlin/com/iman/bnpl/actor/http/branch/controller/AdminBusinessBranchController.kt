package com.iman.bnpl.actor.http.branch.controller

import com.iman.bnpl.domain.branch.data.model.BusinessBranchEntity
import com.iman.bnpl.domain.branch.service.BusinessBranchService
import com.iman.bnpl.domain.business.data.model.Address
import com.iman.bnpl.domain.business.data.model.WorkHours
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVPrinter
import org.apache.commons.csv.CSVRecord
import org.springframework.core.io.InputStreamResource
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.io.*
import java.nio.charset.StandardCharsets

@RestController
@RequestMapping("api/admin/v1/business-branches/")
class AdminBusinessBranchController(
    private val businessBranchService: BusinessBranchService,
) {
    @GetMapping("/download-csv")
    fun exportBusinessBranchesToCSV(
        @RequestParam businessId: String,
        @RequestParam searchTerm: String?,
        @RequestParam pageSize: Int?,
        @RequestParam pageNumber: Int?
    ): ResponseEntity<InputStreamResource> {
        val pageable = PageRequest.of(pageNumber ?: 0, pageSize ?: 10)
        val branches = businessBranchService.getBusinessBranches(businessId, searchTerm, pageable)
        val byteArrayOutputStream = ByteArrayOutputStream()
        OutputStreamWriter(byteArrayOutputStream, Charsets.UTF_8).use { writer ->
            CSVPrinter(writer, CSVFormat.Builder.create()
                .setHeader(
                    "id", "businessId", "name", "address_full", "address_short",
                    "address_lat", "address_lng", "phoneNumber", "workHours_from", "workHours_to"
                )
                .build()
            ).use { csvPrinter ->
                branches.forEach { branch ->
                    csvPrinter.printRecord(
                        branch.id,
                        branch.businessId,
                        branch.name,
                        branch.address?.full,
                        branch.address?.short,
                        branch.address?.lat,
                        branch.address?.lng,
                        branch.phoneNumber,
                        branch.workHours?.from,
                        branch.workHours?.to
                    )
                }
            }
        }
        
        val inputStreamResource = InputStreamResource(ByteArrayInputStream(byteArrayOutputStream.toByteArray()))
        
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=business_branches.csv")
            .contentType(MediaType.parseMediaType("application/csv"))
            .body(inputStreamResource)
    }
    
    @PostMapping("/upload-csv")
    fun uploadCSV(@RequestParam("file") file: MultipartFile): ResponseEntity<String> {
        if (file.isEmpty) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please upload a valid CSV file.")
        }
        
        try {
            BufferedReader(InputStreamReader(file.inputStream, StandardCharsets.UTF_8)).use { reader ->
                val csvFormat = CSVFormat.Builder.create()
                    .setHeader(
                        "id", "businessId", "name", "address_full", "address_short",
                        "address_lat", "address_lng", "phoneNumber", "workHours_from", "workHours_to"
                    )
                    .setSkipHeaderRecord(true)
                    .build()
                
                val records: Iterable<CSVRecord> = csvFormat.parse(reader)
                
                for (record in records) {
                    val businessBranchEntity = mapCsvRecordToBusinessBranchEntity(record)
                    businessBranchService.saveOrUpdate(businessBranchEntity)
                }
                
                return ResponseEntity.status(HttpStatus.OK).body("CSV file processed successfully.")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while processing the CSV file.")
        }
    }
    
    private fun mapCsvRecordToBusinessBranchEntity(record: CSVRecord): BusinessBranchEntity {
        return BusinessBranchEntity(
            id = record.get("id"),
            businessId = record.get("businessId"),
            name = record.get("name"),
            address = Address(
                full = record.get("address_full"),
                short = record.get("address_short"),
                lat = record.get("address_lat").toDoubleOrNull(),
                lng = record.get("address_lng").toDoubleOrNull()
            ),
            phoneNumber = record.get("phoneNumber"),
            workHours = WorkHours(
                from = record.get("workHours_from"),
                to = record.get("workHours_to")
            )
        )
    }
}