package com.example.recipebook.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeDao {
    @Query("SELECT * FROM recipe ORDER BY name ASC")
    fun getAllRecipe(): Flow<List<Recipe>>

    @Transaction
    @Query("SELECT * FROM recipe WHERE id = :id")
    fun getRecipeWithIngredients(id: Int): Flow<RecipeWithIngredients>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addRecipe(recipe: Recipe)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addIngredient(ingredient: Ingredient)

    @Update
    suspend fun updateRecipe(recipe: Recipe)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addIngredients(ingredients: List<Ingredient>)

    @Delete
    suspend fun deleteRecipe(recipe: Recipe)

    @Delete
    suspend fun deleteIngredients(ingredients: List<Ingredient>)
}