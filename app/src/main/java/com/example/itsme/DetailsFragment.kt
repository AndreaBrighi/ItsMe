package com.example.itsme

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.widget.AppCompatSpinner
import androidx.fragment.app.Fragment
import com.example.itsme.databinding.FragmentDetailsBinding
import com.example.itsme.recyclerview.ElementType
import com.example.itsme.recyclerview.element.ContactAdapter
import com.google.android.material.textfield.TextInputEditText


/**
 * A simple [Fragment] subclass.
 */
class DetailsFragment : Fragment() {

    private lateinit var binding: FragmentDetailsBinding
    private lateinit var spinner: AppCompatSpinner
    private lateinit var nameEditText: TextInputEditText
    private lateinit var surnameEditText: TextInputEditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentDetailsBinding.inflate(inflater, container, false)

        nameEditText = binding.nameTextView
        nameEditText.setText("10")
        nameEditText.isEnabled = false

        surnameEditText = binding.nameTextView
        surnameEditText.setText("10")
        surnameEditText.isEnabled = false

        spinner = binding.typeSpinner
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.spinner_array_select,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }
        spinner.setSelection(0)
        spinner.isClickable = false
        spinner.isEnabled = false

        val adapter = ContactAdapter(ElementType.values().toList(), requireActivity())
        adapter.isEditable = false
        binding.contactRecyclerView.adapter = adapter

        binding.fabEdit.setOnClickListener {
            spinner.isClickable = true
            spinner.isEnabled = true
            nameEditText.isEnabled = true
            surnameEditText.isEnabled = true
            binding.fabEdit.hide()
            binding.fabSave.show()
            adapter.isEditable = true
        }

        binding.fabSave.setOnClickListener {
            spinner.isClickable = false
            spinner.isEnabled = false
            nameEditText.isEnabled = false
            surnameEditText.isEnabled = false
            binding.fabEdit.show()
            binding.fabSave.hide()
            adapter.isEditable = false
        }





        // Inflate the layout for this fragment
        return binding.root
    }
}