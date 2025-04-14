package com.example.biofit.ui.activity

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.biofit.R
import com.example.biofit.data.utils.UserSharedPrefsHelper
import com.example.biofit.ui.components.FoodItem
import com.example.biofit.ui.components.ToggleButtonBar
import com.example.biofit.ui.components.TopBar
import com.example.biofit.ui.components.getStandardPadding
import com.example.biofit.ui.theme.BioFitTheme
import com.example.biofit.view_model.FoodViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import com.example.biofit.data.model.dto.FoodDoneDTO
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.collections.component1
import kotlin.collections.component2

class AddActivity : ComponentActivity() {
    private val foodViewModel: FoodViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val session = intent.getIntExtra("SESSION", -1)
        setContent {
            BioFitTheme {
                AddScreen(session)
            }
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        recreate()
    }

    override fun onResume() {
        super.onResume()
        val userId = UserSharedPrefsHelper.getUserData(this)?.userId ?: 0L
        foodViewModel.fetchFood(userId)
    }
}

@Composable
fun AddScreen(
    session: Int?,
    foodViewModel: FoodViewModel = viewModel()
) {
    val context = LocalContext.current
    val activity = context as? Activity

    val standardPadding = getStandardPadding().first
    val modifier = getStandardPadding().second

    var expanded by remember { mutableStateOf(false) }

    val foodList by foodViewModel.foodList.collectAsState()
    Log.d("Session", "Default option: ${foodList.firstOrNull()?.session}")
    /*foodList.firstOrNull()?.session?.toIntOrNull() ?: R.string.morning*/
    val defaultOption = session
    var selectedOption by remember { mutableIntStateOf(defaultOption ?: -1) }

    val options = listOf(
        R.string.morning,
        R.string.afternoon,
        R.string.evening,
        R.string.snack
    )
    val filteredOptions = options.filter { it != selectedOption }


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
            TopBar(
                onBackClick = { activity?.finish() },
                title = stringResource(selectedOption),
                middleButton = {
                    Box {
                        IconButton(
                            onClick = { expanded = !expanded }
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_back),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(standardPadding)
                                    .rotate(270f),
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }

                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            filteredOptions.forEach { selection ->
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            text = stringResource(id = selection),
                                            color = when (selection) {
                                                R.string.morning -> Color(0xFFFFAB00)
                                                R.string.afternoon -> Color(0xFFDD2C00)
                                                R.string.evening -> Color(0xFF2962FF)
                                                else -> Color(0xFF00BFA5)
                                            }
                                        )
                                    },
                                    onClick = {
                                        selectedOption = selection
                                        expanded = false
                                    },
                                    leadingIcon = {
                                        Icon(
                                            painter = when (selection) {
                                                R.string.morning -> painterResource(R.drawable.cloud_sun_fill)
                                                R.string.afternoon -> painterResource(R.drawable.sun_max_fill)
                                                R.string.evening -> painterResource(R.drawable.cloud_moon_fill)
                                                else -> painterResource(R.drawable.popcorn_fill)
                                            },
                                            contentDescription = stringResource(R.string.morning),
                                            modifier = Modifier.size(standardPadding * 1.5f),
                                            tint = when (selection) {
                                                R.string.morning -> Color(0xFFFFAB00)
                                                R.string.afternoon -> Color(0xFFDD2C00)
                                                R.string.evening -> Color(0xFF2962FF)
                                                else -> Color(0xFF00BFA5)
                                            }
                                        )
                                    }
                                )
                            }
                        }
                    }
                },
                rightButton = {
                    IconButton(
                        onClick = {
                            activity?.let {
                                val intent = Intent(it, CreateFoodActivity::class.java).apply {
                                    putExtra("SESSION", selectedOption) // ✅ Truyền buổi ăn
                                }
                                it.startActivity(intent)
                            }
                        }
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_plus),
                            contentDescription = "Add button",
                            modifier = Modifier.size(standardPadding * 1.5f),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                standardPadding = standardPadding
            )

            AddContent(
                selectedOption = selectedOption,
                standardPadding = standardPadding,
                modifier = modifier
            )
        }
    }
}

fun base64ToBitmap(base64: String?): Bitmap? {
    return try {
        base64?.let {
            val decodedBytes = Base64.decode(it, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
        }
    } catch (e: Exception) {
        null // Trả về null nếu lỗi
    }
}

@Composable
fun AddContent(
    selectedOption: Int,
    standardPadding: Dp,
    modifier: Modifier,
    foodViewModel: FoodViewModel = viewModel()
) {
    val context = LocalContext.current
    val activity = context as? Activity

    val userId = UserSharedPrefsHelper.getUserData(context)?.userId ?: 0L

    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                foodViewModel.fetchFood(userId)
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    val foodListDTO by foodViewModel.foodList.collectAsState()
    // Chuyển đổi danh sách FoodDTO thành FoodInfoDTO

    val foodListInfoDTO = remember(foodListDTO) {
        foodListDTO.map { it.toFoodInfoDTO() }
    }

    LaunchedEffect(foodListDTO) {
        foodViewModel.fetchFood(userId)
    }

    var search by remember { mutableStateOf("") }

    OutlinedTextField(
        value = search,
        onValueChange = { newValue ->
            search = newValue
        },
        modifier = modifier.padding(vertical = standardPadding),
        placeholder = { Text(text = stringResource(id = R.string.search)) },
        trailingIcon = {
            if (search.isEmpty()) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = stringResource(R.string.search),
                    tint = MaterialTheme.colorScheme.onBackground
                )
            } else {
                IconButton(
                    onClick = { search = "" }
                ) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = stringResource(R.string.delete),
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        singleLine = true,
        shape = MaterialTheme.shapes.large,
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
            unfocusedBorderColor = Color.Transparent
        )
    )

    val foodListCreateMorning = foodListInfoDTO.filter {
        it.session.equals(
            stringResource(R.string.morning),
            ignoreCase = true
        )
    }
    val foodListCreateAfternoon = foodListInfoDTO.filter {
        it.session.equals(
            stringResource(R.string.afternoon),
            ignoreCase = true
        )
    }
    val foodListCreateEvening = foodListInfoDTO.filter {
        it.session.equals(
            stringResource(R.string.evening),
            ignoreCase = true
        )
    }
    val foodListCreateSnack = foodListInfoDTO.filter {
        it.session.equals(
            stringResource(R.string.snack),
            ignoreCase = true
        )
    }


    val foodListCreate = when (selectedOption) {
        R.string.morning -> foodListCreateMorning
        R.string.afternoon -> foodListCreateAfternoon
        R.string.evening -> foodListCreateEvening
        else -> foodListCreateSnack
    }

    val filteredList = if (search.isEmpty()) {
        foodListCreate
    } else {
        foodListCreate.filter { it.foodName.contains(search, ignoreCase = true) }
    }

    val groupedFoods = filteredList
        .sortedBy { it.foodName }
        .groupBy { it.foodName.first().uppercaseChar() }

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(standardPadding)
    ) {
        item {
            Log.d("FoodList", "foodListCreate: $foodListCreate")
            when (groupedFoods.isNotEmpty()) {
                true ->
                    Column(
                        modifier = modifier,
                        verticalArrangement = Arrangement.spacedBy(standardPadding)
                    ) {
                        groupedFoods.forEach { (letter, foods) ->
                            Text(
                                text = letter.toString(),
                                modifier = modifier.padding(top = standardPadding * 2),
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.displaySmall,
                            )

                            foods.forEach { food ->
                                var expanded by remember { mutableStateOf(false) }

                                Box {
                                    FoodItem(
                                        foodImg = food.foodImage,
                                        foodName = food.foodName,
                                        servingSize = Pair(
                                            food.servingSize.first,
                                            stringResource(R.string.serving_size)
                                        ),
                                        mass = food.mass,
                                        calories = food.calories,
                                        macros = listOf(
                                            Pair(
                                                food.protein.first,
                                                food.protein.third
                                            ),
                                            Pair(
                                                food.carbohydrate.first,
                                                food.carbohydrate.third
                                            ),
                                            Pair(
                                                food.fat.first,
                                                food.fat.third
                                            )
                                        ),
                                        onClick = {
                                            activity?.let {
                                                val intent =
                                                    Intent(
                                                        it,
                                                        FoodDetailActivity::class.java
                                                    )
                                                val foodId = food.foodId
                                                Log.d("FoodItem", "Clicked foodId: $foodId")
                                                intent.putExtra("FOOD_ID", foodId)
                                                it.startActivity(intent)
                                            }
                                        },
                                        onEatClick = {
                                            val currentDate = LocalDate.now()
                                                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                                            // Lấy đúng currentFood từ danh sách chuẩn foodListRecent
                                            val selectedFoodId =
                                                food.foodId
                                            val currentFood =
                                                foodListCreate.find { it.foodId == selectedFoodId }

                                            if (currentFood != null) {
                                                // Kiểm tra xem món ăn đã được thêm vào ngày hôm nay chưa
                                                val existingFood =
                                                    foodViewModel.foodDoneList.value.any {
                                                        it.foodId == currentFood.foodId &&
                                                                it.date == currentDate &&
                                                                it.session.equals(
                                                                    currentFood.session,
                                                                    ignoreCase = true
                                                                )
                                                    }

                                                if (existingFood) {
                                                    // Nếu món ăn đã được thêm, không cho phép thêm lại và thông báo
                                                    Toast.makeText(
                                                        context,
                                                        "Món này đã được thêm vào hôm nay rồi!",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                } else {
                                                    // Nếu món ăn chưa được thêm vào ngày hôm nay, tiến hành thêm món ăn mới vào danh sách
                                                    val foodDoneDTO =
                                                        FoodDoneDTO.default().copy(
                                                            foodDoneId = 0,
                                                            foodId = currentFood.foodId,
                                                            date = currentDate,
                                                            session = currentFood.session
                                                        )

                                                    // Ghi log thông tin về món ăn mới được thêm vào
                                                    Log.d(
                                                        "DEBUG",
                                                        "Creating foodDone: $foodDoneDTO"
                                                    )

                                                    // Thêm món ăn vào danh sách
                                                    foodViewModel.createFoodDone(foodDoneDTO)

                                                    // Thông báo món ăn đã được thêm vào danh sách hôm nay
                                                    Toast.makeText(
                                                        context,
                                                        "Đã thêm món ăn vào danh sách hôm nay!",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                }
                                            } else {
                                                // Thông báo nếu không tìm thấy món ăn trong danh sách
                                                Toast.makeText(
                                                    context,
                                                    "Không tìm thấy món ăn!",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                        },
                                        onLongClick = { expanded = true },
                                        standardPadding = standardPadding
                                    )

                                    DropdownMenu(
                                        expanded = expanded,
                                        onDismissRequest = { expanded = false }
                                    ) {
                                        DropdownMenuItem(
                                            text = {
                                                Text(
                                                    text = stringResource(R.string.edit_food),
                                                    color = Color(0xFFFF6D00)
                                                )
                                            },
                                            onClick = {
                                                activity?.let {
                                                    val intent =
                                                        Intent(
                                                            it,
                                                            EditFoodActivity::class.java
                                                        ).apply {
                                                            putExtra("foodId", food.foodId)
                                                        }
                                                    it.startActivity(intent)
                                                }
                                                expanded = false
                                            },
                                            leadingIcon = {
                                                Icon(
                                                    painter = painterResource(R.drawable.ic_edit),
                                                    contentDescription = stringResource(R.string.edit_food),
                                                    modifier = Modifier.size(standardPadding * 1.5f),
                                                    tint = Color(0xFFFF6D00)
                                                )
                                            }
                                        )

                                        DropdownMenuItem(
                                            text = {
                                                Text(
                                                    text = stringResource(R.string.delete_food),
                                                    color = Color(0xFFDD2C00)
                                                )
                                            },
                                            onClick = {
                                                Log.d(
                                                    "FoodListScreen",
                                                    "Deleting food: ${food.foodId}"
                                                )
                                                foodViewModel.deleteFood(food.foodId)
                                                expanded = false
                                                Toast.makeText(
                                                    context,
                                                    context.getString(R.string.food_deleted_successfully),
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            },
                                            leadingIcon = {
                                                Icon(
                                                    painter = painterResource(R.drawable.trash),
                                                    contentDescription = stringResource(R.string.delete_food),
                                                    modifier = Modifier.size(standardPadding * 1.5f),
                                                    tint = Color(0xFFDD2C00)
                                                )
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }

                else -> EmptyAddScreen(
                    standardPadding = standardPadding,
                    modifier = modifier,
                    selectedOption = selectedOption
                )

            }
        }

        item {
            Spacer(
                modifier = Modifier.padding(
                    bottom = WindowInsets.safeDrawing.asPaddingValues()
                        .calculateBottomPadding() * 2
                            + standardPadding
                )
            )
        }
    }
}

@Composable
fun EmptyAddScreen(
    standardPadding: Dp,
    modifier: Modifier,
    selectedOption: Int
) {
    val context = LocalContext.current
    val activity = context as? Activity

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
                onClick = {
                    activity?.let {
                        val intent = Intent(it, CreateFoodActivity::class.java).apply {
                            putExtra("SESSION", selectedOption) // Truyền buổi ăn
                        }
                        it.startActivity(intent)
                    }
                },
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(standardPadding / 2),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_plus),
                        contentDescription = "Add button",
                        modifier = Modifier.size(standardPadding),
                        tint = MaterialTheme.colorScheme.primary
                    )

                    Text(
                        text = stringResource(R.string.create_new),
                        color = MaterialTheme.colorScheme.primary
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
private fun AddScreenDarkModePreviewInSmallPhone() {
    BioFitTheme {
        AddScreen(R.string.morning)
    }
}

@Preview(
    device = "id:pixel_9_pro_xl",
    showSystemUi = true,
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL,
)
@Composable
private fun AddScreenPreviewInLargePhone() {
    BioFitTheme {
        AddScreen(R.string.morning)
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
private fun AddScreenPreviewInTablet() {
    BioFitTheme {
        AddScreen(R.string.morning)
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
private fun AddScreenLandscapeDarkModePreviewInSmallPhone() {
    BioFitTheme {
        AddScreen(R.string.morning)
    }
}

@Preview(
    device = "spec:parent=pixel_9_pro_xl,orientation=landscape",
    showSystemUi = true,
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
private fun AddScreenLandscapePreviewInLargePhone() {
    BioFitTheme {
        AddScreen(R.string.morning)
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
private fun AddScreenLandscapePreviewInTablet() {
    BioFitTheme {
        AddScreen(R.string.morning)
    }
}