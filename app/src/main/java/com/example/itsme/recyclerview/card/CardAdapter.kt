package com.example.itsme.recyclerview.card

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.itsme.databinding.CardPreviewBinding
import com.example.itsme.db.BusinessCardWithElements
import java.util.*

class CardAdapter(
    private val activity: FragmentActivity,
    private val config: Boolean
): RecyclerView.Adapter<CardViewHolder>() {

    private var cardItemList: List<BusinessCardWithElements> = ArrayList<BusinessCardWithElements>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val itemBinding = CardPreviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CardViewHolder(itemBinding, activity, config)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        holder.bind(cardItemList[position])
    }

    override fun getItemCount(): Int {
        return cardItemList.size
    }

    fun setData(cardItems: List<BusinessCardWithElements>?) {
        cardItemList = ArrayList(cardItems)
        notifyDataSetChanged()
    }
}