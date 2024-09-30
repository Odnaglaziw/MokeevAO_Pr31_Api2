package com.amok.music

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson

fun searchArtist(context: Context, artistName: String, onResponse: (List<Result>) -> Unit, onError: (Exception) -> Unit) {
    val url = "https://api.discogs.com/database/search?q=${artistName.replace(" ", "+")}&type=artist"
    val token = "Discogs token=kPnuurvYybLqjJKwWTBKdqDAupZwDXBlBOUUqGXn"
    val request = object : StringRequest(
        Request.Method.GET, url,
        { response ->
            val searchResult = Gson().fromJson(response, SearchResult::class.java)
            onResponse(searchResult.results)
        },
        { error ->
            onError(Exception(error))
        }) {
        override fun getHeaders(): Map<String, String> {
            val headers = HashMap<String, String>()
            headers["Authorization"] = token
            return headers
        }
    }

    Volley.newRequestQueue(context).add(request)
}

fun mapToArtist(artistDetails: ArtistDetails): Artist {
    val imageUrl: String = artistDetails.images.firstOrNull()?.uri ?: ""

    return Artist(
        id = artistDetails.id,
        name = artistDetails.name,
        image = imageUrl
    )
}
