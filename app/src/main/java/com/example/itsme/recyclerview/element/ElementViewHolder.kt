package com.example.itsme.recyclerview.element

import android.content.DialogInterface
import android.content.res.Resources
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.itsme.EditDialogFragment
import com.example.itsme.R
import com.example.itsme.databinding.ElementViewBinding
import com.example.itsme.db.BusinessCardElement
import com.example.itsme.db.BusinessCardWithElements
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class ElementViewHolder(
    private val itemBinding: ElementViewBinding,
    private val activity: FragmentActivity
) : RecyclerView.ViewHolder(itemBinding.root) {

    private var _state = States.ACTION
    private val res: Resources = activity.resources

    fun bind(element: BusinessCardElement, cardItem: MutableLiveData<BusinessCardWithElements>) {

        itemBinding.InfoTextView.text = element.value

        itemBinding.actionButton.setImageDrawable(
            ResourcesCompat.getDrawable(
                res,
                element.elementType.getIcon(),
                activity.theme
            )
        )

        itemBinding.deleteButton.setOnClickListener{
            MaterialAlertDialogBuilder(activity)
                .setTitle(res.getString(R.string.delete))
                .setMessage(res.getString(R.string.delete_info,element.value,res.getString(element.elementType.getTitle())))
                .setPositiveButton(res.getString(R.string.delete)) { _: DialogInterface, _: Int ->
                    cardItem.value!!.elements.remove(element).also { cardItem.value=cardItem.value }
                }
                .setNegativeButton(res.getString(R.string.keep)) { di: DialogInterface, _: Int ->
                    di.dismiss()
                }.show()
        }

        itemBinding.editButton.setOnClickListener{
            showEditDialog(element, cardItem)
        }

        itemBinding.actionButton.setOnClickListener {
            try {
                activity.startActivity(element.elementType.getIntent(element.value))
            } catch (e: Exception) {
                Toast.makeText(
                    activity,
                    res.getString(R.string.error_action),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun showEditDialog(
        element: BusinessCardElement,
        card: MutableLiveData<BusinessCardWithElements> ) {
        val fm: FragmentManager = activity.supportFragmentManager
        val editNameDialogFragment: EditDialogFragment =
            EditDialogFragment.newInstance(res.getString(element.elementType.getTitle()), element.elementType.getInput() ,element.value )
        editNameDialogFragment.show(fm, "fragment_edit_name")
        fm.setFragmentResultListener("requestKey", activity) { key, bundle ->
            if (key == "requestKey") {
                card.value!!.elements.find { it.uid == element.uid }!!.value = bundle.getString("value")!!
                // Get result from bundle
                card.value = card.value
            }
        }
    }

    var state: States
        get() {
            return _state
        }
        set(value) {
            _state = value
            itemBinding.editButton.isEnabled = value.editButton()
            itemBinding.deleteButton.isEnabled = value.editButton()
            itemBinding.actionButton.isEnabled = value.actionButton()
        }
}
