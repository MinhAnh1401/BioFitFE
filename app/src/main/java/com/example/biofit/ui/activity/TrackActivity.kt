package com.example.biofit.ui.activity

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import com.example.biofit.R
import com.example.biofit.data.model.UserDTO
import com.example.biofit.navigation.MainActivity
import com.example.biofit.ui.components.FoodItem
import com.example.biofit.ui.components.TopBar
import com.example.biofit.ui.components.getStandardPadding
import com.example.biofit.ui.theme.BioFitTheme
import com.patrykandpatrick.vico.core.extension.sumOf
import java.math.RoundingMode

class TrackActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val initialSelectedOption = intent.getIntExtra("SESSION_TITLE", R.string.morning)
        val userDTO: UserDTO? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("USER_DATA", UserDTO::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra("USER_DATA")
        }
        setContent {
            BioFitTheme {
                userDTO?.let {
                    TrackScreen(
                        initialSelectedOption = initialSelectedOption,
                        userDTO = userDTO
                    )
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
fun TrackScreen(
    initialSelectedOption: Int,
    userDTO: UserDTO
) {
    val context = LocalContext.current
    val activity = context as? Activity

    val standardPadding = getStandardPadding().first
    val modifier = getStandardPadding().second

    var expanded by remember { mutableStateOf(false) }
    var selectedOption by rememberSaveable { mutableIntStateOf(initialSelectedOption) }

    val options = listOf(
        R.string.morning,
        R.string.afternoon,
        R.string.evening,
        R.string.snack
    )
    val filteredOptions = options.filter { it != selectedOption }


    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = WindowInsets.safeDrawing.asPaddingValues().calculateTopPadding(),
                    start = standardPadding,
                    end = standardPadding,
                ),
            verticalArrangement = Arrangement.spacedBy(standardPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TopBar(
                onBackClick = { activity?.finish() },
                onHomeClick = {
                    activity?.let {
                        val intent = Intent(it, MainActivity::class.java)
                        intent.putExtra("USER_DATA", userDTO)
                        it.startActivity(intent)
                    }
                },
                title = stringResource(selectedOption),
                middleButton = {
                    Box {
                        IconButton(
                            onClick = { expanded = true }
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = null,
                                modifier = Modifier.size(standardPadding * 3),
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                        }

                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            filteredOptions.forEach { selection ->
                                DropdownMenuItem(
                                    text = { Text(stringResource(selection)) },
                                    onClick = {
                                        selectedOption = selection
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }
                },
                rightButton = null,
                standardPadding = standardPadding
            )

            TrackContent(
                selectedOption = selectedOption,
                standardPadding = standardPadding,
                modifier = modifier
            )
        }
    }
}

@Composable
fun TrackContent(
    selectedOption: Int,
    standardPadding: Dp,
    modifier: Modifier
) {
    val context = LocalContext.current
    val activity = context as? Activity

    Column(
        verticalArrangement = Arrangement.spacedBy(standardPadding),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(standardPadding * 2)
        ) {
            item {
                NutritionalComposition(
                    selectedOption = selectedOption,
                    standardPadding = standardPadding,
                    modifier = modifier
                )
            }

            item {
                HorizontalDivider(
                    modifier = modifier,
                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                )
            }

            item {
                MenuForSession(
                    selectedOption = selectedOption,
                    standardPadding = standardPadding,
                    modifier = modifier
                )
            }
        }

        Button(
            onClick = {
                activity?.let {
                    val intent = Intent(it, AddActivity::class.java)
                    intent.putExtra(
                        "SESSION_TITLE",
                        when (selectedOption) {
                            R.string.morning -> R.string.morning
                            R.string.afternoon -> R.string.afternoon
                            R.string.evening -> R.string.evening
                            else -> R.string.snack
                        }
                    )
                    it.startActivity(intent)
                }
            },
            shape = MaterialTheme.shapes.extraLarge,
        ) {
            Text(
                text = stringResource(R.string.add_food),
                modifier = Modifier.padding(horizontal = standardPadding * 2),
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.labelLarge
            )
        }

        Spacer(
            modifier = Modifier.padding(
                bottom = WindowInsets.safeDrawing.asPaddingValues().calculateBottomPadding()
            )
        )
    }
}

@Composable
fun NutritionalComposition(
    selectedOption: Int,
    standardPadding: Dp,
    modifier: Modifier
) {
    val morningMacroTable = listOf(
        Triple(
            food1.protein.first,
            food1.protein.second,
            foodListMorning.sumOf { it.protein.third }),
        Triple(
            food1.carbohydrate.first,
            food1.carbohydrate.second,
            foodListMorning.sumOf { it.carbohydrate.third }),
        Triple(food1.fat.first, food1.fat.second, foodListMorning.sumOf { it.fat.third })
    )

    val afternoonMacroTable = listOf(
        Triple(
            food2.protein.first,
            food2.protein.second,
            foodListAfternoon.sumOf { it.protein.third }),
        Triple(
            food2.carbohydrate.first,
            food2.carbohydrate.second,
            foodListAfternoon.sumOf { it.carbohydrate.third }),
        Triple(food2.fat.first, food2.fat.second, foodListAfternoon.sumOf { it.fat.third })
    )

    val eveningMacroTable = listOf(
        Triple(
            food3.protein.first,
            food3.protein.second,
            foodListEvening.sumOf { it.protein.third }),
        Triple(
            food3.carbohydrate.first,
            food3.carbohydrate.second,
            foodListEvening.sumOf { it.carbohydrate.third }),
        Triple(food3.fat.first, food3.fat.second, foodListEvening.sumOf { it.fat.third })
    )

    val snackMacroTable = listOf(
        Triple(food1.protein.first, food1.protein.second, foodListSnack.sumOf { it.protein.third }),
        Triple(
            food1.carbohydrate.first,
            food1.carbohydrate.second,
            foodListSnack.sumOf { it.carbohydrate.third }),
        Triple(food1.fat.first, food1.fat.second, foodListSnack.sumOf { it.fat.third })
    )

    val sessionMacroTable = when (selectedOption) {
        R.string.morning -> morningMacroTable
        R.string.afternoon -> afternoonMacroTable
        R.string.evening -> eveningMacroTable
        else -> snackMacroTable
    }

    val morningCal = foodListMorning.sumOf { it.calories }
    val afternoonCal = foodListAfternoon.sumOf { it.calories }
    val eveningCal = foodListEvening.sumOf { it.calories }
    val snackCal = foodListSnack.sumOf { it.calories }

    val loadedCal = when (selectedOption) {
        R.string.morning -> morningCal
        R.string.afternoon -> afternoonCal
        R.string.evening -> eveningCal
        else -> snackCal
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(standardPadding * 2)
    ) {
        Text(
            text = stringResource(R.string.nutritional_composition),
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleSmall
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(standardPadding),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(0.3f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                FoodCalorieChart(
                    sizeChart = 7f,
                    value = loadedCal,
                    chartColor = MaterialTheme.colorScheme.primary,
                    textColor = MaterialTheme.colorScheme.outline,
                    standardPadding = standardPadding
                )
            }

            Column(
                modifier = Modifier.weight(0.7f),
                verticalArrangement = Arrangement.spacedBy(standardPadding * 2)
            ) {
                sessionMacroTable.forEach { (icon, title, value) ->
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(standardPadding),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(icon),
                            contentDescription = stringResource(title)
                        )

                        Text(
                            text = stringResource(title),
                            color = MaterialTheme.colorScheme.outline,
                            style = MaterialTheme.typography.bodySmall
                        )

                        Text(
                            text = "(${value.toBigDecimal().setScale(1, RoundingMode.HALF_UP)} g)",
                            modifier = Modifier.weight(1f),
                            color = MaterialTheme.colorScheme.outline,
                            textAlign = TextAlign.End,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }
    }
}

val foodListMorning = listOf<FoodInfo>()
val foodListAfternoon = listOf(
    food2, food3
)
val foodListEvening = listOf(
    food3, food1
)
val foodListSnack = listOf(
    food1
)

@Composable
fun MenuForSession(
    selectedOption: Int,
    standardPadding: Dp,
    modifier: Modifier
) {
    val context = LocalContext.current
    val activity = context as? Activity

    val meal = when (selectedOption) {
        R.string.morning -> R.string.breakfast
        R.string.afternoon -> R.string.lunch
        R.string.evening -> R.string.dinner
        else -> R.string.snack
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(standardPadding * 2)
    ) {
        Text(
            text = "${stringResource(R.string.menu_for)} ${stringResource(meal).lowercase()}",
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleSmall
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(standardPadding)
        ) {
            when (selectedOption) {
                R.string.morning ->
                    foodListMorning.forEachIndexed { index, _ ->
                        FoodItem(
                            foodImg = foodListMorning[index].foodImage,
                            foodName = foodListMorning[index].foodName,
                            servingSize = Pair(
                                foodListMorning[index].servingSize.first,
                                foodListMorning[index].servingSize.second
                            ),
                            mass = foodListMorning[index].mass,
                            calories = foodListMorning[index].calories,
                            macros = listOf(
                                Pair(
                                    foodListMorning[index].protein.first,
                                    foodListMorning[index].protein.third
                                ),
                                Pair(
                                    foodListMorning[index].carbohydrate.first,
                                    foodListMorning[index].carbohydrate.third
                                ),
                                Pair(
                                    foodListMorning[index].fat.first,
                                    foodListMorning[index].fat.third
                                )
                            ),
                            onClick = {
                                activity?.let {
                                    val intent = Intent(it, FoodDetailActivity::class.java)
                                    it.startActivity(intent)
                                }
                            },
                            standardPadding = standardPadding
                        )
                    }

                R.string.afternoon ->
                    foodListAfternoon.forEachIndexed { index, _ ->
                        FoodItem(
                            foodImg = foodListAfternoon[index].foodImage,
                            foodName = foodListAfternoon[index].foodName,
                            servingSize = Pair(
                                foodListAfternoon[index].servingSize.first,
                                foodListAfternoon[index].servingSize.second
                            ),
                            mass = foodListAfternoon[index].mass,
                            calories = foodListAfternoon[index].calories,
                            macros = listOf(
                                Pair(
                                    foodListAfternoon[index].protein.first,
                                    foodListAfternoon[index].protein.third
                                ),
                                Pair(
                                    foodListAfternoon[index].carbohydrate.first,
                                    foodListAfternoon[index].carbohydrate.third
                                ),
                                Pair(
                                    foodListAfternoon[index].fat.first,
                                    foodListAfternoon[index].fat.third
                                )
                            ),
                            onClick = {
                                activity?.let {
                                    val intent = Intent(it, FoodDetailActivity::class.java)
                                    it.startActivity(intent)
                                }
                            },
                            standardPadding = standardPadding
                        )
                    }

                R.string.evening ->
                    foodListEvening.forEachIndexed { index, _ ->
                        FoodItem(
                            foodImg = foodListEvening[index].foodImage,
                            foodName = foodListEvening[index].foodName,
                            servingSize = Pair(
                                foodListEvening[index].servingSize.first,
                                foodListEvening[index].servingSize.second
                            ),
                            mass = foodListEvening[index].mass,
                            calories = foodListEvening[index].calories,
                            macros = listOf(
                                Pair(
                                    foodListEvening[index].protein.first,
                                    foodListEvening[index].protein.third
                                ),
                                Pair(
                                    foodListEvening[index].carbohydrate.first,
                                    foodListEvening[index].carbohydrate.third
                                ),
                                Pair(
                                    foodListEvening[index].fat.first,
                                    foodListEvening[index].fat.third
                                )
                            ),
                            onClick = {
                                activity?.let {
                                    val intent = Intent(it, FoodDetailActivity::class.java)
                                    it.startActivity(intent)
                                }
                            },
                            standardPadding = standardPadding
                        )
                    }

                else ->
                    foodListSnack.forEachIndexed { index, _ ->
                        FoodItem(
                            foodImg = foodListSnack[index].foodImage,
                            foodName = foodListSnack[index].foodName,
                            servingSize = Pair(
                                foodListSnack[index].servingSize.first,
                                foodListSnack[index].servingSize.second
                            ),
                            mass = foodListSnack[index].mass,
                            calories = foodListSnack[index].calories,
                            macros = listOf(
                                Pair(
                                    foodListSnack[index].protein.first,
                                    foodListSnack[index].protein.third
                                ),
                                Pair(
                                    foodListSnack[index].carbohydrate.first,
                                    foodListSnack[index].carbohydrate.third
                                ),
                                Pair(foodListSnack[index].fat.first, foodListSnack[index].fat.third)
                            ),
                            onClick = {
                                activity?.let {
                                    val intent = Intent(it, FoodDetailActivity::class.java)
                                    it.startActivity(intent)
                                }
                            },
                            standardPadding = standardPadding
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
private fun TrackScreenDarkModePreviewInSmallPhone() {
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
        TrackScreen(
            initialSelectedOption = R.string.morning,
            userDTO = userDTO
        )
    }
}

@Preview(
    device = "id:pixel_9_pro_xl",
    showSystemUi = true,
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL,
)
@Composable
private fun TrackScreenPreviewInLargePhone() {
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
        TrackScreen(
            initialSelectedOption = R.string.morning,
            userDTO = userDTO
        )
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
private fun TrackScreenPreviewInTablet() {
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
        TrackScreen(
            initialSelectedOption = R.string.morning,
            userDTO = userDTO
        )
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
private fun TrackScreenLandscapeDarkModePreviewInSmallPhone() {
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
        TrackScreen(
            initialSelectedOption = R.string.morning,
            userDTO = userDTO
        )
    }
}

@Preview(
    device = "spec:parent=pixel_9_pro_xl,orientation=landscape",
    showSystemUi = true,
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
private fun TrackScreenLandscapePreviewInLargePhone() {
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
        TrackScreen(
            initialSelectedOption = R.string.morning,
            userDTO = userDTO
        )
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
private fun TrackScreenLandscapePreviewInTablet() {
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
        TrackScreen(
            initialSelectedOption = R.string.morning,
            userDTO = userDTO
        )
    }
}