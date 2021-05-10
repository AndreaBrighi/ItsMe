package com.example.itsme

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.itsme.bluetooth.BluetoothChannel
import com.example.itsme.bluetooth.BluetoothServer
import com.example.itsme.bluetooth.CommChannel
import com.example.itsme.bluetooth.RealBluetoothChannel
import com.example.itsme.bluetooth.utils.C
import com.example.itsme.databinding.ActivityReceivedBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class ReceivedActivity : AppCompatActivity() {

    private lateinit var resultLauncher: ActivityResultLauncher<Intent>
    private lateinit var binding: ActivityReceivedBinding
    private lateinit var mBluetoothAdapter: BluetoothAdapter
    private var btChannel: BluetoothChannel? = null
    private var btServer: BluetoothServer? = null
    private val btServerListener = BluetoothServerListener()

    private var bluetoothChannelList: MutableList<RealBluetoothChannel>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityReceivedBinding.inflate(layoutInflater)

        setContentView(binding.root)
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
        bluetoothChannelList = ArrayList()
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
                    binding.textView.text = receivedMessage
                }

                override fun onMessageSent(sentMessage: String?) {
                }
            })
            bluetoothChannelList?.add(btChannel as RealBluetoothChannel)
        }
    }
}