package com.example.itsme.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.itsme.databinding.CardPreviewBinding

class CardAdapter(private val list:List<Int>, private val activity: FragmentActivity): RecyclerView.Adapter<CardViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val itemBinding = CardPreviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CardViewHolder(itemBinding, activity)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return 6
    }
}