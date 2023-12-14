package com.sharingmap.location

data class LocationCreateDto(val name: String, val type: LocationType, val cityId: Long)

data class LocationUpdateDto(val name: String?)