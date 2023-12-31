package com.example.itsme.bluetooth

import android.os.Handler
import android.os.Looper
import android.os.Message
import com.example.itsme.bluetooth.utils.C
import java.util.*

abstract class BluetoothChannel : CommChannel {
    private val listeners: MutableList<CommChannel.Listener> = ArrayList()
    var btChannelHandler = BluetoothChannelHandler(Looper.getMainLooper())
    var worker: ExtendedRunnable? = null

    override fun close() {
        worker!!.cancel()
    }

    override fun registerListener(listener: CommChannel.Listener) {
        listeners.add(listener)
    }

    override fun removeListener(listener: CommChannel.Listener) {
        listeners.remove(listener)
    }

    override fun sendMessage(message: String) {
        worker!!.write(message.toByteArray())
    }

    /**
     *
     */
    inner class BluetoothChannelHandler(looper: Looper?) : Handler(looper!!) {
        override fun handleMessage(message: Message) {
            if (message.what == C.Channel.MESSAGE_RECEIVED) {
                for (l in listeners) {
                    l.onMessageReceived(String((message.obj as ByteArray)))
                }
            }
            if (message.what == C.Channel.MESSAGE_SENT) {
                for (l in listeners) {
                    l.onMessageSent(String((message.obj as ByteArray)))
                }
            }
        }
    }
}