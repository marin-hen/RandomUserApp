package com.example.data.local.model

import androidx.room.TypeConverter
import com.example.data.remote.model.Picture
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class PictureConverter {

    @TypeConverter
    fun save(picture: Picture): String {
        return Json.encodeToString(picture)
    }

    @TypeConverter
    fun restore(pictureString: String): Picture {
        return Json.decodeFromString(pictureString)
    }
}