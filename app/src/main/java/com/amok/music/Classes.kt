package com.amok.music

import android.content.Context
import android.icu.text.ListFormatter.Width
import androidx.room.*
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

@Entity(tableName = "artists")
data class Artist(
    @PrimaryKey(autoGenerate = false) val id: Int,
    val name: String,
    val image: Image
)

@Database(entities = [Artist::class], version = 1)
abstract class AppDatabase : RoomDatabase(){

    abstract fun artistDao(): ArtistDao

    companion object{
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context):AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}