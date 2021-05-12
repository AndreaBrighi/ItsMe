package com.example.itsme.recyclerview.element

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.itsme.databinding.ElementViewBinding
import com.example.itsme.recyclerview.Element

class ElementAdapter(
    private val elementsType: List<Element>,
    private val activity: FragmentActivity,
    private val element: Element
) : RecyclerView.Adapter<ElementViewHolder>() {

    private val holders: MutableList<ElementViewHolder> = ArrayList()
    private var _isEditable = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ElementViewHolder {
        val itemBinding =
            ElementViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ElementViewHolder(itemBinding, activity, element)
    }

    override fun onBindViewHolder(holder: ElementViewHolder, position: Int) {
        holder.bind(position, _isEditable)
        holders.add(holder)
    }

    override fun getItemCount(): Int {
        return 3
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