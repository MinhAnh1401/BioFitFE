package com.example.biofit.ui.activity

import android.app.Activity
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.biofit.R
import com.example.biofit.ui.components.SelectionDialog
import com.example.biofit.ui.components.TopBar
import com.example.biofit.ui.components.getStandardPadding
import com.example.biofit.ui.theme.BioFitTheme

class CreateFoodActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BioFitTheme {
                CreateFoodScreen()
            }
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        recreate()
    }
}

@Composable
fun CreateFoodScreen() {
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
            verticalArrangement = Arrangement.spacedBy(standardPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TopBar(
                onBackClick = { activity?.finish() },
                title = stringResource(R.string.create_new_food),
                middleButton = null,
                rightButton = null,
                standardPadding = standardPadding
            )

            CreateFoodContent(
                standardPadding = standardPadding,
                modifier = modifier
            )
        }
    }
}

@Composable
fun CreateFoodContent(
    standardPadding: Dp,
    modifier: Modifier
) {
    val context = LocalContext.current
    val activity = context as? Activity

    var foodName by rememberSaveable { mutableStateOf("") }
    var servingSize by rememberSaveable { mutableStateOf("") }
    var mass by rememberSaveable { mutableStateOf("") }
    val defaultMass = stringResource(R.string.gram)
    var selectedUnitMeasure by rememberSaveable { mutableStateOf(defaultMass) }
    var showUnitDialog by rememberSaveable { mutableStateOf(false) }
    var calories by rememberSaveable { mutableStateOf("") }
    var protein by rememberSaveable { mutableStateOf("") }
    var carb by rememberSaveable { mutableStateOf("") }
    var fat by rememberSaveable { mutableStateOf("") }
    var sodium by rememberSaveable { mutableStateOf("") }

    val focusManager = LocalFocusManager.current

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(standardPadding * 2),
    ) {
        item {
            Column(
                modifier = modifier.padding(top = standardPadding),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(R.drawable.img_food_default),
                    contentDescription = "Food image",
                    modifier = Modifier
                        .size(standardPadding * 15)
                        .clip(MaterialTheme.shapes.extraLarge)
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.25f),
                            shape = MaterialTheme.shapes.extraLarge
                        )
                )

                TextButton(
                    onClick = { TODO() }
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(standardPadding / 2),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_plus),
                            contentDescription = "Add button",
                            modifier = Modifier.size(standardPadding),
                            tint = MaterialTheme.colorScheme.primary
                        )

                        Text(
                            text = stringResource(R.string.upload_new_photo),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }

        item {
            Column(
                modifier = modifier,
                verticalArrangement = Arrangement.spacedBy(standardPadding)
            ) {
                Text(
                    text = stringResource(R.string.nutrition_information),
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.titleMedium
                )

                Column(
                    modifier = modifier,
                    verticalArrangement = Arrangement.spacedBy(standardPadding)
                ) {
                    OutlinedTextField(
                        value = foodName,
                        onValueChange = { foodName = it },
                        modifier = modifier,
                        textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.End),
                        placeholder = {
                            Text(
                                text = stringResource(R.string.enter_food_name),
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.End
                            )
                        },
                        prefix = { Text(text = stringResource(R.string.food_name) + "*") },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next
                        ),
                        keyboardActions = KeyboardActions(
                            onNext = { focusManager.moveFocus(FocusDirection.Down) }
                        ),
                        singleLine = true,
                        shape = MaterialTheme.shapes.large
                    )

                    OutlinedTextField(
                        value = servingSize,
                        onValueChange = { servingSize = it },
                        modifier = modifier,
                        textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.End),
                        placeholder = {
                            Text(
                                text = stringResource(R.string.enter_serving_size),
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.End
                            )
                        },
                        prefix = { Text(text = stringResource(R.string.serving_size)) },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Decimal,
                            imeAction = ImeAction.Next
                        ),
                        keyboardActions = KeyboardActions(
                            onNext = { focusManager.moveFocus(FocusDirection.Down) }
                        ),
                        singleLine = true,
                        shape = MaterialTheme.shapes.large
                    )

                    OutlinedTextField(
                        value = mass,
                        onValueChange = { mass = it },
                        modifier = modifier,
                        textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.End),
                        prefix = { Text(text = stringResource(R.string.mass)) },
                        trailingIcon = {
                            IconButton(onClick = { showUnitDialog = true }) {
                                Icon(
                                    painter = painterResource(R.drawable.ic_back),
                                    contentDescription = stringResource(R.string.unit_of_measure),
                                    modifier = Modifier
                                        .rotate(270f)
                                        .size(standardPadding),
                                    tint = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        },
                        suffix = { Text(text = selectedUnitMeasure) },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Decimal,
                            imeAction = ImeAction.Next
                        ),
                        keyboardActions = KeyboardActions(
                            onNext = { focusManager.moveFocus(FocusDirection.Down) }
                        ),
                        singleLine = true,
                        shape = MaterialTheme.shapes.large
                    )

                    if (showUnitDialog) {
                        SelectionDialog(
                            selectedOption = selectedUnitMeasure,
                            onOptionSelected = { selectedUnit ->
                                selectedUnitMeasure = selectedUnit
                                showUnitDialog = false
                            },
                            onDismissRequest = { showUnitDialog = false },
                            title = R.string.select_unit_of_measure,
                            listOptions = listOf(
                                stringResource(R.string.gram),
                                stringResource(R.string.milligram)
                            ),
                            standardPadding = standardPadding
                        )
                    }

                    OutlinedTextField(
                        value = calories,
                        onValueChange = { calories = it },
                        modifier = modifier,
                        textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.End),
                        prefix = { Text(text = stringResource(R.string.calories) + "*") },
                        suffix = { Text(text = stringResource(R.string.kcal)) },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Decimal,
                            imeAction = ImeAction.Next
                        ),
                        keyboardActions = KeyboardActions(
                            onNext = { focusManager.moveFocus(FocusDirection.Down) }
                        ),
                        singleLine = true,
                        shape = MaterialTheme.shapes.large
                    )

                    OutlinedTextField(
                        value = protein,
                        onValueChange = { protein = it },
                        modifier = modifier,
                        textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.End),
                        prefix = { Text(text = stringResource(R.string.protein)) },
                        suffix = { Text(text = stringResource(R.string.gram)) },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Decimal,
                            imeAction = ImeAction.Next
                        ),
                        keyboardActions = KeyboardActions(
                            onNext = { focusManager.moveFocus(FocusDirection.Down) }
                        ),
                        singleLine = true,
                        shape = MaterialTheme.shapes.large
                    )

                    OutlinedTextField(
                        value = carb,
                        onValueChange = { carb = it },
                        modifier = modifier,
                        textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.End),
                        prefix = { Text(text = stringResource(R.string.carbohydrate)) },
                        suffix = { Text(text = stringResource(R.string.gram)) },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Decimal,
                            imeAction = ImeAction.Next
                        ),
                        keyboardActions = KeyboardActions(
                            onNext = { focusManager.moveFocus(FocusDirection.Down) }
                        ),
                        singleLine = true,
                        shape = MaterialTheme.shapes.large
                    )

                    OutlinedTextField(
                        value = fat,
                        onValueChange = { fat = it },
                        modifier = modifier,
                        textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.End),
                        prefix = { Text(text = stringResource(R.string.fat)) },
                        suffix = { Text(text = stringResource(R.string.gram)) },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Decimal,
                            imeAction = ImeAction.Next
                        ),
                        keyboardActions = KeyboardActions(
                            onNext = { focusManager.moveFocus(FocusDirection.Down) }
                        ),
                        singleLine = true,
                        shape = MaterialTheme.shapes.large
                    )

                    OutlinedTextField(
                        value = sodium,
                        onValueChange = { sodium = it },
                        modifier = modifier,
                        textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.End),
                        prefix = { Text(text = stringResource(R.string.sodium)) },
                        suffix = { Text(text = stringResource(R.string.milligram)) },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Decimal,
                            imeAction = ImeAction.Go
                        ),
                        keyboardActions = KeyboardActions(onGo = { TODO() }),
                        singleLine = true,
                        shape = MaterialTheme.shapes.large
                    )
                }
            }
        }

        item {
            Column(
                modifier = modifier,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ElevatedButton(
                    onClick = { activity?.finish() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text(
                        text = stringResource(R.string.save_food),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
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

@Preview(
    device = "id:pixel",
    showSystemUi = true,
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL,
    locale = "vi"
)
@Composable
private fun CreateFoodScreenDarkModePreviewInSmallPhone() {
    BioFitTheme {
        CreateFoodScreen()
    }
}

@Preview(
    device = "id:pixel_9_pro_xl",
    showSystemUi = true,
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL,
)
@Composable
private fun CreateFoodScreenPreviewInLargePhone() {
    BioFitTheme {
        CreateFoodScreen()
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
private fun CreateFoodScreenPreviewInTablet() {
    BioFitTheme {
        CreateFoodScreen()
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
private fun CreateFoodScreenLandscapeDarkModePreviewInSmallPhone() {
    BioFitTheme {
        CreateFoodScreen()
    }
}

@Preview(
    device = "spec:parent=pixel_9_pro_xl,orientation=landscape",
    showSystemUi = true,
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
private fun CreateFoodScreenLandscapePreviewInLargePhone() {
    BioFitTheme {
        CreateFoodScreen()
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
private fun CreateFoodScreenLandscapePreviewInTablet() {
    BioFitTheme {
        CreateFoodScreen()
    }
}