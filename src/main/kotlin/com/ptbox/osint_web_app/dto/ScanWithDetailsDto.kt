package com.ptbox.osint_web_app.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.ptbox.osint_web_app.entity.ScanDetail
import com.ptbox.osint_web_app.entity.ScanInfo
import java.time.Instant

data class ScanWithDetailsDto(
    val id: Long,
    val domain: String,
    val status: String,

    @JsonProperty("start_time")
    val startTime: Instant,

    @JsonProperty("end_time")
    val endTime: Instant?,
    val logs: List<String>
) {
    companion object {
        fun create(scanInfo: ScanInfo): ScanWithDetailsDto {
            return ScanWithDetailsDto(
                id = scanInfo.id,
                domain = scanInfo.domain,
                status = scanInfo.status.name,
                startTime = scanInfo.startTime,
                endTime = scanInfo.endTime,
                logs = scanInfo.logs.map(ScanDetail::logMessage)
            )
        }
    }
}