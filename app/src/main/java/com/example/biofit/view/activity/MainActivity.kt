package com.example.biofit.view.activity

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.biofit.R
import com.example.biofit.view.fragment.HomeScreen
import com.example.biofit.view.fragment.KnowledgeScreen
import com.example.biofit.view.fragment.PlanningScreen
import com.example.biofit.view.fragment.ProfileScreen
import com.example.biofit.view.ui_theme.BioFitTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BioFitTheme {
                MainScreen()
            }
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        recreate()
    }
}

@Composable
fun MainScreen() {
    val standardPadding = getStandardPadding().first
    val navController = rememberNavController()
    var showPopup by remember { mutableStateOf(false) }
    if (showPopup) {
        AddPopup(
            onDismissPopup = { showPopup = false }, // Đóng popup khi nhấn ra ngoài hoặc nhấn nút đóng
            standardPadding = standardPadding
        )
    }

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
                        .padding(
                            top = WindowInsets.safeDrawing.asPaddingValues().calculateTopPadding(),
                            start = standardPadding,
                            end = standardPadding,
                        ),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    MainContent(
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
                            .background(
                                color = MaterialTheme.colorScheme.primaryContainer,
                                shape = MaterialTheme.shapes.large.copy(
                                    bottomStart = CornerSize(0.dp),
                                    bottomEnd = CornerSize(0.dp)
                                )
                            ),
                        enter = slideInVertically { it } + fadeIn() + expandVertically(),
                        exit = slideOutVertically { it } + fadeOut() + shrinkVertically()
                    ) {
                        AddPopup(
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
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") { HomeScreen() }
        composable("plan") { PlanningScreen() }
        composable("knowledge") { KnowledgeScreen() }
        composable("profile") { ProfileScreen() }
    }
}

@Composable
fun AddPopup(
    onDismissPopup: () -> Unit,
    standardPadding: Dp
) {
    val context = LocalContext.current
    val activity = context as? Activity

    val itemPopupList = listOf(
        Pair(R.drawable.ic_exercise_2, R.string.exercise),
        Pair(R.drawable.ic_weight, R.string.weight),
        Pair(R.drawable.ic_drink_water, R.string.drinking_water)
    )
    val sessionPopupList = listOf(
        Pair(R.drawable.ic_morning_2, R.string.morning),
        Pair(R.drawable.ic_afternoon_2, R.string.afternoon),
        Pair(R.drawable.ic_evening_2, R.string.evening),
        Pair(R.drawable.ic_snack_2, R.string.snack)
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(intrinsicSize = IntrinsicSize.Min)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(standardPadding),
            verticalArrangement = Arrangement.spacedBy(standardPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(standardPadding),
                verticalAlignment = Alignment.CenterVertically
            ) {
                itemPopupList.forEach { (icon, title) ->
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(standardPadding),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Card(
                            onClick = {
                                when (title) {
                                    R.string.exercise ->
                                        activity?.let {
                                            val intent = Intent(it, ExerciseActivity::class.java)
                                            it.startActivity(intent)
                                        }

                                    R.string.weight ->
                                        activity?.let {
                                            val intent = Intent(it, UpdateWeightActivity::class.java)
                                            it.startActivity(intent)
                                        }

                                    R.string.drinking_water ->
                                        activity?.let {
                                            val intent = Intent(it, ExerciseActivity::class.java)
                                            it.startActivity(intent)
                                        }
                                }

                                onDismissPopup()
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = MaterialTheme.shapes.large,
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.outline.copy(
                                    alpha = 0.5f
                                )
                            )
                        ) {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    painter = painterResource(icon),
                                    contentDescription = stringResource(R.string.activity),
                                    modifier = Modifier
                                        .padding(standardPadding)
                                        .size(standardPadding * 2),
                                    tint = MaterialTheme.colorScheme.background
                                )
                            }
                        }

                        Text(
                            text = stringResource(title),
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(standardPadding),
                verticalAlignment = Alignment.CenterVertically
            ) {
                sessionPopupList.forEach { (icon, title) ->
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(standardPadding),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Card(
                            onClick = {
                                when (title) {
                                    R.string.morning ->
                                        activity?.let {
                                            val intent = Intent(it, AddActivity::class.java)
                                            intent.putExtra("SESSION_TITLE", R.string.morning)
                                            it.startActivity(intent)
                                        }

                                    R.string.afternoon ->
                                        activity?.let {
                                            val intent = Intent(it, AddActivity::class.java)
                                            intent.putExtra("SESSION_TITLE", R.string.afternoon)
                                            it.startActivity(intent)
                                        }

                                    R.string.evening ->
                                        activity?.let {
                                            val intent = Intent(it, AddActivity::class.java)
                                            intent.putExtra("SESSION_TITLE", R.string.evening)
                                            it.startActivity(intent)
                                        }

                                    R.string.snack ->
                                        activity?.let {
                                            val intent = Intent(it, AddActivity::class.java)
                                            intent.putExtra("SESSION_TITLE", R.string.snack)
                                            it.startActivity(intent)
                                        }
                                }

                                onDismissPopup()
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = MaterialTheme.shapes.large,
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.outline.copy(
                                    alpha = 0.5f
                                )
                            )
                        ) {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    painter = painterResource(icon),
                                    contentDescription = stringResource(R.string.activity),
                                    modifier = Modifier
                                        .padding(standardPadding)
                                        .size(standardPadding * 2),
                                    tint = MaterialTheme.colorScheme.background
                                )
                            }
                        }

                        Text(
                            text = stringResource(title),
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun BottomBar(
    onItemSelected: (String) -> Unit,
    showPopup: Boolean,
    onDismissPopup: () -> Unit,
    standardPadding: Dp
) {
    val items = listOf(
        stringResource(R.string.home),
        stringResource(R.string.plan),
        "Add",
        stringResource(R.string.knowledge),
        stringResource(R.string.profile)
    )
    val selectedItem = remember { mutableStateOf(items.first()) }

    val icons = listOf(
        R.drawable.ic_home,
        R.drawable.ic_plan,
        R.drawable.ic_add,
        R.drawable.ic_knowledge,
        R.drawable.ic_person
    )

    val rotationAngle by animateFloatAsState(
        targetValue = if (showPopup) 45f else 0f,
        animationSpec = tween(durationMillis = 300)
    )

    BottomAppBar(
        containerColor = MaterialTheme.colorScheme.primaryContainer
    ) {
        items.forEachIndexed { index, label ->
            Spacer(modifier = Modifier.weight(1f))
            IconButton(
                onClick = {
                    if (label != "Add") {
                        onDismissPopup()
                        selectedItem.value = label
                    }
                    onItemSelected(label)
                },
                modifier = Modifier.size(standardPadding * 3.5f)
            ) {
                Icon(
                    painter = painterResource(id = icons[index]),
                    contentDescription = label,
                    modifier = Modifier
                        .size(if (index != 2) standardPadding * 2f else standardPadding * 3.5f)
                        .graphicsLayer {
                            rotationZ = if (label == "Add") rotationAngle else 0f
                        },
                    tint = if (selectedItem.value == label && label != "Add") {
                        MaterialTheme.colorScheme.onSecondaryContainer
                    } else {
                        MaterialTheme.colorScheme.primary
                    }
                )
            }
            Spacer(modifier = Modifier.weight(1f))
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
private fun MainScreenDarkModePreviewInSmallPhone() {
    BioFitTheme {
        MainScreen()
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
        MainScreen()
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
        MainScreen()
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
        MainScreen()
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
        MainScreen()
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
        MainScreen()
    }
}