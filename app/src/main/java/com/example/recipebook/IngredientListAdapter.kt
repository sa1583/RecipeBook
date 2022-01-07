package com.example.recipebook

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.recipebook.data.Ingredient
import com.example.recipebook.databinding.IngredientListItemBinding

class IngredientListAdapter() :
    ListAdapter<Ingredient, IngredientListAdapter.IngredientViewHolder>(DiffCallback) {

    class IngredientViewHolder(private var binding: IngredientListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(ingredient: Ingredient) {
            binding.apply {
                ingredientName.text = ingredient.ingredientName
                ingredientAmount.text = ingredient.ingredientAmount.toString()
                ingredientAmountUnit.text = ingredient.ingredientUnit
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientListAdapter.IngredientViewHolder {
        return IngredientListAdapter.IngredientViewHolder(
            IngredientListItemBinding.inflate(LayoutInflater.from(parent.context))
        )
    }

    override fun onBindViewHolder(holder: IngredientViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Ingredient>() {
            override fun areItemsTheSame(oldItem: Ingredient, newItem: Ingredient): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Ingredient, newItem: Ingredient): Boolean {
                return oldItem.ingredientName == newItem.ingredientName
            }
        }
    }
}