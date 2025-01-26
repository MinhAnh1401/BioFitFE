package com.example.biofit.view

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.biofit.R
import com.example.biofit.ui.theme.BioFitTheme

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

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
        content = {
            Box(
                content = {
                    SignInAndSignUpBackground()
                    LoginContent(screenWidth)
                }
            )
        }
    )
}

@Composable
fun SignInAndSignUpBackground() {
    Box(
        modifier = Modifier.fillMaxSize(),
        content = {
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
    )
}

@Composable
fun LoginContent(screenWidth: Int) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .padding(WindowInsets.ime.asPaddingValues()), // tuỳ chỉnh kích thước và vị trí của LazyColumn
        // state = , // tuỳ chỉnh trạng thái của LazyColumn
        // contentPadding = , // tuỳ chỉnh padding bên trong LazyColumn
        // reverseLayout = , // tuỳ chỉnh layout ngược lại
        verticalArrangement = Arrangement.Center, // sắp xếp các phần tử theo chiều dọc
        horizontalAlignment = Alignment.CenterHorizontally, // sắp xếp các phần tử theo chiều ngang
        // flingBehavior = , // tuỳ chỉnh hành vi cuộn
        // userScrollEnabled = , // tuỳ chỉnh xem có cho phép cuộn hay không
        content = {
            item {
                Logo()
                LoginForm(
                    modifier = Modifier.padding(top = 16.dp),
                    screenWidth = screenWidth
                )
            }
        }
    )
    TermsAndPrivacy()
}

@Composable
fun Logo() {
    Image(
        painter = painterResource(id = R.drawable.logo),
        contentDescription = "Logo",
        modifier = Modifier
            .fillMaxWidth()
            .size(LocalConfiguration.current.screenHeightDp.dp * 0.15f)
    )
}

@Composable
fun LoginForm(modifier: Modifier, screenWidth: Int) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        content = {
            var email by remember { mutableStateOf("") }
            var password by remember { mutableStateOf("") }
            var passwordVisible by remember { mutableStateOf(false) }

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                modifier = if (screenWidth > 600) {
                    Modifier.width(screenWidth.dp * 0.6f)
                } else {
                    Modifier.fillMaxWidth()
                },
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
                shape = MaterialTheme.shapes.large
            )

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                modifier = if (screenWidth > 600) {
                    Modifier.width(screenWidth.dp * 0.6f)
                } else {
                    Modifier.fillMaxWidth()
                }.padding(top = 8.dp),
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
                        modifier = Modifier.padding(end = 8.dp)
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
                shape = MaterialTheme.shapes.large
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = if (screenWidth > 600) {
                    Arrangement.Center
                } else {
                    Arrangement.Start
                },
                content = {
                    TextButton(
                        onClick = { /* TODO */ },
                        content = {
                            Text(
                                text = stringResource(R.string.forgot_password),
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    )
                }
            )

            Button(
                onClick = { /* TODO */ },
                modifier = Modifier.padding(vertical = 16.dp),
                shape = MaterialTheme.shapes.large,
                content = {
                    Text(
                        text = stringResource(R.string.sign_in_uppercase),
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            )

            SignUpPrompt()
            SocialLoginButtons(screenWidth)
        }
    )
}

@Composable
fun SignUpPrompt() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        content = {
            Text(
                text = stringResource(R.string.don_t_have_an_account),
                style = MaterialTheme.typography.bodySmall
            )
            TextButton(
                onClick = { /* TODO */ },
                content = {
                    Text(
                        text = stringResource(R.string.create_account),
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            )
        }
    )
}

@Composable
fun SocialLoginButtons(screenWidth: Int) {
    // Sign in with Google button
    Button(
        onClick = { /* TODO */ },
        modifier = if (screenWidth > 500) {
            Modifier.width(screenWidth.dp * 0.6f)
        } else {
            Modifier.fillMaxWidth()
        },
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        content = {
            SocialLoginContent(
                iconId = R.drawable.ic_google,
                text = stringResource(R.string.sign_in_with_google)
            )
        }
    )

    // Sign in with Facebook button
    Button(
        onClick = { /* TODO */ },
        modifier = if (screenWidth > 500) {
            Modifier
                .width(screenWidth.dp * 0.6f)
                .padding(top = 8.dp)
        } else {
            Modifier.fillMaxWidth()
        },
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        content = {
            SocialLoginContent(
                iconId = R.drawable.ic_facebook,
                text = stringResource(R.string.sign_in_with_facebook)
            )
        }
    )
}

@Composable
fun SocialLoginContent(iconId: Int, text: String) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        content = {
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
    )
}

@Composable
fun TermsAndPrivacy() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Bottom,
        content = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                content = {
                    TextButton(
                        onClick = { /* TODO */ },
                        content = {
                            Text(
                                text = stringResource(R.string.term_of_use),
                                color = MaterialTheme.colorScheme.onBackground,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    )

                    Text(
                        text = stringResource(R.string.and),
                        color = MaterialTheme.colorScheme.background,
                        style = MaterialTheme.typography.bodySmall
                    )

                    TextButton(
                        onClick = { /* TODO */ },
                        content = {
                            Text(
                                text = stringResource(R.string.privacy_policy),
                                color = MaterialTheme.colorScheme.onBackground,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    )
                }
            )
        }
    )
}

@Preview(
    device = "id:pixel_9_pro_xl",
    showSystemUi = true,
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
private fun LoginScreenPreviewInLargePhone() {
    BioFitTheme {
        LoginScreen()
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
private fun LoginScreenDarkModePreviewInSmallPhone() {
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
private fun LoginScreenPreviewInTablet() {
    BioFitTheme {
        LoginScreen()
    }
}