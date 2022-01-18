package com.example.recipebook.data

import androidx.room.*

@Entity(tableName = "recipe")
data class Recipe(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "name") val recipeName: String,
    @ColumnInfo(name = "image") val recipeImageUri: String
)

@Entity(tableName = "Ingredient")
data class IngredientDB(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "recipe_id") val recipeId: Long,
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
    val ingredientDBList: List<IngredientDB>
)



