package com.example.biofit.ui.activity

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.biofit.R
import com.example.biofit.data.dto.UserDTO
import com.example.biofit.data.model.ChatBotModel
import com.example.biofit.navigation.MainActivity
import com.example.biofit.ui.animated.AnimatedGradientText
import com.example.biofit.ui.animated.BlinkingGradientBox
import com.example.biofit.ui.animated.OneTimeAnimatedGradientText
import com.example.biofit.ui.components.TopBar
import com.example.biofit.ui.components.getStandardPadding
import com.example.biofit.ui.theme.BioFitTheme
import com.example.biofit.view_model.AIChatbotViewModel


class AIChatbotActivity : ComponentActivity() {
    private lateinit var chatViewModel: AIChatbotViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val userDTO: UserDTO? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("USER_DATA", UserDTO::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra("USER_DATA")
        }
        if (userDTO == null) {
            finish()
            return
        }
        val model = ChatBotModel(
            userDTO = userDTO,
            context = this,
            apiKey = "AIzaSyD5vPJ7S-mnKpnc-Pf3lKXZqB3G6p5vZ6s"
        )
        chatViewModel = AIChatbotViewModel(model, this)
        setContent {
            BioFitTheme {
                AIChatbotScreen(viewModel = chatViewModel)
            }
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        recreate()
    }
}

@Composable
fun AIChatbotScreen(viewModel: AIChatbotViewModel) {
    val context = LocalContext.current
    val activity = context as? Activity
    val keyboardController = LocalSoftwareKeyboardController.current

    val standardPadding = getStandardPadding().first

    val chatHistory by remember { mutableStateOf(viewModel.chatHistory) }
    val scope = rememberCoroutineScope()
    var userInput by remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .padding(
                    top = WindowInsets.safeDrawing.asPaddingValues().calculateTopPadding(),
                    start = standardPadding,
                    end = standardPadding,
                    bottom = standardPadding
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
                title = stringResource(R.string.ai_assistant_bionix),
                middleButton = null,
                rightButton = null,
                standardPadding = standardPadding
            )

            LazyColumn(
                state = listState
            ) {
                items(chatHistory) { chat ->
                    ChatBubble(
                        text = chat.userMessage,
                        isUser = true,
                        standardPadding = standardPadding
                    )

                    ChatBubble(
                        text = chat.botResponse,
                        isUser = false,
                        standardPadding = standardPadding
                    )
                }

                item {
                    Spacer(
                        modifier = Modifier.padding(
                            bottom = WindowInsets.safeDrawing.asPaddingValues()
                                .calculateBottomPadding() * 5
                                    + standardPadding
                        )
                    )
                }
            }

            LaunchedEffect(chatHistory.size) {
                listState.animateScrollToItem(chatHistory.size)
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = standardPadding,
                    end = standardPadding,
                    bottom = WindowInsets.safeDrawing.asPaddingValues().calculateBottomPadding()
                            + standardPadding
                ),
            verticalAlignment = Alignment.Bottom
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                BlinkingGradientBox(
                    borderAlpha = 0.25f,
                    alpha = 0.75f,
                    shape = MaterialTheme.shapes.extraLarge
                ) {
                    OutlinedTextField(
                        value = userInput,
                        onValueChange = { userInput = it },
                        textStyle = MaterialTheme.typography.bodySmall,
                        placeholder = {
                            Text(
                                text = stringResource(R.string.enter_message),
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.bodySmall
                            )
                        },
                        trailingIcon = {
                            if (userInput != "") {
                                IconButton(
                                    onClick = {
                                        viewModel.sendMessage(userInput, scope)
                                        userInput = ""
                                        keyboardController?.hide()
                                    },
                                    modifier = Modifier.padding(end = standardPadding)
                                ) {
                                    Icon(
                                        painter = painterResource(R.drawable.ic_send),
                                        contentDescription = "Send",
                                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                }
                            }
                        },
                        maxLines = 4,
                        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Send),
                        keyboardActions = KeyboardActions(
                            onSend = {
                                viewModel.sendMessage(userInput, scope)
                                userInput = ""
                                keyboardController?.hide()
                            }
                        ),
                        shape = MaterialTheme.shapes.large,
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedContainerColor = Color.Transparent,
                            focusedContainerColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent,
                            focusedBorderColor = Color.Transparent
                        )
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
    var isAnimationFinished by rememberSaveable { mutableStateOf(false) }

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
                        MaterialTheme.colorScheme.outline.copy(alpha = 0.25f)
                    } else {
                        Color.Transparent
                    },
                    shape = MaterialTheme.shapes.extraLarge.copy(
                        bottomEnd = CornerSize(15f)
                    )
                )
                .border(
                    width = 1.dp,
                    color = if (isUser) {
                        MaterialTheme.colorScheme.outline.copy(alpha = 0.25f)
                    } else {
                        Color.Transparent
                    },
                    shape = MaterialTheme.shapes.extraLarge.copy(
                        bottomEnd = CornerSize(15f)
                    )
                )
                .padding(if (isUser) standardPadding else 0.dp)
        ) {
            if (isUser) {
                Text(
                    text = text,
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.bodyMedium
                )
            } else {
                if (!isAnimationFinished) {
                    if (text == stringResource(R.string.thinking)) {
                        AnimatedGradientText(
                            highlightColor = Color(0xFFAEEA00),
                            textBodyColor1 = MaterialTheme.colorScheme.primary,
                            textBodyColor2 = MaterialTheme.colorScheme.primary,
                            text = text,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    } else {
                        OneTimeAnimatedGradientText(
                            highlightColor = MaterialTheme.colorScheme.primary,
                            baseColor = MaterialTheme.colorScheme.onBackground,
                            hideColor = Color.Transparent,
                            text = text,
                            style = MaterialTheme.typography.bodyMedium,
                            onAnimationEnd = {
                                isAnimationFinished = true
                            }
                        )
                    }
                } else {
                    Text(
                        text = text,
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
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
private fun BioAIChatbotScreenDarkModePreviewInSmallPhone() {
    BioFitTheme {
        lateinit var chatBotViewModel: AIChatbotViewModel
        val userDTO = UserDTO(
            userId = 0,
            fullName = "Nguyen Van A",
            email = "anguyenvan@gmail.com",
            gender = 0,
            height = 170f,
            weight = 57f,
            targetWeight = 60f,
            dateOfBirth = "2000-01-01",
            avatar = null,
            createdAccount = "2025-02-28"
        )
        val model = ChatBotModel(
            userDTO = userDTO,
            context = LocalContext.current,
            apiKey = "AIzaSyD5vPJ7S-mnKpnc-Pf3lKXZqB3G6p5vZ6s"
        )
        chatBotViewModel = AIChatbotViewModel(model, LocalContext.current)
        AIChatbotScreen(viewModel = chatBotViewModel)
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
        lateinit var chatBotViewModel: AIChatbotViewModel
        val userDTO = UserDTO(
            userId = 0,
            fullName = "Nguyen Van A",
            email = "anguyenvan@gmail.com",
            gender = 0,
            height = 170f,
            weight = 57f,
            targetWeight = 60f,
            dateOfBirth = "2000-01-01",
            avatar = null,
            createdAccount = "2025-02-28"
        )
        val model = ChatBotModel(
            userDTO = userDTO,
            context = LocalContext.current,
            apiKey = "AIzaSyD5vPJ7S-mnKpnc-Pf3lKXZqB3G6p5vZ6s"
        )
        chatBotViewModel = AIChatbotViewModel(model, LocalContext.current)
        AIChatbotScreen(viewModel = chatBotViewModel)
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
        lateinit var chatBotViewModel: AIChatbotViewModel
        val userDTO = UserDTO(
            userId = 0,
            fullName = "Nguyen Van A",
            email = "anguyenvan@gmail.com",
            gender = 0,
            height = 170f,
            weight = 57f,
            targetWeight = 60f,
            dateOfBirth = "2000-01-01",
            avatar = null,
            createdAccount = "2025-02-28"
        )
        val model = ChatBotModel(
            userDTO = userDTO,
            context = LocalContext.current,
            apiKey = "AIzaSyD5vPJ7S-mnKpnc-Pf3lKXZqB3G6p5vZ6s"
        )
        chatBotViewModel = AIChatbotViewModel(model, LocalContext.current)
        AIChatbotScreen(viewModel = chatBotViewModel)
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
        lateinit var chatBotViewModel: AIChatbotViewModel
        val userDTO = UserDTO(
            userId = 0,
            fullName = "Nguyen Van A",
            email = "anguyenvan@gmail.com",
            gender = 0,
            height = 170f,
            weight = 57f,
            targetWeight = 60f,
            dateOfBirth = "2000-01-01",
            avatar = null,
            createdAccount = "2025-02-28"
        )
        val model = ChatBotModel(
            userDTO = userDTO,
            context = LocalContext.current,
            apiKey = "AIzaSyD5vPJ7S-mnKpnc-Pf3lKXZqB3G6p5vZ6s"
        )
        chatBotViewModel = AIChatbotViewModel(model, LocalContext.current)
        AIChatbotScreen(viewModel = chatBotViewModel)
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
        lateinit var chatBotViewModel: AIChatbotViewModel
        val userDTO = UserDTO(
            userId = 0,
            fullName = "Nguyen Van A",
            email = "anguyenvan@gmail.com",
            gender = 0,
            height = 170f,
            weight = 57f,
            targetWeight = 60f,
            dateOfBirth = "2000-01-01",
            avatar = null,
            createdAccount = "2025-02-28"
        )
        val model = ChatBotModel(
            userDTO = userDTO,
            context = LocalContext.current,
            apiKey = "AIzaSyD5vPJ7S-mnKpnc-Pf3lKXZqB3G6p5vZ6s"
        )
        chatBotViewModel = AIChatbotViewModel(model, LocalContext.current)
        AIChatbotScreen(viewModel = chatBotViewModel)
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
        lateinit var chatBotViewModel: AIChatbotViewModel
        val userDTO = UserDTO(
            userId = 0,
            fullName = "Nguyen Van A",
            email = "anguyenvan@gmail.com",
            gender = 0,
            height = 170f,
            weight = 57f,
            targetWeight = 60f,
            dateOfBirth = "2000-01-01",
            avatar = null,
            createdAccount = "2025-02-28"
        )
        val model = ChatBotModel(
            userDTO = userDTO,
            context = LocalContext.current,
            apiKey = "AIzaSyD5vPJ7S-mnKpnc-Pf3lKXZqB3G6p5vZ6s"
        )
        chatBotViewModel = AIChatbotViewModel(model, LocalContext.current)
        AIChatbotScreen(viewModel = chatBotViewModel)
    }
}
