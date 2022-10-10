package com.ardmos.nfcreader

import android.nfc.NfcAdapter
import android.nfc.NfcAdapter.*
import android.nfc.tech.NfcA
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {

    lateinit var nfcAdapter : NfcAdapter
    lateinit var readerCallback: ReaderCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val ReaderFlags = NfcAdapter.FLAG_READER_NFC_A or FLAG_READER_SKIP_NDEF_CHECK

        nfcAdapter = NfcAdapter.getDefaultAdapter(this)
        readerCallback = NfcAdapter.ReaderCallback { tag -> onTagDiscovered(tag) }

        nfcAdapter?.enableReaderMode(
            this, this.readerCallback,
            ReaderFlags, null
        )


        // 통신처리 할 차례 
    }
}