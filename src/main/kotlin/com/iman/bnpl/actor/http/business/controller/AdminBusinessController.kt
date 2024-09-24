package com.iman.bnpl.actor.http.business.controller

import com.iman.bnpl.application.shared.enums.BusinessMode
import com.iman.bnpl.application.shared.enums.Category
import com.iman.bnpl.domain.bnpl.service.BnplService
import com.iman.bnpl.domain.business.data.model.*
import com.iman.bnpl.domain.business.service.BusinessService
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVPrinter
import org.apache.commons.csv.CSVRecord
import org.springframework.core.io.ByteArrayResource
import org.springframework.core.io.InputStreamResource
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.io.BufferedReader
import java.io.ByteArrayOutputStream
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.nio.charset.StandardCharsets
import java.util.*

@RestController
@RequestMapping("api/admin/v1/businesses/")
class AdminBusinessController(
    private val businessService: BusinessService,
    private val bnplService: BnplService,
) {
    @GetMapping("/download-csv")
    fun downloadCSV(
        @RequestParam categoryId: Long?,
        @RequestParam searchTerm: String?,
        @RequestParam businessTypes: List<BusinessMode>?,
        @RequestParam bnplIds: List<String>?,
        @RequestParam pageSize: Int?,
        @RequestParam pageNumber: Int?
    ): ResponseEntity<InputStreamResource> {
        try {
            val pageable = PageRequest.of(pageNumber ?: 0, pageSize ?: 500)
            val businesses = businessService.getBusinesses(categoryId, searchTerm, businessTypes, bnplIds, pageable)
            
            val byteArrayOutputStream = ByteArrayOutputStream()
            OutputStreamWriter(byteArrayOutputStream, StandardCharsets.UTF_8).use { writer ->
                val csvPrinter = CSVPrinter(
                    writer, CSVFormat.Builder.create().setHeader(
                            "id",
                            "name",
                            "logo_url",
                            "logo_title",
                            "thumbnail_url",
                            "thumbnail_title",
                            "businessModes",
                            "bnplIds",
                            "category",
                            "address_full",
                            "address_short",
                            "address_lat",
                            "address_lng",
                            "phoneNumber",
                            "images_urls_titles",
                            "websiteInfo_url",
                            "websiteInfo_title",
                            "workHours_from",
                            "workHours_to"
                        ).build()
                )
                
                for (business in businesses) {
                    csvPrinter.printRecord(
                        business.id,
                        business.name,
                        business.logo?.url,
                        business.logo?.title,
                        business.thumbnail?.url,
                        business.thumbnail?.title,
                        business.businessModes.joinToString(";"),
                        business.bnplIds.joinToString(";"),
                        business.category.name,
                        business.address?.full,
                        business.address?.short,
                        business.address?.lat,
                        business.address?.lng,
                        business.phoneNumbers,
                        business.images?.joinToString(";") { "${it.url}|${it.title}" },
                        business.websiteInfo?.url,
                        business.websiteInfo?.title,
                        business.workHours?.from,
                        business.workHours?.to
                    )
                }
                csvPrinter.flush()
            }
            val resource = ByteArrayResource(byteArrayOutputStream.toByteArray())
            
            return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=business_entities.csv")
                .contentType(MediaType.parseMediaType("application/csv"))
                .body(InputStreamResource(resource.inputStream))
            
        } catch (e: Exception) {
            e.printStackTrace()
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null)
        }
    }
    
    @PostMapping("/upload-csv", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun uploadCSV(@RequestParam("file") file: MultipartFile): ResponseEntity<String> {
        if (file.isEmpty) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please upload a valid CSV file.")
        }
        
        try {
            BufferedReader(InputStreamReader(file.inputStream, StandardCharsets.UTF_8)).use { reader ->
                val records: Iterable<CSVRecord> = CSVFormat.Builder.create().setHeader(
                        "id",
                        "name",
                        "logo_url",
                        "logo_title",
                        "thumbnail_url",
                        "thumbnail_title",
                        "businessModes",
                        "bnplIds",
                        "category",
                        "address_full",
                        "address_short",
                        "address_lat",
                        "address_lng",
                        "phoneNumber",
                        "images_urls_titles",
                        "websiteInfo_url",
                        "websiteInfo_title",
                        "workHours_from",
                        "workHours_to"
                    ).setSkipHeaderRecord(true).build().parse(reader)
                for (record in records) {
                    val businessEntity = saveOrUpdateCsvRecordToBusinessEntity(record)
                    businessService.saveOrUpdate(businessEntity)
                }
                return ResponseEntity.status(HttpStatus.OK).body("CSV file processed successfully.")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("An error occurred while processing the CSV file.")
        }
    }
    
    private fun saveOrUpdateCsvRecordToBusinessEntity(record: CSVRecord): BusinessEntity {
        val logo = Image(record.get("logo_url"), record.get("logo_title"))
        val thumbnail = Image(record.get("thumbnail_url"), record.get("thumbnail_title"))
        val businessModes = record.get("businessModes").split(";").map { BusinessMode.valueOf(it) }
        val bnplIds: List<String> = bnplService.findByOrders(
            record.get("bnplIds").split(";").mapNotNull { it.toLongOrNull() }
        ).mapNotNull { it.id }
        val category: Category = Category.valueOf(record.get("category").uppercase())
        val address = Address(
            record.get("address_full"),
            record.get("address_short"),
            record.get("address_lat")?.toDoubleOrNull(),
            record.get("address_lng")?.toDoubleOrNull()
        )
        val images: List<Image>? = parseImages(record.get("images_urls_titles"))
        val websiteInfo = Link(record.get("websiteInfo_url"), record.get("websiteInfo_title"))
        val workHours = WorkHours(record.get("workHours_from"), record.get("workHours_to"))
        
        return BusinessEntity(
            id = record.get("id"),
            name = record.get("name"),
            logo = logo,
            thumbnail = thumbnail,
            businessModes = businessModes,
            bnplIds = bnplIds,
            category = category,
            address = address,
            phoneNumbers = record.get("phoneNumbers").split(";").map { it.trim() }.filter { it.matches(Regex("\\b9\\d{9}")) },
            images = images,
            websiteInfo = websiteInfo,
            workHours = workHours,
        )
    }
    
    private fun parseImages(imagesData: String?): List<Image>? {
        if (imagesData.isNullOrEmpty()) {
            return null
        }
        return Arrays.stream(imagesData.split(";".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray())
            .map { data ->
                val parts = data.split("|")
                Image(parts[0], if (parts.size > 1) parts[1] else null)
            }.toList()
    }
}