package com.example.biofit.view

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.biofit.R
import com.example.biofit.ui.theme.BioFitTheme

class SettingActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BioFitTheme {
                SettingScreen()
            }
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        recreate()
    }
}

@Composable
fun SettingScreen() {
    val screenWidth = LocalConfiguration.current.screenWidthDp
    val screenHeight = LocalConfiguration.current.screenHeightDp
    val standardPadding = getStandardPadding().first
    val modifier = getStandardPadding().second

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
            TopBarSetting(
                onBackClick = { TODO() }, // Xử lý sự kiện khi người dùng nhấn nút Back
                title = R.string.setting,
                middleButton = null,
                rightButton = {
                    TextButton(
                        onClick = { TODO() } // Xử lý sự kiện khi người dùng nhấn nút Save
                    ) {
                        Text(
                            text = stringResource(R.string.save),
                            color = MaterialTheme.colorScheme.inverseSurface,
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                },
                standardPadding = standardPadding
            )
            SettingContent(
                screenWidth,
                screenHeight,
                standardPadding,
                modifier
            )
        }
    }
}

@Composable
fun TopBarSetting(
    onBackClick: () -> Unit,
    @StringRes title: Int? = null,
    middleButton: (@Composable () -> Unit)? = null,
    rightButton: (@Composable () -> Unit)? = null,
    standardPadding: Dp
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.weight(0.5f),
            horizontalAlignment = Alignment.Start
        ) {
            BackButton(
                onBackClick,
                standardPadding
            )
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                title?.let {
                    Text(
                        text = stringResource(title),
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.titleMedium
                    )
                }

                middleButton?.invoke()
            }
        }
        Column(
            modifier = Modifier.weight(0.5f),
            horizontalAlignment = Alignment.End
        ) {
            rightButton?.invoke()
        }
    }
}

@Composable
fun SettingContent(
    screenWidth: Int,
    screenHeight: Int,
    standardPadding: Dp,
    modifier: Modifier
) {
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
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                var userName by rememberSaveable { mutableStateOf(value = "User name") } // Thay tên người dùng từ database vào User name
                var gender by rememberSaveable { mutableIntStateOf(R.string.gender) } // Thay giới tính từ database vào Gender
                var showGenderDialog by rememberSaveable { mutableStateOf(false) }
                var dateOfBirth by rememberSaveable { mutableStateOf("dd / mm / yyyy") } // Thay ngày sinh từ database vào dd / mm / yyyy
                var height by rememberSaveable { mutableStateOf("hhh") } // Thay chiều cao từ database vào hhh
                var weight by rememberSaveable { mutableStateOf("ww") } // Thay cân nặng từ database vào ww
                var email by rememberSaveable { mutableStateOf(value = "biofit@example.com") } // Thay email từ database vào Email

                TextField(
                    value = userName,
                    onValueChange = { userName = it },
                    modifier = modifier,
                    textStyle = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.End
                    ),
                    prefix = {
                        Text(
                            text = stringResource(R.string.name),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.outline
                        )
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
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
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedTextColor = MaterialTheme.colorScheme.onBackground,
                        unfocusedLabelColor = MaterialTheme.colorScheme.onBackground,
                    ),
                )

                TextField(
                    value = stringResource(gender),
                    onValueChange = { gender = it.toInt() },
                    modifier = modifier,
                    readOnly = true,
                    textStyle = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.End
                    ),
                    prefix = {
                        Text(
                            text = stringResource(R.string.gender),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.outline
                        )
                    },
                    trailingIcon = {
                        IconButton(onClick = { showGenderDialog = true }) {
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = stringResource(R.string.gender),
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    keyboardActions = KeyboardActions(
                        onDone = { TODO() },
                        onGo = { TODO() },
                        onNext = { TODO() },
                        onPrevious = { TODO() },
                        onSearch = { TODO() },
                        onSend = { TODO() }
                    ),
                    singleLine = true,
                    maxLines = 1,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedTextColor = MaterialTheme.colorScheme.onBackground,
                        unfocusedLabelColor = MaterialTheme.colorScheme.onBackground,
                    ),
                )

                if (showGenderDialog) {
                    Dialog(
                        selectedOption = gender,
                        onOptionSelected = { selectedGender ->
                            gender = selectedGender
                            showGenderDialog = false
                        },
                        onDismissRequest = { showGenderDialog = false },
                        title = R.string.select_gender,
                        listOptions = listOf(
                            R.string.male,
                            R.string.female
                        ),
                        standardPadding = standardPadding
                    )
                }

                TextField(
                    value = dateOfBirth,
                    onValueChange = { dateOfBirth = it },
                    modifier = modifier,
                    readOnly = true,
                    textStyle = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.End
                    ),
                    prefix = {
                        Text(
                            text = stringResource(R.string.date_of_birth),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.outline
                        )
                    },
                    trailingIcon = {
                        IconButton(
                            onClick = { TODO() } // Xử lý sự kiện khi người dùng nhấn icon DateRange
                        ) {
                            Icon(
                                imageVector = Icons.Default.DateRange,
                                contentDescription = stringResource(R.string.date_of_birth),
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    },
                    keyboardActions = KeyboardActions(
                        onDone = { TODO() },
                        onGo = { TODO() },
                        onNext = { TODO() },
                        onPrevious = { TODO() },
                        onSearch = { TODO() },
                        onSend = { TODO() }
                    ),
                    singleLine = true,
                    maxLines = 1,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedTextColor = MaterialTheme.colorScheme.onBackground,
                        unfocusedLabelColor = MaterialTheme.colorScheme.onBackground,
                    )
                )

                TextField(
                    value = height,
                    onValueChange = { height = it },
                    modifier = modifier,
                    textStyle = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.End
                    ),
                    prefix = {
                        Text(
                            text = stringResource(R.string.height),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.outline
                        )
                    },
                    suffix = {
                        Text(
                            text = stringResource(R.string.cm),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    keyboardActions = KeyboardActions(
                        onDone = { TODO() },
                        onGo = { TODO() },
                        onNext = { TODO() },
                        onPrevious = { TODO() },
                        onSearch = { TODO() },
                        onSend = { TODO() }
                    ),
                    singleLine = true,
                    maxLines = 1,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedTextColor = MaterialTheme.colorScheme.onBackground,
                        unfocusedLabelColor = MaterialTheme.colorScheme.onBackground,
                    )
                )

                TextField(
                    value = weight,
                    onValueChange = { weight = it },
                    modifier = modifier,
                    textStyle = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.End
                    ),
                    prefix = {
                        Text(
                            text = stringResource(R.string.weight),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.outline
                        )
                    },
                    suffix = {
                        Text(
                            text = stringResource(R.string.kg),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    keyboardActions = KeyboardActions(
                        onDone = { TODO() },
                        onGo = { TODO() },
                        onNext = { TODO() },
                        onPrevious = { TODO() },
                        onSearch = { TODO() },
                        onSend = { TODO() }
                    ),
                    singleLine = true,
                    maxLines = 1,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedTextColor = MaterialTheme.colorScheme.onBackground,
                        unfocusedLabelColor = MaterialTheme.colorScheme.onBackground,
                    )
                )

                TextField(
                    value = email,
                    onValueChange = { email = it },
                    modifier = modifier,
                    textStyle = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.End
                    ),
                    prefix = {
                        Text(
                            text = stringResource(R.string.email),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.outline
                        )
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    keyboardActions = KeyboardActions(
                        onDone = { TODO() },
                        onGo = { TODO() },
                        onNext = { TODO() },
                        onPrevious = { TODO() },
                        onSearch = { TODO() },
                        onSend = { TODO() }
                    ),
                    singleLine = true,
                    maxLines = 1,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedTextColor = MaterialTheme.colorScheme.onBackground,
                        unfocusedLabelColor = MaterialTheme.colorScheme.onBackground,
                    )
                )
            }
        }

        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Card(
                    modifier = modifier,
                    shape = MaterialTheme.shapes.large,
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainerHighest
                    )
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
                            Card(
                                modifier = Modifier.weight(1f),
                                shape = MaterialTheme.shapes.large,
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.primary
                                )
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
                                        text = caloOfDaily.toString(),
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

                            Card(
                                modifier = Modifier.weight(1f),
                                shape = MaterialTheme.shapes.large,
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.primary
                                )
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
                                        text = caloOfWeekly.toString(),
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
                                text = caloOfDailyBMR.toString() + " " +
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
                                text = caloOfWeeklyBMR.toString() + " " +
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
                Card(
                    modifier = modifier,
                    shape = MaterialTheme.shapes.large,
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainerHighest
                    )
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

                        val bmiIndex by rememberSaveable {
                            mutableStateOf(value = "__")
                        } // Thay bmiIndex từ database vào value
                        val bmiCategory by rememberSaveable {
                            mutableStateOf(value = "__")
                        } // Thay bmiCategory từ database vào value

                        val textWithIcon = buildAnnotatedString {
                            append(
                                stringResource(R.string.your_bmi_is) + " " +
                                        bmiIndex.toString() + ", " +
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
                            18.5f,
                            standardPadding
                        ) // Thay bmiIndex từ database vào parameter 1

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = standardPadding),
                            shape = MaterialTheme.shapes.large,
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            )
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
                                        tint = Color(0xFF64DD17),
                                    )

                                    val estimatedWeight by rememberSaveable {
                                        mutableStateOf(value = "__")
                                    } // Thay estimatedWeight từ database vào value

                                    Text(
                                        text = stringResource(R.string.your_best_weight_is_estimated_to_be) +
                                                estimatedWeight.toString() +
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
fun Dialog(
    selectedOption: Int,
    onOptionSelected: (Int) -> Unit,
    onDismissRequest: () -> Unit,
    title: Int,
    listOptions: List<Int>,
    standardPadding: Dp
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(onClick = onDismissRequest) {
                Text(text = stringResource(R.string.ok))
            }
        },
        title = {
            Text(text = stringResource(title))
        },
        text = {
            Column {
                listOptions.forEach { option ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onOptionSelected(option)
                            }
                            .padding(vertical = standardPadding),
                    ) {
                        Text(
                            text = stringResource(option),
                            color = if (option == selectedOption) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.onBackground
                            }
                        )
                    }
                }
            }
        }
    )
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
                    color = color
                ) {
                    Text(
                        text = weightCategories[index],
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        color = Color.White,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 10.sp
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
        SettingScreen()
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
        SettingScreen()
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
        SettingScreen()
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
        SettingScreen()
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
        SettingScreen()
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
        SettingScreen()
    }
}