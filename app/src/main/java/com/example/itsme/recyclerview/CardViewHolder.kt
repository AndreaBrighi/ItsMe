package com.example.itsme.recyclerview

import android.content.Intent
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.commit
import androidx.recyclerview.widget.RecyclerView
import com.example.itsme.DetailsFragment
import com.example.itsme.FindActivity
import com.example.itsme.R
import com.example.itsme.databinding.CardPreviewBinding

class CardViewHolder(
    private val itemBinding: CardPreviewBinding,
    private val activity: FragmentActivity
) : RecyclerView.ViewHolder(itemBinding.root) {

    fun bind(card: Int) {
        itemBinding.nameView.text = card.toString() + " " + itemBinding.nameView.text

        itemBinding.detailsButton.setOnClickListener {
            activity.supportFragmentManager.commit {
                setReorderingAllowed(true)
                replace(R.id.fragment_container_view, DetailsFragment(), null)
                addToBackStack(this.javaClass.name)
            }
        }

        itemBinding.card.setOnClickListener{
            activity.supportFragmentManager.commit {
                setReorderingAllowed(true)
                replace(R.id.fragment_container_view, DetailsFragment(), null)
                addToBackStack(this.javaClass.name)
            }
        }

        itemBinding.sendButton.setOnClickListener {
            val intent = Intent(activity.baseContext, FindActivity::class.java)
            //intent.putExtra(EXTRA_ADDRESS, address)
            activity.startActivity(intent)
        }

        itemBinding.shareButton.setOnClickListener {

        }
    }

}
