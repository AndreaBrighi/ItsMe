package com.example.itsme

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatSpinner
import com.example.itsme.bluetooth.BluetoothChannel
import com.example.itsme.bluetooth.BluetoothServer
import com.example.itsme.bluetooth.CommChannel
import com.example.itsme.bluetooth.utils.C
import com.example.itsme.databinding.ActivityReceivedBinding
import com.example.itsme.databinding.FragmentDetailsBinding
import com.example.itsme.recyclerview.ElementType
import com.example.itsme.recyclerview.element.ContactAdapter
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class ReceivedActivity : AppCompatActivity() {

    private lateinit var spinner: AppCompatSpinner
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>
    private lateinit var binding: ActivityReceivedBinding
    private lateinit var bindingInclude: FragmentDetailsBinding
    private lateinit var mBluetoothAdapter: BluetoothAdapter
    private var btChannel: BluetoothChannel? = null
    private var btServer: BluetoothServer? = null
    private val btServerListener = BluetoothServerListener()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityReceivedBinding.inflate(layoutInflater)
        btChannel = null
        setContentView(binding.root)
        bindingInclude = binding.id

        val adapter = ContactAdapter(ElementType.values().toList(), this)
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

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

        resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    if (mBluetoothAdapter.isEnabled) {
                        Toast.makeText(this, "Bluetooth has been enabled", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        Toast.makeText(this, "Bluetooth has been disabled", Toast.LENGTH_SHORT)
                            .show()
                    }
                } else if (result.resultCode == Activity.RESULT_CANCELED) {
                    Toast.makeText(
                        this,
                        "Bluetooth enabling has been canceled",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

        permissionGranted()
        startBluetoothServer()
    }

    override fun onStop() {
        super.onStop()
        btServer?.terminate()
        btChannel?.close()
    }

    private fun permissionGranted() {
        if (BluetoothAdapter.getDefaultAdapter() == null) {
            Toast.makeText(this, "this device doesn't support bluetooth", Toast.LENGTH_SHORT).show()
            return
        }
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

        if (!mBluetoothAdapter.isEnabled) {
            bluetoothEnable()
        }

    }

    private fun bluetoothEnable() {
        MaterialAlertDialogBuilder(this)
            .setTitle("Bluetooth required")
            .setMessage("Bluetooth permission required") // Specifying a listener allows you to take an action before dismissing the dialog.
            // The dialog is automatically dismissed when a dialog button is clicked.
            .setPositiveButton(
                android.R.string.ok
            ) { _, _ ->
                val enableBluetoothIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                resultLauncher.launch(enableBluetoothIntent)
            } // A null listener allows the button to dismiss the dialog and take no further action.
            .setNegativeButton(android.R.string.cancel) { _, _ ->
                finish()
            }
            .show()
    }

    private fun startBluetoothServer() {
        btServer = BluetoothServer(
            C.Bluetooth.BT_SERVER_UUID,
            "server",
            btServerListener
        )
        btServer?.start()
    }

    /**
     *
     */
    inner class BluetoothServerListener : BluetoothServer.Listener {
        override fun onServerActive() {
        }

        override fun onConnectionAccepted(btChannel: CommChannel?) {
//            runOnUiThread {
//                (getString(
//                    R.string.status
//                ) + "connected").also { binding.statusLabel.text = it }
//            }
            btChannel?.registerListener(object : CommChannel.Listener {
                override fun onMessageReceived(receivedMessage: String?) {
                    runOnUiThread {
                        binding.status.visibility = View.GONE
                    }
                }

                override fun onMessageSent(sentMessage: String?) {
                }
            })
        }
    }
}