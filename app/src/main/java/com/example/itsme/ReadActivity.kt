package com.example.itsme

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatSpinner
import com.example.itsme.databinding.ActivityReadBinding
import com.example.itsme.databinding.FragmentDetailsBinding
import com.example.itsme.model.ElementTypes
import com.example.itsme.recyclerview.element.ContactAdapter
import java.io.*


class ReadActivity : AppCompatActivity() {

    private lateinit var spinner: AppCompatSpinner
    private lateinit var bindingInclude: FragmentDetailsBinding
    private lateinit var binding: ActivityReadBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityReadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = intent
        val action = intent.action
        val contentUri: Uri? = intent.data


        if (contentUri == null) {
            Toast.makeText(this, " file not found", Toast.LENGTH_SHORT)
                .show()
        } else {

            // Create a new coroutine on the UI thread
            var text =""
            try {
                val inputStream: InputStream? =
                    contentResolver.openInputStream(contentUri)
                val input: Reader = BufferedReader(InputStreamReader(inputStream))
                text = input.readLines().joinToString("\n")
                Log.v("tess", text)
                input.close()
                inputStream?.close()
            } catch (e: IOException) {
                Toast.makeText(baseContext, "Fail to read file", Toast.LENGTH_SHORT)
                    .show()
            }

            binding.textView.text = text
            bindingInclude = binding.id

            val adapter = ContactAdapter(ElementTypes.values().toList(), this)
            adapter.isEditable = true
            bindingInclude.contactRecyclerView.adapter = adapter

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
        }

    }
}