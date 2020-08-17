package com.eahm.exploringlibraries.utils

import android.content.Context
import android.view.inputmethod.InputMethodManager
import androidx.activity.ComponentActivity

object Util {
    @JvmStatic fun hideKeyboard(parentActivity : ComponentActivity) {
        val currentView = parentActivity.currentFocus
        if(currentView != null){
            val inputMethodManager = parentActivity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(currentView.windowToken, 0)
        }
    }
}