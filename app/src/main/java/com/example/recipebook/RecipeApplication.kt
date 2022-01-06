package com.example.recipebook

import android.app.Application
import com.example.recipebook.data.RecipeDatabase

class RecipeApplication : Application() {
    val database: RecipeDatabase by lazy { RecipeDatabase.getDatabase(this) }
}