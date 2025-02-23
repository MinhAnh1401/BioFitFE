package com.example.biofit.view.dialog

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import com.example.biofit.R
import com.example.biofit.view.activity.getStandardPadding
import com.example.biofit.view.ui_theme.BioFitTheme
import java.math.RoundingMode

@Composable
fun FoodItemScreen() {
    Surface (
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
    ) {
        FoodItem(
            foodImg = R.drawable.img_food_default,
            foodName = stringResource(R.string.food_name),
            servingSize = Pair(1f, "sandwich"),
            mass = 100f,
            calories = 250f,
            macros = listOf(
                Pair(R.drawable.ic_protein, 10f),
                Pair(R.drawable.ic_carbohydrate, 20f),
                Pair(R.drawable.ic_fat, 30f)
            ),
            onClick = {},
            standardPadding = getStandardPadding().first
        )
    }
}

@Composable
fun FoodItem(
    foodImg: Int,
    foodName: String,
    servingSize: Pair<Float, String>,
    mass: Float,
    calories: Float,
    macros: List<Pair<Int, Float>>,
    onClick: () -> Unit,
    standardPadding: Dp
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        horizontalArrangement = Arrangement.spacedBy(standardPadding),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(foodImg),
            contentDescription = "Food image",
            modifier = Modifier
                .size(standardPadding * 5)
                .clip(MaterialTheme.shapes.large)
        )

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = foodName,
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.bodySmall
            )

            Text(
                text = "${servingSize.first}${servingSize.second}, " +
                        "${
                            mass.toBigDecimal().setScale(
                                1,
                                RoundingMode.HALF_UP
                            )
                        }${stringResource(R.string.gam)}, " +
                        "${
                            calories.toBigDecimal().setScale(
                                1,
                                RoundingMode.HALF_UP
                            )
                        }${stringResource(R.string.kcal)}",
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
                            text = "${
                                value.toBigDecimal().setScale(
                                    1,
                                    RoundingMode.HALF_UP
                                )
                            }${stringResource(R.string.gam)}",
                            color = MaterialTheme.colorScheme.outline,
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
            }
        }

        Image(
            painter = painterResource(R.drawable.btn_back),
            contentDescription = "Extend button",
            modifier = Modifier.rotate(180f)
        )
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
private fun FoodItemScreenDarkModePreviewInSmallPhone() {
    BioFitTheme {
        FoodItemScreen()
    }
}

@Preview(
    device = "id:pixel_9_pro_xl",
    showSystemUi = true,
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
private fun FoodItemScreenPreviewInLargePhone() {
    BioFitTheme {
        FoodItemScreen()
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
private fun FoodItemScreenPreviewInTablet() {
    BioFitTheme {
        FoodItemScreen()
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
private fun FoodItemScreenLandscapeDarkModePreviewInSmallPhone() {
    BioFitTheme {
        FoodItemScreen()
    }
}

@Preview(
    device = "spec:parent=pixel_9_pro_xl,orientation=landscape",
    showSystemUi = true,
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
private fun FoodItemScreenLandscapePreviewInLargePhone() {
    BioFitTheme {
        FoodItemScreen()
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
private fun FoodItemScreenLandscapePreviewInTablet() {
    BioFitTheme {
        FoodItemScreen()
    }
}