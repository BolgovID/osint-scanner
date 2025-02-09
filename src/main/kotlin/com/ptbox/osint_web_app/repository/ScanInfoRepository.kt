package com.ptbox.osint_web_app.repository

import com.ptbox.osint_web_app.entity.ScanInfo
import com.ptbox.osint_web_app.entity.ScanStatus
import jakarta.persistence.LockModeType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ScanInfoRepository : JpaRepository<ScanInfo, Long> {
    fun findByContainerId(containerId: String): Optional<ScanInfo>

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    fun findByStatus(status: ScanStatus): List<ScanInfo>

    fun findAllByOrderByStartTimeDesc(): List<ScanInfo>
}