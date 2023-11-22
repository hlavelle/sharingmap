package com.sharingmap.settings

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "settings")
class SettingEntity(
    @Column(nullable = false, unique = true)
    var key: String,
    var value: String
){
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "setting_generator")
    var id: Long? = null
}