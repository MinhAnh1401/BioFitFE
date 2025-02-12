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
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
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
import androidx.compose.ui.text.style.TextAlign
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
    var expanded by rememberSaveable { mutableStateOf(false) }

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
                                        selectedOption.value = selection
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }
                },
                rightButton = {
                    IconButton(
                        onClick = { TODO() }
                    ) {
                        Image(
                            painter = painterResource(R.drawable.ic_plus),
                            contentDescription = "Add button",
                            modifier = Modifier.size(standardPadding * 2)
                        )
                    }
                },
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

    val foodListCreateMorning = listOf<FoodInfo>()
    val foodListCreateAfternoon = listOf(food1, food2)
    val foodListCreateEvening = listOf(food1)
    val foodListCreateSnack = listOf(food3)

    val foodListRecent = when (selectedOption) {
        R.string.morning -> foodListMorning
        R.string.afternoon -> foodListAfternoon
        R.string.evening -> foodListEvening
        else -> foodListSnack
    }

    val foodListCreate = when (selectedOption) {
        R.string.morning -> foodListCreateMorning
        R.string.afternoon -> foodListCreateAfternoon
        R.string.evening -> foodListCreateEvening
        else -> foodListCreateSnack
    }

    LazyColumn(
        modifier = Modifier.padding(top = standardPadding * 2),
        verticalArrangement = Arrangement.spacedBy(standardPadding)
    ) {
        item {
            when (selectedToggle) {
                R.string.recently ->
                    when (foodListRecent.isNotEmpty()) {
                        true ->
                            Column(
                                modifier = modifier,
                                verticalArrangement = Arrangement.spacedBy(standardPadding)
                            ) {
                                foodListRecent.forEachIndexed { index, _ ->
                                    FoodItem2(
                                        foodImg = foodListRecent[index].foodImage,
                                        foodName = foodListRecent[index].foodName,
                                        servingSize = Pair(
                                            foodListRecent[index].servingSize.first,
                                            foodListRecent[index].servingSize.second
                                        ),
                                        mass = foodListRecent[index].mass,
                                        calories = foodListRecent[index].calories,
                                        macros = listOf(
                                            Pair(
                                                foodListRecent[index].protein.first,
                                                foodListRecent[index].protein.third
                                            ),
                                            Pair(
                                                foodListRecent[index].carbohydrate.first,
                                                foodListRecent[index].carbohydrate.third
                                            ),
                                            Pair(
                                                foodListRecent[index].fat.first,
                                                foodListRecent[index].fat.third
                                            )
                                        ),
                                        standardPadding = standardPadding
                                    )
                                }
                            }

                        else -> EmptyAddScreen(
                            standardPadding = standardPadding,
                            modifier = modifier
                        )
                    }

                else ->
                    when (foodListCreate.isNotEmpty()) {
                        true ->
                            Column(
                                modifier = modifier,
                                verticalArrangement = Arrangement.spacedBy(standardPadding)
                            ) {
                                foodListCreate.forEachIndexed { index, _ ->
                                    FoodItem2(
                                        foodImg = foodListCreate[index].foodImage,
                                        foodName = foodListCreate[index].foodName,
                                        servingSize = Pair(
                                            foodListCreate[index].servingSize.first,
                                            foodListCreate[index].servingSize.second
                                        ),
                                        mass = foodListCreate[index].mass,
                                        calories = foodListCreate[index].calories,
                                        macros = listOf(
                                            Pair(
                                                foodListCreate[index].protein.first,
                                                foodListCreate[index].protein.third
                                            ),
                                            Pair(
                                                foodListCreate[index].carbohydrate.first,
                                                foodListCreate[index].carbohydrate.third
                                            ),
                                            Pair(
                                                foodListCreate[index].fat.first,
                                                foodListCreate[index].fat.third
                                            )
                                        ),
                                        standardPadding = standardPadding
                                    )
                                }
                            }

                        else -> EmptyAddScreen(
                            standardPadding = standardPadding,
                            modifier = modifier
                        )

                    }

            }
        }
    }
}

@Composable
fun EmptyAddScreen(
    standardPadding: Dp,
    modifier: Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.create_new_food),
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.titleSmall
            )

            Text(
                text = stringResource(R.string.des_create_new_food),
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.outline,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodySmall
            )

            TextButton(
                onClick = { TODO() },
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(standardPadding / 2),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_plus),
                        contentDescription = "Add button",
                        tint = MaterialTheme.colorScheme.primary
                    )

                    Text(
                        text = stringResource(R.string.create_new),
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.bodySmall
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