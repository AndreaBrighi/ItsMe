package com.example.itsme.recyclerview.element

import android.content.res.Resources
import android.view.View
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.itsme.databinding.ElementViewBinding
import com.example.itsme.model.ElementType

class ElementViewHolder(
    private val itemBinding: ElementViewBinding,
    private val activity: FragmentActivity,
    private val elementType: ElementType
) : RecyclerView.ViewHolder(itemBinding.root) {

    private var _isEditable = false

    fun bind(position: Int, editable: Boolean) {

        val res: Resources = activity.resources

        itemBinding.actionButton.setImageDrawable(
            ResourcesCompat.getDrawable(
                res,
                elementType.getIcon(),
                activity.theme
            )
        )

        itemBinding.actionButton.setOnClickListener {
            try {
                activity.startActivity(elementType.getIntent("222"))
            } catch (e: Exception) {
                Toast.makeText(
                    activity,
                    "Unable to execute the action",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        isEditable = editable
    }

    var isEditable: Boolean
        get() {
            return _isEditable
        }
        set(value) {
            _isEditable = value
            val visibility: Int = if (value) View.VISIBLE else View.INVISIBLE
            itemBinding.editButton.isEnabled = value
            itemBinding.deleteButton.isEnabled = value
        }
}
