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

    @Transaction
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addRecipe(recipeWithIngredients: RecipeWithIngredients)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addIngredient(ingredient: Ingredient)

    @Transaction
    @Update
    suspend fun updateRecipe(recipeWithIngredients: RecipeWithIngredients)

    @Transaction
    @Delete
    suspend fun deleteRecipe(recipeWithIngredients: RecipeWithIngredients)
}