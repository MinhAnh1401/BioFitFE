package com.example.biofit.ui.screen

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.example.biofit.R
import com.example.biofit.ui.activity.KnowledgeVideoActivity
import com.example.biofit.ui.components.SubCard
import com.example.biofit.ui.components.getStandardPadding
import com.example.biofit.ui.theme.BioFitTheme

@Composable
fun KnowledgeScreen() {
    val standardPadding = getStandardPadding().first
    val modifier = getStandardPadding().second

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = WindowInsets.safeDrawing.asPaddingValues().calculateTopPadding(),
                    start = standardPadding,
                    end = standardPadding,
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = modifier,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = stringResource(R.string.nutritional_knowledge),
                    modifier = Modifier.weight(1f),
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.headlineSmall
                )

                IconButton(
                    onClick = { TODO() }
                ) {
                    Icon(
                        painter = painterResource(R.drawable.brain_filled_head_profile),
                        contentDescription = "Knowledge",
                        modifier = Modifier.size(standardPadding * 2f),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }

            KnowledgeContent(
                standardPadding,
                modifier
            )
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
    val activity = context as? Activity

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(standardPadding * 2)
    ) {
        item {
            Column(
                modifier = modifier
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(standardPadding),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(R.drawable.heart_fill),
                        contentDescription = "Heart",
                        modifier = Modifier.size(standardPadding * 2f),
                        tint = Color(0xFFDD2C00)
                    )

                    Text(
                        text = stringResource(R.string.nutrition_and_care),
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.headlineSmall
                    )
                }

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
                                    activity?.let {
                                        val intent = Intent(it, KnowledgeVideoActivity::class.java)
                                        intent.putExtra("VIDEO_ID", videoItem.videoId)
                                        it.startActivity(intent)
                                    }
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
                Row(
                    horizontalArrangement = Arrangement.spacedBy(standardPadding),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(R.drawable.figure_strengthtraining_traditional),
                        contentDescription = "Exercise",
                        modifier = Modifier.size(standardPadding * 2f),
                        tint = Color(0xFF00C853)
                    )

                    Text(
                        text = stringResource(R.string.exercise),
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.headlineSmall
                    )
                }

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
                                    activity?.let {
                                        val intent = Intent(it, KnowledgeVideoActivity::class.java)
                                        intent.putExtra("VIDEO_ID", videoItem.videoId)
                                        it.startActivity(intent)
                                    }
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
                Row(
                    horizontalArrangement = Arrangement.spacedBy(standardPadding),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(R.drawable.magazine_fill),
                        contentDescription = "Article",
                        modifier = Modifier.size(standardPadding * 2f),
                        tint = Color(0xFF0091EA)
                    )

                    Text(
                        text = stringResource(R.string.article),
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.headlineSmall
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(standardPadding)
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        ArticleThumbnail(
                            article = dummyArticles[0],
                            standardPadding = standardPadding
                        )
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        ArticleThumbnail(
                            article = dummyArticles[1],
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
                            standardPadding = standardPadding
                        )
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        ArticleThumbnail(
                            article = dummyArticles[3],
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
    val imageUrl: String,
    val url: String,
)

val dummyArticles = listOf(
    Article(
        id = 1,
        title = "Steps for Improving Your Eating Habits",
        description = "Healthy eating is important for maintaining a healthy weight and general health.",
        imageUrl = "https://www.cdc.gov/healthy-weight-growth/media/images/Writinginjournal.jpg",
        url = "https://www.cdc.gov/healthy-weight-growth/losing-weight/improve-eating-habits.html"
    ),
    Article(
        2,
        "The 7 foods you should add to your diet",
        "Eating healthily doesn't need to be about depriving yourself - adding to your diet can be just as effective.",
        "https://www.saga.co.uk/helix-contentlibrary/saga/magazine/articles/2024/08-august/gettyimages-1398325989-sardines-healthy-food-768.jpg?sc=max&mw=800&h=450&la=en&h=576&w=768&hash=01EB1628EC5879BB68741663DBAA1839",
        url = "https://www.saga.co.uk/magazine/health-and-wellbeing/foods-you-should-add-to-your-diet?srsltid=AfmBOoo5dr1PKVQBrujF7rzCH2B3kgg4n1PNmvCI7RWISrhNeaXuGDGm"
    ),
    Article(
        3,
        "How to Calculate BMR and TDEE and Why You Should",
        "From celebrity-endorsed eating plans to influencer-led weight loss programmes.",
        "https://www.naturesbest.co.uk/images/blog/CalculateBMRandTDEE_1.jpg",
        url = "https://www.naturesbest.co.uk/sports-articles/how-to-calculate-bmr-and-tdee-and-why-you-should/"
    ),
    Article(
        4,
        "Revealing 6 healthy habits to improve quality of life",
        "Many studies have shown that human life expectancy depends mainly on living environment and lifestyle and diet.",
        "https://cdn.nhathuoclongchau.com.vn/unsafe/800x0/https://cms-prod.s3-sgn09.fptcloud.com/bat_mi_6_thoi_quen_lanh_manh_nang_cao_chat_luong_cuoc_song_4_239314bc95.jpg",
        url = "https://nhathuoclongchau.com.vn/bai-viet/bat-mi-6-thoi-quen-lanh-manh-nang-cao-chat-luong-cuoc-song.html"
    )
)

@Composable
fun ArticleThumbnail(
    article: Article,
    standardPadding: Dp
) {
    val context = LocalContext.current

    SubCard(
        onClick = {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(article.url))
            context.startActivity(intent)
        },
        modifier = Modifier.fillMaxWidth()
    ) {
        AsyncImage(
            model = article.imageUrl,
            contentDescription = "Article Thumbnail",
            modifier = Modifier
                .fillMaxWidth()
                .height(standardPadding * 6),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier.padding(standardPadding),
            verticalArrangement = Arrangement.spacedBy(standardPadding / 2)
        ) {
            Text(
                text = article.title,
                color = MaterialTheme.colorScheme.onBackground,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.Bold
                )
            )

            Text(
                text = article.description,
                color = MaterialTheme.colorScheme.onBackground,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.labelSmall
            )
        }

        TextButton(
            onClick = {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(article.url))
                context.startActivity(intent)
            }
        ) {
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