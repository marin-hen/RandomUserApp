package com.example.domain.model

data class UserDomainModel(
    val id: Long,
    val firstName: String,
    val lastName: String,
    val gender: String,
    val location: LocationModel,
    val email: String,
    val phone: String,
    val cell: String,
    val idName: String,
    val idValue: String?,
    val picture: PictureModel,
    val isFavorite: Boolean
)

data class LocationModel(
    val street: StreetModel,
    val city: String,
    val country: String,
    val timezone: String
)

data class PictureModel(
    val large: String,
    val medium: String,
    val thumbnail: String
)

data class StreetModel(
    val number: Int,
    val name: String
)

fun UserDomainModel.getFormattedLocation(): String {
    return "${location.city}, ${location.country}"
}

fun UserDomainModel.getFormattedUserName(): String {
    return "$firstName $lastName"
}