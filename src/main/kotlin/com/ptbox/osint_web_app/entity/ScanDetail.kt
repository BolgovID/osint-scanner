package com.ptbox.osint_web_app.entity

import jakarta.persistence.*

@Entity
@Table(name = "scan_detail")
class ScanDetail(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false)
    var logMessage: String,
)