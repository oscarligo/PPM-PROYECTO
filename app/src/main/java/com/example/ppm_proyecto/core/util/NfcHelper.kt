package com.example.ppm_proyecto.core.util

import android.app.Activity
import android.app.PendingIntent
import android.content.Intent
import android.content.IntentFilter
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.Ndef
import android.nfc.tech.NdefFormatable
import android.os.Build

/**
 * Helper class para operaciones NFC
 * Proporciona utilidades para leer tags NFC y manejar eventos NFC en Android
 */
object NfcHelper {

    /**
     * Obtiene el ID único de un tag NFC en formato hexadecimal
     * @param tag El tag NFC detectado
     * @return String con el ID del tag en formato hexadecimal
     */
    fun getTagId(tag: Tag): String {
        return tag.id.joinToString("") { "%02X".format(it) }
    }

    /**
     * Extrae el tag NFC de un intent
     *
     * @param intent El intent que contiene el tag NFC
     * @return El tag NFC o null si no existe
     */
    fun getTagFromIntent(intent: Intent): Tag? {
        return intent.getParcelableExtra(NfcAdapter.EXTRA_TAG, Tag::class.java)
    }

    /**
     * Verifica si el intent contiene información de un tag NFC
     *
     * @param intent El intent a verificar
     * @return true si el intent contiene un tag NFC
     */
    fun isNfcIntent(intent: Intent): Boolean {
        val action = intent.action
        return action == NfcAdapter.ACTION_TAG_DISCOVERED ||
                action == NfcAdapter.ACTION_NDEF_DISCOVERED ||
                action == NfcAdapter.ACTION_TECH_DISCOVERED
    }


}

