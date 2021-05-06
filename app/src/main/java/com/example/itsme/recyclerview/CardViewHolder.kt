package com.example.itsme.recyclerview

import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.itsme.R
import com.example.itsme.databinding.CardPreviewBinding
import com.example.itsme.ui.main.PlaceholderFragment

class CardViewHolder(private val itemBinding: CardPreviewBinding, private val activity: FragmentActivity) : RecyclerView.ViewHolder(itemBinding.root) {

    fun bind(card: Int) {
        itemBinding.nameView.text = card.toString() +" "+ itemBinding.nameView.text

        itemBinding.detailsButton.setOnClickListener {
          activity.supportFragmentManager.beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.action_container ,PlaceholderFragment(), null)
                .addToBackStack(this.javaClass.name)
                .commit()
        }

        itemBinding.sendButton.setOnClickListener {

        }

        itemBinding.shareButton.setOnClickListener {

        }
    }

}
