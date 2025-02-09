package com.ptbox.osint_web_app.entity

import jakarta.persistence.*
import java.time.Instant

@Entity
@Table(name = "scan_info")
class ScanInfo(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(name = "container_id", nullable = false)
    val containerId: String,

    @Column(nullable = false)
    var domain: String,

    @Column(name = "start_time", nullable = false)
    var startTime: Instant = Instant.now(),

    @Column(name = "end_time")
    var endTime: Instant? = null,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var status: ScanStatus = ScanStatus.PROCESSING,

    @OneToMany(cascade = [CascadeType.ALL], fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "scan_info_id")
    var logs: MutableList<ScanDetail> = mutableListOf()
) {
    fun addLog(message: String) {
        val scanDetail = ScanDetail(logMessage = message)
        logs.add(scanDetail)
    }
}