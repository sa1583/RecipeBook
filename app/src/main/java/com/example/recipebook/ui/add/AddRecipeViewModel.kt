package com.example.recipebook.ui.add

import android.net.Uri
import android.util.Log
import androidx.lifecycle.*
import com.example.recipebook.data.*
import kotlinx.coroutines.launch
import java.time.temporal.TemporalAmount

class AddRecipeViewModel(private val recipeDao: RecipeDao) : ViewModel() {
    // TODO: 재료목록이 실시간으로 업데이트 되지 않음
    private val ingredients = mutableListOf<Ingredient>()
    private val _ingredientList = MutableLiveData<List<Ingredient>>()
    val ingredientList: LiveData<List<Ingredient>> = _ingredientList
    private var _imageUri: Uri? = Uri.parse("android.resource://com.example.recipebook/drawable/ic_recipe_image_default")
    val imageUri: String get() = _imageUri.toString()

    init {
        _ingredientList.value = ingredients
    }

    fun addIngredients(ingredientDBList: List<IngredientDB>) {
        for (ingredientDB in ingredientDBList) {
            val ingredient = ingredientDBToIngredient(ingredientDB)
            addIngredient(ingredient)
        }
    }

    private fun addIngredient(ingredient: Ingredient) {
        ingredients.add(ingredient)
        _ingredientList.value = ingredients
    }

    fun removeIngredients() {
        ingredients.clear()
        _ingredientList.value = ingredients
    }

    fun removeIngredient(ingredient: Ingredient) {
        ingredients.remove(ingredient)
        _ingredientList.value = ingredients
    }

    fun setImageUri(uri: Uri?) {
        _imageUri = uri
    }

    private fun ingredientDBToIngredient(ingredientDB: IngredientDB): Ingredient {
        return Ingredient(
            recipeId = ingredientDB.recipeId,
            name = ingredientDB.ingredientName,
            amount = ingredientDB.ingredientAmount,
            unit = ingredientDB.ingredientUnit
        )
    }

    private fun ingredientToIngredientDB(ingredient: Ingredient, id: Long): IngredientDB {
        return IngredientDB(
            recipeId = id,
            ingredientName = ingredient.name,
            ingredientAmount = ingredient.amount,
            ingredientUnit = ingredient.unit
        )
    }

    private fun getNewRecipe(id: Long, recipeName: String, recipeImage: String): Recipe {
        return Recipe(
            id = id,
            recipeName = recipeName,
            recipeImageUri = recipeImage
        )
    }

    private fun getNewIngredient(name: String, amount: Int, unit: Int): Ingredient {
        return Ingredient(
            recipeId = 0L,
            name = name,
            amount = amount,
            unit = unit
        )
    }

    private fun setIngredientsRecipeId(id: Long): List<IngredientDB> {
        val ingredientDB = mutableListOf<IngredientDB>()
        for (ingredient in ingredients) {
            ingredientDB.add(ingredientToIngredientDB(ingredient, id))
        }
        return ingredientDB
    }

    private fun insertRecipeAndIngredient(
        recipe: Recipe
    ) {
        viewModelScope.launch {
            val id = recipeDao.addRecipe(recipe)
            val ingredientDBs = setIngredientsRecipeId(id)
            addAndUpdateIngredients(ingredientDBs)
        }
    }

    private fun addAndUpdateIngredients(ingredientDBs: List<IngredientDB>) {
        val newIngredientDBs = mutableListOf<IngredientDB>()
        val updateIngredientDBs = mutableListOf<IngredientDB>()
        for (ingredient in ingredientDBs) {
            if (ingredient.id == 0L) {
                newIngredientDBs.add(ingredient)
            } else {
                updateIngredientDBs.add(ingredient)
            }
        }
        viewModelScope.launch {
            recipeDao.addIngredients(newIngredientDBs)
            recipeDao.updateIngredients(updateIngredientDBs)
        }
    }

    fun retrieveRecipeWithIngredients(id: Long): LiveData<RecipeWithIngredients> {
        return recipeDao.getRecipeWithIngredients(id).asLiveData()
    }

    fun addNewRecipe(
        recipeName: String,
        recipeImageUri: String
    ) {
        val recipe = getNewRecipe(0L, recipeName, recipeImageUri)
        insertRecipeAndIngredient(recipe)
    }

    fun modifyRecipe(recipeId: Long, recipeName: String, recipeImageUri: String) {
        val updatedRecipe = getNewRecipe(recipeId, recipeName, recipeImageUri)
        viewModelScope.launch {
            recipeDao.updateRecipe(updatedRecipe)
        }

    }

    fun addNewIngredient(name: String, amount: Int, unit: Int) {
        val newIngredient = getNewIngredient(name, amount, unit)
        addIngredient(newIngredient)
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