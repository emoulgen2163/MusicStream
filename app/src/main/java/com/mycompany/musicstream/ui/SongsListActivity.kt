package com.mycompany.musicstream.ui

import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.mycompany.musicstream.R
import com.mycompany.musicstream.adapter.SongsListAdapter
import com.mycompany.musicstream.databinding.ActivitySongsListBinding
import com.mycompany.musicstream.models.CategoryModel

class SongsListActivity : AppCompatActivity() {

    companion object{
        lateinit var category: CategoryModel
    }

    lateinit var binding: ActivitySongsListBinding
    lateinit var songsListAdapter: SongsListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySongsListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Change the color dynamically
        val upArrow = ContextCompat.getDrawable(this, R.drawable.baseline_arrow_back_24)
        upArrow?.colorFilter = BlendModeColorFilter(Color.WHITE, BlendMode.SRC_ATOP)
        supportActionBar?.setHomeAsUpIndicator(upArrow)

        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.nameTextView.text = category.name

        Glide.with(binding.coverImageView).load(category.coverUrl).apply(RequestOptions().transform(RoundedCorners(32))).into(binding.coverImageView)

        setUpSongsListRecyclerView()

    }

    fun setUpSongsListRecyclerView(){
        songsListAdapter = SongsListAdapter(category.songs)
        binding.songsListRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@SongsListActivity)
            adapter = songsListAdapter
        }

    }
}