package com.countries.core.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import com.countries.core.extension.isCurrentlyOnline
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import javax.inject.Inject
import javax.inject.Singleton

interface ConnectivityObserver {
    val isOnline: Flow<Boolean>
}

@Singleton
class ConnectivityObserverImpl @Inject constructor(
    @param:ApplicationContext private val context: Context
) : ConnectivityObserver {

    private val connectivityManager: ConnectivityManager
        get() = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    override val isOnline: Flow<Boolean> = callbackFlow {
        fun trySendStatus(available: Boolean) {
            trySend(available)
        }

        trySendStatus(connectivityManager.isCurrentlyOnline())

        val callback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) = trySendStatus(true)
            override fun onLost(network: Network) = trySendStatus(false)
            override fun onCapabilitiesChanged(
                network: Network,
                networkCapabilities: NetworkCapabilities
            ) =
                trySendStatus(connectivityManager.isCurrentlyOnline())
        }

        runCatching { connectivityManager.registerDefaultNetworkCallback(callback) }
            .onFailure { trySend(false) }

        awaitClose {
            runCatching { connectivityManager.unregisterNetworkCallback(callback) }
        }
    }.distinctUntilChanged()
}
