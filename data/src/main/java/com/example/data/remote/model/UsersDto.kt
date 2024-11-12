package com.example.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UsersDto(
    @SerialName("info")
    val info: Info,
    @SerialName("results")
    val results: List<Result>,
)

@Serializable
data class Result(
    @SerialName("gender")
    val gender: String,
    @SerialName("name")
    val name: Name,
    @SerialName("location")
    val location: Location,
    @SerialName("email")
    val email: String,
    @SerialName("phone")
    val phone: String,
    @SerialName("cell")
    val cell: String,
    @SerialName("id")
    val id: Id,
    @SerialName("picture")
    val picture: Picture
)

@Serializable
data class Id(
    @SerialName("name")
    val name: String,
    @SerialName("value")
    val value: String?
)

@Serializable
data class Info(
    @SerialName("seed")
    val seed: String,
    @SerialName("results")
    val results: Int,
    @SerialName("page")
    val page: Int,
    @SerialName("version")
    val version: String
)

@Serializable
data class Location(
    @SerialName("street")
    val street: Street,
    @SerialName("city")
    val city: String,
    @SerialName("country")
    val country: String,
    @SerialName("timezone")
    val timezone: Timezone
)

@Serializable
data class Name(
    @SerialName("title")
    val title: String,
    @SerialName("first")
    val first: String,
    @SerialName("last")
    val last: String
)

@Serializable
data class Picture(
    @SerialName("large")
    val large: String,
    @SerialName("medium")
    val medium: String,
    @SerialName("thumbnail")
    val thumbnail: String
)

@Serializable
data class Street(
    @SerialName("number")
    val number: Int,
    @SerialName("name")
    val name: String
)

@Serializable
data class Timezone(
    @SerialName("offset")
    val offset: String,
    @SerialName("description")
    val description: String
)