package com.example.recipebook.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.example.recipebook.IngredientListAdapter
import com.example.recipebook.RecipeApplication
import com.example.recipebook.data.RecipeWithIngredients
import com.example.recipebook.databinding.FragmentRecipeDetailBinding

class RecipeDetailFragment : Fragment() {
    private val navigationArs: RecipeDetailFragmentArgs by navArgs()
    private var _binding: FragmentRecipeDetailBinding? = null
    private val binding get() = _binding!!
    lateinit var recipeWithIngredients: RecipeWithIngredients
    private val viewModel: RecipeDetailViewModel by activityViewModels {
        RecipeDetailViewModelFactory(
            (activity?.application as RecipeApplication).database.recipeDao()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipeDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.ingredientsList.adapter = IngredientListAdapter()
        val id = navigationArs.recipeId
        viewModel.retrieveRecipe(id).observe(this.viewLifecycleOwner) { selectedRecipe ->
            recipeWithIngredients = selectedRecipe
            bind(recipeWithIngredients)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun bind(recipeWithIngredients: RecipeWithIngredients) {
        binding.apply {
            recipeName.text = recipeWithIngredients.recipe.recipeName
            // recipeImage.setImageResource(recipeWithIngredients.recipe.recipeImage)
        }
    }
}