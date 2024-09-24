package com.amok.music

import com.google.gson.annotations.SerializedName

data class Release(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("year") val year: Int
)