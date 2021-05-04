package com.example.itsme.bluetooth

interface ExtendedRunnable : Runnable {
    fun write(bytes: ByteArray)
    fun cancel()
}