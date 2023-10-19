package com.example.videoplayer
import android.content.Context
import android.content.SharedPreferences




public class SharedPrefManager(context: Context) {
    private lateinit var sharedPref: SharedPreferences
    private var editor: SharedPreferences.Editor? = null

    private val SHARED_PREF = "SharedPref"
    private val IS_FIRST_OPEN = "isFirstOpen"
    private val IS_LOGGED_IN = "isLoggedIn"
    private val IS_POPUP_SHOWN = "IsPopupShown"

   init{
        sharedPref = context.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE)
        editor = sharedPref.edit()
    }

    fun isFirstOpen(): Boolean {
        return sharedPref!!.getBoolean(IS_FIRST_OPEN, true)
    }

    fun setIsFirstOpen(isFirstOpen: Boolean) {
        editor!!.putBoolean(IS_FIRST_OPEN, isFirstOpen).apply()
    }

    fun isLoggedIn(): Boolean {
        return sharedPref!!.getBoolean(IS_LOGGED_IN, false)
    }

    fun setIsLoggedIn(isLoggedIn: Boolean) {
        editor!!.putBoolean(IS_LOGGED_IN, isLoggedIn).apply()
    }

    fun isPopupDialogShown(): Boolean {
        return sharedPref!!.getBoolean(IS_POPUP_SHOWN, false)
    }

    fun setPopupShown(isFirstTime: Boolean) {
        editor!!.putBoolean(IS_POPUP_SHOWN, isFirstTime)
        editor!!.commit()
    }


    }
