package com.example.biofit.ui.activity

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.biofit.R
import com.example.biofit.data.dto.UserDTO
import com.example.biofit.navigation.getUserData
import com.example.biofit.ui.components.getStandardPadding
import com.example.biofit.ui.theme.BioFitTheme
import com.example.biofit.view_model.LoginViewModel
import com.example.biofit.view_model.UpdateUserViewModel

class InfoUserGenderActivity : ComponentActivity() {
    private var userData: UserDTO? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        userData = getUserData(this)
        enableEdgeToEdge()
        setContent {
            BioFitTheme {
                InfoUserGenderScreen(userData ?: UserDTO.default())
            }
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        recreate()
    }
}

@Composable
fun InfoUserGenderScreen(
    userData: UserDTO,
    updateViewModel: UpdateUserViewModel = viewModel(),
    loginViewModel: LoginViewModel = viewModel()
) {
    val context = LocalContext.current
    val activity = context as? Activity

    val screenWidth = LocalConfiguration.current.screenWidthDp
    val screenHeight = LocalConfiguration.current.screenHeightDp

    val standardPadding = getStandardPadding().first
    val modifier = getStandardPadding().second

    val userId = userData.userId
    var selectedGender by rememberSaveable { mutableStateOf<String?>(null) }
    val gender = when (selectedGender) {
        stringResource(R.string.male) -> 1
        stringResource(R.string.female) -> 0
        else -> null
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
    ) {
        Box {
            BackgroundInfoScreen()

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = WindowInsets.safeDrawing.asPaddingValues().calculateTopPadding(),
                        start = standardPadding,
                        end = standardPadding,
                    ),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                TopBarInfoScreen(
                    onBackClick = { activity?.finish() },
                    stepColors = listOf(
                        MaterialTheme.colorScheme.primary,
                        MaterialTheme.colorScheme.primary,
                        MaterialTheme.colorScheme.secondary,
                        MaterialTheme.colorScheme.secondary,
                        MaterialTheme.colorScheme.secondary
                    ),
                    screenWidth,
                    screenHeight,
                    standardPadding
                )
                InfoUserGenderContent(
                    selectedGender, // Truyền vào đây
                    onGenderSelected = { selectedGender = it },
                    standardPadding,
                    modifier
                )
            }

            NextButtonInfoScreen(
                onClick = {
                    selectedGender?.let { updateViewModel.gender.value = gender }

                    updateViewModel.updateUser(context, userId, loginViewModel) {
                        val intent = Intent(context, InfoUserBirthdayActivity::class.java)
                        context.startActivity(intent)
                    }
                },
                standardPadding
            )
        }
    }
}

@Composable
fun InfoUserGenderContent(
    selectedGender: String?,
    onGenderSelected: (String) -> Unit,
    standardPadding: Dp,
    modifier: Modifier
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        item {
            Text(
                text = stringResource(R.string.what_is_your_gender),
                modifier = Modifier.padding(top = standardPadding * 3),
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.displaySmall
            )
        }

        item {
            Text(
                text = stringResource(R.string.description_gender),
                modifier = Modifier.padding(bottom = standardPadding),
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodySmall
            )
        }

        item {
            GenderOption(
                options = listOf(
                    stringResource(R.string.male),
                    stringResource(R.string.female)
                ),
                selectedOption = selectedGender,
                onOptionSelected = onGenderSelected,
                modifier
            )
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
fun GenderOption(
    options: List<String>,
    selectedOption: String?,
    onOptionSelected: (String) -> Unit,
    modifier: Modifier
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        content = {
            options.forEach { option ->
                ElevatedButton(
                    onClick = { onOptionSelected(option) },
                    modifier = modifier,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectedOption == option) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.surfaceVariant
                        },
                        contentColor = if (selectedOption == option) {
                            MaterialTheme.colorScheme.onPrimary
                        } else {
                            MaterialTheme.colorScheme.onSurface
                        }
                    ),
                    content = {
                        Text(
                            text = option,
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                )
            }
        }
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
private fun InfoUserGenderPortraitScreenDarkModePreviewInSmallPhone() {
    BioFitTheme {
        InfoUserGenderScreen(UserDTO.default())
    }
}

@Preview(
    device = "id:pixel_9_pro_xl",
    showSystemUi = true,
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
private fun InfoUserGenderPortraitScreenPreviewInLargePhone() {
    BioFitTheme {
        InfoUserGenderScreen(UserDTO.default())
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
private fun InfoUserGenderPortraitScreenPreviewInTablet() {
    BioFitTheme {
        InfoUserGenderScreen(UserDTO.default())
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
private fun InfoUserGenderLandscapeScreenDarkModePreviewInSmallPhone() {
    BioFitTheme {
        InfoUserGenderScreen(UserDTO.default())
    }
}

@Preview(
    device = "spec:parent=pixel_9_pro_xl,orientation=landscape",
    showSystemUi = true,
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
private fun InfoUserGenderLandscapeScreenPreviewInLargePhone() {
    BioFitTheme {
        InfoUserGenderScreen(UserDTO.default())
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
private fun InfoUserGenderLandscapeScreenPreviewInTablet() {
    BioFitTheme {
        InfoUserGenderScreen(UserDTO.default())
    }
}