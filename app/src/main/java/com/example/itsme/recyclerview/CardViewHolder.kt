package com.example.itsme.recyclerview

import androidx.recyclerview.widget.RecyclerView
import com.example.itsme.databinding.CardPreviewBinding

class CardViewHolder(private val itemBinding: CardPreviewBinding) : RecyclerView.ViewHolder(itemBinding.root) {

    fun bind(card: Int) {
        itemBinding.nameView.text = card.toString() +" "+ itemBinding.nameView.text
    }

}
