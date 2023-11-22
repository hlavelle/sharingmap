package com.sharingmap.settings

import org.springframework.stereotype.Service

@Service
class SettingServiceImpl(private val settingRepository: SettingRepository): SettingService {

    override fun getAllSettings(): List<SettingEntity> = settingRepository.findAll().toList()

    override fun getSettingById(id: Long): SettingEntity = settingRepository.findById(id).get()

    override fun createSetting(setting: SettingEntity) {
        settingRepository.save(setting)
    }

    override fun deleteSetting(id: Long) {
        settingRepository.deleteById(id)
    }

    override fun updateSetting(id: Long, setting: SettingDto) {
        val newSetting = settingRepository.findById(id).get()
        if (setting.key != null) newSetting.key = setting.key
        if (setting.value != null) newSetting.value = setting.value
        settingRepository.save(newSetting)
    }
}