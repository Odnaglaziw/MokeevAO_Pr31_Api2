package com.amok.music

import androidx.room.*

@Dao
interface ArtistDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(artist: Artist)

    @Update
    suspend fun update(artist: Artist)

    @Delete
    suspend fun delete(artist: Artist)

    @Query("Select * from artists")
    suspend fun getAllArtists() : List<Artist>

    @Query("Select * from artists where id = :artistId")
    suspend fun getArtistById(artistId: Int) : Artist?
}