package com.example.itsme.recyclerview.element

import android.content.res.Resources
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.itsme.EditDialogFragment
import com.example.itsme.R
import com.example.itsme.databinding.ContactItemBinding
import com.example.itsme.db.BusinessCardElement
import com.example.itsme.db.BusinessCardWithElements
import com.example.itsme.model.ElementType
import com.example.itsme.model.ElementTypes

class ContactViewHolder(
    private val itemBinding: ContactItemBinding,
    private val activity: FragmentActivity
) : RecyclerView.ViewHolder(itemBinding.root) {

    private var _state: States = States.ACTION
    private val res: Resources = activity.resources

    var state: States
        get() {
            return _state
        }
        set(value) {
            _state = value
            itemBinding.newButton.isEnabled = value.editButton()
            (itemBinding.elementRecyclerView.adapter as ElementAdapter).state = value
            if (_state == States.EDIT) {
                itemBinding.elementRecyclerView.visibility = View.VISIBLE
                itemBinding.openCloseImage.setImageDrawable(
                    ResourcesCompat.getDrawable(
                        res,
                        R.drawable.ic_baseline_arrow_drop_down_24,
                        activity.theme
                    )
                )
            }
        }

    fun bind(elementType: ElementType, card: MutableLiveData<BusinessCardWithElements>) {


        val mapPar = card.value?.elements?.groupBy { it.elementType }

        val list =
            if (mapPar == null || mapPar[elementType].isNullOrEmpty()) listOf() else mapPar[elementType]!!


        itemBinding.numberElementTextView.text =
            res.getQuantityString(R.plurals.elements, list.size, list.size)
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

        itemBinding.newButton.setOnClickListener {
            showEditDialog(elementType, card)
        }

        val adapter = ElementAdapter(activity, list)
        adapter.setData(card)
        adapter.state = _state
        itemBinding.elementRecyclerView.adapter = adapter
        if (_state == States.EDIT) {
            itemBinding.elementRecyclerView.visibility = View.VISIBLE
            itemBinding.openCloseImage.setImageDrawable(
                ResourcesCompat.getDrawable(
                    res,
                    R.drawable.ic_baseline_arrow_drop_down_24,
                    activity.theme
                )
            )
        }

    }

    private fun showEditDialog(
        element: ElementType,
        card: MutableLiveData<BusinessCardWithElements>,
    ) {
        val fm: FragmentManager = activity.supportFragmentManager
        val editNameDialogFragment: EditDialogFragment =
            EditDialogFragment.newInstance(res.getString(element.getTitle()), element.getInput())
        editNameDialogFragment.show(fm, "fragment_edit_name")
        fm.setFragmentResultListener("requestKey", activity) { key, bundle ->
            if (key == "requestKey") {
                card.value!!.elements.add(
                    BusinessCardElement(
                        card.value!!.card.uidC,
                        element as ElementTypes,
                        bundle.getString("value")!!
                    )
                )
                // Get result from bundle
                card.value = card.value
            }
        }
    }


}