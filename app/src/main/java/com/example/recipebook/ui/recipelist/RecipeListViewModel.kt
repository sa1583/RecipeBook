package com.example.recipebook.ui.recipelist

import androidx.lifecycle.*
import com.example.recipebook.data.Recipe
import com.example.recipebook.data.RecipeDao
import java.lang.IllegalArgumentException

class RecipeListViewModel(private val recipeDao: RecipeDao): ViewModel() {
    val allRecipes: LiveData<List<Recipe>> = recipeDao.getAllRecipe().asLiveData()

}

class RecipeListViewModelFactory(private val recipeDao: RecipeDao): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RecipeListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RecipeListViewModel(recipeDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}