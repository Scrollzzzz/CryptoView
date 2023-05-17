package com.scrollz.cryptoview.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import android.util.Log
import com.scrollz.cryptoview.domain.model.DeferredNotification
import com.scrollz.cryptoview.domain.repository.CryptoViewRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class AlarmReceiver : BroadcastReceiver() {

    @Inject lateinit var repository: CryptoViewRepository

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context != null) {
            intent?.extras?.getString("coinID")?.let { id ->
                val handler = CoroutineExceptionHandler { _, throwable ->
                    Log.e("Request error", throwable.message ?: "err")
                    CoroutineScope(Dispatchers.IO).launch {
                        val isEmpty = repository.addDeferredNotificationEmptinessChecking(
                            DeferredNotification(id)
                        )
                        if (isEmpty) {
                            registerNetworkCallback(context)
                        }
                    }
                }
                CoroutineScope(Dispatchers.IO + handler).launch {
                    val coin = repository.getCoin(id)
                    NotificationService(context).showNotification(coin)
                }
            }
        }
    }

    private fun registerNetworkCallback(context: Context) {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        connectivityManager.registerNetworkCallback(
            NetworkRequest.Builder().build(),
            object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    CoroutineScope(Dispatchers.IO).launch {
                        val deferredNotifications = withContext(Dispatchers.IO) {
                            repository.getDeferredNotificationsDeleting()
                        }
                        supervisorScope {
                            for (coinId in deferredNotifications) {
                                launch {
                                    val coin = repository.getCoin(coinId)
                                    NotificationService(context).showNotification(coin)
                                }
                            }
                        }
                    }
                    connectivityManager.unregisterNetworkCallback(this)
                }
            }
        )
    }
}
