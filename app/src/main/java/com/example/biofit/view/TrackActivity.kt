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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.biofit.R
import com.example.biofit.ui.theme.BioFitTheme
import com.patrykandpatrick.vico.core.extension.sumOf
import java.math.RoundingMode

class TrackActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BioFitTheme {
                TrackScreen()
            }
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        recreate()
    }
}

@Composable
fun TrackScreen() {
    val screenWidth = LocalConfiguration.current.screenWidthDp
    val screenHeight = LocalConfiguration.current.screenHeightDp
    val standardPadding = ((screenWidth + screenHeight) / 2).dp * 0.02f
    val modifier = if (screenWidth > screenHeight) {
        Modifier.width(((screenWidth + screenHeight) / 2).dp)
    } else {
        Modifier.fillMaxWidth()
    }

    val selectedOption = getSelectedOption()
    var expanded by remember { mutableStateOf(false) }

    val options = listOf(
        R.string.morning,
        R.string.afternoon,
        R.string.evening,
        R.string.snack
    )
    val filteredOptions = options.filter { it != selectedOption.value }


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
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TopBarSetting(
                onBackClick = { TODO() },
                title = selectedOption.value,
                middleButton = {
                    Box {
                        IconButton(
                            onClick = { expanded = true }
                        ) {
                            Image(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = null
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
                                        selectedOption.value = selection
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
                selectedOption = selectedOption.value,
                standardPadding = standardPadding,
                modifier = modifier
            )
        }
    }
}

@Composable
fun getSelectedOption(): MutableState<Int> {
    return remember { mutableIntStateOf(R.string.morning) }
}

@Composable
fun TrackContent(
    selectedOption: Int,
    standardPadding: Dp,
    modifier: Modifier
) {
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
            onClick = { TODO() },
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

data class SessionMacro(
    val icon: Painter,
    val title: String,
    val value: Float,
    val targetValue: Float
)

@Composable
fun getSessionMacroTable(
    foodInfo: List<Triple<Pair<Int, String>, Float, List<Pair<Int, Float>>>>,
    selectedOption: Int
): List<SessionMacro> {
    // Tạo map để lưu tổng macro
    val macroTotals = mutableMapOf(
        R.drawable.ic_protein to 0f,
        R.drawable.ic_carbohydrate to 0f,
        R.drawable.ic_fat to 0f
    )

    // Cộng dồn macro từ danh sách thực phẩm
    foodInfo.forEach { (_, _, macros) ->
        macros.forEach { (macroType, value) ->
            macroTotals[macroType] = (macroTotals[macroType] ?: 0f) + value
        }
    }

    val proteinTarget = (getTargetCal(selectedOption) * 0.30f) / 4f
    val carbohydrateTarget = (getTargetCal(selectedOption) * 0.50f) / 4f
    val fatTarget = (getTargetCal(selectedOption) * 0.20f) / 9f

    // Chuyển đổi map thành danh sách SessionMacro
    return macroTotals.map { (icon, value) ->
        val target = when (icon) {
            R.drawable.ic_protein -> proteinTarget
            R.drawable.ic_carbohydrate -> carbohydrateTarget
            R.drawable.ic_fat -> fatTarget
            else -> 0f
        }
        SessionMacro(painterResource(icon), stringResource(getMacroTitle(icon)), value, target)
    }
}

// Hàm lấy mục tiêu calo trên từng buổi (1 ngày của người bình thường khoảng 2000 - 2500cal)
fun getTargetCal(selectedOption: Int): Float {
    return when (selectedOption) {
        R.string.morning -> 750f
        R.string.afternoon -> 750f
        R.string.evening -> 750f
        else -> 250f
    }
}

// Hàm trả về tiêu đề của từng macro
fun getMacroTitle(icon: Int): Int {
    return when (icon) {
        R.drawable.ic_protein -> R.string.protein
        R.drawable.ic_carbohydrate -> R.string.carbohydrate
        R.drawable.ic_fat -> R.string.fat
        else -> R.string.unknown
    }
}

@Composable
fun NutritionalComposition(
    selectedOption: Int,
    standardPadding: Dp,
    modifier: Modifier
) {
    val targetCal = getTargetCal(selectedOption)

    val foodInfo = getFoodInfo(selectedOption) // Lấy danh sách thức ăn từ hàm dưới

    // Tính tổng macro nutrients
    var totalProtein = 0f
    var totalCarbs = 0f
    var totalFat = 0f

    foodInfo.forEach { (_, _, macros) ->
        macros.forEach { (icon, value) ->
            when (icon) {
                R.drawable.ic_protein -> totalProtein += value
                R.drawable.ic_carbohydrate -> totalCarbs += value
                R.drawable.ic_fat -> totalFat += value
            }
        }
    }

    // Tính tổng calo theo công thức chuẩn
    val loadedCal = (totalProtein * 4) + (totalCarbs * 4) + (totalFat * 9)

    val sessionMacroTable = getSessionMacroTable(foodInfo, selectedOption)

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
                WaterChart(
                    sizeChart = standardPadding * 7,
                    loadedValue = loadedCal,
                    targetValue = targetCal,
                    circleColor = MaterialTheme.colorScheme.outline,
                    progressColor = MaterialTheme.colorScheme.primary,
                    exceededColor = Color(0xFF960000),
                    stringResource(R.string.cal),
                    standardPadding = standardPadding
                )
            }

            Column(
                modifier = Modifier.weight(0.7f),
                verticalArrangement = Arrangement.spacedBy(standardPadding * 2)
            ) {
                sessionMacroTable.forEach { (icon, title, value, targetValue) ->
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(standardPadding),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = icon,
                            contentDescription = title
                        )

                        Text(
                            text = title,
                            color = MaterialTheme.colorScheme.outline,
                            style = MaterialTheme.typography.bodySmall
                        )

                        Text(
                            text = "(${value.toBigDecimal().setScale(2, RoundingMode.HALF_UP)}g / " +
                                    "${targetValue.toBigDecimal().setScale(2, RoundingMode.HALF_UP)}g)",
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

fun getFoodInfo(selectedOption: Int): List<Triple<Pair<Int, String>, Float, List<Pair<Int, Float>>>> {
    return when (selectedOption) {
        R.string.morning -> listOf(
            Triple(
                Pair(R.drawable.img_food_default, "Breakfast 1"),
                1.5f,
                listOf(
                    Pair(R.drawable.ic_protein, 15f),
                    Pair(R.drawable.ic_carbohydrate, 25f),
                    Pair(R.drawable.ic_fat, 5f)
                )
            ),
            Triple(
                Pair(R.drawable.img_food_default, "Breakfast 2"),
                2f,
                listOf(
                    Pair(R.drawable.ic_protein, 15f),
                    Pair(R.drawable.ic_carbohydrate, 25f),
                    Pair(R.drawable.ic_fat, 5f)
                )
            ),
            Triple(
                Pair(R.drawable.img_food_default, "Breakfast 3"),
                2f,
                listOf(
                    Pair(R.drawable.ic_protein, 15f),
                    Pair(R.drawable.ic_carbohydrate, 25f),
                    Pair(R.drawable.ic_fat, 5f)
                )
            )
        )

        R.string.afternoon -> listOf(
            Triple(
                Pair(R.drawable.img_food_default, "Lunch 1"),
                1.5f,
                listOf(
                    Pair(R.drawable.ic_protein, 25f),
                    Pair(R.drawable.ic_carbohydrate, 45f),
                    Pair(R.drawable.ic_fat, 5f)
                )
            ),
            Triple(
                Pair(R.drawable.img_food_default, "Lunch 2"),
                1f,
                listOf(
                    Pair(R.drawable.ic_protein, 25f),
                    Pair(R.drawable.ic_carbohydrate, 45f),
                    Pair(R.drawable.ic_fat, 5f)
                )
            )
        )

        R.string.evening -> listOf(
            Triple(
                Pair(R.drawable.img_food_default, "Dinner 1"),
                1f,
                listOf(
                    Pair(R.drawable.ic_protein, 25f),
                    Pair(R.drawable.ic_carbohydrate, 45f),
                    Pair(R.drawable.ic_fat, 5f)
                )
            ),
            Triple(
                Pair(R.drawable.img_food_default, "Dinner 2"),
                1f,
                listOf(
                    Pair(R.drawable.ic_protein, 25f),
                    Pair(R.drawable.ic_carbohydrate, 45f),
                    Pair(R.drawable.ic_fat, 5f)
                )
            )
        )

        else -> listOf( // Snack
            Triple(
                Pair(R.drawable.img_food_default, "Snack 1"),
                1f,
                listOf(
                    Pair(R.drawable.ic_protein, 5f),
                    Pair(R.drawable.ic_carbohydrate, 15f),
                    Pair(R.drawable.ic_fat, 0f)
                )
            ),
            Triple(
                Pair(R.drawable.img_food_default, "Snack 2"),
                0.5f,
                listOf(
                    Pair(R.drawable.ic_protein, 5f),
                    Pair(R.drawable.ic_carbohydrate, 15f),
                    Pair(R.drawable.ic_fat, 0f)
                )
            )
        )
    }
}

@Composable
fun MenuForSession(
    selectedOption: Int,
    standardPadding: Dp,
    modifier: Modifier
) {
    val meal = when (selectedOption) {
        R.string.morning -> R.string.breakfast
        R.string.afternoon -> R.string.lunch
        R.string.evening -> R.string.dinner
        else -> R.string.snack
    }

    val foodInfo = getFoodInfo(selectedOption)

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(standardPadding * 2)
    ) {
        Text(
            text = "${stringResource(R.string.menu_for)} ${stringResource(meal).lowercase()}",
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleSmall
        )

        foodInfo.forEach { (selectedOption, servingSize, macros) ->
            FoodItem(
                selectedOption = selectedOption,
                servingSize = servingSize,
                macros = macros,
                standardPadding = standardPadding
            )
        }
    }
}

@Composable
fun FoodItem(
    selectedOption: Pair<Int, String>,
    servingSize: Float,
    macros: List<Pair<Int, Float>>,
    standardPadding: Dp
) {
    val totalGrams = macros.sumOf { it.second }

    val totalCalories = macros.sumOf { (icon, value) ->
        when (icon) {
            R.drawable.ic_protein -> value * 4
            R.drawable.ic_carbohydrate -> value * 4
            R.drawable.ic_fat -> value * 9
            else -> 0f
        }
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(standardPadding),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(selectedOption.first),
            contentDescription = "Food image",
            modifier = Modifier
                .size(standardPadding * 5)
                .clip(MaterialTheme.shapes.large)
        )

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = selectedOption.second,
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.bodySmall
            )

            Text(
                text = "$servingSize servings, " +
                        "${totalGrams.toBigDecimal().setScale(2, RoundingMode.HALF_UP)}g, " +
                        "${totalCalories.toBigDecimal().setScale(2, RoundingMode.HALF_UP)}cal",
                color = MaterialTheme.colorScheme.outline,
                style = MaterialTheme.typography.labelSmall
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(standardPadding),
            ) {
                macros.forEach { (icon, value) ->
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(standardPadding / 2),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(icon),
                            contentDescription = "Macro icon"
                        )
                        Text(
                            text = "${value.toBigDecimal().setScale(2, RoundingMode.HALF_UP)}g",
                            color = MaterialTheme.colorScheme.outline,
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
            }
        }

        IconButton(
            onClick = { TODO() }
        ) {
            Image(
                painter = painterResource(R.drawable.btn_back),
                contentDescription = "Extend button",
                modifier = Modifier.rotate(180f)
            )
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
private fun DarkModePreviewInSmallPhone() {
    BioFitTheme {
        TrackScreen()
    }
}

@Preview(
    device = "id:pixel_9_pro_xl",
    showSystemUi = true,
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL,
)
@Composable
private fun PreviewInLargePhone() {
    BioFitTheme {
        TrackScreen()
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
private fun PreviewInTablet() {
    BioFitTheme {
        TrackScreen()
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
private fun LandscapeDarkModePreviewInSmallPhone() {
    BioFitTheme {
        TrackScreen()
    }
}

@Preview(
    device = "spec:parent=pixel_9_pro_xl,orientation=landscape",
    showSystemUi = true,
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
private fun LandscapePreviewInLargePhone() {
    BioFitTheme {
        TrackScreen()
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
private fun LandscapePreviewInTablet() {
    BioFitTheme {
        TrackScreen()
    }
}