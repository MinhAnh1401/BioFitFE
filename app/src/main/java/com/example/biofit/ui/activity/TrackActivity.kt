package com.example.biofit.ui.activity

import android.app.Activity
import androidx.activity.viewModels
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.biofit.R
import com.example.biofit.data.model.dto.FoodDTO
import com.example.biofit.data.model.dto.FoodInfoDTO
import com.example.biofit.data.utils.UserSharedPrefsHelper
import com.example.biofit.ui.components.FoodItem
import com.example.biofit.ui.components.TopBar
import com.example.biofit.ui.components.getStandardPadding
import com.example.biofit.ui.theme.BioFitTheme
import com.example.biofit.view_model.FoodViewModel
import com.patrykandpatrick.vico.core.extension.sumOf
import java.math.RoundingMode
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TrackActivity : ComponentActivity() {
    private val foodViewModel: FoodViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val userId = UserSharedPrefsHelper.getUserData(this)?.userId ?: 0L
        foodViewModel.fetchFood(userId)
        enableEdgeToEdge()
        val initialSelectedOption = intent.getIntExtra("SESSION_TITLE", R.string.morning)
        setContent {
            BioFitTheme {
                TrackScreen(initialSelectedOption = initialSelectedOption,userId = userId)
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
fun TrackScreen(
    initialSelectedOption: Int,
    userId: Long
) {
    val context = LocalContext.current
    val activity = context as? Activity

    val standardPadding = getStandardPadding().first
    val modifier = getStandardPadding().second

    var expanded by remember { mutableStateOf(false) }
    var selectedOption by rememberSaveable { mutableIntStateOf(initialSelectedOption) }

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
            verticalArrangement = Arrangement.spacedBy(standardPadding),
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
                                            text = stringResource(selection),
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
                                                else -> painterResource(R.drawable.circle_hexagongrid_fill)
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
                rightButton = null,
                standardPadding = standardPadding
            )

            TrackContent(
                selectedOption = selectedOption,
                standardPadding = standardPadding,
                modifier = modifier,
                userId = userId
            )
        }
    }
}

@Composable
fun TrackContent(
    selectedOption: Int,
    standardPadding: Dp,
    modifier: Modifier,
    userId: Long,
    foodViewModel: FoodViewModel = viewModel()
) {
    val context = LocalContext.current
    val activity = context as? Activity

    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                foodViewModel.fetchFoodDoneList(userId, today)
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    val foodDoneList by foodViewModel.foodDoneList.collectAsState()
    val foodList by foodViewModel.foodList.collectAsState()

    val foodListInfoDTO = remember(foodDoneList, foodList) {
        val foodListDTO = foodDoneList.mapNotNull { foodDone ->
            foodList.find { it.foodId == foodDone.foodId }
        }
        foodListDTO.map { it.toFoodInfoDTO() }
    }

    LaunchedEffect(userId) {
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        foodViewModel.fetchFoodDoneList(userId, today)
    }

    val foodListMorning = foodListInfoDTO.filter { it.session.equals(stringResource(R.string.morning), ignoreCase = true) }
    val foodListAfternoon = foodListInfoDTO.filter { it.session.equals(stringResource(R.string.afternoon), ignoreCase = true) }
    val foodListEvening = foodListInfoDTO.filter { it.session.equals(stringResource(R.string.evening), ignoreCase = true) }
    val foodListSnack = foodListInfoDTO.filter { it.session.equals(stringResource(R.string.snack), ignoreCase = true) }

    Column(
        verticalArrangement = Arrangement.spacedBy(standardPadding),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(standardPadding * 2)
        ) {
            item {
                NutritionalComposition(
                    selectedOption = selectedOption,
                    standardPadding = standardPadding,
                    modifier = modifier,
                    foodListMorning = foodListMorning,
                    foodListAfternoon = foodListAfternoon,
                    foodListEvening = foodListEvening,
                    foodListSnack = foodListSnack

                )
            }

            item {
                HorizontalDivider(
                    modifier = modifier,
                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                )
            }

            item {
                MenuForSession(
                    selectedOption = selectedOption,
                    standardPadding = standardPadding,
                    modifier = modifier,
                    userId = userId
                )
            }
        }

        ElevatedButton(
            onClick = {
                activity?.let {
                    val intent = Intent(it, AddActivity::class.java)
                    intent.putExtra(
                        "SESSION",
                        when (selectedOption) {
                            R.string.morning -> R.string.morning
                            R.string.afternoon -> R.string.afternoon
                            R.string.evening -> R.string.evening
                            else -> R.string.snack
                        }
                    )
                    it.startActivity(intent)
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Text(
                text = stringResource(R.string.add_food),
                color = MaterialTheme.colorScheme.onPrimary
            )
        }

        Spacer(
            modifier = Modifier.padding(
                bottom = WindowInsets.safeDrawing.asPaddingValues().calculateBottomPadding()
            )
        )
    }
}

@Composable
fun NutritionalComposition(
    selectedOption: Int,
    standardPadding: Dp,
    modifier: Modifier,
    foodListMorning: List<FoodInfoDTO>,
    foodListAfternoon: List<FoodInfoDTO>,
    foodListEvening: List<FoodInfoDTO>,
    foodListSnack: List<FoodInfoDTO>
) {

    val food1 = foodListMorning.firstOrNull() ?: FoodDTO.default().toFoodInfoDTO()
    val food2 = foodListAfternoon.firstOrNull() ?: FoodDTO.default().toFoodInfoDTO()
    val food3 = foodListEvening.firstOrNull() ?: FoodDTO.default().toFoodInfoDTO()
    val food4 = foodListSnack.firstOrNull() ?: FoodDTO.default().toFoodInfoDTO()

    val morningMacroTable = listOf(
        Triple(
            food1.protein.first,
            food1.protein.second,
            foodListMorning.sumOf { it.protein.third }),
        Triple(
            food1.carbohydrate.first,
            food1.carbohydrate.second,
            foodListMorning.sumOf { it.carbohydrate.third }),
        Triple(food1.fat.first, food1.fat.second, foodListMorning.sumOf { it.fat.third })
    )

    val afternoonMacroTable = listOf(
        Triple(
            food2.protein.first,
            food2.protein.second,
            foodListAfternoon.sumOf { it.protein.third }),
        Triple(
            food2.carbohydrate.first,
            food2.carbohydrate.second,
            foodListAfternoon.sumOf { it.carbohydrate.third }),
        Triple(food2.fat.first, food2.fat.second, foodListAfternoon.sumOf { it.fat.third })
    )

    val eveningMacroTable = listOf(
        Triple(
            food3.protein.first,
            food3.protein.second,
            foodListEvening.sumOf { it.protein.third }),
        Triple(
            food3.carbohydrate.first,
            food3.carbohydrate.second,
            foodListEvening.sumOf { it.carbohydrate.third }),
        Triple(food3.fat.first, food3.fat.second, foodListEvening.sumOf { it.fat.third })
    )

    val snackMacroTable = listOf(
        Triple(food4.protein.first, food4.protein.second, foodListSnack.sumOf { it.protein.third }),
        Triple(
            food4.carbohydrate.first,
            food4.carbohydrate.second,
            foodListSnack.sumOf { it.carbohydrate.third }),
        Triple(food4.fat.first, food4.fat.second, foodListSnack.sumOf { it.fat.third })
    )

    val sessionMacroTable = when (selectedOption) {
        R.string.morning -> morningMacroTable
        R.string.afternoon -> afternoonMacroTable
        R.string.evening -> eveningMacroTable
        else -> snackMacroTable
    }

    val morningCal = foodListMorning.sumOf { it.calories }
    val afternoonCal = foodListAfternoon.sumOf { it.calories }
    val eveningCal = foodListEvening.sumOf { it.calories }
    val snackCal = foodListSnack.sumOf { it.calories }

    val loadedCal = when (selectedOption) {
        R.string.morning -> morningCal
        R.string.afternoon -> afternoonCal
        R.string.evening -> eveningCal
        else -> snackCal
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(standardPadding * 2)
    ) {
        Text(
            text = stringResource(R.string.nutritional_composition),
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleMedium
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(standardPadding),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(0.3f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                FoodCalorieChart(
                    sizeChart = 7f,
                    value = loadedCal,
                    chartColor = MaterialTheme.colorScheme.primary,
                    textColor = MaterialTheme.colorScheme.outline,
                    standardPadding = standardPadding
                )
            }

            Column(
                modifier = Modifier.weight(0.7f),
                verticalArrangement = Arrangement.spacedBy(standardPadding * 2)
            ) {
                sessionMacroTable.forEach { (icon, title, value) ->
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
                            text = "(${value.toBigDecimal().setScale(1, RoundingMode.HALF_UP)} g)",
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
fun MenuForSession(
    selectedOption: Int,
    standardPadding: Dp,
    modifier: Modifier,
    userId : Long,
    foodViewModel: FoodViewModel = viewModel(),
    ) {
    val context = LocalContext.current
    val activity = context as? Activity

    // Lấy danh sách món ăn đã ăn từ FoodViewModel
    val foodDoneList by foodViewModel.foodDoneList.collectAsState()
    val foodList by foodViewModel.foodList.collectAsState()

    // Tính toán foodListDTO và foodListInfoDTO trong LaunchedEffect để đảm bảo cập nhật khi foodDoneList hoặc foodList thay đổi
    val foodListInfoDTO = remember(foodDoneList, foodList) {
        val foodListDTO = foodDoneList.mapNotNull { foodDone ->
            foodList.find { it.foodId == foodDone.foodId }
        }
        foodListDTO.map { it.toFoodInfoDTO() }
    }

    LaunchedEffect(userId) {
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        foodViewModel.fetchFoodDoneList(userId, today)
    }

    Log.d("MenuForSession", "Food done list size: ${foodDoneList.size}")
    Log.d("MenuForSession", "Food info list size: ${foodListInfoDTO.size}")

    val foodListMorning = foodListInfoDTO.filter { it.session.equals(stringResource(R.string.morning), ignoreCase = true) }
    val foodListAfternoon = foodListInfoDTO.filter { it.session.equals(stringResource(R.string.afternoon), ignoreCase = true) }
    val foodListEvening = foodListInfoDTO.filter { it.session.equals(stringResource(R.string.evening), ignoreCase = true) }
    val foodListSnack = foodListInfoDTO.filter { it.session.equals(stringResource(R.string.snack), ignoreCase = true) }

    val meal = when (selectedOption) {
        R.string.morning -> R.string.breakfast
        R.string.afternoon -> R.string.lunch
        R.string.evening -> R.string.dinner
        else -> R.string.snack
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(standardPadding * 2)
    ) {
        Text(
            text = "${stringResource(R.string.menu_for)} ${stringResource(meal).lowercase()}",
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleMedium
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(standardPadding)
        ) {
            when (selectedOption) {
                R.string.morning ->
                    foodListMorning.forEachIndexed { index, food ->
                        var expanded by remember { mutableStateOf(false) }

                        Box {
                            FoodItem(
                                foodImg = foodListMorning[index].foodImage,
                                foodName = foodListMorning[index].foodName,
                                servingSize = Pair(
                                    foodListMorning[index].servingSize.first,
                                    foodListMorning[index].servingSize.second
                                ),
                                mass = foodListMorning[index].mass,
                                calories = foodListMorning[index].calories,
                                macros = listOf(
                                    Pair(
                                        foodListMorning[index].protein.first,
                                        foodListMorning[index].protein.third
                                    ),
                                    Pair(
                                        foodListMorning[index].carbohydrate.first,
                                        foodListMorning[index].carbohydrate.third
                                    ),
                                    Pair(
                                        foodListMorning[index].fat.first,
                                        foodListMorning[index].fat.third
                                    )
                                ),
                                onClick = {
                                    activity?.let {
                                        Log.d("MenuForSession", "Navigating to FoodDetailActivity with foodId: ${food.foodId}")
                                        val intent = Intent(it, FoodDetailActivity::class.java).apply {
                                            putExtra("FOOD_ID", food.foodId) // <- Đúng key FOOD_ID
                                        }
                                        it.startActivity(intent)
                                    }
                                },
                                onLongClick = { expanded = true },
                                onEatClick = null,
                                standardPadding = standardPadding
                            )

                            DropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false }
                            ) {
                                /*
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
                                 */

                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            text = stringResource(R.string.delete_food),
                                            color = Color(0xFFDD2C00)
                                        )
                                    },
                                    onClick = {
                                        val matchingFoodDone = foodDoneList.find { it.foodId == food.foodId }
                                        matchingFoodDone?.let {
                                            foodViewModel.deleteFoodDone(it.foodDoneId)
                                            Toast.makeText(
                                                context,
                                                context.getString(R.string.food_deleted_successfully),
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                        expanded = false
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

                R.string.afternoon ->
                    foodListAfternoon.forEachIndexed { index, food ->
                        var expanded by remember { mutableStateOf(false) }

                        Box {
                            FoodItem(
                                foodImg = foodListAfternoon[index].foodImage,
                                foodName = foodListAfternoon[index].foodName,
                                servingSize = Pair(
                                    foodListAfternoon[index].servingSize.first,
                                    foodListAfternoon[index].servingSize.second
                                ),
                                mass = foodListAfternoon[index].mass,
                                calories = foodListAfternoon[index].calories,
                                macros = listOf(
                                    Pair(
                                        foodListAfternoon[index].protein.first,
                                        foodListAfternoon[index].protein.third
                                    ),
                                    Pair(
                                        foodListAfternoon[index].carbohydrate.first,
                                        foodListAfternoon[index].carbohydrate.third
                                    ),
                                    Pair(
                                        foodListAfternoon[index].fat.first,
                                        foodListAfternoon[index].fat.third
                                    )
                                ),
                                onClick = {
                                    activity?.let {
                                        Log.d("MenuForSession", "Navigating to FoodDetailActivity with foodId: ${food.foodId}")
                                        val intent = Intent(it, FoodDetailActivity::class.java).apply {
                                            putExtra("FOOD_ID", food.foodId) // <- Đúng key FOOD_ID
                                        }
                                        it.startActivity(intent)
                                    }
                                },
                                onLongClick = { expanded = true },
                                onEatClick = null,
                                standardPadding = standardPadding
                            )

                            DropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false }
                            ) {
                                /*
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

                                 */
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            text = stringResource(R.string.delete_food),
                                            color = Color(0xFFDD2C00)
                                        )
                                    },
                                    onClick = {
                                        val matchingFoodDone = foodDoneList.find { it.foodId == food.foodId }
                                        matchingFoodDone?.let {
                                            foodViewModel.deleteFoodDone(it.foodDoneId)
                                            Toast.makeText(
                                                context,
                                                context.getString(R.string.food_deleted_successfully),
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                        expanded = false
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

                R.string.evening ->
                    foodListEvening.forEachIndexed { index, food ->
                        var expanded by remember { mutableStateOf(false) }

                        Box {
                            FoodItem(
                                foodImg = foodListEvening[index].foodImage,
                                foodName = foodListEvening[index].foodName,
                                servingSize = Pair(
                                    foodListEvening[index].servingSize.first,
                                    foodListEvening[index].servingSize.second
                                ),
                                mass = foodListEvening[index].mass,
                                calories = foodListEvening[index].calories,
                                macros = listOf(
                                    Pair(
                                        foodListEvening[index].protein.first,
                                        foodListEvening[index].protein.third
                                    ),
                                    Pair(
                                        foodListEvening[index].carbohydrate.first,
                                        foodListEvening[index].carbohydrate.third
                                    ),
                                    Pair(
                                        foodListEvening[index].fat.first,
                                        foodListEvening[index].fat.third
                                    )
                                ),
                                onClick = {
                                    activity?.let {
                                        Log.d("MenuForSession", "Navigating to FoodDetailActivity with foodId: ${food.foodId}")
                                        val intent = Intent(it, FoodDetailActivity::class.java).apply {
                                            putExtra("FOOD_ID", food.foodId) // <- Đúng key FOOD_ID
                                        }
                                        it.startActivity(intent)
                                    }
                                },
                                onLongClick = { expanded = true },
                                onEatClick = null,
                                standardPadding = standardPadding
                            )

                            DropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false }
                            ) {
                                /*
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
                                 */

                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            text = stringResource(R.string.delete_food),
                                            color = Color(0xFFDD2C00)
                                        )
                                    },
                                    onClick = {
                                        val matchingFoodDone = foodDoneList.find { it.foodId == food.foodId }
                                        matchingFoodDone?.let {
                                            foodViewModel.deleteFoodDone(it.foodDoneId)
                                            Toast.makeText(
                                                context,
                                                context.getString(R.string.food_deleted_successfully),
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                        expanded = false
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

                else ->
                    foodListSnack.forEachIndexed { index, food ->
                        var expanded by remember { mutableStateOf(false) }

                        Box {
                            FoodItem(
                                foodImg = foodListSnack[index].foodImage,
                                foodName = foodListSnack[index].foodName,
                                servingSize = Pair(
                                    foodListSnack[index].servingSize.first,
                                    foodListSnack[index].servingSize.second
                                ),
                                mass = foodListSnack[index].mass,
                                calories = foodListSnack[index].calories,
                                macros = listOf(
                                    Pair(
                                        foodListSnack[index].protein.first,
                                        foodListSnack[index].protein.third
                                    ),
                                    Pair(
                                        foodListSnack[index].carbohydrate.first,
                                        foodListSnack[index].carbohydrate.third
                                    ),
                                    Pair(
                                        foodListSnack[index].fat.first,
                                        foodListSnack[index].fat.third
                                    )
                                ),
                                onClick = {
                                    activity?.let {
                                        Log.d("MenuForSession", "Navigating to FoodDetailActivity with foodId: ${food.foodId}")
                                        val intent = Intent(it, FoodDetailActivity::class.java).apply {
                                            putExtra("FOOD_ID", food.foodId) // <- Đúng key FOOD_ID
                                        }
                                        it.startActivity(intent)
                                    }
                                },
                                onEatClick = null,
                                onLongClick = { expanded = true },
                                standardPadding = standardPadding
                            )
                            /*
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
                             */

                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            text = stringResource(R.string.delete_food),
                                            color = Color(0xFFDD2C00)
                                        )
                                    },
                                    onClick = {
                                        val matchingFoodDone = foodDoneList.find { it.foodId == food.foodId }
                                        matchingFoodDone?.let {
                                            foodViewModel.deleteFoodDone(it.foodDoneId)
                                            Toast.makeText(
                                                context,
                                                context.getString(R.string.food_deleted_successfully),
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                        expanded = false
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
    }
@Preview(
    device = "id:pixel",
    showSystemUi = true,
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL,
    locale = "vi"
)
@Composable
private fun TrackScreenDarkModePreviewInSmallPhone() {
    BioFitTheme {
        TrackScreen(initialSelectedOption = R.string.morning, userId = 0)
    }
}

@Preview(
    device = "id:pixel_9_pro_xl",
    showSystemUi = true,
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL,
)
@Composable
private fun TrackScreenPreviewInLargePhone() {
    BioFitTheme {
        TrackScreen(initialSelectedOption = R.string.morning,userId = 0)
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
private fun TrackScreenPreviewInTablet() {
    BioFitTheme {
        TrackScreen(initialSelectedOption = R.string.morning, userId = 0)
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
private fun TrackScreenLandscapeDarkModePreviewInSmallPhone() {
    BioFitTheme {
        TrackScreen(initialSelectedOption = R.string.morning,userId = 0)
    }
}

@Preview(
    device = "spec:parent=pixel_9_pro_xl,orientation=landscape",
    showSystemUi = true,
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
private fun TrackScreenLandscapePreviewInLargePhone() {
    BioFitTheme {
        TrackScreen(initialSelectedOption = R.string.morning,userId = 0)
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
private fun TrackScreenLandscapePreviewInTablet() {
    BioFitTheme {
        TrackScreen(initialSelectedOption = R.string.morning,userId = 0)
    }
}