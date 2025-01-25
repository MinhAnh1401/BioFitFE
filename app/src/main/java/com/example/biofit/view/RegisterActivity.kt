package com.example.biofit.view

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.biofit.R
import com.example.biofit.ui.theme.BioFitTheme

class RegisterActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BioFitTheme {
                RegisterScreen()
            }
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        recreate()
    }
}

@Composable
fun RegisterScreen() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Box {
            SignInAndSignUpBackground()
            RegisterContent()
        }
    }
}

@Composable
fun RegisterContent() {
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
            RegisterForm(modifier = Modifier.padding(top = 16.dp))
        }
    }
    TermsAndPrivacy()
}

@Composable
fun RegisterForm(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var username by remember { mutableStateOf("") }
        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        var passwordVisible by remember { mutableStateOf(false) }
        var confirmPassword by remember { mutableStateOf("") }
        var confirmPasswordVisible by remember { mutableStateOf(false) }

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = {
                Text(
                    stringResource(R.string.full_name),
                    style = MaterialTheme.typography.bodySmall
                )
            },
            modifier = if (LocalConfiguration.current.screenWidthDp > 500) {
                Modifier.width((LocalConfiguration.current.screenWidthDp * 0.6f).dp)
            } else {
                Modifier.fillMaxWidth()
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            singleLine = true,
            shape = RoundedCornerShape(30.dp)
        )

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
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
            modifier = if (LocalConfiguration.current.screenWidthDp > 500) {
                Modifier.width((LocalConfiguration.current.screenWidthDp * 0.6f).dp)
            } else {
                Modifier.fillMaxWidth()
            }.padding(top = 8.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            singleLine = true,
            shape = RoundedCornerShape(30.dp)
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = {
                Text(
                    stringResource(R.string.password),
                    style = MaterialTheme.typography.bodySmall
                )
            },
            placeholder = {
                Text(
                    stringResource(R.string.min_8_chars_upper_lower_numbers),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.outline
                )
            },
            trailingIcon = {
                Checkbox(
                    checked = passwordVisible,
                    onCheckedChange = { passwordVisible = it },
                    modifier = Modifier.padding(end = 8.dp)
                )
            },
            modifier = if (LocalConfiguration.current.screenWidthDp > 500) {
                Modifier.width((LocalConfiguration.current.screenWidthDp * 0.6f).dp)
            } else {
                Modifier.fillMaxWidth()
            }.padding(top = 8.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = if (passwordVisible) VisualTransformation.None
            else PasswordVisualTransformation(),
            singleLine = true,
            shape = RoundedCornerShape(30.dp)
        )

        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = {
                Text(
                    stringResource(R.string.confirm_password),
                    style = MaterialTheme.typography.bodySmall
                )
            },
            placeholder = {
                Text(
                    stringResource(R.string.re_enter_password),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.outline
                )
            },
            trailingIcon = {
                Checkbox(
                    checked = confirmPasswordVisible,
                    onCheckedChange = { confirmPasswordVisible = it },
                    modifier = Modifier.padding(end = 8.dp)
                )
            },
            modifier = if (LocalConfiguration.current.screenWidthDp > 500) {
                Modifier.width((LocalConfiguration.current.screenWidthDp * 0.6f).dp)
            } else {
                Modifier.fillMaxWidth()
            }.padding(top = 8.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = if (confirmPasswordVisible) VisualTransformation.None
            else PasswordVisualTransformation(),
            singleLine = true,
            shape = RoundedCornerShape(30.dp)
        )

        Button(
            onClick = { /* TODO */ },
            modifier = Modifier.padding(vertical = 16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Text(
                text = stringResource(R.string.sign_up_uppercase),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }

        SocialLoginButtons()
    }
}

@Preview(
    device = "id:pixel_9_pro_xl",
    showSystemUi = true,
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
private fun RegisterScreenPreviewInLargePhone() {
    BioFitTheme {
        RegisterScreen()
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
private fun RegisterScreenDarkModePreviewInSmallPhone() {
    BioFitTheme {
        RegisterScreen()
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
private fun RegisterScreenPreviewInTablet() {
    BioFitTheme {
        RegisterScreen()
    }
}