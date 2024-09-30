package com.amok.music

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import android.widget.ToggleButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Artist_Activity : AppCompatActivity() {

    private lateinit var toggle:ToggleButton
    private lateinit var db: AppDatabase
    private lateinit var artistDao: ArtistDao
    private lateinit var artistDetails : ArtistDetails
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_artist)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        db = AppDatabase.getDatabase(this)
        artistDao = db.artistDao()

        val artistId = intent.getIntExtra("ARTIST_ID", 0)
        val artistNameTextView = findViewById<TextView>(R.id.artistNameTextView)
        val artistProfileTextView = findViewById<TextView>(R.id.artistProfileTextView)
        val url = "https://api.discogs.com/artists/$artistId"
        val artistImageView = findViewById<ImageView>(R.id.artistImageView)
        val token = "Discogs token=kPnuurvYybLqjJKwWTBKdqDAupZwDXBlBOUUqGXn"
        toggle = findViewById<ToggleButton>(R.id.favourite_toggle)

        val request = object : StringRequest(Method.GET, url, { response ->

            artistDetails = Gson().fromJson(response, ArtistDetails::class.java)
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

        lifecycleScope.launch {
            val artist = artistDao.getArtistById(artistId)
            toggle.isChecked = artist != null
            toggle.setOnCheckedChangeListener { buttonView, isChecked ->
                saveFavouriteState(isChecked)
                if (isChecked) {
                    addToFavourites(true, mapToArtist(artistDetails))
                } else {
                    removeFromFavourites(true,mapToArtist(artistDetails))
                }
            }
        }




    }
    private fun saveFavouriteState(isFavourite: Boolean) {
        //val sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        //val editor = sharedPreferences.edit()
        //editor.putBoolean(FAVOURITE_KEY, isFavourite)
        //editor.apply()
    }

    private fun addToFavourites(action: Boolean, artist: Artist) {
        toggle.isChecked = true
        val rootView = findViewById<View>(android.R.id.content)
        val sn = Snackbar.make(rootView, "Добавлено в избранное", Snackbar.LENGTH_SHORT)

        CoroutineScope(Dispatchers.IO).launch {
            artistDao.insert(artist)
            Log.e("Xexe",db.artistDao().getAllArtists().toString())

            withContext(Dispatchers.Main) {
                if (action) {
                    sn.setAction("Отменить") {
                        removeFromFavourites(false, artist)
                    }
                }
                sn.show()
            }
        }
    }

    private fun removeFromFavourites(action: Boolean, artist: Artist) {
        toggle.isChecked = false
        val rootView = findViewById<View>(android.R.id.content)
        val sn = Snackbar.make(rootView, "Убрано из избранного", Snackbar.LENGTH_SHORT)

        CoroutineScope(Dispatchers.IO).launch {
            artistDao.delete(artist)

            lifecycleScope.launch {
                if (action) {
                    sn.setAction("Отменить") {
                        addToFavourites(true, mapToArtist(artistDetails))
                    }
                }
                sn.show()
            }
        }
    }

}