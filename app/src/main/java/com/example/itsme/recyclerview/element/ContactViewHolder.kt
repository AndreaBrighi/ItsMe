package com.example.itsme.recyclerview.element

import android.content.res.Resources
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.itsme.R
import com.example.itsme.databinding.ContactItemBinding
import com.example.itsme.model.ElementType
import com.example.itsme.model.ElementTypes

class ContactViewHolder(
    private val itemBinding: ContactItemBinding,
    private val activity: FragmentActivity
) : RecyclerView.ViewHolder(itemBinding.root) {

    private var _isEditable = false

    var isEditable: Boolean
        get() {
            return _isEditable
        }
        set(value) {
            _isEditable = value
            itemBinding.newButton.isEnabled = value// if (value) View.VISIBLE else View.INVISIBLE
            (itemBinding.elementRecyclerView.adapter as ElementAdapter).isEditable = value

        }

    fun bind(elementType: ElementType, editable: Boolean) {

        val res: Resources = activity.resources

        val number = 1


        itemBinding.numberElementTextView.text =
            res.getQuantityString(R.plurals.elements, number, number)
        itemBinding.typeTextView.text = res.getString(elementType.getTitle())

        itemBinding.clickableView.setOnClickListener {
            when (itemBinding.elementRecyclerView.visibility) {
                View.VISIBLE -> {
                    itemBinding.elementRecyclerView.visibility = View.GONE
                    itemBinding.openCloseImage.setImageDrawable(
                        ResourcesCompat.getDrawable(
                            res,
                            R.drawable.ic_baseline_arrow_right_24,
                            activity.theme
                        )
                    )
                }
                View.GONE -> {
                    itemBinding.elementRecyclerView.visibility = View.VISIBLE
                    itemBinding.openCloseImage.setImageDrawable(
                        ResourcesCompat.getDrawable(
                            res,
                            R.drawable.ic_baseline_arrow_drop_down_24,
                            activity.theme
                        )
                    )
                }
                View.INVISIBLE -> {
                }
            }
        }

        itemBinding.elementRecyclerView.adapter =
            ElementAdapter(ElementTypes.values().toList(), activity, elementType)

        isEditable = editable
    }


}