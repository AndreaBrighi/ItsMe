package com.example.itsme

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatSpinner
import com.example.itsme.databinding.FragmentDetailsBinding
import com.example.itsme.recyclerview.ElementType
import com.example.itsme.recyclerview.element.ContactAdapter

class AddActivity : AppCompatActivity() {

    private lateinit var spinner: AppCompatSpinner
    private lateinit var binding: FragmentDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = FragmentDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.fabSave.show()
        binding.fabEdit.hide()

        val adapter = ContactAdapter(ElementType.values().toList(), this)
        adapter.isEditable = true
        binding.contactRecyclerView.adapter = adapter

        spinner = binding.typeSpinner
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            this,
            R.array.spinner_array_select,
            android.R.layout.simple_spinner_item
        ).also {
            // Specify the layout to use when the list of choices appears
            it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = it
        }
        spinner.setSelection(0)

    }
}