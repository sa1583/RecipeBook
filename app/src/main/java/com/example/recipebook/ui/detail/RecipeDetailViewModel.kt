package com.example.recipebook.ui.detail

import androidx.lifecycle.*
import com.example.recipebook.data.*

class RecipeDetailViewModel(private val recipeDao: RecipeDao) : ViewModel() {
    init {

    }

    private fun getIngredient(recipeId: Long, name: String, amount: Int, unit: Int): Ingredient {
        return Ingredient(
            recipeId = recipeId,
            name = name,
            amount = amount,
            unit = unit
        )
    }

    fun retrieveRecipe(id: Long): LiveData<RecipeWithIngredients> {
        return recipeDao.getRecipeWithIngredients(id).asLiveData()
    }

    fun ingredientDBsToIngredients(ingredientDBs: List<IngredientDB>): List<Ingredient> {
        val ingredients = mutableListOf<Ingredient>()
        for (ingredientDB in ingredientDBs) {
            // Log.d("RecipeDetailViewModel", "${ingredientDB.id} ${ingredientDB.ingredientName}")
            ingredients.add(
                getIngredient(
                    ingredientDB.recipeId,
                    ingredientDB.ingredientName,
                    ingredientDB.ingredientAmount,
                    ingredientDB.ingredientUnit
                )
            )
        }
        // Log.d("RecipeDetailViewModel", ingredients.size.toString())
        return ingredients
    }
}

class RecipeDetailViewModelFactory(private val recipeDao: RecipeDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RecipeDetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RecipeDetailViewModel(recipeDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}