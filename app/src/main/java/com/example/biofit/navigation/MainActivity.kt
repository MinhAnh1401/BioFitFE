package com.example.biofit.navigation

import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.biofit.R
import com.example.biofit.data.model.UserDTO
import com.example.biofit.ui.components.ActionPopup
import com.example.biofit.ui.components.BottomBar
import com.example.biofit.ui.components.getStandardPadding
import com.example.biofit.ui.screen.HomeScreen
import com.example.biofit.ui.screen.KnowledgeScreen
import com.example.biofit.ui.screen.PlanningScreen
import com.example.biofit.ui.screen.ProfileScreen
import com.example.biofit.ui.theme.BioFitTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val userDTO: UserDTO? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("USER_DATA", UserDTO::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra("USER_DATA")
        }
        setContent {
            BioFitTheme {
                userDTO?.let {
                    MainScreen(userDTO = userDTO)
                }
            }
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        recreate()
    }
}

@Composable
fun MainScreen(userDTO: UserDTO) {
    val standardPadding = getStandardPadding().first

    val navController = rememberNavController()
    var showPopup by remember { mutableStateOf(false) }

    val home = stringResource(R.string.home)
    val plan = stringResource(R.string.plan)
    val knowledge = stringResource(R.string.knowledge)
    val profile = stringResource(R.string.profile)

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column {
            Row(
                modifier = Modifier.weight(1f)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                    /*.padding(
                        top = WindowInsets.safeDrawing.asPaddingValues().calculateTopPadding(),
                        start = standardPadding,
                        end = standardPadding,
                    )*/,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    MainContent(
                        userDTO = userDTO,
                        navController = navController
                    )
                }
            }

            Row {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.Bottom
                ) {
                    AnimatedVisibility(
                        visible = showPopup,
                        modifier = Modifier.background(MaterialTheme.colorScheme.primary),
                        enter = slideInVertically { it } + fadeIn() + expandVertically(),
                        exit = slideOutVertically { it } + fadeOut() + shrinkVertically()
                    ) {
                        ActionPopup(
                            userDTO = userDTO,
                            onDismissPopup = { showPopup = false },
                            standardPadding = standardPadding
                        )
                    }

                    BottomBar(
                        onItemSelected = { label ->
                            when (label) {
                                home -> navController.navigate("home")
                                plan -> navController.navigate("plan")
                                "Add" -> showPopup = !showPopup
                                knowledge -> navController.navigate("knowledge")
                                profile -> navController.navigate("profile")
                            }
                        },
                        showPopup = showPopup,
                        onDismissPopup = { showPopup = false },
                        standardPadding = standardPadding
                    )
                }
            }
        }
    }
}

@Composable
fun MainContent(
    userDTO: UserDTO,
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") { HomeScreen(userDTO) }
        composable("plan") { PlanningScreen() }
        composable("knowledge") { KnowledgeScreen() }
        composable("profile") { ProfileScreen() }
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
private fun MainScreenDarkModePreviewInSmallPhone() {
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
    BioFitTheme {
        MainScreen(userDTO = userDTO)
    }
}

@Preview(
    device = "id:pixel_9_pro_xl",
    showSystemUi = true,
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
private fun MainScreenPreviewInLargePhone() {
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
    BioFitTheme {
        MainScreen(userDTO = userDTO)
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
private fun MainScreenPreviewInTablet() {
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
    BioFitTheme {
        MainScreen(userDTO = userDTO)
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
private fun MainLandscapeScreenDarkModePreviewInSmallPhone() {
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
    BioFitTheme {
        MainScreen(userDTO = userDTO)
    }
}

@Preview(
    device = "spec:parent=pixel_9_pro_xl,orientation=landscape",
    showSystemUi = true,
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
private fun MainLandscapeScreenPreviewInLargePhone() {
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
    BioFitTheme {
        MainScreen(userDTO = userDTO)
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
private fun MainLandscapeScreenPreviewInTablet() {
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
    BioFitTheme {
        MainScreen(userDTO = userDTO)
    }
}