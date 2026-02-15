package com.countries.core.extension

import android.net.ConnectivityManager
import android.net.NetworkCapabilities

fun ConnectivityManager.isCurrentlyOnline(): Boolean {
    val active = activeNetwork ?: return false
    val caps = getNetworkCapabilities(active) ?: return false
    return caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
            caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
}
