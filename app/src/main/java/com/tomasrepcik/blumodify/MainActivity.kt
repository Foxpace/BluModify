package com.tomasrepcik.blumodify

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.tomasrepcik.blumodify.intro.introGraph
import com.tomasrepcik.blumodify.main.mainGraph
import com.tomasrepcik.blumodify.ui.theme.BluModifyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            BluModifyTheme {
                NavHost(navController, startDestination = "intro") {
                    introGraph(navController)
                    mainGraph(navController)
                }
            }
        }
    }
}

