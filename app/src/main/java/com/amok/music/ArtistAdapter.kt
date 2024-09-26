package com.amok.music

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import android.widget.TextView

class ArtistAdapter(private val artists: List<Result>, private val listener: OnArtistClickListener) :
    RecyclerView.Adapter<ArtistAdapter.ArtistViewHolder>() {

    interface OnArtistClickListener {
        fun onArtistClick(artist: Result)
    }

    class ArtistViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val artistName: TextView = itemView.findViewById(R.id.artistName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtistViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.artist_item, parent, false)
        return ArtistViewHolder(view)
    }

    override fun onBindViewHolder(holder: ArtistViewHolder, position: Int) {
        val artist = artists[position]
        holder.artistName.text = artist.title

        holder.itemView.setOnClickListener {
            listener.onArtistClick(artist)
        }
    }

    override fun getItemCount(): Int {
        return artists.size
    }
}

