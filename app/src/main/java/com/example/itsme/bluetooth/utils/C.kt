package com.example.itsme.bluetooth.utils

object C {
    const val LIB_TAG = "BluetoothLib"

    object Channel {
        const val MESSAGE_RECEIVED = 0
        const val MESSAGE_SENT = 1
    }

    object Message {
        const val MESSAGE_TERMINATOR = '\n'
    }

    object Bluetooth {

        const val ENABLE_BT_REQUEST = 1

        const val BT_SERVER_UUID = "7ba55836-01eb-11e9-8eb2-f2801f1b9fd1"
    }
}