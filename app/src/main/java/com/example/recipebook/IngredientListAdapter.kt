package com.example.recipebook

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.recipebook.data.Ingredient
import com.example.recipebook.databinding.IngredientListItemBinding

class IngredientListAdapter :
    ListAdapter<Ingredient, IngredientListAdapter.IngredientViewHolder>(DiffCallback) {

    class IngredientViewHolder(private var binding: IngredientListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(ingredient: Ingredient, resources: Resources) {
            binding.apply {
                ingredientName.text = ingredient.name
                ingredientAmount.text = ingredient.amount.toString()
                ingredientAmountUnit.text = resources.getStringArray(R.array.unitList)[ingredient.unit]
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientViewHolder {
        return IngredientViewHolder(
            IngredientListItemBinding.inflate(LayoutInflater.from(parent.context))
        )
    }

    override fun onBindViewHolder(holder: IngredientViewHolder, position: Int) {
        val resources = holder.itemView.resources
        holder.bind(getItem(position), resources)
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Ingredient>() {
            override fun areItemsTheSame(oldItem: Ingredient, newItem: Ingredient): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Ingredient, newItem: Ingredient): Boolean {
                return oldItem.name == newItem.name
            }
        }
    }
}