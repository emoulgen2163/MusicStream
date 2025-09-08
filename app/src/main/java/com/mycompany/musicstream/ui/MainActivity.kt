package com.mycompany.musicstream.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.toObjects
import com.mycompany.musicstream.utils.MusicExoPlayer
import com.mycompany.musicstream.R
import com.mycompany.musicstream.adapter.CategoryAdapter
import com.mycompany.musicstream.adapter.SectionSongListAdapter
import com.mycompany.musicstream.databinding.ActivityMainBinding
import com.mycompany.musicstream.models.CategoryModel
import com.mycompany.musicstream.models.SongModel

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var categoryAdapter: CategoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getCategories()
        setUpSections("section_1", binding.section1RelativeLayout, binding.section1Title, binding.section1RecyclerView, binding.section1Expand)
        setUpSections("section_2", binding.section2RelativeLayout, binding.section2Title, binding.section2RecyclerView, binding.section2Expand)
        setUpMostlyPlayed("mostly_played", binding.section3RelativeLayout, binding.section3Title, binding.section3RecyclerView, binding.section3Expand)

        binding.options.setOnClickListener {
            showPopUpMenu()
        }
    }

    private fun showPopUpMenu() {
        val menu = PopupMenu(this, binding.options)
        val inflater =  menu.menuInflater
        inflater.inflate(R.menu.option_menu, menu.menu)
        menu.show()

        menu.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.logout -> {
                    logout()
                    true
                }
            }
            false
        }
    }

    private fun logout() {
        MusicExoPlayer.getInstance()?.release()
        FirebaseAuth.getInstance().signOut()
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    fun getCategories(){
        FirebaseFirestore.getInstance().collection("category").get().addOnSuccessListener {
            val categoryList: List<CategoryModel> = it.toObjects(CategoryModel::class.java)
            setUpCategoryRecyclerView(categoryList)
        }
    }

    fun setUpCategoryRecyclerView(categoryList: List<CategoryModel>){
        categoryAdapter = CategoryAdapter(categoryList)
        binding.categoriesRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.categoriesRecyclerView.adapter = categoryAdapter
    }

    fun setUpSections(id: String, layout: RelativeLayout, titleView: TextView, recyclerView: RecyclerView, expandView: ImageView){
        FirebaseFirestore.getInstance().collection("sections").document(id).get().addOnSuccessListener {
            val section = it.toObject(CategoryModel::class.java)
            section?.apply {
                layout.visibility = View.VISIBLE
                titleView.text = name
                recyclerView.layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
                recyclerView.adapter = SectionSongListAdapter(songs)
                expandView.setOnClickListener {
                    SongsListActivity.category = section
                    startActivity(Intent(this@MainActivity, SongsListActivity::class.java))
                }
            }

        }
    }

    fun setUpMostlyPlayed(id: String, layout: RelativeLayout, titleView: TextView, recyclerView: RecyclerView, expandView: ImageView){
        FirebaseFirestore.getInstance().collection("sections").document(id).get().addOnSuccessListener { documentSnapshot ->
            FirebaseFirestore.getInstance().collection("songs").orderBy("count", Query.Direction.DESCENDING).limit(5).get().addOnSuccessListener { querySnapshots ->
                val songModelList = querySnapshots.toObjects<SongModel>()
                val songIdList = songModelList.map { it.id }.toList()
                val section = documentSnapshot.toObject(CategoryModel::class.java)
                section?.apply {
                    songs = songIdList
                    layout.visibility = View.VISIBLE
                    titleView.text = name
                    recyclerView.layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
                    recyclerView.adapter = SectionSongListAdapter(songs)
                    expandView.setOnClickListener {
                        SongsListActivity.category = section
                        startActivity(Intent(this@MainActivity, SongsListActivity::class.java))
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        showPlayerView()
    }

    fun showPlayerView(){
        binding.playerView.setOnClickListener {
            startActivity(Intent(this, PlayerActivity::class.java))
        }
        MusicExoPlayer.getCurrentSong()?.let {
            binding.playerView.visibility = View.VISIBLE
            binding.songTitleTextView.text = "Playing: ${it.title}"
            Glide.with(binding.songCoverImageView).load(it.coverUrl).apply(RequestOptions().transform(RoundedCorners(32))).into(binding.songCoverImageView)
        } ?: run {
            binding.playerView.visibility = View.GONE
        }
    }
}