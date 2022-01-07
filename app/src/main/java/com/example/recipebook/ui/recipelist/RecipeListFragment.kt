package com.example.recipebook.ui.recipelist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recipebook.RecipeApplication
import com.example.recipebook.RecipeListAdapter
import com.example.recipebook.databinding.FragmentRecipeListBinding

class RecipeListFragment : Fragment() {
    private var _binding: FragmentRecipeListBinding? = null
    private val binding get() = _binding!!
    private val viewModel: RecipeListViewModel by activityViewModels {
        RecipeListViewModelFactory(
            (activity?.application as RecipeApplication).database.recipeDao()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipeListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = RecipeListAdapter {
            val action = RecipeListFragmentDirections.actionRecipeListToRecipeDetail(it.id)
            this.findNavController().navigate(action)
        }
        binding.recipeList.adapter = adapter
        viewModel.allRecipes.observe(this.viewLifecycleOwner) { recipes ->
            recipes.let {
                adapter.submitList(it)
            }
        }
        binding.recipeList.layoutManager = LinearLayoutManager(this.context)
        binding.addRecipeFloatingButton.setOnClickListener {
            val action = RecipeListFragmentDirections.actionRecipeListToAddRecipe()
            this.findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}