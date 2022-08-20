package ru.netology.nmedia.presentation.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import ru.netology.nmedia.R
import ru.netology.nmedia.di.AppContainer
import ru.netology.nmedia.application.App
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.di.AppContainerHolder

class MainActivity : AppCompatActivity(), AppContainerHolder {

    override val appContainer: AppContainer by lazy { createContainer() }
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        createContainer()

        val navHost = supportFragmentManager.findFragmentById(R.id.rootNavController) as NavHostFragment
        navController = navHost.navController
        val config = AppBarConfiguration(navController.graph)

        NavigationUI.setupActionBarWithNavController(this, navController, config)

        checkGoogleApiAvailability()
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    private fun createContainer(): AppContainer {
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
