package com.jcy.ch24_subwayapp.preference

interface PreferenceManager {
    fun getLong(key: String): Long?

    fun putLong(key: String, value: Long)
}