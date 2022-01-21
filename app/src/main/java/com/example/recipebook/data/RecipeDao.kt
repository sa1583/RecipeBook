package com.example.recipebook.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeDao {
    @Query("SELECT * FROM recipe ORDER BY name ASC")
    fun getAllRecipe(): Flow<List<Recipe>>

    @Transaction
    @Query("SELECT * FROM recipe WHERE id = :id")
    fun getRecipeWithIngredients(id: Long): Flow<RecipeWithIngredients>

    @Query("SELECT * FROM Ingredient WHERE recipe_id = :id")
    fun getIngredientsByRecipeId(id: Long): Flow<List<IngredientDB>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addRecipe(recipe: Recipe): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addIngredient(ingredientDB: IngredientDB)

    @Update
    suspend fun updateIngredients(ingredientDBs: List<IngredientDB>)

    @Update
    suspend fun updateRecipe(recipe: Recipe)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addIngredients(ingredientDBS: List<IngredientDB>)

    @Delete
    suspend fun deleteRecipe(recipe: Recipe)

    @Delete
    suspend fun deleteIngredients(ingredientDBS: List<IngredientDB>)
}