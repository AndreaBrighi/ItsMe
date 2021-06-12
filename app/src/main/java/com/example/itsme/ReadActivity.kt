package com.example.itsme

import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatSpinner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.example.itsme.databinding.ActivityReadBinding
import com.example.itsme.databinding.FragmentDetailsBinding
import com.example.itsme.db.BusinessCard
import com.example.itsme.db.BusinessCardWithElements
import com.example.itsme.model.CardTypes
import com.example.itsme.model.ElementTypes
import com.example.itsme.recyclerview.element.ContactAdapter
import com.example.itsme.recyclerview.element.States
import com.example.itsme.viewModel.ListViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder


class ReadActivity : AppCompatActivity() {

    private lateinit var spinner: AppCompatSpinner
    private lateinit var bindingInclude: FragmentDetailsBinding
    private lateinit var binding: ActivityReadBinding
    private lateinit var listViewModel: ListViewModel
    private val cardLive = MutableLiveData<BusinessCardWithElements>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityReadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = intent
        intent.action
        val contentUri: Uri? = intent.data

        listViewModel = ViewModelProvider((this as ViewModelStoreOwner?)!!).get(
            ListViewModel::class.java
        )


        if (contentUri == null) {
            Toast.makeText(this, getString(R.string.file_not_found), Toast.LENGTH_SHORT)
                .show()
            finish()
        } else {

            //binding.textView.text = text
            bindingInclude = binding.id


            val adapter = ContactAdapter(ElementTypes.values().toList(), this)
            adapter.state = States.EDIT
            bindingInclude.contactRecyclerView.adapter = adapter

            cardLive.value =
                BusinessCardWithElements(
                    BusinessCard(0, "", "", CardTypes.WORK, true),
                    ArrayList()
                )
            adapter.setData(cardLive)

            cardLive.observe(this, {
                adapter.setData(cardLive)
                bindingInclude.firstNameTextView.setText(it.card.firstName)
                bindingInclude.lastNameTextView.setText(it.card.lastName)
            })

            spinner = bindingInclude.typeSpinner
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
            bindingInclude.shareButton.visibility = View.GONE
            bindingInclude.sendButton.visibility = View.GONE

            bindingInclude.fabEdit.visibility = View.GONE
            bindingInclude.fabSave.visibility = View.VISIBLE

            listViewModel.read(cardLive, contentUri, this)

            bindingInclude.fabSave.setOnClickListener { save(cardLive) }

        }

    }

    private fun save(cardLive: MutableLiveData<BusinessCardWithElements>) {
        cardLive.value!!.card.firstName = bindingInclude.firstNameTextView.text.toString()
        cardLive.value!!.card.lastName = bindingInclude.lastNameTextView.text.toString()
        cardLive.value!!.card.types =
            CardTypes.values()[spinner.selectedItemPosition]

        if (cardLive.value!!.card.firstName.isBlank() || cardLive.value!!.card.lastName.isBlank() || cardLive.value!!.elements.size == 0) {
            MaterialAlertDialogBuilder(this)
                .setTitle(getString(R.string.error))
                .setMessage(getString(R.string.error_info))
                .setNeutralButton(getString(R.string.ok)) { di: DialogInterface, _: Int ->
                    di.dismiss()
                }.show()
        } else {
            listViewModel.addCard(cardLive.value!!)
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}