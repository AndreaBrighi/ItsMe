package com.example.itsme.bluetooth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

abstract class ConnectionTask : ViewModel() {
    var connectedChannel: BluetoothChannel? = null
    var eventListener: EventListener? = null

    abstract fun doInBackground(): Int

    fun execute() {
        viewModelScope.launch {
            when (executeInBackground()) {
                CONNECTION_DONE -> if (eventListener != null) {
                    eventListener!!.onConnectionActive(connectedChannel)
                }
                CONNECTION_CANCELED -> if (eventListener != null) {
                    eventListener!!.onConnectionCanceled()
                }
            }
        }
    }

    private suspend fun executeInBackground():Int{
        return withContext(Dispatchers.IO) {
            doInBackground()
        }
    }

    interface EventListener {
        fun onConnectionActive(channel: BluetoothChannel?)
        fun onConnectionCanceled()
    }

    companion object {
        const val CONNECTION_DONE = 1
        const val CONNECTION_CANCELED = 2
    }
}