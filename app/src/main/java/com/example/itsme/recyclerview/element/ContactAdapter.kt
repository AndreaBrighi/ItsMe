package com.example.itsme.recyclerview.element

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.itsme.databinding.ContactItemBinding
import com.example.itsme.recyclerview.Element

class ContactAdapter(
    private val elementsType: List<Element>,
    private val activity: FragmentActivity
): RecyclerView.Adapter<ContactViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val itemBinding = ContactItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ContactViewHolder(itemBinding, activity)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        holder.bind(elementsType[position])
    }

    override fun getItemCount(): Int {
        return elementsType.size
    }
}