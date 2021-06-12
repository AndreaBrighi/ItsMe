package com.example.itsme.recyclerview.element

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.itsme.databinding.ContactItemBinding
import com.example.itsme.db.BusinessCardWithElements
import com.example.itsme.model.ElementType

class ContactAdapter(
    private val elementsType: List<ElementType>,
    private val activity: FragmentActivity,
) : RecyclerView.Adapter<ContactViewHolder>() {

    private var cardItem: MutableLiveData<BusinessCardWithElements> = MutableLiveData()
    private val holders: MutableList<ContactViewHolder> = ArrayList()
    private var _state = States.ACTION

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

    fun setData(cardItem: MutableLiveData<BusinessCardWithElements>) {
        this.cardItem = cardItem
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        holder.bind(elementsType[position], cardItem)
        holder.state =_state
        holders.add(holder)
    }

    override fun getItemCount(): Int {
        return elementsType.size
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