package com.amok.music

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var artistAdapter: ArtistAdapter
    private val artists = mutableListOf<Result>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }



        val artistSearchEditText = findViewById<EditText>(R.id.artistSearchEditText)
        val artistRecyclerView = findViewById<RecyclerView>(R.id.artistRecyclerView)

        val searched = intent.getStringExtra("searched")
        if (!searched.isNullOrEmpty()){
            artistSearchEditText.setText(searched)
            searchArtist(this@MainActivity, searched, { results ->
                artists.clear()
                artists.addAll(results)
                artistAdapter.notifyDataSetChanged()
            }, { error ->
                println("Error: ${error.message}")
            })
        }

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNav.selectedItemId = R.id.nav_search

        bottomNav.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_search -> {
                    true
                }
                R.id.nav_favourite -> {
                    val intent = Intent(this, Favourite::class.java)
                    intent.putExtra("searched",artistSearchEditText.text.toString())
                    startActivity(intent)
                    finish()
                    true
                }
                else -> false
            }
        }

        artistAdapter = ArtistAdapter(artists, object : ArtistAdapter.OnArtistClickListener {
            override fun onArtistClick(artist: Result) {
                val intent = Intent(this@MainActivity, Artist_Activity::class.java)
                intent.putExtra("ARTIST_ID", artist.id)
                startActivity(intent)
            }
        })

        artistRecyclerView.layoutManager = LinearLayoutManager(this)
        artistRecyclerView.adapter = artistAdapter



        artistSearchEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val searchText = s.toString()
                if (searchText.isNotEmpty()) {
                    searchArtist(this@MainActivity, searchText, { results ->
                        artists.clear()
                        artists.addAll(results)
                        artistAdapter.notifyDataSetChanged()
                    }, { error ->
                        println("Error: ${error.message}")
                    })
                } else {
                    artists.clear()
                    artistAdapter.notifyDataSetChanged()
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }
}