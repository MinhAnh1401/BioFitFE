package com.example.biofit.ui.activity

import android.app.Activity
import androidx.compose.ui.draw.clip
import android.content.res.Configuration
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.biofit.R
import com.example.biofit.data.model.dto.FoodInfoDTO
import com.example.biofit.data.utils.UserSharedPrefsHelper
import com.example.biofit.ui.components.TopBar
import com.example.biofit.ui.components.getStandardPadding
import com.example.biofit.ui.theme.BioFitTheme
import com.example.biofit.view_model.FoodViewModel
import java.math.RoundingMode

class FoodDetailActivity : ComponentActivity() {
    private val foodViewModel: FoodViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val userId = UserSharedPrefsHelper.getUserData(this)?.userId ?: 0L
        foodViewModel.fetchFood(userId)
        enableEdgeToEdge()
        val foodId = intent.getLongExtra("FOOD_ID", -1)
        // Ghi log kiểm tra foodId đã nhận đúng chưa
        Log.d("FoodDetailActivity", "Received foodId: $foodId")
        setContent {
            BioFitTheme {
                FoodDetailScreen(foodId = foodId, foodViewModel = foodViewModel)
            }
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        recreate()
    }
}

@Composable
fun FoodDetailScreen(
    foodId: Long,
    foodViewModel: FoodViewModel = viewModel()
) {
    val context = LocalContext.current
    val activity = context as? Activity

    // Lấy danh sách thực phẩm từ ViewModel
    val foodList by foodViewModel.foodList.collectAsState()

    // Tìm thực phẩm có foodId tương ứng
    val selectedFoodDTO = foodList.find { it.foodId == foodId }

    // Nếu tìm thấy thực phẩm, chuyển đổi thành FoodInfoDTO
    val selectedFoodInfoDTO = selectedFoodDTO?.toFoodInfoDTO() ?: FoodInfoDTO.default()

    // Ghi log kiểm tra thực phẩm tìm được
    Log.d("FoodDetailScreen", "Selected food: $selectedFoodInfoDTO")

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
                selectedFoodInfoDTO = selectedFoodInfoDTO,
                standardPadding = standardPadding,
                modifier = modifier
            )
        }
    }
}

@Composable
fun FoodDetailContent(
    selectedFoodInfoDTO: FoodInfoDTO,
    standardPadding: Dp,
    modifier: Modifier
) {

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(standardPadding * 2)
    ) {
        item {
            FoodNutritionalComposition(
                foodInfo = selectedFoodInfoDTO,
                standardPadding = standardPadding,
                modifier = modifier
            )
        }

        item {
            FoodNutritionalValue(
                foodInfo = selectedFoodInfoDTO,
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
    foodInfo: FoodInfoDTO,
    standardPadding: Dp,
    modifier: Modifier
) {
    val foodMacro = listOf(foodInfo.protein,foodInfo.carbohydrate,foodInfo.fat)
    val bitmap = base64ToBitmap(foodInfo.foodImage)

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
                painter = if (bitmap != null) {
                    BitmapPainter(bitmap.asImageBitmap())
                } else {
                    painterResource(R.drawable.img_food_default)
                },
                contentDescription = "Food image",
                modifier = Modifier
                    .size(standardPadding * 8)
                    .clip(MaterialTheme.shapes.large)
            )

            Text(
                text = foodInfo.foodName,
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.headlineSmall
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
                style = MaterialTheme.typography.titleMedium
            )

            FoodCalorieChart(
                sizeChart = 5f,
                value = foodInfo.calories,
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
    foodInfo: FoodInfoDTO,
    standardPadding: Dp,
    modifier: Modifier
) {
    val nutrients = listOf(
        Pair(
            stringResource(R.string.serving_size),
            "${foodInfo.servingSize.first} ${foodInfo.servingSize.second}"
        ),
        Pair(stringResource(R.string.mass), "${foodInfo.mass} g"),
        Pair(stringResource(R.string.calories), "${foodInfo.calories} kcal"),
        Pair(stringResource(foodInfo.fat.second), "${foodInfo.fat.third} g"),
        Pair(stringResource(R.string.sodium), "${foodInfo.sodium.takeIf { it != null } ?: 0} mg"),
        Pair(stringResource(foodInfo.carbohydrate.second), "${foodInfo.carbohydrate.third} g"),
        Pair(stringResource(foodInfo.protein.second), "${foodInfo.protein.third} g")
    )

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(standardPadding)
    ) {
        Text(
            text = stringResource(R.string.nutritional_value),
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleMedium
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
                        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.25f),
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
        FoodDetailScreen(foodId = 0)
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
        FoodDetailScreen(foodId = 0)
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
        FoodDetailScreen(foodId = 0)
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
        FoodDetailScreen(foodId = 0)
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
        FoodDetailScreen(foodId = 0)
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
        FoodDetailScreen(foodId = 0)
    }
}