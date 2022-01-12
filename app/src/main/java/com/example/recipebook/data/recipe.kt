package com.example.recipebook.data

import android.net.Uri
import androidx.room.*

@Entity(tableName = "recipe")
data class Recipe(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "name") val recipeName: String,
    @ColumnInfo(name = "image") val recipeImage: Uri
)

@Entity(tableName = "Ingredient")
data class Ingredient(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "recipe_id") val recipeId: Int,
    @ColumnInfo(name = "name") val ingredientName: String,
    @ColumnInfo(name = "amount") val ingredientAmount: Int,
    @ColumnInfo(name = "unit") val ingredientUnit: Int
)

data class RecipeWithIngredients(
    @Embedded val recipe: Recipe,
    @Relation(
        parentColumn = "id",
        entityColumn = "recipe_id"
    )
    val ingredientList: List<Ingredient>
)



