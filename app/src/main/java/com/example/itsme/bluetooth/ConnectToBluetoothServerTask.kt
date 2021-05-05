package com.example.itsme.bluetooth

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import java.io.IOException
import java.util.*

class ConnectToBluetoothServerTask(
    serverBtDevice: BluetoothDevice,
    uuid: UUID?,
    eventListener: EventListener
) : ConnectionTask() {
    private var btSocket: BluetoothSocket? = null

    override fun doInBackground(): Int {
        try {
            btSocket!!.connect()
        } catch (e: IOException) {
            e.printStackTrace()
            try {
                btSocket!!.close()
            } catch (e1: IOException) {
                e1.printStackTrace()
            }
            return CONNECTION_CANCELED
        }
        connectedChannel = RealBluetoothChannel(btSocket)
        return CONNECTION_DONE
    }

    init {
        try {
            btSocket = serverBtDevice.createRfcommSocketToServiceRecord(uuid)
            this.eventListener = eventListener
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}