package com.ardmos.hceapp

import android.nfc.cardemulation.HostApduService
import android.os.Bundle

class MyHostApduService : HostApduService() {
    override fun processCommandApdu(commandApdu: ByteArray?, extras: Bundle?): ByteArray {

        return "My Message".toByteArray()
    }

    override fun onDeactivated(reason: Int) {

    }
}