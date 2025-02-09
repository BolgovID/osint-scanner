package com.ptbox.osint_web_app.controller

import com.ptbox.osint_web_app.dto.ScanDto
import com.ptbox.osint_web_app.dto.ScanRequest
import com.ptbox.osint_web_app.dto.ScanWithDetailsDto
import com.ptbox.osint_web_app.service.DomainScanFacade
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/scans")
class ScanController(
    private val domainScanFacade: DomainScanFacade
) {

    @PostMapping
    fun startScanning(@Valid @RequestBody scanRequest: ScanRequest): ScanDto {
        return domainScanFacade.scanDomain(scanRequest.domain)
    }

    @GetMapping
    fun getAllScans(): List<ScanDto> {
        return domainScanFacade.findAllScans()
    }

    @GetMapping("/{scanId}")
    fun getScanDetails(@PathVariable("scanId") scanId: Long): ScanWithDetailsDto {
        return domainScanFacade.getScanDetails(scanId)
    }

    @PostMapping("/{scanId}/stop")
    fun stopScanning(@PathVariable("scanId") scanId: Long): ScanDto {
        return domainScanFacade.stopScanning(scanId)
    }

}