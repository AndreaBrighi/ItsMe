package com.example.itsme.recyclerview.element

import android.content.res.Resources
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.itsme.databinding.ElementViewBinding
import com.example.itsme.recyclerview.Element

class ElementViewHolder(
    private val itemBinding: ElementViewBinding,
    private val activity: FragmentActivity,
    private val element: Element
) : RecyclerView.ViewHolder(itemBinding.root) {

    fun bind(position: Int) {

        val res: Resources = activity.resources

        itemBinding.actionButton.setImageDrawable(
            ResourcesCompat.getDrawable(
                res,
                element.getIcon(),
                activity.theme
            )
        )

        itemBinding.actionButton.setOnClickListener {
            try {
                activity.startActivity(element.getIntent("222"))
            } catch (e: Exception) {
                Toast.makeText(
                    activity,
                    "Unable to execute the action",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}
