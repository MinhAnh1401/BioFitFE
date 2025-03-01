package com.example.biofit.ui.activity

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.webkit.WebView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.LifecycleOwner
import com.example.biofit.R
import com.example.biofit.navigation.MainActivity
import com.example.biofit.ui.components.TopBar
import com.example.biofit.ui.components.getStandardPadding
import com.example.biofit.ui.screen.VideoItem
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
        val getVideoId = intent.getStringExtra("VIDEO_ID")
        setContent {
            BioFitTheme {
                if (getVideoId != null) {
                    KnowledgeVideoScreen(
                        getVideoId = getVideoId,
                        isFullScreen = isFullScreen,
                        currentTime = currentTime
                    ) { time ->
                        currentTime = time
                    }
                }
            }
        }
        WebView.setWebContentsDebuggingEnabled(true)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        isFullScreen = newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE
        val getVideoId = intent.getStringExtra("VIDEO_ID")
        setContent {
            BioFitTheme {
                if (getVideoId != null) {
                    KnowledgeVideoScreen(
                        getVideoId = getVideoId,
                        isFullScreen = isFullScreen,
                        currentTime = currentTime
                    ) { time ->
                        currentTime = time
                    }
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
    getVideoId: String,
    isFullScreen: Boolean,
    currentTime: Float,
    onTimeUpdate: (Float) -> Unit
) {
    val context = LocalContext.current
    val activity = context as? Activity

    val standardPadding = getStandardPadding().first

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
                    TopBar(
                        onBackClick = { activity?.finish() },
                        onHomeClick = {
                            activity?.let {
                                val intent = Intent(it, MainActivity::class.java)
                                it.startActivity(intent)
                            }
                        },
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
                getVideoId = getVideoId,
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
    getVideoId: String,
    standardPadding: Dp,
    isFullScreen: Boolean,
    currentTime: Float,
    onTimeUpdate: (Float) -> Unit
) {
    val videoId by rememberSaveable { mutableStateOf(getVideoId) }

    Column(modifier = Modifier.fillMaxSize()) {
        YouTubePlayer(
            youtubeVideoId = videoId,
            lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current,
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
        modifier = if (isFullScreen) Modifier
            .fillMaxWidth()
            .fillMaxHeight()
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
            getVideoId = "MHBOOP_eKZE",
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
            getVideoId = "MHBOOP_eKZE",
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
            getVideoId = "MHBOOP_eKZE",
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
            getVideoId = "MHBOOP_eKZE",
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
            getVideoId = "MHBOOP_eKZE",
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
            getVideoId = "MHBOOP_eKZE",
            isFullScreen = true,
            currentTime = 0f,
            onTimeUpdate = { }
        )
    }
}