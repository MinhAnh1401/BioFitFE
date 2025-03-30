package com.example.biofit.ui.activity

import android.app.Activity
import android.content.res.Configuration
import android.graphics.Paint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import com.example.biofit.R
import com.example.biofit.ui.components.TopBar
import com.example.biofit.ui.components.getStandardPadding
import com.example.biofit.ui.theme.BioFitTheme
import java.math.RoundingMode

class FoodDetailActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BioFitTheme {
                FoodDetailScreen()
            }
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        recreate()
    }
}

@Composable
fun FoodDetailScreen() {
    val context = LocalContext.current
    val activity = context as? Activity

    val standardPadding = getStandardPadding().first
    val modifier = getStandardPadding().second

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
            verticalArrangement = Arrangement.spacedBy(standardPadding * 2),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TopBar(
                onBackClick = { activity?.finish() },
                title = stringResource(R.string.food_detail),
                middleButton = null,
                rightButton = null,
                standardPadding = standardPadding
            )

            FoodDetailContent(
                standardPadding = standardPadding,
                modifier = modifier
            )
        }
    }
}

data class FoodInfo(
    val foodImage: Int,
    val foodName: String,
    val servingSize: Pair<Float, String>,
    val mass: Float,
    val calories: Float,
    val protein: Triple<Int, Int, Float>,
    val carbohydrate: Triple<Int, Int, Float>,
    val fat: Triple<Int, Int, Float>,
    val sodium: Float? = null,
)

//Dữ liệu giả
val food1 = FoodInfo(
    foodImage = R.drawable.img_food_default,
    foodName = "Pizza",
    servingSize = Pair(1f, "slice"),
    mass = 107f,
    calories = 285f,
    protein = Triple(R.drawable.ic_protein, R.string.protein, 12f),
    carbohydrate = Triple(R.drawable.ic_carbohydrate, R.string.carbohydrate, 36f),
    fat = Triple(R.drawable.ic_fat, R.string.fat, 10f),
    sodium = 640f
)

@Composable
fun FoodDetailContent(
    standardPadding: Dp,
    modifier: Modifier
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(standardPadding * 2)
    ) {
        item {
            FoodNutritionalComposition(
                standardPadding = standardPadding,
                modifier = modifier
            )
        }

        item {
            FoodNutritionalValue(
                standardPadding = standardPadding,
                modifier = modifier
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
fun FoodNutritionalComposition(
    standardPadding: Dp,
    modifier: Modifier
) {
    val foodMacro = listOf(food1.protein, food1.carbohydrate, food1.fat)

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(standardPadding * 2),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(0.3f),
            verticalArrangement = Arrangement.spacedBy(standardPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(food1.foodImage),
                contentDescription = "Food image",
                modifier = Modifier.size(standardPadding * 10)
            )

            Text(
                text = food1.foodName,
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.titleSmall
            )
        }

        Column(
            modifier = Modifier.weight(0.7f),
            verticalArrangement = Arrangement.spacedBy(standardPadding * 2),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.nutritional_composition),
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.titleSmall
            )

            FoodCalorieChart(
                sizeChart = 5f,
                value = food1.calories,
                chartColor = MaterialTheme.colorScheme.primary,
                textColor = MaterialTheme.colorScheme.outline,
                standardPadding = standardPadding
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(standardPadding * 2)
            ) {
                foodMacro.forEach { (icon, title, value) ->
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
                            text = "(${value.toBigDecimal().setScale(1, RoundingMode.HALF_UP)} " +
                                    "${stringResource(R.string.gam)})",
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

@Composable
fun FoodCalorieChart(
    sizeChart: Float,
    value: Float,
    chartColor: Color,
    textColor: Color,
    standardPadding: Dp
) {
    Canvas(
        modifier = Modifier.size(standardPadding * sizeChart)
    ) {
        val center = Offset(size.width / 2, size.height / 2)
        val radius = size.minDimension / 2f

        drawCircle(
            color = chartColor,
            center = center,
            radius = radius,
            style = Stroke(width = radius / 3f)
        )

        drawContext.canvas.nativeCanvas.apply {
            drawText(
                "${value.toBigDecimal().setScale(0, RoundingMode.HALF_UP)} kcal",
                center.x,
                center.y + radius / 10f,
                Paint().apply {
                    textSize = radius / 3f
                    color = textColor.toArgb()
                    textAlign = Paint.Align.CENTER
                }
            )
        }
    }
}

@Composable
fun FoodNutritionalValue(
    standardPadding: Dp,
    modifier: Modifier
) {
    val nutrients = listOf(
        Pair(
            stringResource(R.string.serving_size),
            "${food1.servingSize.first} ${food1.servingSize.second}"
        ),
        Pair(stringResource(R.string.mass), "${food1.mass} g"),
        Pair(stringResource(R.string.calories), "${food1.calories} kcal"),
        Pair(stringResource(food1.fat.second), "${food1.fat.third} g"),
        Pair(stringResource(R.string.sodium), "${food1.sodium.takeIf { it != null } ?: 0} mg"),
        Pair(stringResource(food1.carbohydrate.second), "${food1.carbohydrate.third} g"),
        Pair(stringResource(food1.protein.second), "${food1.protein.third} g")
    )

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(standardPadding)
    ) {
        Text(
            text = stringResource(R.string.nutritional_value),
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleSmall
        )

        Column(
            modifier = Modifier.padding(standardPadding),
            verticalArrangement = Arrangement.spacedBy(standardPadding)
        ) {
            nutrients.forEach { (name, value) ->
                Row {
                    Text(
                        text = name,
                        modifier = Modifier.weight(1f),
                        color = MaterialTheme.colorScheme.outline,
                        style = MaterialTheme.typography.bodySmall
                    )

                    Text(
                        text = value,
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                if (name != nutrients.last().first) {
                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
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
private fun FoodDetailScreenDarkModePreviewInSmallPhone() {
    BioFitTheme {
        FoodDetailScreen()
    }
}

@Preview(
    device = "id:pixel_9_pro_xl",
    showSystemUi = true,
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL,
)
@Composable
private fun FoodDetailScreenPreviewInLargePhone() {
    BioFitTheme {
        FoodDetailScreen()
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
private fun FoodDetailScreenPreviewInTablet() {
    BioFitTheme {
        FoodDetailScreen()
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
private fun FoodDetailScreenLandscapeDarkModePreviewInSmallPhone() {
    BioFitTheme {
        FoodDetailScreen()
    }
}

@Preview(
    device = "spec:parent=pixel_9_pro_xl,orientation=landscape",
    showSystemUi = true,
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
private fun FoodDetailScreenLandscapePreviewInLargePhone() {
    BioFitTheme {
        FoodDetailScreen()
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
private fun FoodDetailScreenLandscapePreviewInTablet() {
    BioFitTheme {
        FoodDetailScreen()
    }
}