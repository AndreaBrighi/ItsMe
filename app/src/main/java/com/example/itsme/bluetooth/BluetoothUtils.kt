package com.example.itsme.bluetooth

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import com.example.itsme.bluetooth.exception.BluetoothDeviceNotFound
import java.util.*

object BluetoothUtils {
    fun generateRandomUuid(): UUID {
        return UUID.randomUUID()
    }

    fun generateUuidFromString(uuid: String?): UUID {
        return UUID.fromString(uuid)
    }

    val pairedDevices: Set<BluetoothDevice>
        get() = BluetoothAdapter.getDefaultAdapter().bondedDevices

    @Throws(BluetoothDeviceNotFound::class)
    fun getPairedDeviceByName(deviceName: String): BluetoothDevice {
        val pairedList = BluetoothAdapter.getDefaultAdapter().bondedDevices
        if (pairedList.size > 0) {
            for (device in pairedList) {
                if (device.name == deviceName) {
                    return device
                }
            }
        }
        throw BluetoothDeviceNotFound()
    }

    @Throws(BluetoothDeviceNotFound::class)
    fun getPairedDeviceByAddress(deviceMacAddress: String): BluetoothDevice {
        val pairedList = BluetoothAdapter.getDefaultAdapter().bondedDevices
        if (pairedList.size > 0) {
            for (device in pairedList) {
                if (device.address == deviceMacAddress) {
                    return device
                }
            }
        }
        throw BluetoothDeviceNotFound()
    }
}