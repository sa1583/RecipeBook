package com.example.recipebook.ui.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.example.recipebook.data.Recipe
import com.example.recipebook.data.RecipeDao

class RecipeListViewModel(private val recipeDao: RecipeDao) : ViewModel() {
    val allRecipes: LiveData<List<Recipe>> = recipeDao.getAllRecipe().asLiveData()

}

class RecipeListViewModelFactory(private val recipeDao: RecipeDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RecipeListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RecipeListViewModel(recipeDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}