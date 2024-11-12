package com.example.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.data.remote.model.Location
import com.example.data.remote.model.Picture
import com.example.domain.model.LocationModel
import com.example.domain.model.PictureModel
import com.example.domain.model.StreetModel
import com.example.domain.model.UserDomainModel
import com.example.data.remote.model.Result

@Entity(tableName = UserEntity.TABLE_NAME)
@TypeConverters(LocationConverter::class, PictureConverter::class)
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = COLUMN_ID)
    val id: Long = 0,
    val firstName: String,
    val lastName: String,
    val gender: String,
    val location: Location,
    val email: String,
    val phone: String,
    val cell: String,
    val idName: String,
    val idValue: String?,
    val picture: Picture,
    @ColumnInfo(name = COLUMN_IS_FAVORITE)
    val isFavorite: Boolean = false
) {
    companion object {
        internal const val TABLE_NAME = "users"
        internal const val COLUMN_ID = "id"
        internal const val COLUMN_IS_FAVORITE = "is_favorite"
    }
}

fun Result.toEntity(): UserEntity {
    return UserEntity(
        firstName = name.first,
        lastName = name.last,
        gender = gender,
        location = location,
        email = email,
        phone = phone,
        cell = cell,
        idName = id.name,
        idValue = id.value,
        picture = picture
    )
}

fun UserEntity.toDomain(): UserDomainModel {
    return UserDomainModel(
        id = id,
        firstName = firstName,
        lastName = lastName,
        gender = gender,
        location = LocationModel(
            street = StreetModel(
                number = location.street.number,
                name = location.street.name
            ),
            city = location.city,
            country = location.country,
            timezone = location.timezone.description
        ),
        email = email,
        phone = phone,
        cell = cell,
        idName = idName,
        idValue = idValue,
        picture = PictureModel(
            large = picture.large,
            medium = picture.medium,
            thumbnail = picture.thumbnail
        ),
        isFavorite = isFavorite
    )
}