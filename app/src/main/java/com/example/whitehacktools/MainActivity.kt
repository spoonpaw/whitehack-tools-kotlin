package com.example.whitehacktools

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.whitehacktools.data.CharacterStore
import com.example.whitehacktools.navigation.AppNavigation
import com.example.whitehacktools.ui.theme.WhitehackToolsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WhitehackToolsTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    val context = LocalContext.current
                    val characterStore = CharacterStore(context)
                    AppNavigation(
                        navController = navController,
                        characterStore = characterStore
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    WhitehackToolsTheme {
        val navController = rememberNavController()
        val context = LocalContext.current
        val characterStore = CharacterStore(context)
        AppNavigation(
            navController = navController,
            characterStore = characterStore
        )
    }
}