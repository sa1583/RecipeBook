package com.example.recipebook.ui.detail

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
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
        binding.ingredientsList.layoutManager = LinearLayoutManager(this.context)
        val id = navigationArs.recipeId
        viewModel.retrieveRecipe(id).observe(this.viewLifecycleOwner) { selectedRecipe ->
            recipeWithIngredients = selectedRecipe
            bind(recipeWithIngredients)
        }
        binding.btnModifyRecipe.setOnClickListener {
            val action = RecipeDetailFragmentDirections.actionRecipeDetailToAddRecipe(id, "수정")
            findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun bind(recipeWithIngredients: RecipeWithIngredients) {
        val adapter = IngredientListAdapter()
        val ingredients = viewModel.ingredientDBsToIngredients(recipeWithIngredients.ingredientDBList)
        binding.apply {
            recipeName.text = recipeWithIngredients.recipe.recipeName
            recipeImage.setImageURI(Uri.parse(recipeWithIngredients.recipe.recipeImageUri))
            ingredientsList.adapter = adapter
            adapter.submitList(ingredients)
        }
    }
}