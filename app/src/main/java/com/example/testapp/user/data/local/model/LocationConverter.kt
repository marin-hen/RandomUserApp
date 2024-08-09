package com.example.testapp.user.data.local.model

import androidx.room.TypeConverter
import com.example.testapp.user.data.remote.model.Location
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class LocationConverter {

    @TypeConverter
    fun save(location: Location): String {
        return Json.encodeToString(location)
    }

    @TypeConverter
    fun restore(locationString: String): Location {
        return Json.decodeFromString(locationString)
    }
}