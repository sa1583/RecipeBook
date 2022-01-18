package com.example.recipebook.data

data class Ingredient(
    val recipeId: Long,
    val name: String,
    val amount: Int,
    val unit: Int
)
