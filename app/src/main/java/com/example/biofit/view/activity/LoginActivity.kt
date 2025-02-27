package com.example.biofit.view.activity

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.biofit.R
import com.example.biofit.controller.LoginController
import com.example.biofit.view.ui_theme.BioFitTheme

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BioFitTheme {
                LoginScreen()
            }
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        recreate()
    }
}

@Composable
fun LoginScreen() {
    val screenWidth = LocalConfiguration.current.screenWidthDp // lấy chiều rộng màn hình thiết bị
    val screenHeight = LocalConfiguration.current.screenHeightDp // lấy chiều cao màn hình thiết bị
    val standardPadding = getStandardPadding().first
    val modifier = getStandardPadding().second

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
    ) {
        Box(
            content = {
                SignInAndSignUpBackground()
                LoginContent(
                    screenWidth = screenWidth,
                    screenHeight = screenHeight,
                    standardPadding = standardPadding,
                    modifier = modifier
                )
            }
        )
    }
}

@Composable
fun SignInAndSignUpBackground() {
    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        Image(
            painter = painterResource(id = R.drawable.bg_sign_in_up_top_screen),
            contentDescription = "Login Screen Background Top",
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter),
            contentScale = ContentScale.FillBounds
        )

        Image(
            painter = painterResource(id = R.drawable.bg_sign_in_up_bot_screen),
            contentDescription = "Login Screen Background Bottom",
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            contentScale = ContentScale.FillBounds
        )
    }
}

@Composable
fun LoginContent(
    screenWidth: Int,
    screenHeight: Int,
    standardPadding: Dp,
    modifier: Modifier
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                if (screenWidth > screenHeight) {
                    PaddingValues(
                        top = standardPadding,
                        start = standardPadding,
                        end = standardPadding,
                    )
                } else {
                    PaddingValues(
                        top = WindowInsets.safeDrawing.asPaddingValues().calculateTopPadding(),
                        start = standardPadding,
                        end = standardPadding,
                    )
                }
            ), // tuỳ chỉnh kích thước và vị trí của LazyColumn
        // state = , // tuỳ chỉnh trạng thái của LazyColumn
        // contentPadding = , // tuỳ chỉnh padding bên trong LazyColumn
        // reverseLayout = , // tuỳ chỉnh layout ngược lại
        verticalArrangement = Arrangement.Center, // sắp xếp các phần tử theo chiều dọc
        horizontalAlignment = Alignment.CenterHorizontally, // sắp xếp các phần tử theo chiều ngang
        // flingBehavior = , // tuỳ chỉnh hành vi cuộn
        // userScrollEnabled = , // tuỳ chỉnh xem có cho phép cuộn hay không
    ) {
        item {
            AppLogo(standardPadding)
            LoginForm(
                modifier = Modifier.padding(top = standardPadding),
                screenWidth = screenWidth,
                screenHeight = screenHeight,
                standardPadding = standardPadding,
                modifier2 = modifier
            )
            TermsAndPrivacy(standardPadding)
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
fun AppLogo(standardPadding: Dp) {
    Image(
        painter = painterResource(id = R.drawable.logo),
        contentDescription = "Logo",
        modifier = Modifier
            .fillMaxWidth()
            .size(standardPadding * 10f)
    )
}

@Composable
fun LoginForm(
    modifier: Modifier,
    screenWidth: Int,
    screenHeight: Int,
    standardPadding: Dp,
    modifier2: Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        val context = LocalContext.current
        val activity = context as? Activity

        var email by rememberSaveable { mutableStateOf("") }
        var password by rememberSaveable { mutableStateOf("") }
        var passwordVisible by rememberSaveable { mutableStateOf(false) }

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            modifier = modifier2.padding(top = standardPadding),
            textStyle = MaterialTheme.typography.bodySmall,
            label = {
                Text(
                    stringResource(R.string.email),
                    style = MaterialTheme.typography.bodySmall
                )
            },
            placeholder = {
                Text(
                    stringResource(R.string.biofit_example_com),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.outline
                )
            },
            isError = false,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            keyboardActions = KeyboardActions(
                onDone = { /*TODO*/ },
                onGo = { /*TODO*/ },
                onNext = { /*TODO*/ },
                onPrevious = { /*TODO*/ },
                onSearch = { /*TODO*/ },
                onSend = { /*TODO*/ }
            ),
            singleLine = true,
            maxLines = 1,
            shape = MaterialTheme.shapes.extraLarge,
            colors = OutlinedTextFieldDefaults.colors(
                focusedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer,
                focusedBorderColor = MaterialTheme.colorScheme.onPrimaryContainer
            )
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            modifier = modifier2.padding(top = standardPadding),
            textStyle = MaterialTheme.typography.bodySmall,
            label = {
                Text(
                    stringResource(R.string.password),
                    style = MaterialTheme.typography.bodySmall
                )
            },
            trailingIcon = {
                Checkbox(
                    checked = passwordVisible,
                    onCheckedChange = { passwordVisible = it },
                    modifier = Modifier.padding(end = standardPadding / 2)
                )
            },
            supportingText = {
                Text(
                    stringResource(R.string.min_8_chars_upper_lower_numbers),
                    style = MaterialTheme.typography.bodySmall
                )
            },
            isError = false,
            visualTransformation = if (passwordVisible) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            keyboardActions = KeyboardActions(
                onDone = { /*TODO*/ },
                onGo = { /*TODO*/ },
                onNext = { /*TODO*/ },
                onPrevious = { /*TODO*/ },
                onSearch = { /*TODO*/ },
                onSend = { /*TODO*/ }
            ),
            singleLine = true,
            maxLines = 1,
            shape = MaterialTheme.shapes.extraLarge,
            colors = OutlinedTextFieldDefaults.colors(
                focusedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer,
                focusedBorderColor = MaterialTheme.colorScheme.onPrimaryContainer
            )
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = if (screenWidth > screenHeight) {
                Arrangement.Center
            } else {
                Arrangement.Start
            },
        ) {
            TextButton(
                onClick = { /* TODO */ },
            ) {
                Text(
                    text = stringResource(R.string.forgot_password),
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
        Button(
            onClick = {
                val loginController = LoginController(context)
                val user = loginController.loginUser(email, password)

                if (user != null) {
                    // Đăng nhập thành công
                    activity?.let {
                        val intent = Intent(it, MainActivity::class.java)
                        it.startActivity(intent)
                        it.finish()
                    }
                }
            },
            modifier = Modifier.padding(vertical = standardPadding),
            shape = MaterialTheme.shapes.extraLarge,
        ) {
            Text(
                text = stringResource(R.string.sign_in_uppercase),
                style = MaterialTheme.typography.labelLarge
            )
        }

        SignUpPrompt()
        SocialLoginButtons(
            standardPadding,
            modifier2
        )
    }
}

@Composable
fun SignUpPrompt() {
    val context = LocalContext.current
    val activity = context as? Activity

    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(R.string.don_t_have_an_account),
            style = MaterialTheme.typography.bodySmall
        )
        TextButton(
            onClick = {
                activity?.let {
                    val intent = Intent(it, RegisterActivity::class.java)
                    it.startActivity(intent)
                    it.finish()
                }
            },
        ) {
            Text(
                text = stringResource(R.string.create_account),
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
fun SocialLoginButtons(
    standardPadding: Dp,
    modifier: Modifier
) {
    // Sign in with Google button
    Button(
        onClick = { /* TODO */ },
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f)
        ),
    ) {
        SocialLoginContent(
            iconId = R.drawable.ic_google,
            text = stringResource(R.string.sign_in_with_google)
        )
    }

    // Sign in with Facebook button
    Button(
        onClick = { /* TODO */ },
        modifier = modifier.padding(top = standardPadding),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.8f)
        ),
    ) {
        SocialLoginContent(
            iconId = R.drawable.ic_facebook,
            text = stringResource(R.string.sign_in_with_facebook)
        )
    }
}

@Composable
fun SocialLoginContent(
    iconId: Int,
    text: String
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(id = iconId), // tuỳ chỉnh nguồn ảnh
            contentDescription = null, // tuỳ chỉnh mô tả nội dung của ảnh
            // modifier = , // tuỳ chỉnh kích thước và vị trí của Icon
            tint = Color.Unspecified // tuỳ chỉnh màu sắc của Icon
        )
        Text(
            text = text,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.labelMedium
        )
    }
}

@Composable
fun TermsAndPrivacy(standardPadding: Dp) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Bottom,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = standardPadding),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            TextButton(
                onClick = { /* TODO */ },
            ) {
                Text(
                    text = stringResource(R.string.term_of_use),
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Text(
                text = stringResource(R.string.and),
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.bodySmall
            )

            TextButton(
                onClick = { /* TODO */ },
            ) {
                Text(
                    text = stringResource(R.string.privacy_policy),
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.bodySmall
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
private fun LoginPortraitScreenDarkModePreviewInSmallPhone() {
    BioFitTheme {
        LoginScreen()
    }
}

@Preview(
    device = "id:pixel_9_pro_xl",
    showSystemUi = true,
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
private fun LoginPortraitScreenPreviewInLargePhone() {
    BioFitTheme {
        LoginScreen()
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
private fun LoginPortraitScreenPreviewInTablet() {
    BioFitTheme {
        LoginScreen()
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
private fun LoginLandscapeScreenDarkModePreviewInSmallPhone() {
    BioFitTheme {
        LoginScreen()
    }
}

@Preview(
    device = "spec:parent=pixel_9_pro_xl,orientation=landscape",
    showSystemUi = true,
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
private fun LoginLandscapeScreenPreviewInLargePhone() {
    BioFitTheme {
        LoginScreen()
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
private fun LoginLandscapeScreenPreviewInTablet() {
    BioFitTheme {
        LoginScreen()
    }
}