package com.example.user.presentation.model

import com.example.domain.model.UserDomainModel
import com.example.domain.model.getFormattedLocation
import com.example.domain.model.getFormattedUserName

data class UserUiModel(
    val id: Long = 0,
    val name: String,
    val gender: String,
    val location: String,
    val picture: PictureUiModel,
    val email: String,
    val phone: String,
    val cell: String,
    val timezone: String,
    val isFavorite: Boolean
)

data class PictureUiModel(
    val large: String,
    val medium: String,
    val thumbnail: String
)

fun UserDomainModel.toUiModel(): UserUiModel {
    return UserUiModel(
        id = id,
        name = getFormattedUserName(),
        gender = gender,
        location = getFormattedLocation(),
        picture = PictureUiModel(
            large = picture.large,
            thumbnail = picture.thumbnail,
            medium = picture.medium
        ),
        email = email,
        phone = phone,
        cell = cell,
        timezone = location.timezone,
        isFavorite = isFavorite
    )
}