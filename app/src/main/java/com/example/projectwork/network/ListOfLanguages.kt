package com.example.projectwork.network

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ListOfLanguages (
    val count: Long,
    val languages: String,
    @Json(name = "countWords")val wordsCount: String
)