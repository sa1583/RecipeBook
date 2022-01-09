package com.example.recipebook

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.get
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.recipebook.data.Ingredient
import com.example.recipebook.databinding.AddIngredientListItemBinding
import com.example.recipebook.databinding.IngredientListItemBinding

class IngredientModifyAdapter() :
    ListAdapter<Ingredient, IngredientModifyAdapter.IngredientViewHolder>(DiffCallback) {

    class IngredientViewHolder(private var binding: AddIngredientListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(ingredient: Ingredient) {
            binding.apply {
                ingredientName.setText(ingredient.ingredientName, TextView.BufferType.SPANNABLE)
                ingredientAmount.setText(ingredient.ingredientAmount, TextView.BufferType.SPANNABLE)
                // ingredientAmountUnit.get()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientModifyAdapter.IngredientViewHolder {
        return IngredientModifyAdapter.IngredientViewHolder(
            AddIngredientListItemBinding.inflate(LayoutInflater.from(parent.context))
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