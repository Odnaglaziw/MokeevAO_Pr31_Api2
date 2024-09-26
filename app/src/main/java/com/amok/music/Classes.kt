package com.amok.music

import com.google.gson.annotations.SerializedName

data class SearchResult(
    val results: List<Result>
)

data class Result(
    val id: Int,
    val title: String,
    val uri: String
)