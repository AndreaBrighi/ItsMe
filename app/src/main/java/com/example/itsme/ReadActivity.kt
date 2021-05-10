package com.example.itsme

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.itsme.databinding.ActivityReadBinding
import java.io.*


class ReadActivity : AppCompatActivity() {

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
                .show();
        } else {

            // Create a new coroutine on the UI thread
            var text: String =""
            try {
                val inputStream: InputStream? =
                    contentResolver.openInputStream(contentUri);
                val input: Reader = BufferedReader(InputStreamReader(inputStream))
                text = input.readLines().joinToString("\n")
                Log.v("tess", text)
                input.close()
                inputStream?.close()
            } catch (e: IOException) {
                Toast.makeText(baseContext, "Fail to read file", Toast.LENGTH_SHORT)
                    .show();
            }

            binding.textView.text = text
        }

    }
}