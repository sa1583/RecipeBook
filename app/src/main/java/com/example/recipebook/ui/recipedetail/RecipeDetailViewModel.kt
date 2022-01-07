package com.example.recipebook.ui.recipedetail

import androidx.lifecycle.*
import com.example.recipebook.data.RecipeDao
import com.example.recipebook.data.RecipeWithIngredients
import java.lang.IllegalArgumentException

class RecipeDetailViewModel(private val recipeDao: RecipeDao) : ViewModel() {

    fun retrieveRecipe(id: Int): LiveData<RecipeWithIngredients> {
        return recipeDao.getRecipeWithIngredients(id).asLiveData()
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