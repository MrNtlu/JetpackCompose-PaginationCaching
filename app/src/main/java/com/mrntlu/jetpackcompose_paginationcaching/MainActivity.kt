package com.mrntlu.jetpackcompose_paginationcaching

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.mrntlu.jetpackcompose_paginationcaching.ui.theme.JetpackComposePaginationCachingTheme
import com.mrntlu.jetpackcompose_paginationcaching.viewmodels.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    /* Sources
    https://www.youtube.com/watch?v=dqj9aj-Z898&ab_channel=Stevdza-San
    https://www.youtube.com/watch?v=3yIIiaDN0CI&ab_channel=HimanshuGaur
    https://developer.android.com/topic/libraries/architecture/paging/v3-network-db
    https://github.com/MrNtlu/PassVault/blob/master/app/src/main/java/com/mrntlu/PassVault/utils/NetworkBoundResource.kt
     */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JetpackComposePaginationCachingTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    MainScreen()
                }
            }
        }
    }
}

//TODO Implement Placeholders

@Composable
fun MainScreen() {
    val newsViewModel = hiltViewModel<NewsViewModel>()

    val articles = newsViewModel.getNews().collectAsLazyPagingItems()

    LazyColumn {
        items(
            items = articles
        ) { article ->
            article?.let {
                Text(
                    modifier = Modifier
                        .padding(vertical = 18.dp, horizontal = 8.dp),
                    text = it.title
                )

                Divider()
            }
        }

        val loadState = articles.loadState
        if (loadState.refresh == LoadState.Loading) {
            item {
                Column(
                    modifier = Modifier
                        .fillParentMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    Text(
                        modifier = Modifier
                            .padding(8.dp),
                        text = "Refresh Loading"
                    )

                    CircularProgressIndicator(color = MaterialTheme.colors.primary)
                }
            }
        }

        if (loadState.append == LoadState.Loading) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator(color = MaterialTheme.colors.primary)
                }
            }
        }
    }
}