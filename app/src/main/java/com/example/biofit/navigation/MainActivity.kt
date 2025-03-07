package com.example.biofit.navigation

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.biofit.R
import com.example.biofit.data.model.dto.UserDTO
import com.example.biofit.data.utils.UserSharedPrefsHelper
import com.example.biofit.ui.components.ActionPopup
import com.example.biofit.ui.components.BottomBar
import com.example.biofit.ui.components.getStandardPadding
import com.example.biofit.ui.screen.HomeScreen
import com.example.biofit.ui.screen.KnowledgeScreen
import com.example.biofit.ui.screen.PlanningScreen
import com.example.biofit.ui.screen.ProfileScreen
import com.example.biofit.ui.theme.BioFitTheme

class MainActivity : ComponentActivity() {
    private var userData: UserDTO? = null

    private val prefsListener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
        if (key == "USER_DATA") {
            userData = UserSharedPrefsHelper.getUserData(this)
            setContent {
                BioFitTheme {
                    MainScreen(userData ?: UserDTO.default())
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        sharedPreferences.registerOnSharedPreferenceChangeListener(prefsListener)

        userData = UserSharedPrefsHelper.getUserData(this)
        setContent {
            BioFitTheme {
                MainScreen(userData ?: UserDTO.default())
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        val sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(prefsListener)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        recreate()
    }
}
/*

fun getUserData(context: Context): UserDTO? {
    val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
    val gson = Gson()
    val json = sharedPreferences.getString("USER_DATA", null)

    return gson.fromJson(json, UserDTO::class.java)
}
*/

@Composable
fun MainScreen(userData: UserDTO) {
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
                        userData,
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
                        modifier = Modifier
                            .shadow(
                                elevation = 10.dp,
                                ambientColor = MaterialTheme.colorScheme.onBackground,
                                spotColor = MaterialTheme.colorScheme.onBackground
                            )
                            .background(MaterialTheme.colorScheme.primary),
                        enter = slideInVertically { it } + fadeIn() + expandVertically(),
                        exit = slideOutVertically { it } + fadeOut() + shrinkVertically()
                    ) {
                        ActionPopup(
                            userData = userData,
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
    userData: UserDTO,
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") { HomeScreen(userData) }
        composable("plan") { PlanningScreen() }
        composable("knowledge") { KnowledgeScreen() }
        composable("profile") { ProfileScreen(userData) }
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
    BioFitTheme {
        MainScreen(UserDTO.default())
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
    BioFitTheme {
        MainScreen(UserDTO.default())
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
    BioFitTheme {
        MainScreen(UserDTO.default())
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
    BioFitTheme {
        MainScreen(UserDTO.default())
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
    BioFitTheme {
        MainScreen(UserDTO.default())
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
    BioFitTheme {
        MainScreen(UserDTO.default())
    }
}