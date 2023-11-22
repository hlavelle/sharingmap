package com.sharingmap.settings

import org.springframework.web.bind.annotation.*

@RestController
class SettingController(
    private val settingService: SettingService
) {

    @GetMapping("/settings/all")
    fun getAllSettings(): List<SettingEntity> {
        return settingService.getAllSettings()
    }

    @GetMapping("/settings/{id}")
    fun getSettingById(@PathVariable id: Long): SettingEntity {
        return settingService.getSettingById(id)
    }

    @PostMapping("/settings/create")
    fun createSetting(@RequestBody setting: SettingEntity) {
        settingService.createSetting(setting)
    }

    @DeleteMapping("/settings/delete/{id}")
    fun deleteSetting(@PathVariable id: Long) {
        settingService.deleteSetting(id)
    }

    @PutMapping("/settings/update/{id}")
    fun updateSetting(@PathVariable id: Long, @RequestBody setting: SettingDto) {
        settingService.updateSetting(id, setting)
    }
}