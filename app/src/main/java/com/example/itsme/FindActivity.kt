package com.example.itsme

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.itsme.bluetooth.*
import com.example.itsme.bluetooth.exception.BluetoothDeviceNotFound
import com.example.itsme.bluetooth.utils.C
import com.example.itsme.databinding.ActivityFindBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.util.*


class FindActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFindBinding
    private lateinit var mBluetoothAdapter: BluetoothAdapter
    private lateinit var mPairedDevices: MutableList<BluetoothDevice>
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    private lateinit var cardLive: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFindBinding.inflate(layoutInflater)

        setContentView(binding.root)

        cardLive = intent.extras?.getString("xml")!!

        requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { result ->
            if (!result) {
                MaterialAlertDialogBuilder(this)
                    .setTitle("Permission request")
                    .setMessage("Location permission needed")
                    .setPositiveButton(
                        android.R.string.ok
                    ) { _, _ -> //Prompt the user once explanation has been shown
                        openAppSystemSettings()
                        //startActivity(Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                    }
                    .create()
                    .show()
            }
        }
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

        mPairedDevices = ArrayList()
        binding.refreshButton.setOnClickListener { pairedDeviceList() }

    }

    override fun onStart() {
        super.onStart()
        permissionGranted()

        pairedDeviceList()
    }

    override fun onDestroy() {
        unregisterReceiver(mReceiver)
        super.onDestroy()
    }

    private val mReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            if (BluetoothDevice.ACTION_FOUND == action) {
                val device = intent
                    .getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
//                val list: ArrayList<BluetoothDevice> = ArrayList()
                if (device != null && device.name != null) {
//                    if (mBluetoothAdapter.bondedDevices.isNotEmpty()) {
//                        if (mBluetoothAdapter.bondedDevices.contains(device)) {
//                            list.add(device)
//                            Log.i("device", "" + device)
//                        }
//
//                    } else {
//                        Toast.makeText(context, "no paired bluetooth device found", Toast.LENGTH_SHORT)
//                            .show()
//                    }
                    mPairedDevices.add(device)
                    mPairedDevices = mPairedDevices.distinct().toMutableList()
                    Log.i(
                        "BTT", """
                         ${device.name}
                         ${device.address}
                         """.trimIndent()
                    )
                    val adapter = ArrayAdapter(
                        context,
                        android.R.layout.simple_list_item_1,
                        mPairedDevices.map { it.name }.distinct()
                    )
                    binding.selectDeviceList.adapter = adapter
                    binding.selectDeviceList.onItemClickListener =
                        AdapterView.OnItemClickListener { _, _, position, _ ->
                            val t: BluetoothDevice = mPairedDevices[position]
                            send(t.name)

//            val intent = Intent(this, ControlActivity::class.java)
//            intent.putExtra(EXTRA_ADDRESS, address)
//            startActivity(intent)
                        }
                }
            }
        }
    }

    private fun pairedDeviceList() {


        if (mBluetoothAdapter.isDiscovering) {
            mBluetoothAdapter.cancelDiscovery()
        }

        mBluetoothAdapter.startDiscovery()


        val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
        registerReceiver(mReceiver, filter)

        //mPairedDevices = mBluetoothAdapter.bondedDevices


    }

    var btChannel: BluetoothChannel? = null

    private fun send(name: String) {

        try {
            connectToBTServer(name)

        } catch (bluetoothDeviceNotFound: BluetoothDeviceNotFound) {
            bluetoothDeviceNotFound.printStackTrace()
        }
    }

    @Throws(BluetoothDeviceNotFound::class)
    private fun connectToBTServer(name: String) {
        val serverDevice: BluetoothDevice =
            BluetoothUtils.getPairedDeviceByName(name)

        val uuid: UUID = BluetoothUtils.generateUuidFromString(C.Bluetooth.BT_SERVER_UUID)
        ConnectToBluetoothServerTask(serverDevice, uuid, object : ConnectionTask.EventListener {
            override fun onConnectionActive(channel: BluetoothChannel?) {
                btChannel = channel
                btChannel?.registerListener(object : CommChannel.Listener {
                    override fun onMessageReceived(receivedMessage: String?) {
                    }

                    override fun onMessageSent(sentMessage: String?) {
                        Toast.makeText(baseContext, "send", Toast.LENGTH_SHORT)
                            .show()
                        btChannel?.close()
                    }
                })
                //btChannel?.sendMessage("test")
                btChannel?.sendMessage(cardLive)


                //btChannel?.sendMessage(cardLive.value!!.getXMLString())
            }

            override fun onConnectionCanceled() {
            }
        }).execute()
    }

/*override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    when (requestCode) {
        SELECT_DEVICE_REQUEST_CODE -> when(resultCode) {
            Activity.RESULT_OK -> {
                // The user chose to pair the app with a Bluetooth device.
                val deviceToPair: BluetoothDevice? =
                    data?.getParcelableExtra(CompanionDeviceManager.EXTRA_DEVICE)
                deviceToPair?.name?.let { Log.v("Tess", it) }
                deviceToPair?.createBond()
            }
        }
        else -> super.onActivityResult(requestCode, resultCode, data)
    }
}*/

    private fun permissionGranted() {
        if (BluetoothAdapter.getDefaultAdapter() == null) {
            Toast.makeText(this, "this device doesn't support bluetooth", Toast.LENGTH_SHORT).show()
            finish()
        }
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

        if (!mBluetoothAdapter.isEnabled) {
            bluetoothEnable()
        }
        checkLocationPermission()


    }

    private fun bluetoothEnable() {
        MaterialAlertDialogBuilder(this)
            .setTitle("Bluetooth required")
            .setMessage("Bluetooth permission required") // Specifying a listener allows you to take an action before dismissing the dialog.
            // The dialog is automatically dismissed when a dialog button is clicked.
            .setPositiveButton(
                android.R.string.ok
            ) { id, _ ->
                id.dismiss()
                val enableBluetoothIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                resultLauncher.launch(enableBluetoothIntent)
            } // A null listener allows the button to dismiss the dialog and take no further action.
            .setNegativeButton(android.R.string.cancel) { _, _ ->
                finish()
            }.show()
    }

    private fun checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED
        ) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                MaterialAlertDialogBuilder(this)
                    .setTitle("Localization Permission")
                    .setMessage("This app request to access to you position to find other device")
                    .setPositiveButton(
                        android.R.string.ok
                    ) { _, _ -> //Prompt the user once explanation has been shown
                        requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                        //startActivity(Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                    }
                    .create()
                    .show()
            } else {
                // No explanation needed, we can request the permission.
                requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }
    }


    private fun Context.openAppSystemSettings() {
        startActivity(Intent().apply {
            action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            data = Uri.fromParts("package", packageName, null)
        })
    }


}