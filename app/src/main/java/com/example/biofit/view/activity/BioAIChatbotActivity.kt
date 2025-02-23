package com.example.biofit.view.activity

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import com.example.biofit.R
import com.example.biofit.controller.ChatBotController
import com.example.biofit.controller.DatabaseHelper
import com.example.biofit.model.ChatBotModel
import com.example.biofit.model.UserData
import com.example.biofit.view.dialog.TopBar
import com.example.biofit.view.ui_theme.BioFitTheme

class BioAIChatbotActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val databaseHelper = DatabaseHelper(this)
        val model = ChatBotModel(
            context = this,
            apiKey = "AIzaSyD5vPJ7S-mnKpnc-Pf3lKXZqB3G6p5vZ6s",
            databaseHelper = databaseHelper,
        )
        val controller = ChatBotController(
            model = model,
            context = this
        )
        val userData = UserData(1, "Nam", "Bóng đá, Đọc sách", 70.5f, 175.0f)
        databaseHelper.addUserData(userData)
        setContent {
            BioFitTheme {
                BioAIChatbotScreen(
                    controller = controller
                )
            }
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        recreate()
    }
}

@Composable
fun BioAIChatbotScreen(controller: ChatBotController) {
    val context = LocalContext.current
    val activity = context as? Activity
    val keyboardController = LocalSoftwareKeyboardController.current

    val standardPadding = getStandardPadding().first
    val modifier = getStandardPadding().second

    val chatHistory by remember { mutableStateOf(controller.chatHistory) }
    val isLoading by controller.isLoading
    val scope = rememberCoroutineScope()
    var userInput by remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsPadding(WindowInsets.ime)
                .padding(
                    top = WindowInsets.safeDrawing.asPaddingValues().calculateTopPadding(),
                    start = standardPadding,
                    end = standardPadding,
                    bottom = WindowInsets.safeDrawing.asPaddingValues().calculateBottomPadding()
                ),
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
                gradientTitle = stringResource(R.string.bionix),
                middleButton = null,
                rightButton = null,
                standardPadding = standardPadding
            )

            Spacer(modifier = Modifier.height(standardPadding))

            LazyColumn(state = listState, modifier = Modifier.weight(1f)) {
                items(chatHistory) { chat ->
                    ChatBubble(
                        text = chat.userMessage,
                        isUser = true,
                        standardPadding = standardPadding
                    )
                    Row {
                        ChatBubble(
                            text = chat.botResponse,
                            isUser = false,
                            standardPadding = standardPadding
                        )
                    }
                }
                if (isLoading) {
                    item { LoadingIndicator() }
                }
            }

            LaunchedEffect(chatHistory.size) {
                listState.animateScrollToItem(chatHistory.size)
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = userInput,
                    onValueChange = { userInput = it },
                    modifier = Modifier
                        .weight(1f)
                        .padding(standardPadding),
                    textStyle = MaterialTheme.typography.bodySmall,
                    placeholder = {
                        Text(
                            text = stringResource(R.string.enter_message),
                            color = MaterialTheme.colorScheme.outline,
                            style = MaterialTheme.typography.bodySmall
                        )
                    },
                    maxLines = 4,
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Send),
                    keyboardActions = KeyboardActions(
                        onSend = {
                            controller.sendMessage(userInput, scope)
                            userInput = ""
                            keyboardController?.hide()
                        }
                    ),
                    shape = MaterialTheme.shapes.large,
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedTextColor = MaterialTheme.colorScheme.onBackground,
                        focusedTextColor = MaterialTheme.colorScheme.onBackground,
                        unfocusedContainerColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent,
                        focusedBorderColor = Color.Transparent
                    )
                )
                IconButton(
                    onClick = {
                        controller.sendMessage(userInput, scope)
                        userInput = ""
                        keyboardController?.hide()
                    },
                    modifier = Modifier.size(standardPadding * 4)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_send),
                        contentDescription = "Send",
                        modifier = Modifier.size(standardPadding * 2),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

@Composable
fun ChatBubble(
    text: String,
    isUser: Boolean,
    standardPadding: Dp
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(standardPadding),
        contentAlignment = if (isUser) Alignment.CenterEnd else Alignment.CenterStart
    ) {
        Box(
            modifier = Modifier
                .background(
                    brush = if (isUser) {
                        Brush.linearGradient(
                            colors = listOf(
                                if (isSystemInDarkTheme()) {
                                    Color(0xFF64DD17).copy(alpha = 0.5f)
                                } else {
                                    Color(0xFFAEEA00).copy(alpha = 0.5f)
                                },
                                MaterialTheme.colorScheme.primary
                            ),
                            start = Offset(0f, 0f),
                            end = Offset(
                                Float.POSITIVE_INFINITY,
                                Float.POSITIVE_INFINITY
                            )
                        )
                    } else {
                        Brush.linearGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f),
                                MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                            ),
                            start = Offset(0f, 0f),
                            end = Offset(
                                Float.POSITIVE_INFINITY,
                                Float.POSITIVE_INFINITY
                            )
                        )
                    },
                    shape = MaterialTheme.shapes.large
                )
                .padding(standardPadding)
        ) {
            Text(
                text = text,
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
fun LoadingIndicator() {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        CircularProgressIndicator()
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
private fun BioAIChatbotScreenDarkModePreviewInSmallPhone() {
    BioFitTheme {
        val model = ChatBotModel(
            apiKey = "AIzaSyD5vPJ7S-mnKpnc-Pf3lKXZqB3G6p5vZ6s",
            context = LocalContext.current,
            databaseHelper = DatabaseHelper(LocalContext.current)
        )
        val controller = ChatBotController(
            model,
            context = LocalContext.current
        )
        BioAIChatbotScreen(
            controller = controller
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
private fun BioAIChatbotScreenPreviewInLargePhone() {
    BioFitTheme {
        val model = ChatBotModel(
            apiKey = "AIzaSyD5vPJ7S-mnKpnc-Pf3lKXZqB3G6p5vZ6s",
            context = LocalContext.current,
            databaseHelper = DatabaseHelper(LocalContext.current)
        )
        val controller = ChatBotController(
            model,
            context = LocalContext.current
        )
        BioAIChatbotScreen(
            controller = controller
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
private fun BioAIChatbotScreenPreviewInTablet() {
    BioFitTheme {
        val model = ChatBotModel(
            apiKey = "AIzaSyD5vPJ7S-mnKpnc-Pf3lKXZqB3G6p5vZ6s",
            context = LocalContext.current,
            databaseHelper = DatabaseHelper(LocalContext.current)
        )
        val controller = ChatBotController(
            model,
            context = LocalContext.current
        )
        BioAIChatbotScreen(
            controller = controller
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
private fun BioAIChatbotScreenLandscapeDarkModePreviewInSmallPhone() {
    BioFitTheme {
        val model = ChatBotModel(
            apiKey = "AIzaSyD5vPJ7S-mnKpnc-Pf3lKXZqB3G6p5vZ6s",
            context = LocalContext.current,
            databaseHelper = DatabaseHelper(LocalContext.current)
        )
        val controller = ChatBotController(
            model,
            context = LocalContext.current
        )
        BioAIChatbotScreen(
            controller = controller
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
private fun BioAIChatbotScreenLandscapePreviewInLargePhone() {
    BioFitTheme {
        val model = ChatBotModel(
            apiKey = "AIzaSyD5vPJ7S-mnKpnc-Pf3lKXZqB3G6p5vZ6s",
            context = LocalContext.current,
            databaseHelper = DatabaseHelper(LocalContext.current)
        )
        val controller = ChatBotController(
            model,
            context = LocalContext.current
        )
        BioAIChatbotScreen(
            controller = controller
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
private fun BioAIChatbotScreenLandscapePreviewInTablet() {
    BioFitTheme {
        val model = ChatBotModel(
            apiKey = "AIzaSyD5vPJ7S-mnKpnc-Pf3lKXZqB3G6p5vZ6s",
            context = LocalContext.current,
            databaseHelper = DatabaseHelper(LocalContext.current)
        )
        val controller = ChatBotController(
            model,
            context = LocalContext.current
        )
        BioAIChatbotScreen(
            controller = controller
        )
    }
}