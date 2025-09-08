package com.mycompany.musicstream.models

data class CategoryModel(
    var name: String = "",
    val coverUrl: String = "",
    var songs: List<String> = listOf()
)
