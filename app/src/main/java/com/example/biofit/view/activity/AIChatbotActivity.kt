package com.example.biofit.view.activity

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.RepeatMode
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.biofit.R
import com.example.biofit.controller.ChatBotController
import com.example.biofit.controller.DatabaseHelper
import com.example.biofit.model.ChatBotModel
import com.example.biofit.model.UserData
import com.example.biofit.view.sub_components.AnimatedGradientText
import com.example.biofit.view.sub_components.OneTimeAnimatedGradientText
import com.example.biofit.view.sub_components.TopBar
import com.example.biofit.view.ui_theme.BioFitTheme

class AIChatbotActivity : ComponentActivity() {
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
        val userData = UserData(
            id = 1,
            fullName = "Nguyễn Văn Chiến",
            email = "anguyen55@gmail.com",
            password = "Wsdfs343r",
            gender = 0,
            dateOfBirth = "2005-08-21",
            height = 165f,
            weight = 51f,
            targetWeight = 57f
        )
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

    val screenWidth = LocalConfiguration.current.screenWidthDp
    val standardPadding = getStandardPadding().first
    val modifier = getStandardPadding().second

    val chatHistory by remember { mutableStateOf(controller.chatHistory) }
    val scope = rememberCoroutineScope()
    var userInput by remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    val keywords = listOf("log out", "sign out", "đăng xuất")

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = if (isSystemInDarkTheme()) {
                    Brush.radialGradient(
                        center = Offset(screenWidth * 1.35f, Float.POSITIVE_INFINITY),
                        colors = listOf(
                            MaterialTheme.colorScheme.surfaceContainerHigh,
                            MaterialTheme.colorScheme.background
                        ),
                        radius = screenWidth * 1.5f
                    )
                } else {
                    SolidColor(MaterialTheme.colorScheme.background)
                }
            ),
        color = Color.Transparent
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
                gradientTitle = stringResource(R.string.ai_assistant_bionix),
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

                    Column {
                        ChatBubble(
                            text = chat.botResponse,
                            isUser = false,
                            standardPadding = standardPadding
                        )

                        if (keywords.any { it in chat.botResponse.lowercase() }) {
                            AIButton(
                                onClick = {
                                    activity?.let {
                                        val intent = Intent(it, LoginActivity::class.java)
                                        it.startActivity(intent)
                                        it.finish()
                                    }
                                },
                                title = stringResource(R.string.sign_out),
                                buttonColor = MaterialTheme.colorScheme.primary,
                                textColor = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }
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

                if (userInput != "") {
                    IconButton(
                        onClick = {
                            controller.sendMessage(userInput, scope)
                            userInput = ""
                            keyboardController?.hide()
                        },
                        modifier = Modifier
                            .size(standardPadding * 4)
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
}

@Composable
fun ChatBubble(
    text: String,
    isUser: Boolean,
    standardPadding: Dp
) {
    var isAnimationFinished by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(standardPadding),
        contentAlignment = if (isUser) Alignment.CenterEnd else Alignment.CenterStart
    ) {
        Box(
            modifier = Modifier
                .background(
                    color = if (isUser) {
                        MaterialTheme.colorScheme.outline.copy(alpha = 0.1f)
                    } else {
                        Color.Transparent
                    },
                    shape = MaterialTheme.shapes.large
                )
                .border(
                    width = 0.1.dp,
                    brush = if (isUser) {
                        SolidColor(MaterialTheme.colorScheme.outline)
                    } else {
                        SolidColor(Color.Transparent)
                    },
                    shape = MaterialTheme.shapes.large
                )
                .padding(if (isUser) standardPadding else 0.dp)
        ) {
            if (isUser) {
                Text(
                    text = text,
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.bodySmall
                )
            } else {
                if (!isAnimationFinished) {
                    if (text == stringResource(R.string.thinking)) {
                        AnimatedGradientText(
                            repeatMode = RepeatMode.Restart,
                            highlightColor = Color(0xFFAEEA00),
                            baseColor = MaterialTheme.colorScheme.onBackground,
                            text = text,
                            style = MaterialTheme.typography.bodySmall
                        )
                    } else {
                        OneTimeAnimatedGradientText(
                            highlightColor = Color(0xFFAEEA00),
                            baseColor = MaterialTheme.colorScheme.onBackground,
                            hideColor = Color.Transparent,
                            text = text,
                            style = MaterialTheme.typography.bodySmall,
                            onAnimationEnd = {
                                isAnimationFinished = true
                            }
                        )
                    }
                } else {
                    Text(
                        text = text,
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}

@Composable
fun AIButton(
    onClick: () -> Unit,
    title: String,
    buttonColor: Color,
    textColor: Color
) {
    Button(
        onClick = onClick,
        shape = MaterialTheme.shapes.large,
        colors = ButtonDefaults.buttonColors(
            containerColor = buttonColor
        )
    ) {
        Text(
            text = title,
            color = textColor,
            style = MaterialTheme.typography.labelLarge
        )
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