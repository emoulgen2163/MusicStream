package com.mycompany.musicstream.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.mycompany.musicstream.utils.MusicExoPlayer
import com.mycompany.musicstream.ui.PlayerActivity
import com.mycompany.musicstream.databinding.SongListItemBinding
import com.mycompany.musicstream.models.SongModel

class SongsListAdapter(private val songIdList: List<String>): RecyclerView.Adapter<SongsListAdapter.SongsListViewHolder>() {
    class SongsListViewHolder(private val binding: SongListItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bindData(songId: String){
            FirebaseFirestore.getInstance().collection("songs").document(songId).get().addOnSuccessListener { documentSnapshot ->
                val song = documentSnapshot.toObject(SongModel::class.java)
                song?.apply {
                    binding.songTitleTextView.text = song.title
                    binding.songSubtitleTextView.text = song.subtitle
                    Glide.with(binding.songCoverImageView).load(coverUrl).apply(RequestOptions().transform(RoundedCorners(32))).into(binding.songCoverImageView)
                    binding.root.setOnClickListener {
                        MusicExoPlayer.startPlaying(binding.root.context, song)
                        it.context.startActivity(Intent(it.context, PlayerActivity::class.java))
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SongsListAdapter.SongsListViewHolder = SongsListViewHolder(SongListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(
        holder: SongsListAdapter.SongsListViewHolder,
        position: Int
    ) {
        holder.bindData(songIdList[position])
    }

    override fun getItemCount(): Int = songIdList.size


}
