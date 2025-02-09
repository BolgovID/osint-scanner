package com.ptbox.osint_web_app.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.ptbox.osint_web_app.entity.ScanInfo
import java.time.Instant

data class ScanDto(
    val id: Long,
    val domain: String,
    val status: String,
    @JsonProperty("start_time")
    val startTime: Instant,
    @JsonProperty("end_time")
    val endTime: Instant?
) {
    companion object {
        fun create(scanInfo: ScanInfo): ScanDto {
            return ScanDto(
                id = scanInfo.id,
                domain = scanInfo.domain,
                status = scanInfo.status.name,
                startTime = scanInfo.startTime,
                endTime = scanInfo.endTime
            )
        }
    }
}