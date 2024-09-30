package com.amok.music

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class FavouriteAdapter(private val artists: List<Artist>) : RecyclerView.Adapter<FavouriteAdapter.FavouriteViewHolder>(),
    List<Artist> {

    inner class FavouriteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val artistNameTextView: TextView = itemView.findViewById(R.id.artistNameTextView)
        val artistImageView: ImageView = itemView.findViewById(R.id.artistImageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.favourite_item, parent, false)
        return FavouriteViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavouriteViewHolder, position: Int) {
        val artist = artists[position]
        holder.artistNameTextView.text = artist.name
        Glide.with(holder.artistImageView.context)
            .load(artist.image)
            .placeholder(R.drawable.profile)
            .error(R.drawable.error)
            .into(holder.artistImageView)
    }

    override fun getItemCount() = artists.size
    override val size: Int
        get() = TODO("Not yet implemented")

    override fun get(index: Int): Artist {
        TODO("Not yet implemented")
    }

    override fun isEmpty(): Boolean {
        TODO("Not yet implemented")
    }

    override fun iterator(): Iterator<Artist> {
        TODO("Not yet implemented")
    }

    override fun listIterator(): ListIterator<Artist> {
        TODO("Not yet implemented")
    }

    override fun listIterator(index: Int): ListIterator<Artist> {
        TODO("Not yet implemented")
    }

    override fun subList(fromIndex: Int, toIndex: Int): List<Artist> {
        TODO("Not yet implemented")
    }

    override fun lastIndexOf(element: Artist): Int {
        TODO("Not yet implemented")
    }

    override fun indexOf(element: Artist): Int {
        TODO("Not yet implemented")
    }

    override fun containsAll(elements: Collection<Artist>): Boolean {
        TODO("Not yet implemented")
    }

    override fun contains(element: Artist): Boolean {
        TODO("Not yet implemented")
    }
}
