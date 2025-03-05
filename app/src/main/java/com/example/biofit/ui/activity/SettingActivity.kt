package com.example.biofit.ui.activity

import android.app.Activity
import android.app.DatePickerDialog
import android.content.res.Configuration
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.biofit.R
import com.example.biofit.data.dto.UserDTO
import com.example.biofit.navigation.getUserData
import com.example.biofit.ui.components.ItemCard
import com.example.biofit.ui.components.MainCard
import com.example.biofit.ui.components.SelectionDialog
import com.example.biofit.ui.components.SubCard
import com.example.biofit.ui.components.TopBar
import com.example.biofit.ui.components.getStandardPadding
import com.example.biofit.ui.theme.BioFitTheme
import com.example.biofit.view_model.LoginViewModel
import com.example.biofit.view_model.UpdateUserViewModel
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class SettingActivity : ComponentActivity() {
    private var userData: UserDTO? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        userData = getUserData(this)
        setContent {
            BioFitTheme {
                SettingScreen(userData ?: UserDTO.default())
            }
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        recreate()
    }
}

@Composable
fun SettingScreen(
    userData: UserDTO,
    updateViewModel: UpdateUserViewModel = viewModel(),
    loginViewModel: LoginViewModel = viewModel()
) {
    val context = LocalContext.current
    val activity = context as? Activity

    val screenWidth = LocalConfiguration.current.screenWidthDp
    val screenHeight = LocalConfiguration.current.screenHeightDp

    val standardPadding = getStandardPadding().first
    val modifier = getStandardPadding().second

    val userId = userData.userId

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
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
            TopBar(
                onBackClick = { activity?.finish() },
                title = stringResource(R.string.setting),
                middleButton = null,
                rightButton = {
                    TextButton(
                        onClick = {
                            updateViewModel.updateUser(context, userId, loginViewModel) {
                                if (updateViewModel.updatedState.value == true) {
                                    activity?.finish()
                                } else {
                                    Toast.makeText(
                                        context,
                                        updateViewModel.updatedMessage.value,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }
                    ) {
                        Text(
                            text = stringResource(R.string.save),
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                },
                standardPadding = standardPadding
            )

            val updatedMessage = updateViewModel.updatedMessage.value
            LaunchedEffect(updatedMessage) {
                updatedMessage?.let {
                    Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                    updateViewModel.updatedMessage.value = null
                }
            }

            SettingContent(
                userData = userData,
                screenWidth,
                screenHeight,
                standardPadding,
                modifier
            )
        }
    }
}

@Composable
fun SettingContent(
    userData: UserDTO,
    screenWidth: Int,
    screenHeight: Int,
    standardPadding: Dp,
    modifier: Modifier,
    updateViewModel: UpdateUserViewModel = viewModel()
) {
    val height = ((userData.height ?: UserDTO.default().height) ?: 0f) / 100f
    val weight = (userData.weight ?: UserDTO.default().weight) ?: 0f

    val bmiIndex: Float? = if (height != null && height > 0.001f) {
        weight?.div(height * height)
    } else {
        null
    }

    val roundedBmi = bmiIndex?.let {
        BigDecimal(it.toDouble()).setScale(1, RoundingMode.HALF_UP).toFloat()
    } ?: 0f

    val bmiCategory = when {
        bmiIndex == null -> stringResource(R.string.unknown)
        bmiIndex < 18.5f -> stringResource(R.string.underweight)
        bmiIndex >= 18.5f && bmiIndex < 25f -> stringResource(R.string.healthy_weight)
        bmiIndex >= 25f && bmiIndex < 30f -> stringResource(R.string.overweight)
        else -> stringResource(R.string.obese)
    }

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(standardPadding * 2)
    ) {
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = standardPadding),
                horizontalAlignment = if (screenWidth > screenHeight) {
                    Alignment.CenterHorizontally
                } else {
                    Alignment.Start
                }
            ) {
                Text(
                    text = stringResource(R.string.my_profile),
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.titleSmall,
                )
            }
        }

        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(standardPadding),
                verticalArrangement = Arrangement.spacedBy(standardPadding),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val context = LocalContext.current

                var gender by rememberSaveable {
                    mutableStateOf(
                        userData.getGenderString(
                            context,
                            updateViewModel.gender.value
                        )
                    )
                }
                var showGenderDialog by rememberSaveable { mutableStateOf(false) }
                var dateOfBirth by rememberSaveable { mutableStateOf(updateViewModel.dateOfBirth.value) }
                var showDatePicker by rememberSaveable { mutableStateOf(false) }

                LaunchedEffect(Unit) {
                    if (updateViewModel.fullName.value == null) {
                        updateViewModel.fullName.value = userData.fullName
                    }
                    if (updateViewModel.gender.value == null) {
                        updateViewModel.gender.value = userData.gender
                    }
                    if (updateViewModel.dateOfBirth.value == null) {
                        updateViewModel.dateOfBirth.value = userData.dateOfBirth
                    }
                    if (updateViewModel.height.value == null) {
                        updateViewModel.height.value = userData.height
                    }
                    if (updateViewModel.weight.value == null) {
                        updateViewModel.weight.value = userData.weight
                    }
                    if (updateViewModel.email.value == null) {
                        updateViewModel.email.value = userData.email
                    }
                }


                val focusManager = LocalFocusManager.current

                OutlinedTextField(
                    value = updateViewModel.fullName.value ?: "",
                    onValueChange = { updateViewModel.fullName.value = it },
                    modifier = modifier,
                    textStyle = MaterialTheme.typography.bodySmall.copy(
                        textAlign = TextAlign.End
                    ),
                    placeholder = {
                        Text(
                            text = stringResource(R.string.enter_your_full_name),
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.End,
                            style = MaterialTheme.typography.bodySmall
                        )
                    },
                    prefix = {
                        Text(
                            text = stringResource(R.string.name),
                            style = MaterialTheme.typography.bodySmall
                        )
                    },
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

                ItemCard(
                    onClick = { showGenderDialog = true },
                    modifier = modifier
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                horizontal = standardPadding,
                                vertical = standardPadding / 4
                            ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(R.string.gender),
                            style = MaterialTheme.typography.bodySmall
                        )

                        Text(
                            text = if (updateViewModel.gender.value == null) {
                                stringResource(R.string.select_gender)
                            } else {
                                userData.getGenderString(context, updateViewModel.gender.value)
                            },
                            modifier = Modifier.weight(1f),
                            color = if (updateViewModel.gender.value == null) {
                                MaterialTheme.colorScheme.outline
                            } else {
                                MaterialTheme.colorScheme.onBackground
                            },
                            textAlign = TextAlign.End,
                            style = MaterialTheme.typography.bodySmall
                        )

                        IconButton(onClick = { showGenderDialog = true }) {
                            Image(
                                painter = painterResource(R.drawable.btn_back),
                                contentDescription = stringResource(R.string.gender),
                                modifier = Modifier.rotate(270f)
                            )
                        }
                    }
                }

                if (showGenderDialog) {
                    SelectionDialog(
                        selectedOption = gender,
                        onOptionSelected = { selectedGender ->
                            gender = selectedGender
                            updateViewModel.gender.value = userData.getGenderInt(
                                context,
                                selectedGender
                            )
                            showGenderDialog = false
                        },
                        onDismissRequest = { showGenderDialog = false },
                        title = R.string.select_gender,
                        listOptions = listOf(
                            stringResource(R.string.male),
                            stringResource(R.string.female)
                        ),
                        standardPadding = standardPadding
                    )
                }

                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

                ItemCard(
                    onClick = { showDatePicker = true },
                    modifier = modifier
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = standardPadding, vertical = standardPadding / 4),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(R.string.date_of_birth),
                            style = MaterialTheme.typography.bodySmall
                        )

                        Text(
                            text = updateViewModel.dateOfBirth.value?.let { dateString ->
                                try {
                                    val parsedDate =
                                        dateFormat.parse(dateString)
                                    dateFormat.format(parsedDate ?: UserDTO.default().dateOfBirth)
                                } catch (e: Exception) {
                                    stringResource(R.string.select_date_of_birth)
                                }
                            } ?: stringResource(R.string.select_date_of_birth),
                            modifier = Modifier.weight(1f),
                            color = if (updateViewModel.dateOfBirth.value == null) {
                                MaterialTheme.colorScheme.outline
                            } else {
                                MaterialTheme.colorScheme.onBackground
                            },
                            textAlign = TextAlign.End,
                            style = MaterialTheme.typography.bodySmall
                        )

                        IconButton(onClick = { showDatePicker = true }) {
                            Icon(
                                imageVector = Icons.Default.DateRange,
                                contentDescription = stringResource(R.string.date_of_birth),
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }

                if (showDatePicker) {
                    val calendar = Calendar.getInstance()
                    LaunchedEffect(Unit) {
                        DatePickerDialog(
                            context,
                            { _, selectedYear, selectedMonth, selectedDay ->
                                calendar.set(selectedYear, selectedMonth, selectedDay)

                                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                                dateOfBirth = dateFormat.format(calendar.time)

                                updateViewModel.dateOfBirth.value = dateOfBirth
                                showDatePicker = false
                            },
                            calendar.get(Calendar.YEAR),
                            calendar.get(Calendar.MONTH),
                            calendar.get(Calendar.DAY_OF_MONTH)
                        ).show()
                    }
                }

                OutlinedTextField(
                    value = updateViewModel.height.value.toString(),
                    onValueChange = { updateViewModel.height.value = it.toFloatOrNull() ?: 0f },
                    modifier = modifier,
                    textStyle = MaterialTheme.typography.bodySmall.copy(
                        textAlign = TextAlign.End
                    ),
                    prefix = {
                        Text(
                            text = stringResource(R.string.height),
                            style = MaterialTheme.typography.bodySmall
                        )
                    },
                    suffix = {
                        Text(
                            text = stringResource(R.string.cm),
                            style = MaterialTheme.typography.bodySmall
                        )
                    },
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
                    value = updateViewModel.weight.value.toString(),
                    onValueChange = { updateViewModel.weight.value = it.toFloatOrNull() ?: 0f },
                    modifier = modifier,
                    textStyle = MaterialTheme.typography.bodySmall.copy(
                        textAlign = TextAlign.End
                    ),
                    prefix = {
                        Text(
                            text = stringResource(R.string.weight),
                            style = MaterialTheme.typography.bodySmall
                        )
                    },
                    suffix = {
                        Text(
                            text = stringResource(R.string.kg),
                            style = MaterialTheme.typography.bodySmall
                        )
                    },
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
                    value = updateViewModel.email.value ?: "",
                    onValueChange = { updateViewModel.email.value = it },
                    modifier = modifier,
                    textStyle = MaterialTheme.typography.bodySmall.copy(
                        textAlign = TextAlign.End
                    ),
                    prefix = {
                        Text(
                            text = stringResource(R.string.email),
                            style = MaterialTheme.typography.bodySmall
                        )
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Go
                    ),
                    keyboardActions = KeyboardActions(onGo = { TODO() }),
                    singleLine = true,
                    shape = MaterialTheme.shapes.large
                )
            }
        }

        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                SubCard(
                    modifier = modifier
                ) {
                    Column(
                        modifier = Modifier.padding(standardPadding),
                        horizontalAlignment = Alignment.Start
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = stringResource(R.string.calorie_intake_target),
                                color = MaterialTheme.colorScheme.onBackground,
                                style = MaterialTheme.typography.titleSmall
                            )

                            IconButton(
                                onClick = { TODO() } // Xử lý sự kiện khi người dùng nhấn icon Info
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = stringResource(R.string.calorie_intake_target),
                                )
                            }
                        }

                        val textWithIcon = buildAnnotatedString {
                            append(
                                stringResource(
                                    R.string.estimate_calories_needed_for_daily_activities
                                ) + " "
                            )
                            appendInlineContent("fireIcon", "[icon]")
                        }

                        val inlineContent = mapOf(
                            "fireIcon" to InlineTextContent(
                                placeholder = Placeholder(
                                    width = 16.sp,
                                    height = 16.sp,
                                    placeholderVerticalAlign = PlaceholderVerticalAlign.Center
                                )
                            ) {
                                Image(
                                    painter = painterResource(R.drawable.ic_fire),
                                    contentDescription = stringResource(
                                        R.string.calorie_intake_target
                                    ),
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        )

                        Text(
                            text = textWithIcon,
                            modifier = Modifier.fillMaxWidth(),
                            color = MaterialTheme.colorScheme.outline,
                            inlineContent = inlineContent,
                            style = MaterialTheme.typography.bodySmall
                        )

                        Row(
                            modifier = Modifier.padding(top = standardPadding),
                            horizontalArrangement = Arrangement.spacedBy(standardPadding),
                        ) {
                            MainCard(
                                modifier = Modifier.weight(1f)
                            ) {

                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(standardPadding),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    val caloOfDaily by rememberSaveable {
                                        mutableStateOf(value = "__")
                                    } // Thay caloOfDaily từ database vào value

                                    Text(
                                        text = caloOfDaily,
                                        color = MaterialTheme.colorScheme.onPrimary,
                                        style = MaterialTheme.typography.titleMedium
                                    )

                                    Text(
                                        text = stringResource(R.string.calo_day),
                                        color = MaterialTheme.colorScheme.onPrimary,
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                            }

                            MainCard(
                                modifier = Modifier.weight(1f)
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(standardPadding),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    val caloOfWeekly by rememberSaveable {
                                        mutableStateOf(value = "__")
                                    } // Thay caloOfWeekly từ database vào value

                                    Text(
                                        text = caloOfWeekly,
                                        color = MaterialTheme.colorScheme.onPrimary,
                                        style = MaterialTheme.typography.titleMedium
                                    )

                                    Text(
                                        text = stringResource(R.string.calo_week),
                                        color = MaterialTheme.colorScheme.onPrimary,
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                            }
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = stringResource(R.string.basal_metabolic_rate_bmr),
                                color = MaterialTheme.colorScheme.outline,
                                style = MaterialTheme.typography.labelSmall
                            )

                            TextButton(
                                onClick = { TODO() },
                            ) {
                                Text(
                                    text = stringResource(R.string.learn_more),
                                    color = MaterialTheme.colorScheme.primary,
                                    textDecoration = TextDecoration.Underline,
                                    style = MaterialTheme.typography.labelSmall
                                )
                            }

                            val caloOfDailyBMR by rememberSaveable {
                                mutableStateOf(value = "__")
                            } // Thay caloOfDailyBMR từ database vào value

                            Text(
                                text = caloOfDailyBMR + " " +
                                        stringResource(R.string.calo_day),
                                modifier = Modifier.weight(1f),
                                color = MaterialTheme.colorScheme.outline,
                                textAlign = TextAlign.End,
                                style = MaterialTheme.typography.labelSmall
                            )
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = stringResource(R.string.total_energy_expenditure_tdee),
                                color = MaterialTheme.colorScheme.outline,
                                style = MaterialTheme.typography.labelSmall
                            )

                            TextButton(
                                onClick = { TODO() },
                            ) {
                                Text(
                                    text = stringResource(R.string.learn_more),
                                    color = MaterialTheme.colorScheme.primary,
                                    textDecoration = TextDecoration.Underline,
                                    style = MaterialTheme.typography.labelSmall
                                )
                            }

                            val caloOfWeeklyBMR by rememberSaveable {
                                mutableStateOf(value = "__")
                            } // Thay caloOfDailyBMR từ database vào value

                            Text(
                                text = caloOfWeeklyBMR + " " +
                                        stringResource(R.string.calo_week),
                                modifier = Modifier.weight(1f),
                                color = MaterialTheme.colorScheme.outline,
                                textAlign = TextAlign.End,
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                    }
                }
            }
        }

        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                SubCard(
                    modifier = modifier
                ) {
                    Column(
                        modifier = Modifier.padding(standardPadding),
                        horizontalAlignment = Alignment.Start
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = stringResource(R.string.bmi_index),
                                color = MaterialTheme.colorScheme.onBackground,
                                style = MaterialTheme.typography.titleSmall
                            )

                            IconButton(
                                onClick = { TODO() } // Xử lý sự kiện khi người dùng nhấn icon Info
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = stringResource(R.string.bmi_index),
                                )
                            }
                        }

                        val textWithIcon = buildAnnotatedString {
                            append(
                                stringResource(R.string.your_bmi_is) + " " +
                                        roundedBmi + ", " +
                                        stringResource(R.string.you_are_classified_as) + " " +
                                        bmiCategory + "."
                            )
                            appendInlineContent("fireIcon", "[icon]")
                        }

                        val inlineContent = mapOf(
                            "fireIcon" to InlineTextContent(
                                placeholder = Placeholder(
                                    width = 16.sp,
                                    height = 16.sp,
                                    placeholderVerticalAlign = PlaceholderVerticalAlign.Center
                                )
                            ) {
                                Image(
                                    painter = painterResource(R.drawable.ic_fire),
                                    contentDescription = stringResource(
                                        R.string.bmi_index
                                    ),
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        )

                        Text(
                            text = textWithIcon,
                            modifier = Modifier.fillMaxWidth(),
                            color = MaterialTheme.colorScheme.outline,
                            inlineContent = inlineContent,
                            style = MaterialTheme.typography.bodySmall
                        )

                        BMIBar(
                            bmi = roundedBmi,
                            standardPadding = standardPadding
                        )

                        MainCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = standardPadding)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(standardPadding),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(standardPadding),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = "Check Circle Icon",
                                        tint = MaterialTheme.colorScheme.inversePrimary,
                                    )

                                    val estimatedWeight by rememberSaveable {
                                        mutableStateOf(value = "__")
                                    } // Thay estimatedWeight từ database vào value

                                    Text(
                                        text = stringResource(R.string.your_best_weight_is_estimated_to_be) +
                                                estimatedWeight +
                                                stringResource(R.string.kg),
                                        color = MaterialTheme.colorScheme.onPrimary,
                                        style = MaterialTheme.typography.labelSmall
                                    )
                                }
                            }
                        }
                    }
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


@Composable
fun BMIBar(
    bmi: Float,
    standardPadding: Dp
) {
    val minBmi = 15f
    val maxBmi = 35f

    val bmiSegments = listOf(
        18.5f to Color(0xFFAEEA00),
        24.9f to Color(0xFF00C853),
        29.9f to Color(0xFFFFAB00),
        maxBmi to Color(0xFFDD2C00)
    )

    val bmiPercentage = ((bmi - minBmi) / (maxBmi - minBmi)).coerceIn(0f, 1f)

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {

        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Spacer(
                modifier =
                if (bmiPercentage != 0f) {
                    Modifier.weight(bmiPercentage)
                } else {
                    Modifier
                }
            )

            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = stringResource(R.string.bmi_index),
                tint = Color.Red
            )

            Spacer(
                modifier =
                if (bmiPercentage != 1f) {
                    Modifier.weight(1f - bmiPercentage)
                } else {
                    Modifier
                }
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = standardPadding),
            verticalAlignment = Alignment.CenterVertically
        ) {
            var accumulatedWeight = 0f
            val weightCategories = listOf(
                stringResource(R.string.underweight),
                stringResource(R.string.healthy_weight),
                stringResource(R.string.overweight),
                stringResource(R.string.obese)
            )

            bmiSegments.forEachIndexed { index, (threshold, color) ->
                val segmentWidthWeight =
                    ((threshold - (if (index == 0) {
                        minBmi
                    } else {
                        bmiSegments[index - 1].first
                    })) / (maxBmi - minBmi))

                accumulatedWeight += segmentWidthWeight

                Surface(
                    modifier = Modifier
                        .weight(segmentWidthWeight),
                    shape = when (index) {
                        0 -> MaterialTheme.shapes.extraLarge.copy(
                            topEnd = CornerSize(0.dp),
                            bottomEnd = CornerSize(0.dp)
                        )

                        bmiSegments.size - 1 -> MaterialTheme.shapes.extraLarge.copy(
                            topStart = CornerSize(0.dp),
                            bottomStart = CornerSize(0.dp)
                        )

                        else -> RectangleShape
                    },
                    color = color
                ) {
                    Text(
                        text = weightCategories[index],
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.scrim.copy(alpha = 0.75f),
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = standardPadding / 2)
        ) {
            val bmiLabels = listOf("15", "18.5", "24.9", "29.9", "35")

            bmiLabels.forEachIndexed { index, label ->
                val weightModifier = if (index != 0) {
                    val currentBmi = label.toFloat()
                    val previousBmi = bmiLabels[index - 1].toFloat()
                    val weight = ((currentBmi - previousBmi) / (maxBmi - minBmi)).coerceIn(0f, 1f)
                    Modifier.weight(weight)
                } else {
                    Modifier
                }

                Text(
                    text = label,
                    modifier = weightModifier,
                    color = MaterialTheme.colorScheme.outline,
                    textAlign = TextAlign.End,
                    style = MaterialTheme.typography.labelSmall
                )
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
private fun SettingPortraitScreenDarkModePreviewInSmallPhone() {
    BioFitTheme {
        SettingScreen(UserDTO.default())
    }
}

@Preview(
    device = "id:pixel_9_pro_xl",
    showSystemUi = true,
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
private fun SettingPortraitScreenPreviewInLargePhone() {
    BioFitTheme {
        SettingScreen(UserDTO.default())
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
private fun SettingPortraitScreenPreviewInTablet() {
    BioFitTheme {
        SettingScreen(UserDTO.default())
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
private fun SettingLandscapeScreenDarkModePreviewInSmallPhone() {
    BioFitTheme {
        SettingScreen(UserDTO.default())
    }
}

@Preview(
    device = "spec:parent=pixel_9_pro_xl,orientation=landscape",
    showSystemUi = true,
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
private fun SettingLandscapeScreenPreviewInLargePhone() {
    BioFitTheme {
        SettingScreen(UserDTO.default())
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
private fun SettingLandscapeScreenPreviewInTablet() {
    BioFitTheme {
        SettingScreen(UserDTO.default())
    }
}