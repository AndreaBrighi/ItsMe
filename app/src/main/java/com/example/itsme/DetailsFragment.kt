package com.example.itsme

import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.AppCompatSpinner
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.example.itsme.databinding.FragmentDetailsBinding
import com.example.itsme.db.BusinessCardWithElements
import com.example.itsme.model.CardTypes
import com.example.itsme.model.ElementTypes
import com.example.itsme.model.Utilities
import com.example.itsme.recyclerview.element.ContactAdapter
import com.example.itsme.recyclerview.element.States
import com.example.itsme.viewModel.ListViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText


/**
 * A simple [Fragment] subclass.
 */
class DetailsFragment(private val config: Boolean = false) : Fragment() {

    private lateinit var binding: FragmentDetailsBinding
    private lateinit var spinner: AppCompatSpinner
    private lateinit var fistNameEditText: TextInputEditText
    private lateinit var lastNameEditText: TextInputEditText
    private lateinit var listViewModel: ListViewModel
    private var isEditing = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentDetailsBinding.inflate(inflater, container, false)

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

        listViewModel = ViewModelProvider((activity as ViewModelStoreOwner?)!!).get(
            ListViewModel::class.java
        )
        if (config)
            requireActivity().findViewById<FloatingActionButton>(R.id.fab_check).isEnabled = true
        val adapter = ContactAdapter(ElementTypes.values().toList(), requireActivity())
        adapter.state = if (config) States.VIEW else States.ACTION
        binding.contactRecyclerView.adapter = adapter

        if (config) {
            binding.fabEdit.hide()
            binding.fabSave.hide()
            binding.sendButton.visibility = View.GONE
            binding.shareButton.visibility = View.GONE
        }

        binding.fabEdit.setOnClickListener {
            isEditing = !isEditing
            update()
            adapter.state = States.EDIT
        }

        val cardLive = listViewModel.selected

        binding.fabSave.setOnClickListener {
            isEditing = !isEditing
            update()
            adapter.state = States.ACTION
            save(cardLive)
        }

        val array = requireActivity().resources.getStringArray(R.array.spinner_array_select)



        fistNameEditText = binding.firstNameTextView
        lastNameEditText = binding.lastNameTextView

        cardLive.observe(viewLifecycleOwner, { card: BusinessCardWithElements ->

            fistNameEditText.setText(card.card.firstName)
            lastNameEditText.setText(card.card.lastName)
            update()

            val index = array.indexOfFirst {
                card.card.types.name == it.uppercase()
            }
            spinner.setSelection(index)
            adapter.setData(cardLive)

        })

        binding.sendButton.setOnClickListener {
            val intent = Intent(requireActivity().baseContext, FindActivity::class.java)
            requireActivity().startActivity(intent)
        }

        binding.shareButton.setOnClickListener {
            val uriText: Uri? = Utilities.saveFile(requireActivity(), cardLive.value!!)
            if (uriText != null) {

                val shareIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_STREAM, uriText)
                    type = "application/xhtml+xml"
                }
                requireActivity().startActivity(Intent.createChooser(shareIntent, "Send"))
            }
        }
        update()

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (isEditing) {
                        MaterialAlertDialogBuilder(requireActivity())
                            .setTitle("Unsaved change")
                            .setMessage("Do you want to forget the changes?")
                            .setPositiveButton("Forget") { _: DialogInterface, _: Int ->
                                listViewModel.getCardItemFromId(cardLive.value!!.card.uidC)
                                    ?.observe(viewLifecycleOwner) {
                                        cardLive.value = it
                                        isEditing = !isEditing
                                        update()
                                        adapter.state = States.ACTION
                                    }
                            }
                            .setNegativeButton("Save") { di: DialogInterface, _: Int ->
                                di.dismiss()
                                save(cardLive)
                                isEditing = !isEditing
                                update()
                                adapter.state = States.ACTION
                            }.show()
                    } else {
                        activity!!.supportFragmentManager.popBackStack(
                            null,
                            FragmentManager.POP_BACK_STACK_INCLUSIVE
                        )
                    }
                }
            })

        binding.deleteButton.visibility = View.VISIBLE
        binding.deleteButton.setOnClickListener {
            MaterialAlertDialogBuilder(requireActivity())
                .setTitle("Delete")
                .setMessage("Do you want to delete this card?")
                .setNegativeButton("No") { di: DialogInterface, _: Int ->
                    di.dismiss()
                }
                .setPositiveButton("Delete") { _: DialogInterface, _: Int ->
                    listViewModel.deleteCard(cardLive.value!!)
                }
                .show()
        }

        // Inflate the layout for this fragment
        return binding.root
    }

    private fun update() {
        spinner.isClickable = isEditing
        spinner.isEnabled = isEditing
        fistNameEditText.isEnabled = isEditing
        lastNameEditText.isEnabled = isEditing
        binding.firstNameLayout.isEndIconVisible = isEditing
        binding.lastNameLayout.isEndIconVisible = isEditing
        val visibility: Int = if (isEditing) {
            binding.fabEdit.hide()
            binding.fabSave.show()
            View.INVISIBLE
        } else {
            binding.fabEdit.show()
            binding.fabSave.hide()
            View.VISIBLE
        }

        binding.shareButton.visibility = visibility
        binding.sendButton.visibility = visibility

    }

    private fun save(cardLive: MutableLiveData<BusinessCardWithElements>) {
        cardLive.value!!.card.firstName = fistNameEditText.text.toString()
        cardLive.value!!.card.lastName = lastNameEditText.text.toString()
        cardLive.value!!.card.types =
            CardTypes.valueOf(spinner.selectedItem.toString().uppercase())
        if (cardLive.value!!.card.firstName.isBlank() || cardLive.value!!.card.lastName.isBlank() || cardLive.value!!.elements.size == 0) {
            MaterialAlertDialogBuilder(requireActivity())
                .setTitle("Error")
                .setMessage("Incomplete business card.\n You must insert a first name, a last name add, at last one element")
                .setNeutralButton("Ok") { di: DialogInterface, _: Int ->
                    di.dismiss()
                }.show()
        } else {
            listViewModel.updateCard(cardLive.value!!)
        }
    }


}