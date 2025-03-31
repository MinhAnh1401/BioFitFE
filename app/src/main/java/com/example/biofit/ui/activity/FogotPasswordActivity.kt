package com.example.biofit.ui.activity

import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import com.example.biofit.R
import com.example.biofit.ui.theme.BioFitTheme
import com.example.biofit.view_model.PasswordResetViewModel
import com.example.biofit.view_model.ViewModelFactory

private val primaryGreen = Color(0xFF34A853)
private val lightGreen = Color(0xFF66BB6A)
private val darkGreen = Color(0xFF1B5E20)

class ForgotPasswordActivity : ComponentActivity() {

    private lateinit var viewModel: PasswordResetViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(
            this,
            ViewModelFactory()
        )[PasswordResetViewModel::class.java]

        setContent {
            BioFitTheme {
                // Ghi đè màu chủ đề bằng bảng màu xanh lá cây
                val customColorScheme = MaterialTheme.colorScheme.copy(
                    primary = primaryGreen,
                    primaryContainer = lightGreen,
                    secondary = darkGreen,
                    background = Color(0xFFF5F8F5)
                )

                MaterialTheme(colorScheme = customColorScheme) {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        ForgotPasswordScreen(
                            viewModel = viewModel,
                            onBackClick = { finish() },
                            onResetComplete = { finish() }
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordScreen(
    viewModel: PasswordResetViewModel,
    onBackClick: () -> Unit,
    onResetComplete: () -> Unit
) {
    val requestState by viewModel.requestResetState.observeAsState(PasswordResetViewModel.RequestResetState.Initial)
    val confirmState by viewModel.confirmResetState.observeAsState(PasswordResetViewModel.ConfirmResetState.Initial)
    val emailState by viewModel.email.observeAsState("")

    val context = LocalContext.current
    var showResetCodeScreen by remember { mutableStateOf(false) }

    // Chuyển đổi animation giữa các màn hình
    val transitionState = remember {
        MutableTransitionState(showResetCodeScreen).apply {
            targetState = showResetCodeScreen
        }
    }

    LaunchedEffect(emailState) {
        if (emailState.isNotEmpty()) {
            showResetCodeScreen = true
            transitionState.targetState = true
        }
    }

    LaunchedEffect(confirmState) {
        if (confirmState is PasswordResetViewModel.ConfirmResetState.Success) {
            // animation thành công
            val successMessage = (confirmState as PasswordResetViewModel.ConfirmResetState.Success).message
            Toast.makeText(context, successMessage, Toast.LENGTH_LONG).show()
            onResetComplete()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        stringResource(R.string.forgot_password),
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.scale(0.9f)

                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        if (showResetCodeScreen) {
                            showResetCodeScreen = false
                            transitionState.targetState = false
                            viewModel.resetConfirmState()
                        } else {
                            onBackClick()
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                },
                actions = {

                    TextButton(
                        onClick = { onBackClick() },
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.exit),
                            color = MaterialTheme.colorScheme.onPrimary,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                },

                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = primaryGreen
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFFF5F8F5),
                            Color(0xFFE8F5E9)
                        )
                    )
                )
        ) {
            // Animation nâng cao với slide và fade
            AnimatedVisibility(
                visibleState = remember { MutableTransitionState(!showResetCodeScreen) }
                    .apply { targetState = !showResetCodeScreen },
                enter = slideInHorizontally(initialOffsetX = { -it }) + fadeIn(),
                exit = slideOutHorizontally(targetOffsetX = { -it }) + fadeOut()
            ) {
                RequestResetScreen(
                    viewModel = viewModel
                )
            }

            AnimatedVisibility(
                visibleState = transitionState,
                enter = slideInHorizontally(initialOffsetX = { it }) + fadeIn(),
                exit = slideOutHorizontally(targetOffsetX = { it }) + fadeOut()
            ) {
                ConfirmResetScreen(
                    viewModel = viewModel,
                    email = emailState
                )
            }

            // Loading animation
            if (requestState is PasswordResetViewModel.RequestResetState.Loading ||
                confirmState is PasswordResetViewModel.ConfirmResetState.Loading
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0x80FFFFFF))
                        .clickable(enabled = false) { /* Prevent clicks during loading */ },
                    contentAlignment = Alignment.Center
                ) {

                    val infiniteTransition = rememberInfiniteTransition(label = "loading")
                    val scale by infiniteTransition.animateFloat(
                        initialValue = 0.8f,
                        targetValue = 1.2f,
                        animationSpec = infiniteRepeatable(
                            animation = tween(800),
                            repeatMode = RepeatMode.Reverse
                        ),
                        label = "scale"
                    )

                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(60.dp)
                            .scale(scale),
                        color = primaryGreen,
                        trackColor = lightGreen.copy(alpha = 0.3f),
                        strokeWidth = 5.dp
                    )
                }
            }
        }
    }
}

@Composable
fun RequestResetScreen(
    viewModel: PasswordResetViewModel,
) {
    val requestState by viewModel.requestResetState.observeAsState()
    var email by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf<Int?>(null) }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Thẻ có bóng đổ và góc bo tròn
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = 6.dp,
                    shape = RoundedCornerShape(16.dp),
                    spotColor = primaryGreen.copy(alpha = 0.2f)
                )
                .clip(RoundedCornerShape(16.dp))
                .animateContentSize(),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Rounded.CheckCircle,
                    contentDescription = null,
                    tint = primaryGreen,
                    modifier = Modifier
                        .size(72.dp)
                        .padding(bottom = 16.dp)
                )

                Text(
                    text = stringResource(R.string.forgot_password_description),
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 10.dp)
                )

                val emailFocused = remember { mutableStateOf(false) }
                OutlinedTextField(
                    value = email,
                    onValueChange = {
                        email = it
                        emailError = if (!it.isValidEmail() && it.isNotEmpty()) {
                            R.string.enter_valid_email
                        } else {
                            null
                        }
                    },
                    label = {
                        Text(
                            stringResource(R.string.email),
                            color = if (emailFocused.value) primaryGreen else Color.Gray)
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Email,
                            contentDescription = null,
                            tint = if (emailFocused.value) primaryGreen else Color.Gray
                        )
                    },
                    isError = emailError != null,
                    supportingText = { emailError?.let { Text(stringResource(it)) } },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = primaryGreen,
                        focusedLabelColor = primaryGreen,
                        cursorColor = primaryGreen
                    ),
                    shape = RoundedCornerShape(15.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Animated button
                val interactionSource = remember { MutableInteractionSource() }
                val isPressed by interactionSource.collectIsPressedAsState()

                Button(
                    onClick = {
                        if (email.isValidEmail()) {
                            viewModel.requestPasswordReset(email)
                        } else {
                            emailError = R.string.enter_valid_email
                        }
                    },
                    enabled = email.isNotEmpty() && emailError == null &&
                            requestState !is PasswordResetViewModel.RequestResetState.Loading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .scale(if (isPressed) 0.98f else 1f),
                    shape = RoundedCornerShape(15.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = primaryGreen,
                        disabledContainerColor = Color.Gray.copy(alpha = 0.3f)
                    ),
                    interactionSource = interactionSource,
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 4.dp
                    )
                ) {
                    Text(
                        text = stringResource(R.string.send_reset_code),
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun ConfirmResetScreen(
    viewModel: PasswordResetViewModel,
    email: String
) {
    val confirmState by viewModel.confirmResetState.observeAsState()
    val requestState by viewModel.requestResetState.observeAsState()
    val resetCodeFromResponse by viewModel.resetCode.observeAsState() // Lấy resetCode từ ViewModel

    // Sử dụng resetCode từ ViewModel để tự động điền, nếu có
    var resetCode by remember { mutableStateOf(resetCodeFromResponse ?: "") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var resetCodeError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<Int?>(null) }
    var confirmPasswordError by remember { mutableStateOf<Int?>(null) }

    val context = LocalContext.current

    // Cập nhật resetCode khi resetCodeFromResponse thay đổi
    LaunchedEffect(resetCodeFromResponse) {
        resetCodeFromResponse?.let {
            resetCode = it
        }
    }

    LaunchedEffect(requestState) {
        when (requestState) {
            is PasswordResetViewModel.RequestResetState.Success -> {
                val successState = requestState as PasswordResetViewModel.RequestResetState.Success
                Toast.makeText(
                    context,
                    "Mã reset mới: ${successState.resetCode}\n${successState.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
            is PasswordResetViewModel.RequestResetState.Error -> {
                Toast.makeText(
                    context,
                    (requestState as PasswordResetViewModel.RequestResetState.Error).message,
                    Toast.LENGTH_LONG
                ).show()
            }
            else -> { /* Do nothing */ }
        }
    }


    // Báo độ mạnh của mật khẩu
    val passwordStrength = remember(newPassword) {
        when {
            newPassword.isEmpty() -> 0
            newPassword.length < 6 -> 1
            newPassword.length >= 8 &&
                    newPassword.any { it.isDigit() } &&
                    newPassword.any { it.isUpperCase() } -> 3
            else -> 2
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = 6.dp,
                    shape = RoundedCornerShape(16.dp),
                    spotColor = primaryGreen.copy(alpha = 0.2f)
                )
                .clip(RoundedCornerShape(16.dp)),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Image(
                    painter = painterResource(id = R.drawable.fake_avatar),
                    contentDescription = "",
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .padding(bottom = 16.dp)
                )

                Text(
                    text = stringResource(R.string.check_your_email),
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    color = primaryGreen,
                    modifier = Modifier.padding(bottom = 8.dp)
                )


                Row {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = null,
                        tint = primaryGreen,
                        modifier = Modifier.padding(end = 8.dp)
                    )

                    Text(
                        text = email,
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }

                val codeFocused = remember { mutableStateOf(false) }
                OutlinedTextField(
                    value = resetCode,
                    onValueChange = {
                        // Chỉ chấp nhận chữ số và giới hạn 6 ký tự
                        if (it.all { char -> char.isDigit() } && it.length <= 6) {
                            resetCode = it
                            resetCodeError = if (it.length != 6 && it.isNotEmpty()) {
                                "Mã phải có 6 chữ số"
                            } else {
                                null
                            }
                        }
                    },
                    label = {
                        Text(
                            stringResource(R.string.reset_code),
                            color = if (codeFocused.value) primaryGreen else Color.Gray)
                    },
                    placeholder = { Text(stringResource(R.string.reset_code_hint)) },
                    leadingIcon = {
                        Icon(
                            Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = primaryGreen
                        )
                    },
                    isError = resetCodeError != null,
                    supportingText = { resetCodeError?.let { Text(it) } },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = primaryGreen,
                        focusedLabelColor = primaryGreen,
                        cursorColor = primaryGreen
                    ),
                    shape = RoundedCornerShape(15.dp)
                )

                OutlinedTextField(
                    value = newPassword,
                    onValueChange = {
                        newPassword = it
                        passwordError = if (it.length < 6) {
                            R.string.password_too_short
                        } else {
                            null
                        }
                        confirmPasswordError = if (confirmPassword.isNotEmpty() && confirmPassword != it) {
                            R.string.passwords_do_not_match
                        } else {
                            null
                        }
                    },
                    label = {
                        Text(
                            stringResource(R.string.new_password),
                            color = if (codeFocused.value) primaryGreen else Color.Gray)
                    },
                    placeholder = { Text(stringResource(R.string.new_password_hint)) },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Lock,
                            contentDescription = null,
                            tint = primaryGreen
                        )
                    },
                    isError = passwordError != null,
                    supportingText = {
                        Column {
                            passwordError?.let { Text(stringResource(it)) }
                            if (newPassword.isNotEmpty()) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 4.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(4.dp)
                                            .clip(RoundedCornerShape(2.dp))
                                            .background(
                                                if (passwordStrength >= 1)
                                                    if (passwordStrength == 1) Color.Red else primaryGreen
                                                else Color.Gray.copy(alpha = 0.3f)
                                            )
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(4.dp)
                                            .clip(RoundedCornerShape(2.dp))
                                            .background(
                                                if (passwordStrength >= 2)
                                                    if (passwordStrength == 2) Color(0xFFFFA000) else primaryGreen
                                                else Color.Gray.copy(alpha = 0.3f)
                                            )
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(4.dp)
                                            .clip(RoundedCornerShape(2.dp))
                                            .background(
                                                if (passwordStrength >= 3) primaryGreen
                                                else Color.Gray.copy(alpha = 0.3f)
                                            )
                                    )
                                }
                                Text(
                                    text = when (passwordStrength) {
                                        1 -> "Mật khẩu yếu"
                                        2 -> "Mật khẩu trung bình"
                                        3 -> "Mật khẩu mạnh"
                                        else -> ""
                                    },
                                    color = when (passwordStrength) {
                                        1 -> Color.Red
                                        2 -> Color(0xFFFFA000)
                                        3 -> primaryGreen
                                        else -> Color.Transparent
                                    },
                                    style = MaterialTheme.typography.bodySmall,
                                    modifier = Modifier.padding(top = 4.dp)
                                )
                            }
                        }
                    },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = primaryGreen,
                        focusedLabelColor = primaryGreen,
                        cursorColor = primaryGreen
                    ),
                    shape = RoundedCornerShape(15.dp)
                )

                // Confirm password field with animation
                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = {
                        confirmPassword = it
                        confirmPasswordError = if (it != newPassword) {
                            R.string.passwords_do_not_match
                        } else {
                            null
                        }
                    },
                    label = {
                        Text(
                            stringResource(R.string.confirm_password),
                            color = if (codeFocused.value) primaryGreen else Color.Gray)
                    },
                    placeholder = { Text(stringResource(R.string.confirm_password_hint)) },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Lock,
                            contentDescription = null,
                            tint = primaryGreen
                        )
                    },
                    isError = confirmPasswordError != null,
                    supportingText = { confirmPasswordError?.let { Text(stringResource(it)) } },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = primaryGreen,
                        focusedLabelColor = primaryGreen,
                        cursorColor = primaryGreen
                    ),
                    shape = RoundedCornerShape(15.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                val interactionSource = remember { MutableInteractionSource() }
                val isPressed by interactionSource.collectIsPressedAsState()

                Button(
                    onClick = {
                        when {
                            resetCode.length != 6 -> resetCodeError = "Mã phải có 6 chữ số"
                            newPassword.length < 6 -> passwordError = R.string.password_too_short
                            newPassword != confirmPassword -> confirmPasswordError = R.string.passwords_do_not_match
                            else -> viewModel.confirmPasswordReset(email, resetCode, newPassword)
                        }
                    },
                    enabled = resetCode.isNotEmpty() && newPassword.isNotEmpty() && confirmPassword.isNotEmpty() &&
                            resetCodeError == null && passwordError == null && confirmPasswordError == null &&
                            confirmState !is PasswordResetViewModel.ConfirmResetState.Loading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                        .scale(if (isPressed) 0.98f else 1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = primaryGreen,
                        disabledContainerColor = Color.Gray.copy(alpha = 0.3f)
                    ),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 4.dp
                    ),
                    interactionSource = interactionSource
                ) {
                    Text(
                        text = stringResource(R.string.reset_password),
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                }

                TextButton(
                    onClick = { viewModel.requestPasswordReset(email) },
                    modifier = Modifier.padding(top = 8.dp),
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = primaryGreen
                    )
                ) {
                    Text(
                        text = stringResource(R.string.reset_code),
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

fun String.isValidEmail(): Boolean {
    return !isNullOrEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()
}