package com.example.itsme.recyclerview.element

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.itsme.databinding.ElementViewBinding
import com.example.itsme.db.BusinessCardElement
import com.example.itsme.db.BusinessCardWithElements

class ElementAdapter(
    private val activity: FragmentActivity,
    private val list: List<BusinessCardElement>
) : RecyclerView.Adapter<ElementViewHolder>() {

    private var cardItem = MutableLiveData<BusinessCardWithElements>()
    private val holders: MutableList<ElementViewHolder> = ArrayList()
    private var _state = States.ACTION

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ElementViewHolder {
        val itemBinding =
            ElementViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ElementViewHolder(itemBinding, activity)
    }

    override fun onBindViewHolder(holder: ElementViewHolder, position: Int) {
        holder.bind(list[position], cardItem)
        holder.state = state
        holders.add(holder)
    }

    fun setData(cardItem: MutableLiveData<BusinessCardWithElements>) {
        this.cardItem = cardItem
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return list.size
    }

    var state: States
        get() {
            return _state
        }
        set(value) {
            _state = value
            for (holder in holders) {
                holder.state = value
            }
        }
}