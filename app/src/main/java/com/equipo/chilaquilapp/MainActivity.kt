package com.equipo.chilaquilapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.equipo.chilaquilapp.navigation.NavGraph
import com.equipo.chilaquilapp.ui.theme.ChilaquilAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ChilaquilAppTheme {
                NavGraph()
            }
        }
    }
}
