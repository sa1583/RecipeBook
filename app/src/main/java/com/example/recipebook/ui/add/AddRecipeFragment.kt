package com.example.recipebook.ui.add

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recipebook.IngredientListAdapter
import com.example.recipebook.MainActivity
import com.example.recipebook.R
import com.example.recipebook.RecipeApplication
import com.example.recipebook.data.RecipeWithIngredients
import com.example.recipebook.databinding.FragmentAddRecipeBinding

const val PERMISSION_REQUEST_READ_STORAGE = 0
const val TAG = "AddRecipeFragment"

class AddRecipeFragment : Fragment() {
    private var _binding: FragmentAddRecipeBinding? = null
    private val binding get() = _binding!!
    private val navigationArgs: AddRecipeFragmentArgs by navArgs()
    private val viewModel: AddRecipeViewModel by activityViewModels {
        AddRecipeViewModelFactory(
            (activity?.application as RecipeApplication).database.recipeDao()
        )
    }
    private val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == RESULT_OK) {
            binding.recipeImage.setImageURI(it.data?.data)
            viewModel.setImageUri(it.data?.data)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddRecipeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Recipe 수정 시 id로 세팅
        val id: Long = navigationArgs.id
        if (id > 0) {
            viewModel.retrieveRecipeWithIngredients(id).observe(this.viewLifecycleOwner) { selectedRecipe ->
                bind(selectedRecipe)
                viewModel.addIngredients(selectedRecipe.ingredientDBList)
            }
        }
        // 재료 추가 스피너 설정
        ArrayAdapter.createFromResource(
            requireContext(), R.array.unitList, android.R.layout.simple_spinner_item)
            .also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.addIngredient.ingredientAmountUnit.adapter = adapter
            }
        // 재료 리스트
        val adapter = IngredientListAdapter()
        binding.ingredientsList.adapter = adapter
        viewModel.ingredientList.observe(this.viewLifecycleOwner) { ingredients ->
            ingredients.let {
                adapter.submitList(it)
            }
        }
        // layout manager
        binding.ingredientsList.layoutManager = LinearLayoutManager(this.context)
        // 이미지 추가
        binding.recipeImage.setOnClickListener { checkPermission() }
        // 재료 추가
        binding.addIngredient.btnAddIngredient.setOnClickListener {
            val ingredientName = binding.addIngredient.ingredientName.text.toString()
            val ingredientAmount = binding.addIngredient.ingredientAmount.text.toString().toInt()
            val ingredientUnit = binding.addIngredient.ingredientAmountUnit.selectedItemId.toInt()
            viewModel.addNewIngredient(ingredientName, ingredientAmount, ingredientUnit)
        }
        // 레시피 추가
        binding.btnAddRecipe.setOnClickListener {
            val recipeName = binding.recipeName.text.toString()
            val recipeImageUri = viewModel.imageUri
            viewModel.addNewRecipe(recipeName, recipeImageUri)
            val action = AddRecipeFragmentDirections.actionAddRecipeToRecipeList()
            this.findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun bind(recipeWithIngredients: RecipeWithIngredients) {
        val recipe = recipeWithIngredients.recipe
        binding.apply {
            recipeName.setText(recipe.recipeName, TextView.BufferType.SPANNABLE)
            recipeImage.setImageURI(Uri.parse(recipe.recipeImageUri))
        }
    }

    private fun checkPermission() {
        if (ContextCompat.checkSelfPermission(
                activity as MainActivity,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                activity as MainActivity,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                PERMISSION_REQUEST_READ_STORAGE
            )
        }
        openGallery()
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        resultLauncher.launch(intent)
    }
}