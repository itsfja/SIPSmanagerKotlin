package com.example.sipsmanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.sipsmanager.types.UserProfile
import com.example.sipsmanager.ui.AuthScreen
import com.example.sipsmanager.ui.DashboardScreen
import com.example.sipsmanager.utils.PreferencesManager

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val prefsManager = PreferencesManager(this)
        
        setContent {
            var activeUser by remember { mutableStateOf(prefsManager.getActiveProfile()) }

            Surface(
                modifier = Modifier.fillMaxSize(),
                color = Color(0xFFFCF9F2)
            ) {
                val currentActiveUser = activeUser
                if (currentActiveUser == null) {
                    AuthScreen(
                        prefsManager = prefsManager,
                        onLoginSuccess = { profile ->
                            activeUser = profile
                        }
                    )
                } else {
                    DashboardScreen(
                        user = currentActiveUser,
                        prefsManager = prefsManager,
                        onLogout = {
                            activeUser = null
                        }
                    )
                }
            }
        }
    }
}
