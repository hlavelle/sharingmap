package com.sharingmap.location

data class LocationInfoDto(val id : Long?, val name: String, val type: LocationType, val cityId: Long?) {
    constructor(entity: LocationEntity) : this(name =  entity.name, id = entity.id, type = entity.type, cityId = entity.city.id)
}

data class LocationCreateDto(val name: String, val type: LocationType, val cityId: Long)

data class LocationUpdateDto(val name: String?)