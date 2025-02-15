package com.example.biofit.view

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.webkit.WebView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.example.biofit.R
import com.example.biofit.ui.theme.BioFitTheme

class KnowledgeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BioFitTheme {
                KnowledgeScreen()
            }
        }
        WebView.setWebContentsDebuggingEnabled(true)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        recreate()
    }
}

@Composable
fun KnowledgeScreen() {
    val standardPadding = getStandardPadding().first
    val modifier = getStandardPadding().second

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column {
            Row(
                modifier = Modifier.weight(1f)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            top = WindowInsets.safeDrawing.asPaddingValues().calculateTopPadding(),
                            start = standardPadding,
                            end = standardPadding,
                        ),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    KnowledgeContent(
                        standardPadding,
                        modifier
                    )
                }
            }

            Row {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.Bottom
                ) {
                    BottomBar(
                        onItemSelected = { TODO() },
                        standardPadding = standardPadding
                    )
                }
            }
        }
    }
}

data class VideoItem(val videoId: String, val title: String)

val videoList = listOf(
    VideoItem("MHBOOP_eKZE", "What are the Roles of Important Nutrients in the Body?"),
    VideoItem("b8zG207rPYw", "6 simple scientific ways to eat for a healthy body"),
    VideoItem("Klu1bvz7q4I", "Diet and Exercise for Skinny People!"),
    VideoItem("N0Yy5iCrTLU", "10 tips for healthy nutrition towards sustainable public health")
)


@Composable
fun KnowledgeContent(
    standardPadding: Dp,
    modifier: Modifier
) {
    val context = LocalContext.current

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(standardPadding * 2)
    ) {
        item {
            Row(
                modifier = modifier,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = stringResource(R.string.nutritional_knowledge),
                    modifier = Modifier.weight(1f),
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.titleMedium
                )

                IconButton(
                    onClick = { TODO() }
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_knowledge_2),
                        contentDescription = "Knowledge",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }

        item {
            Column(
                modifier = modifier
            ) {
                Text(
                    text = stringResource(R.string.nutrition_and_care),
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.titleSmall
                )

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(standardPadding)
                ) {
                    items(videoList) { videoItem ->
                        Column(
                            modifier = Modifier.padding(vertical = standardPadding),
                            verticalArrangement = Arrangement.spacedBy(standardPadding),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            VideoThumbnail(
                                videoId = videoItem.videoId,
                                onClick = {
                                    val intent = Intent(context, KnowledgeVideoActivity::class.java)
                                        .apply {
                                            putExtra("VIDEO_ID", videoItem.videoId)
                                        }
                                    context.startActivity(intent)
                                },
                                standardPadding = standardPadding
                            )

                            Text(
                                text = videoItem.title,
                                modifier = Modifier.width(standardPadding * 10),
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
            }
        }

        val videoExerciseList = listOf(
            VideoItem(
                "0F-TPouHTAM",
                "3 exercises for a flexible and healthy body"
            ),
            VideoItem(
                "cBBc7lvKSFk",
                "The best group of exercises to develop strength, increase speed and physical fitness"
            )
        )

        item {
            Column(
                modifier = modifier,
                verticalArrangement = Arrangement.spacedBy(standardPadding)
            ) {
                Text(
                    text = stringResource(R.string.exercise),
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.titleSmall
                )

                Row {
                    videoExerciseList.forEach { videoItem ->
                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(standardPadding),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            VideoThumbnail(
                                videoId = videoItem.videoId,
                                onClick = {
                                    val intent = Intent(context, KnowledgeVideoActivity::class.java)
                                        .apply {
                                            putExtra("VIDEO_ID", videoItem.videoId)
                                        }
                                    context.startActivity(intent)
                                },
                                standardPadding = standardPadding
                            )

                            Text(
                                text = videoItem.title,
                                color = MaterialTheme.colorScheme.onBackground,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
            }
        }

        item {
            Column(
                modifier = modifier,
                verticalArrangement = Arrangement.spacedBy(standardPadding)
            ) {
                Text(
                    text = stringResource(R.string.article),
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.titleSmall
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(standardPadding)
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        ArticleThumbnail(
                            article = dummyArticles[0],
                            onClick = { TODO() },
                            standardPadding = standardPadding
                        )
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        ArticleThumbnail(
                            article = dummyArticles[1],
                            onClick = { TODO() },
                            standardPadding = standardPadding
                        )
                    }
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(standardPadding)
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        ArticleThumbnail(
                            article = dummyArticles[2],
                            onClick = { TODO() },
                            standardPadding = standardPadding
                        )
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        ArticleThumbnail(
                            article = dummyArticles[3],
                            onClick = { TODO() },
                            standardPadding = standardPadding
                        )
                    }
                }
            }
        }

        item {
            Spacer(
                modifier = Modifier.padding(
                    bottom = WindowInsets.safeDrawing.asPaddingValues().calculateBottomPadding() * 2
                            + standardPadding
                )
            )
        }
    }
}

@Composable
fun VideoThumbnail(
    videoId: String,
    onClick: () -> Unit,
    standardPadding: Dp
) {
    Box(
        modifier = Modifier
            .size(
                width = standardPadding * 12,
                height = standardPadding * 8
            )
            .clip(MaterialTheme.shapes.large)
            .clickable { onClick() }
    ) {
        Image(
            painter = rememberAsyncImagePainter("https://img.youtube.com/vi/$videoId/0.jpg"),
            contentDescription = "Video Thumbnail",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Icon(
            imageVector = Icons.Default.PlayArrow,
            contentDescription = "Play Video",
            tint = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier
                .align(Alignment.Center)
                .size(standardPadding * 3)
        )
    }
}

data class Article(
    val id: Int,
    val title: String,
    val description: String,
    val imageUrl: String
)

val dummyArticles = listOf(
    Article(
        1,
        "Steps for Improving Your Eating Habits",
        "Healthy eating is important for maintaining a healthy weight and general health.",
        "https://www.cdc.gov/healthy-weight-growth/media/images/Writinginjournal.jpg"
    ),
    Article(
        2,
        "The 7 foods you should add to your diet",
        "Eating healthily doesn't need to be about depriving yourself - adding to your diet can be just as effective.",
        "https://www.saga.co.uk/helix-contentlibrary/saga/magazine/articles/2024/08-august/gettyimages-1398325989-sardines-healthy-food-768.jpg?sc=max&mw=800&h=450&la=en&h=576&w=768&hash=01EB1628EC5879BB68741663DBAA1839"
    ),
    Article(
        3,
        "How to Calculate BMR and TDEE and Why You Should",
        "From celebrity-endorsed eating plans to influencer-led weight loss programmes.",
        "https://www.naturesbest.co.uk/images/blog/CalculateBMRandTDEE_1.jpg"
    ),
    Article(
        4,
        "Revealing 6 healthy habits to improve quality of life",
        "Many studies have shown that human life expectancy depends mainly on living environment and lifestyle and diet.",
        "https://cdn.nhathuoclongchau.com.vn/unsafe/800x0/https://cms-prod.s3-sgn09.fptcloud.com/bat_mi_6_thoi_quen_lanh_manh_nang_cao_chat_luong_cuoc_song_4_239314bc95.jpg"
    )
)

@Composable
fun ArticleThumbnail(
    article: Article,
    onClick: () -> Unit,
    standardPadding: Dp
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
    ) {
        AsyncImage(
            model = article.imageUrl,
            contentDescription = "Article Thumbnail",
            modifier = Modifier
                .fillMaxWidth()
                .height(standardPadding * 6),
            contentScale = ContentScale.Crop
        )

        Column(modifier = Modifier.padding(standardPadding)) {
            Text(
                text = article.title,
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.Bold
                )
            )

            Text(
                text = article.description,
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.labelSmall
            )
        }

        TextButton(onClick = onClick) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "See details",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodySmall
                )
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "See details",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Preview(
    device = "id:pixel",
    showSystemUi = true,
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL,
    locale = "vi"
)
@Composable
private fun KnowledgeScreenDarkModePreviewInSmallPhone() {
    BioFitTheme {
        KnowledgeScreen()
    }
}

@Preview(
    device = "id:pixel_9_pro_xl",
    showSystemUi = true,
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL,
)
@Composable
private fun KnowledgeScreenPreviewInLargePhone() {
    BioFitTheme {
        KnowledgeScreen()
    }
}

@Preview(
    device = "spec:parent=Nexus 10,orientation=portrait",
    locale = "vi",
    showSystemUi = true,
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
private fun KnowledgeScreenPreviewInTablet() {
    BioFitTheme {
        KnowledgeScreen()
    }
}

@Preview(
    device = "spec:parent=pixel,orientation=landscape",
    showSystemUi = true,
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL,
    locale = "vi"
)
@Composable
private fun KnowledgeScreenLandscapeDarkModePreviewInSmallPhone() {
    BioFitTheme {
        KnowledgeScreen()
    }
}

@Preview(
    device = "spec:parent=pixel_9_pro_xl,orientation=landscape",
    showSystemUi = true,
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
private fun KnowledgeScreenLandscapePreviewInLargePhone() {
    BioFitTheme {
        KnowledgeScreen()
    }
}

@Preview(
    device = "spec:parent=Nexus 10",
    locale = "vi",
    showSystemUi = true,
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
private fun KnowledgeScreenLandscapePreviewInTablet() {
    BioFitTheme {
        KnowledgeScreen()
    }
}