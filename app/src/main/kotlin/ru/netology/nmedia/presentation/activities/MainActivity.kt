package ru.netology.nmedia.presentation.activities

import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.firebase.iid.FirebaseInstanceIdReceiver
import com.google.firebase.iid.internal.FirebaseInstanceIdInternal
import com.google.firebase.messaging.FirebaseMessaging
import ru.netology.nmedia.R
import ru.netology.nmedia.common.di.AppContainer
import ru.netology.nmedia.common.application.App
import ru.netology.nmedia.common.utils.log
import ru.netology.nmedia.services.FCMService

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    val appContainer: AppContainer by lazy { createContainer() }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        createContainer()
    }

    private fun createContainer () : AppContainer {
        return (application as App).appContainer

    }

    private fun checkGoogleApiAvailability() {
        GoogleApiAvailability.getInstance().apply {
            val code = isGooglePlayServicesAvailable(this@MainActivity)
            if (code == ConnectionResult.SUCCESS) return@apply
            if (isUserResolvableError(code)) {
                getErrorDialog(this@MainActivity, code, 9000)?.show()
                return
            }
            Toast.makeText(this@MainActivity, "Google Api Unavailable", Toast.LENGTH_SHORT).show()
        }
    }
}
