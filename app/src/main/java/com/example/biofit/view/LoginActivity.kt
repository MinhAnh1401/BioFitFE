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
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Box {
            SignInAndSignUpBackground()
            LoginContent()
        }
    }
}

@Composable
fun SignInAndSignUpBackground() {
    Box(modifier = Modifier.fillMaxSize()) {
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
fun LoginContent() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .padding(WindowInsets.ime.asPaddingValues()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        item {
            Logo()
            LoginForm(modifier = Modifier.padding(top = 16.dp))
        }
    }
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
fun LoginForm(modifier: Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        var passwordVisible by remember { mutableStateOf(false) }

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            modifier = if (LocalConfiguration.current.screenWidthDp > 500) {
                Modifier.width((LocalConfiguration.current.screenWidthDp * 0.6f).dp)
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
            modifier = if (LocalConfiguration.current.screenWidthDp > 500) {
                Modifier.width((LocalConfiguration.current.screenWidthDp * 0.6f).dp)
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
            horizontalArrangement = if (LocalConfiguration.current.screenWidthDp > 500)
                Arrangement.Center else Arrangement.Start
        ) {
            TextButton(
                onClick = { /* TODO */ },
                content = {
                    Text(
                        text = stringResource(R.string.forgot_password),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            )
        }

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
        SocialLoginButtons()
    }
}

@Composable
fun SignUpPrompt() {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = stringResource(R.string.don_t_have_an_account),
            style = MaterialTheme.typography.bodySmall
        )
        TextButton(
            onClick = { /* TODO */ },
            content = {
                Text(
                    text = stringResource(R.string.create_account),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        )
    }
}

@Composable
fun SocialLoginButtons() {
    // nút đăng nhập bằng Google
    Button(
        onClick = { /* TODO */ },
        modifier = if (LocalConfiguration.current.screenWidthDp > 500) {
            Modifier.width((LocalConfiguration.current.screenWidthDp * 0.6f).dp)
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

    // nút đăng nhập bằng Facebook
    Button(
        onClick = { /* TODO */ },
        modifier = if (LocalConfiguration.current.screenWidthDp > 500) {
            Modifier
                .width((LocalConfiguration.current.screenWidthDp * 0.6f).dp)
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
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            painter = painterResource(id = iconId),
            contentDescription = null,
            tint = Color.Unspecified
        )
        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun TermsAndPrivacy() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Bottom
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            TextButton(
                onClick = { /* TODO */ },
                content = {
                    Text(
                        text = stringResource(R.string.term_of_use),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            )

            Text(
                text = stringResource(R.string.and),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.background
            )

            TextButton(
                onClick = { /* TODO */ },
                content = {
                    Text(
                        text = stringResource(R.string.privacy_policy),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            )
        }
    }
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