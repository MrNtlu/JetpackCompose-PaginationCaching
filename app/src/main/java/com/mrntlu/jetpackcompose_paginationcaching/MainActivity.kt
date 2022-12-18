package com.mrntlu.jetpackcompose_paginationcaching

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.mrntlu.jetpackcompose_paginationcaching.ui.theme.JetpackComposePaginationCachingTheme

class MainActivity : ComponentActivity() {

    /* Sources
    https://www.youtube.com/watch?v=dqj9aj-Z898&ab_channel=Stevdza-San
    https://www.youtube.com/watch?v=3yIIiaDN0CI&ab_channel=HimanshuGaur
    https://developer.android.com/topic/libraries/architecture/paging/v3-network-db
     */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JetpackComposePaginationCachingTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Greeting("Android")
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}