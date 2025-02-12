package com.example.biofit.view

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.biofit.R
import com.example.biofit.ui.theme.BioFitTheme
import java.math.RoundingMode

class AddActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BioFitTheme {
                AddScreen()
            }
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        recreate()
    }
}

@Composable
fun getStandardPadding(): Pair<Dp, Modifier> {
    val screenWidth = LocalConfiguration.current.screenWidthDp
    val screenHeight = LocalConfiguration.current.screenHeightDp
    val standardPadding = ((screenWidth + screenHeight) / 2).dp * 0.02f
    val modifier = if (screenWidth > screenHeight) {
        Modifier.width(((screenWidth + screenHeight) / 2).dp)
    } else {
        Modifier.fillMaxWidth()
    }

    return Pair(standardPadding, modifier)
}

@Composable
fun AddScreen() {
    val standardPadding = getStandardPadding().first
    val modifier = getStandardPadding().second

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

            AddContent(
                selectedOption = selectedOption.value,
                standardPadding = standardPadding,
                modifier = modifier
            )
        }
    }
}

@Composable
fun AddContent(
    selectedOption: Int,
    standardPadding: Dp,
    modifier: Modifier
) {
    var search by rememberSaveable { mutableStateOf("") }

    var selectedToggle by rememberSaveable { mutableIntStateOf(R.string.create) }

    OutlinedTextField(
        value = search,
        onValueChange = { search = it },
        modifier = modifier.padding(vertical = standardPadding),
        textStyle = MaterialTheme.typography.bodySmall,
        placeholder = {
            Text(
                text = stringResource(R.string.search),
                style = MaterialTheme.typography.bodySmall
            )
        },
        leadingIcon = {
            IconButton(
                onClick = { TODO() }
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = stringResource(R.string.search),
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        },
        trailingIcon = {
            IconButton(
                onClick = { TODO() },
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_scan),
                    contentDescription = "Scan",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        },
        keyboardActions = KeyboardActions(
            onDone = { /*TODO*/ },
            onGo = { /*TODO*/ },
            onNext = { /*TODO*/ },
            onPrevious = { /*TODO*/ },
            onSearch = { /*TODO*/ },
            onSend = { /*TODO*/ }
        ),
        singleLine = true,
        maxLines = 1,
        shape = MaterialTheme.shapes.large,
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
            unfocusedBorderColor = Color.Transparent,
            focusedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer,
            focusedBorderColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
    )

    Column(
        modifier = modifier
    ) {
        Toggle(
            selectedToggle = selectedToggle,
            onOptionSelected = { selectedToggle = it },
            standardPadding = standardPadding
        )
    }

    val foodList = listOf(
        food1,
        food2,
        food3
    )

    LazyColumn(
        modifier = Modifier.padding(top = standardPadding * 2),
        verticalArrangement = Arrangement.spacedBy(standardPadding)
    ) {
        item {
            Column(
                verticalArrangement = Arrangement.spacedBy(standardPadding)
            ) {
                foodList.forEachIndexed { index, _ ->
                    FoodItem2(
                        foodImg = foodList[index].foodImage,
                        foodName = foodList[index].foodName,
                        servingSize = Pair(
                            foodList[index].servingSize.first,
                            foodList[index].servingSize.second
                        ),
                        mass = foodList[index].mass,
                        calories = foodList[index].calories,
                        macros = listOf(
                            Pair(foodList[index].protein.first, foodList[index].protein.third),
                            Pair(
                                foodList[index].carbohydrate.first,
                                foodList[index].carbohydrate.third
                            ),
                            Pair(foodList[index].fat.first, foodList[index].fat.third)
                        ),
                        standardPadding = standardPadding
                    )
                }
            }
        }
    }
}

val food2 = FoodInfo(
    foodImage = R.drawable.img_food_default,
    foodName = "Hamburger",
    servingSize = Pair(1f, "sandwich"),
    mass = 226f,
    calories = 540f,
    protein = Triple(R.drawable.ic_protein, R.string.protein, 34f),
    carbohydrate = Triple(R.drawable.ic_carbohydrate, R.string.carbohydrate, 40f),
    fat = Triple(R.drawable.ic_fat, R.string.fat, 27f),
    cholesterol = 122f,
    sodium = 791f
)

val food3 = FoodInfo(
    foodImage = R.drawable.img_food_default,
    foodName = "Beefsteaks",
    servingSize = Pair(1f, "steak"),
    mass = 221f,
    calories = 614f,
    protein = Triple(R.drawable.ic_protein, R.string.protein, 58f),
    carbohydrate = Triple(R.drawable.ic_carbohydrate, R.string.carbohydrate, 0f),
    fat = Triple(R.drawable.ic_fat, R.string.fat, 41f),
    cholesterol = 214f,
    sodium = 115f
)

@Composable
fun Toggle(
    selectedToggle: Int,
    onOptionSelected: (Int) -> Unit,
    standardPadding: Dp
) {
    val options = listOf(
        R.string.recently,
        R.string.create
    )

    Row(
        modifier = Modifier
            .clip(shape = MaterialTheme.shapes.extraLarge)
            .background(color = MaterialTheme.colorScheme.surfaceContainerHighest)
            .padding(standardPadding / 4),
        verticalAlignment = Alignment.CenterVertically
    ) {
        options.forEach { option ->
            val isSelected = option == selectedToggle
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(shape = MaterialTheme.shapes.extraLarge)
                    .background(
                        if (isSelected) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            Color.Transparent
                        }
                    )
                    .clickable { onOptionSelected(option) }
                    .padding(vertical = standardPadding / 2),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(id = option),
                    color = if (isSelected) {
                        MaterialTheme.colorScheme.onPrimary
                    } else {
                        MaterialTheme.colorScheme.outline
                    },
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }
    }
}

@Composable
fun FoodItem2(
    foodImg: Int,
    foodName: String,
    servingSize: Pair<Float, String>,
    mass: Float,
    calories: Float,
    macros: List<Pair<Int, Float>>,
    standardPadding: Dp
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
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
                text = "${servingSize.first} ${servingSize.second}, " +
                        "${mass.toBigDecimal().setScale(1, RoundingMode.HALF_UP)} g, " +
                        "${calories.toBigDecimal().setScale(1, RoundingMode.HALF_UP)} kcal",
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
                            text = "${value.toBigDecimal().setScale(1, RoundingMode.HALF_UP)} g",
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
        AddScreen()
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
        AddScreen()
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
        AddScreen()
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
        AddScreen()
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
        AddScreen()
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
        AddScreen()
    }
}