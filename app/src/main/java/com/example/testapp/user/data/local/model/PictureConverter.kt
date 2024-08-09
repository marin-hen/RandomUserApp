package com.example.testapp.user.data.local.model

import androidx.room.TypeConverter
import com.example.testapp.user.data.remote.model.Picture
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