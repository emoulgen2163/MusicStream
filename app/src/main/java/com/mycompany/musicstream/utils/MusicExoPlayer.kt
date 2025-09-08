package com.mycompany.musicstream.utils

import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.google.firebase.firestore.FirebaseFirestore
import com.mycompany.musicstream.models.SongModel

object MusicExoPlayer {
    private var exoPlayer: ExoPlayer? = null
    private var currentSong: SongModel? = null

    fun getInstance(): ExoPlayer? = exoPlayer

    fun startPlaying(context: Context, song: SongModel){
        if (exoPlayer == null) exoPlayer = ExoPlayer.Builder(context).build()

        if (currentSong != song) {
            currentSong = song
            updateCount()
            currentSong?.songUrl?.apply {
                val mediaItem = MediaItem.fromUri(this)
                exoPlayer?.setMediaItem(mediaItem)
                exoPlayer?.prepare()
                exoPlayer?.play()
            }
        }
    }

    fun getCurrentSong(): SongModel? = currentSong

    fun updateCount(){
        currentSong?.id?.let {
            FirebaseFirestore.getInstance().collection("songs").document(it).get().addOnSuccessListener { documentSnapshot ->
                var latestCount = documentSnapshot.getLong("count")
                latestCount = if (latestCount == null){
                    1L
                } else {
                    latestCount + 1
                }

                FirebaseFirestore.getInstance().collection("songs").document(it).update(mapOf("count" to latestCount))
            }
        }
    }
}