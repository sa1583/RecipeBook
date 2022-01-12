package com.example.recipebook.ui.add

import android.net.Uri
import androidx.lifecycle.*
import com.example.recipebook.data.Ingredient
import com.example.recipebook.data.Recipe
import com.example.recipebook.data.RecipeDao
import com.example.recipebook.data.RecipeWithIngredients
import kotlinx.coroutines.launch

class AddRecipeViewModel(private val recipeDao: RecipeDao) : ViewModel() {

    private fun getNewRecipe(recipeName: String, recipeImage: Uri): Recipe {
        return Recipe(
            recipeName = recipeName,
            recipeImage = recipeImage
        )
    }

    private fun getNewIngredient(
        recipeId: Int,
        ingredientName: String,
        ingredientAmount: Int,
        ingredientAmountUnit: Int
    ): Ingredient {
        return Ingredient(
            recipeId = recipeId,
            ingredientName = ingredientName,
            ingredientAmount = ingredientAmount,
            ingredientUnit = ingredientAmountUnit
        )
    }

    private fun insertRecipeAndIngredient(
        recipe: Recipe,
        ingredientNames: List<String>,
        ingredientAmounts: List<Int>,
        ingredientAmountUnits: List<Int>
    ) {
        viewModelScope.launch {
            val id = recipeDao.addRecipe(recipe)
            val ingredients = mutableListOf<Ingredient>()
            for (i: Int in ingredientNames.indices) {
                ingredients.add(
                    getNewIngredient(
                        id,
                        ingredientNames[i],
                        ingredientAmounts[i],
                        ingredientAmountUnits[i]
                    )
                )
            }
            recipeDao.addIngredients(ingredients)
        }
    }

    fun retrieveRecipe(id: Int): LiveData<RecipeWithIngredients> {
        return recipeDao.getRecipeWithIngredients(id).asLiveData()
    }

    fun addNewRecipe(
        recipeName: String,
        recipeImage: Uri,
        ingredientNames: List<String>,
        ingredientAmounts: List<Int>,
        ingredientAmountUnits: List<Int>
    ) {
        val recipe = getNewRecipe(recipeName, recipeImage)
        insertRecipeAndIngredient(recipe, ingredientNames, ingredientAmounts, ingredientAmountUnits)
    }
}

class AddRecipeViewModelFactory(private val recipeDao: RecipeDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddRecipeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AddRecipeViewModel(recipeDao) as T
        }
        throw IllegalArgumentException("Unknown viewModel class")
    }
}