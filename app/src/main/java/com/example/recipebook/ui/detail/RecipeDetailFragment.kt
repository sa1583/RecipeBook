package com.example.recipebook.ui.detail

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recipebook.IngredientListAdapter
import com.example.recipebook.R
import com.example.recipebook.RecipeApplication
import com.example.recipebook.data.Recipe
import com.example.recipebook.data.RecipeWithIngredients
import com.example.recipebook.databinding.FragmentRecipeDetailBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

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

    private var id = 0L

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
        binding.ingredientsList.adapter = IngredientListAdapter {}
        binding.ingredientsList.layoutManager = LinearLayoutManager(this.context)
        id = navigationArs.recipeId
        viewModel.retrieveRecipe(id).observe(this.viewLifecycleOwner) { selectedRecipe ->
            recipeWithIngredients = selectedRecipe
            bind(recipeWithIngredients)
            setRecyclerview()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun onClickModify() {
        val action = RecipeDetailFragmentDirections.actionRecipeDetailToAddRecipe(id, "수정")
        findNavController().navigate(action)
    }

    private fun setRecyclerview() {
        val adapter = IngredientListAdapter {}
        val ingredients = viewModel.ingredientDBsToIngredients(recipeWithIngredients.ingredientDBList)
        binding.ingredientsList.adapter = adapter
        adapter.submitList(ingredients)
    }

    private fun showConfirmationDialog(recipe: Recipe) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(android.R.string.dialog_alert_title))
            .setMessage(getString(R.string.dialog_remove_ingredient))
            .setCancelable(false)
            .setNegativeButton(R.string.dialog_negative) { _, _ -> }
            .setPositiveButton(R.string.dialog_positive) { _, _ ->
                viewModel.deleteRecipe(recipe)
                val action = RecipeDetailFragmentDirections.actionRecipeDetailFragmentToRecipeListFragment()
                findNavController().navigate(action)
            }
            .show()
    }

    private fun bind(recipeWithIngredients: RecipeWithIngredients) {
        binding.apply {
            recipeName.text = recipeWithIngredients.recipe.recipeName
            recipeImage.setImageURI(Uri.parse(recipeWithIngredients.recipe.recipeImageUri))
            btnModifyRecipe.setOnClickListener { onClickModify() }
            btnDeleteRecipe.setOnClickListener { showConfirmationDialog(recipeWithIngredients.recipe) }
        }
    }
}