package com.example.itsme.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.itsme.R

class CardAdapter(private val list:List<Int>): RecyclerView.Adapter<CardViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val layoutView: View = LayoutInflater.from(parent.context).inflate(R.layout.card_preview, parent, false)
        return CardViewHolder(layoutView)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
    }

    override fun getItemCount(): Int {
        return list.size
    }
}