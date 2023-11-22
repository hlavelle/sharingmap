package com.sharingmap.settings

interface SettingService {
    fun getAllSettings(): List<SettingEntity>
    fun getSettingById(id: Long): SettingEntity
    fun createSetting(setting: SettingEntity)
    fun deleteSetting(id: Long)
    fun updateSetting(id: Long, setting: SettingDto)
}