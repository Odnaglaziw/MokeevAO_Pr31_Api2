package com.amok.music

import android.icu.text.ListFormatter.Width
import com.google.gson.annotations.SerializedName

data class SearchResult(
    val results: List<Result>
)

data class Result(
    val id: Int,
    val title: String,
    val uri: String
)

data class ArtistDetails(
    val id: Int,
    val name: String,
    val profile: String,
    val images: List<Image>
)

data class Image(
    @SerializedName("type") val type: String,
    @SerializedName("uri") val uri: String?,
    @SerializedName("resource_url") val resourceUrl: String?,
    @SerializedName("uri150") val uri150 : String?,
    @SerializedName("width") val width: Int,
    @SerializedName("height") val height: Int
)