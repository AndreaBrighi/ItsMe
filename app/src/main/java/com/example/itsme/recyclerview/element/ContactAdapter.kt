package com.example.itsme.recyclerview.element

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.itsme.databinding.ContactItemBinding
import com.example.itsme.model.ElementType

class ContactAdapter(
    private val elementsType: List<ElementType>,
    private val activity: FragmentActivity
) : RecyclerView.Adapter<ContactViewHolder>() {

    private val holders: MutableList<ContactViewHolder> = ArrayList()
    private var _isEditable = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val itemBinding =
            ContactItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val mLayoutManager: RecyclerView.LayoutManager =
            object : LinearLayoutManager(activity) {
                override fun canScrollVertically(): Boolean {
                    return false
                }
            }

        itemBinding.elementRecyclerView.layoutManager = mLayoutManager
        return ContactViewHolder(itemBinding, activity)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        holder.bind(elementsType[position], _isEditable)
        holders.add(holder)
    }

    override fun getItemCount(): Int {
        return elementsType.size
    }

    var isEditable: Boolean
        get() {
            return _isEditable
        }
        set(value) {
            _isEditable = value
            for (holder in holders) {
                holder.isEditable = value
            }
        }
}