package com.example.itsme.bluetooth

import android.bluetooth.BluetoothSocket
import android.util.Log
import com.example.itsme.bluetooth.utils.C
import java.io.DataInputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

class RealBluetoothChannel internal constructor(socket: BluetoothSocket?) : BluetoothChannel() {
    override val remoteDeviceName: String = socket!!.remoteDevice.name

    internal inner class BluetoothWorker(private val socket: BluetoothSocket?) : ExtendedRunnable {
        private val inputStream: InputStream?
        private val outputStream: OutputStream?
        override fun run() {
            while (true) {
                try {
                    val input = DataInputStream(inputStream)
                    var readBuffer = StringBuffer()
                    var inputByte: Byte
                    while (input.readByte().also { inputByte = it }.toInt() != 0) {
                        val chr = inputByte.toChar()
                        if (chr != C.Message.MESSAGE_TERMINATOR) {
                            readBuffer.append(chr)
                        } else {
                            val inputString = readBuffer.toString()
                            val receivedMessage = btChannelHandler.obtainMessage(
                                C.Channel.MESSAGE_RECEIVED,
                                inputString.toByteArray().size,
                                -1,
                                inputString.toByteArray()
                            )
                            receivedMessage.sendToTarget()
                            readBuffer = StringBuffer()
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        override fun write(bytes: ByteArray) {
            try {
                val bytesToBeSent = bytes.copyOf(bytes.size + 1)
                bytesToBeSent[bytesToBeSent.size - 1] = C.Message.MESSAGE_TERMINATOR.toByte()
                outputStream!!.write(bytesToBeSent)
                val writtenMsg =
                    btChannelHandler.obtainMessage(C.Channel.MESSAGE_SENT, -1, -1, bytes)
                writtenMsg.sendToTarget()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        override fun cancel() {
            try {
                socket!!.close()
            } catch (e: IOException) {
                Log.e(C.LIB_TAG, "Could not close the connect socket", e)
            }
        }

        init {
            var tmpIn: InputStream? = null
            var tmpOut: OutputStream? = null
            try {
                tmpIn = socket!!.inputStream
            } catch (e: IOException) {
                Log.e(C.LIB_TAG, "Error occurred when creating input stream", e)
            }
            try {
                tmpOut = socket!!.outputStream
            } catch (e: IOException) {
                Log.e(C.LIB_TAG, "Error occurred when creating output stream", e)
            }
            inputStream = tmpIn
            outputStream = tmpOut
        }
    }

    init {
        worker = BluetoothWorker(socket)
        Thread(worker).start()
    }
}