package com.eahm.exploringlibraries.models

import com.eahm.exploringlibraries.ui.MainActivity

data class MenuElement (
    val title: String = "",
    val subtitle: String  = "",
    val description: String  = "",
    val activity : Class<*> = MainActivity::class.java
)
