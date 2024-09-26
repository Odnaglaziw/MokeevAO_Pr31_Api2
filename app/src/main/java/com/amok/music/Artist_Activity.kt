package com.amok.music

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.google.gson.Gson
import org.json.JSONObject

class Artist_Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_artist)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val artistId = intent.getIntExtra("ARTIST_ID", 0)
        val artistNameTextView = findViewById<TextView>(R.id.artistNameTextView)
        val artistProfileTextView = findViewById<TextView>(R.id.artistProfileTextView)
        val url = "https://api.discogs.com/artists/$artistId"
        val artistImageView = findViewById<ImageView>(R.id.artistImageView)
        val token = "Discogs token=kPnuurvYybLqjJKwWTBKdqDAupZwDXBlBOUUqGXn"

        val request = object : StringRequest(Request.Method.GET, url, { response ->
            val jsonObject = JSONObject(response)
            val images = jsonObject.getJSONArray("images")
            if (images.length() > 0) {
                val imageUrl = images.getJSONObject(0).getString("uri")
                Log.d("API Response", "Response: $imageUrl")
            }
            val artistDetails = Gson().fromJson(response, ArtistDetails::class.java)
            artistNameTextView.text = artistDetails.name
            artistProfileTextView.text = artistDetails.profile

            val imageUrl = artistDetails.images.firstOrNull { !it.uri.isNullOrEmpty() }?.uri
            Log.d("GlideDebug", "Image URL: $imageUrl")
            Log.d("GlideDebug", "Artist Details: $artistDetails")
            if (imageUrl != null) {
                Glide.with(this)
                    .load(imageUrl)
                    .placeholder(R.drawable.profile)
                    .error(R.drawable.error)
                    .into(artistImageView)
            } else {
                artistImageView.setImageResource(R.drawable.profile)
            }
        }, { error ->
            artistProfileTextView.text = "Error: ${error.message}"
        }){
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Authorization"] = token
                return headers
            }
        }
        Volley.newRequestQueue(this).add(request)

    }
}