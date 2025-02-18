package com.example.biofit.view

import android.content.res.Configuration
import android.os.Bundle
import android.webkit.WebView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.LifecycleOwner
import com.example.biofit.R
import com.example.biofit.ui.theme.BioFitTheme
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

class KnowledgeVideoActivity : ComponentActivity() {
    private var isFullScreen = false
    private var currentTime = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        currentTime = savedInstanceState?.getFloat("VIDEO_TIME") ?: 0f

        enableEdgeToEdge()
        setContent {
            BioFitTheme {
                KnowledgeVideoScreen(isFullScreen, currentTime) { time ->
                    currentTime = time
                }
            }
        }
        WebView.setWebContentsDebuggingEnabled(true)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        isFullScreen = newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE
        setContent {
            BioFitTheme {
                KnowledgeVideoScreen(isFullScreen, currentTime) { time ->
                    currentTime = time
                }
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putFloat("VIDEO_TIME", currentTime)
    }
}

val videoItem = VideoItem(
    "0F-TPouHTAM",
    "3 exercises for a flexible and healthy body"
)

@Composable
fun KnowledgeVideoScreen(
    isFullScreen: Boolean,
    currentTime: Float,
    onTimeUpdate: (Float) -> Unit
) {
    val standardPadding = getStandardPadding().first
    val modifier = getStandardPadding().second

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column {
            if (!isFullScreen) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            top = WindowInsets.safeDrawing.asPaddingValues().calculateTopPadding(),
                            start = standardPadding,
                            end = standardPadding,
                        ),
                    verticalArrangement = Arrangement.spacedBy(standardPadding),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    TopBarSetting(
                        onBackClick = { TODO() },
                        title = stringResource(R.string.nutritional_knowledge),
                        middleButton = null,
                        rightButton = {
                            IconButton(
                                onClick = { TODO() },
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.ic_knowledge_2),
                                    contentDescription = "Knowledge",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        },
                        standardPadding = standardPadding
                    )
                }
                Spacer(modifier = Modifier.height(standardPadding))
            }

            KnowledgeVideoContent(
                videoItem = videoItem,
                standardPadding = standardPadding,
                isFullScreen = isFullScreen,
                currentTime = currentTime,
                onTimeUpdate = onTimeUpdate
            )
        }
    }
}

@Composable
fun KnowledgeVideoContent(
    videoItem: VideoItem,
    standardPadding: Dp,
    isFullScreen: Boolean,
    currentTime: Float,
    onTimeUpdate: (Float) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        YouTubePlayer(
            youtubeVideoId = videoItem.videoId,
            lifecycleOwner = LocalLifecycleOwner.current,
            isFullScreen = isFullScreen,
            currentTime = currentTime,
            onTimeUpdate = onTimeUpdate
        )

        if (!isFullScreen) {
            Text(
                text = videoItem.title,
                modifier = Modifier.padding(standardPadding),
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.titleSmall
            )
        }
    }
}

@Composable
fun YouTubePlayer(
    youtubeVideoId: String,
    lifecycleOwner: LifecycleOwner,
    isFullScreen: Boolean,
    currentTime: Float,
    onTimeUpdate: (Float) -> Unit
) {
    AndroidView(
        factory = { context ->
            YouTubePlayerView(context).apply {
                lifecycleOwner.lifecycle.addObserver(this)
                addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                    override fun onReady(youTubePlayer: YouTubePlayer) {
                        youTubePlayer.loadVideo(youtubeVideoId, currentTime)
                        youTubePlayer.play()
                    }

                    override fun onCurrentSecond(youTubePlayer: YouTubePlayer, second: Float) {
                        onTimeUpdate(second)
                    }
                })
            }
        },
        modifier = if (isFullScreen) Modifier.fillMaxWidth().fillMaxHeight()
        else Modifier.fillMaxWidth()
    )
}

@Preview(
    device = "id:pixel",
    showSystemUi = true,
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL,
    locale = "vi"
)
@Composable
private fun KnowledgeVideoScreenDarkModePreviewInSmallPhone() {
    BioFitTheme {
        KnowledgeVideoScreen(
            isFullScreen = false,
            currentTime = 0f,
            onTimeUpdate = { }
        )
    }
}

@Preview(
    device = "id:pixel_9_pro_xl",
    showSystemUi = true,
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL,
)
@Composable
private fun KnowledgeVideoScreenPreviewInLargePhone() {
    BioFitTheme {
        KnowledgeVideoScreen(
            isFullScreen = false,
            currentTime = 0f,
            onTimeUpdate = { }
        )
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
private fun KnowledgeVideoScreenPreviewInTablet() {
    BioFitTheme {
        KnowledgeVideoScreen(
            isFullScreen = false,
            currentTime = 0f,
            onTimeUpdate = { }
        )
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
private fun KnowledgeVideoScreenLandscapeDarkModePreviewInSmallPhone() {
    BioFitTheme {
        KnowledgeVideoScreen(
            isFullScreen = true,
            currentTime = 0f,
            onTimeUpdate = { }
        )
    }
}

@Preview(
    device = "spec:parent=pixel_9_pro_xl,orientation=landscape",
    showSystemUi = true,
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
private fun KnowledgeVideoScreenLandscapePreviewInLargePhone() {
    BioFitTheme {
        KnowledgeVideoScreen(
            isFullScreen = true,
            currentTime = 0f,
            onTimeUpdate = { }
        )
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
private fun KnowledgeVideoScreenLandscapePreviewInTablet() {
    BioFitTheme {
        KnowledgeVideoScreen(
            isFullScreen = true,
            currentTime = 0f,
            onTimeUpdate = { }
        )
    }
}