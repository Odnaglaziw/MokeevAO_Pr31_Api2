package com.amok.music

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch

class Favourite : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var favouriteAdapter: FavouriteAdapter
    private lateinit var db: AppDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_favourite)

        recyclerView = findViewById(R.id.recyclerView)
        db = AppDatabase.getDatabase(this)

        val layoutManager = GridLayoutManager(this, 2)
        recyclerView.layoutManager = layoutManager

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        lifecycleScope.launch {
            var favoriteArtists = db.artistDao().getAllArtists()
            favoriteArtists = FavouriteAdapter(favoriteArtists)
            recyclerView.adapter = favoriteArtists
            db = AppDatabase.getDatabase(applicationContext)
            db.artistDao().getAllArtists().forEach(){
                Log.e("Favourite",it.toString())
            }

        }

        val searched = intent.getStringExtra("searched")
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNav.selectedItemId = R.id.nav_favourite

        bottomNav.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_search -> {
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("searched",searched)
                    startActivity(intent)
                    finish()
                    true
                }
                R.id.nav_favourite -> {
                    true
                }
                else -> false
            }
        }
    }
}