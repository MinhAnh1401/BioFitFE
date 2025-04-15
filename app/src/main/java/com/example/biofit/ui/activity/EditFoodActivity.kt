package com.example.biofit.ui.activity

import android.app.Activity
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.biofit.data.model.dto.FoodDTO
import com.example.biofit.data.utils.UserSharedPrefsHelper
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.biofit.R
import com.example.biofit.ui.components.SelectionDialog
import com.example.biofit.ui.components.TopBar
import com.example.biofit.ui.components.getStandardPadding
import com.example.biofit.ui.screen.base64ToBitmap
import com.example.biofit.ui.theme.BioFitTheme
import com.example.biofit.view_model.FoodViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.getValue

class EditFoodActivity : ComponentActivity() {
    private val foodViewModel: FoodViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val foodId = intent.getLongExtra("foodId", -1)  // Lấy foodId
        Log.d("EditFoodActivity", "onCreate - foodId: $foodId")
        val userId = UserSharedPrefsHelper.getUserData(this)?.userId ?: 0L
        foodViewModel.fetchFood(userId)
        setContent {
            BioFitTheme {
                EditFoodScreen(foodId = foodId ,userId = userId)
            }
        }
    }
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        recreate()
    }
}

@Composable
fun EditFoodScreen(
    foodId: Long,
    foodViewModel: FoodViewModel = viewModel(),
    userId: Long,
) {
    val context = LocalContext.current
    val activity = context as? Activity

    LaunchedEffect(Unit) {
        foodViewModel.fetchFood(userId)
    }

    val foodList by foodViewModel.foodList.collectAsState()
    val food = foodList.find { it.foodId == foodId }
    val foodImageBitmap by foodViewModel.foodImageBitmap.collectAsState()
    fun mapSessionToResId(session: String): Int {
        return when (session.lowercase()) {
            "morning" -> R.string.morning
            "afternoon" -> R.string.afternoon
            "evening" -> R.string.evening
            "snack" -> R.string.snack
            else -> R.string.morning // fallback
        }
    }
    val sessionResId = mapSessionToResId(food?.session ?: "morning")
    val sessionString = stringResource(id = sessionResId)

    val sessionInit = food?.session ?: ""
    var session by remember(food) { mutableStateOf(sessionInit) }
    var foodName by remember { mutableStateOf("") }
    var servingSize by remember { mutableStateOf("") }
    var mass by remember { mutableStateOf("") }
    val defaultMass = stringResource(R.string.gram)
    var selectedUnitMeasure by remember { mutableStateOf(defaultMass) }
    var showUnitDialog by remember { mutableStateOf(false) }
    var calories by remember { mutableStateOf("") }
    var protein by remember { mutableStateOf("") }
    var carbohydrate by remember { mutableStateOf("") }
    var fat by remember { mutableStateOf("") }
    var sodium by remember { mutableStateOf("") }

    LaunchedEffect(food) {
        food?.let {
            session = it.session ?: ""
            foodName = it.foodName
            servingSize = it.servingSize.toString()
            mass = it.mass.toString()
            selectedUnitMeasure = food?.servingSizeUnit ?: defaultMass
            calories = it.calories.toString()
            protein = it.protein.toString()
            carbohydrate = it.carbohydrate.toString()
            fat = it.fat.toString()
            sodium = it.sodium.toString()
            foodViewModel.setFoodImage(base64ToBitmap(it.foodImage) ?: BitmapFactory.decodeResource(context.resources, R.drawable.img_food_default))
        }
    }

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
                title = stringResource(R.string.edit_food),
                middleButton = null,
                rightButton = null,
                standardPadding = standardPadding
            )

            EditFoodContent(
                userId = userId,
                session = session,
                onSessionChange = { session = it },
                foodViewModel = foodViewModel,
                foodId = foodId,
                standardPadding = standardPadding,
                modifier = modifier,
                foodName = foodName,
                servingSize = servingSize,
                mass = mass,
                selectedUnitMeasure = selectedUnitMeasure,
                showUnitDialog = showUnitDialog,
                calories = calories,
                protein = protein,
                carbohydrate = carbohydrate,
                fat = fat,
                sodium = sodium,
                foodImage = foodImageBitmap,
                onFoodNameChange = { foodName = it },
                onServingSizeChange = { servingSize = it },
                onMassChange = { mass = it },
                onSelectedUnitMeasureChange = { selectedUnitMeasure = it },
                onShowUnitDialogChange = { showUnitDialog = it },
                onCaloriesChange = { calories = it },
                onProteinChange = { protein = it },
                onCarbohydrateChange = { carbohydrate = it },
                onFatChange = { fat = it },
                onSodiumChange = { sodium = it }
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun EditFoodContent(
    foodId: Long,
    session: String,
    onSessionChange: (String) -> Unit,
    userId: Long,
    foodViewModel: FoodViewModel,
    standardPadding: Dp,
    modifier: Modifier,
    foodName: String,
    servingSize: String,
    mass: String,
    selectedUnitMeasure: String,
    showUnitDialog: Boolean,
    calories: String,
    protein: String,
    carbohydrate: String,
    fat: String,
    sodium: String,
    foodImage: Bitmap?, // Thêm tham số foodImage
    onFoodNameChange: (String) -> Unit,
    onServingSizeChange: (String) -> Unit,
    onMassChange: (String) -> Unit,
    onSelectedUnitMeasureChange: (String) -> Unit,
    onShowUnitDialogChange: (Boolean) -> Unit,
    onCaloriesChange: (String) -> Unit,
    onProteinChange: (String) -> Unit,
    onCarbohydrateChange: (String) -> Unit,
    onFatChange: (String) -> Unit,
    onSodiumChange: (String) -> Unit
) {
    val context = LocalContext.current
    val activity = context as? Activity
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val currentDate = LocalDate.now().format(formatter)

    val focusManager = LocalFocusManager.current

    val foodViewModel: FoodViewModel = viewModel()
    val updateFoodResult by foodViewModel.updateFoodResult.collectAsState()
    var showFoodImageDialog by rememberSaveable { mutableStateOf(false) }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview(),
        onResult = { bitmap ->
            bitmap?.let {
                foodViewModel.setFoodImage(it)
            }
        }
    )

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            uri?.let {
                val source = ImageDecoder.createSource(context.contentResolver, it)
                val bitmap = ImageDecoder.decodeBitmap(source)
                foodViewModel.setFoodImage(bitmap)
            }
        }
    )

    // Lắng nghe kết quả cập nhật
    LaunchedEffect(updateFoodResult) {
        updateFoodResult?.let { result ->
            when {
                result.isSuccess -> {
                    Toast.makeText(context, "Cập nhật món ăn thành công!", Toast.LENGTH_SHORT).show()
                    activity?.finish()
                }
                result.isFailure -> {
                    Toast.makeText(context, "Cập nhật thất bại: ${result.exceptionOrNull()?.message}", Toast.LENGTH_SHORT).show()
                }
            }
            foodViewModel.resetUpdateFoodResult()
        }
    }

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(standardPadding * 2),
    ) {
        item {
            Column(
                modifier = modifier.padding(top = standardPadding),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = if (foodImage != null) {
                        BitmapPainter(foodImage.asImageBitmap())
                    }else {
                        painterResource(R.drawable.img_food_default)
                    },
                    contentDescription = "Food image",
                    modifier = Modifier
                        .size(standardPadding * 15)
                        .clip(MaterialTheme.shapes.extraLarge)
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.25f),
                            shape = MaterialTheme.shapes.extraLarge
                        )
                        .clickable { showFoodImageDialog = true },
                    contentScale = ContentScale.Crop
                )

                if (showFoodImageDialog) {
                    SelectionDialog(
                        selectedOption = null,
                        onOptionSelected = { option ->
                            showFoodImageDialog = false
                            when (option) {
                                context.getString(R.string.take_a_photo) -> cameraLauncher.launch(null)

                                context.getString(R.string.choose_from_gallery) -> galleryLauncher.launch("image/*")
                            }
                        },
                        onDismissRequest = { showFoodImageDialog = false },
                        title = R.string.choose_how_to_set_food_image,
                        listOptions = listOf(
                            stringResource(R.string.take_a_photo),
                            stringResource(R.string.choose_from_gallery),
                        ),
                        standardPadding = standardPadding
                    )
                }

                TextButton(
                    onClick = { galleryLauncher.launch("image/*") }
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
                    style = MaterialTheme.typography.titleMedium
                )

                Column(
                    modifier = modifier,
                    verticalArrangement = Arrangement.spacedBy(standardPadding)
                ) {
                    OutlinedTextField(
                        value = foodName,
                        onValueChange = onFoodNameChange,
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
                        onValueChange = onServingSizeChange,
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
                        onValueChange = onMassChange,
                        modifier = modifier,
                        textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.End),
                        prefix = { Text(text = stringResource(R.string.mass)) },
                        trailingIcon = {
                            IconButton(onClick = { onShowUnitDialogChange(true) }) {
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
                                onSelectedUnitMeasureChange(selectedUnit) // Cập nhật đơn vị đo lường
                                onShowUnitDialogChange(false) // Đóng dialog đúng cách
                            },
                            onDismissRequest = { onShowUnitDialogChange(false) },
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
                        onValueChange = onCaloriesChange,
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
                        onValueChange = onProteinChange,
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
                        value = carbohydrate,
                        onValueChange = onCarbohydrateChange,
                        modifier = modifier,
                        textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.End),
                        prefix = { Text(text = stringResource(R.string.carbohydrate)) },
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
                        onValueChange = onFatChange,
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
                        onValueChange = onSodiumChange,
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
                    onClick = {
                        val updatedFood = FoodDTO(
                            session = session,
                            foodId = foodId,
                            userId = userId,
                            foodName = foodName,
                            date = currentDate,
                            servingSize = servingSize.toFloatOrNull() ?: 0f,
                            servingSizeUnit = selectedUnitMeasure,
                            mass = mass.toFloatOrNull() ?: 0f,
                            calories = calories.toFloatOrNull() ?: 0f,
                            protein = protein.toFloatOrNull() ?: 0f,
                            carbohydrate = carbohydrate.toFloatOrNull() ?: 0f,
                            fat = fat.toFloatOrNull() ?: 0f,
                            sodium = sodium.toFloatOrNull() ?: 0f,
                        )
                        Log.d("EditFood", "Lưu foodId=$foodId với session=$session")
                        foodViewModel.updateFood(updatedFood, context)
                        activity?.finish()
                    },
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
private fun EditFoodScreenDarkModePreviewInSmallPhone() {
    BioFitTheme {
        EditFoodScreen(foodId = 0, userId = 0)
    }
}

@Preview(
    device = "id:pixel_9_pro_xl",
    showSystemUi = true,
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL,
)
@Composable
private fun EditFoodScreenPreviewInLargePhone() {
    BioFitTheme {
        EditFoodScreen(foodId = 0, userId = 0)
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
private fun EditFoodScreenPreviewInTablet() {
    BioFitTheme {
        EditFoodScreen(foodId = 0, userId = 0)
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
private fun EditFoodScreenLandscapeDarkModePreviewInSmallPhone() {
    BioFitTheme {
        EditFoodScreen(foodId = 0, userId = 0)
    }
}

@Preview(
    device = "spec:parent=pixel_9_pro_xl,orientation=landscape",
    showSystemUi = true,
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
private fun EditFoodScreenLandscapePreviewInLargePhone() {
    BioFitTheme {
        EditFoodScreen(foodId = 0, userId = 0)
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
private fun EditFoodScreenLandscapePreviewInTablet() {
    BioFitTheme {
        EditFoodScreen(foodId = 0, userId = 0)
    }
}