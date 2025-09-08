package com.mycompany.musicstream.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.mycompany.musicstream.ui.SongsListActivity
import com.mycompany.musicstream.databinding.CategoryItemBinding
import com.mycompany.musicstream.models.CategoryModel

class CategoryAdapter(private val categoryList: List<CategoryModel>): RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    class CategoryViewHolder(private val binding: CategoryItemBinding): RecyclerView.ViewHolder(binding.root){

        fun bindData(category: CategoryModel){
            binding.nameTextView.text = category.name

            Glide.with(binding.coverImageView).load(category.coverUrl).apply(RequestOptions().transform(
                RoundedCorners(32))).into(binding.coverImageView)

            val context = binding.root.context
            binding.root.setOnClickListener {
                SongsListActivity.category = category
                context.startActivity(Intent(context, SongsListActivity::class.java))
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CategoryViewHolder {
        return CategoryViewHolder(
            CategoryItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(
        holder: CategoryViewHolder,
        position: Int
    ) {
        holder.bindData(categoryList[position])
    }

    override fun getItemCount(): Int = categoryList.size

}