package com.example.recipebook.data

import androidx.room.*

@Entity(tableName = "recipe")
data class Recipe(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "name") val recipeName: String,
    @ColumnInfo(name = "image") val recipeImage: Int
)

@Entity(tableName = "Ingredient")
data class Ingredient(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "recipe_id") val recipeId: Int,
    @ColumnInfo(name = "name") val ingredientName: String,
    @ColumnInfo(name = "amount") val ingredientAmount: Int,
    @ColumnInfo(name = "unit") val ingredientUnit: String
)

data class RecipeWithIngredients(
    @Embedded val recipe: Recipe,
    @Relation(
        parentColumn = "id",
        entityColumn = "recipeId"
    )
    val ingredientList: List<Ingredient>
)



