package com.example.biofit.ui.components

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.biofit.R
import com.example.biofit.data.model.dto.UserDTO
import com.example.biofit.ui.activity.AIChatbotActivity
import com.example.biofit.ui.activity.AddActivity
import com.example.biofit.ui.activity.ExerciseActivity
import com.example.biofit.ui.activity.UpdateWeightActivity
import com.example.biofit.ui.animated.BlinkingGradientBox
import com.example.biofit.ui.theme.BioFitTheme
import com.example.biofit.view_model.DailyLogViewModel

@Composable
fun ActionPopup(
    userData: UserDTO,
    dailyLogViewModel: DailyLogViewModel = viewModel(),
    onDismissPopup: () -> Unit,
    standardPadding: Dp
) {
    val context = LocalContext.current
    val activity = context as? Activity


    val itemPopupList = listOf(
        Pair(R.drawable.figure_strengthtraining_traditional, R.string.exercise),
        Pair(R.drawable.scalemass, R.string.weight)
    )
    val sessionPopupList = listOf(
        Triple(R.drawable.cloud_sun_fill, R.string.morning, Color(0xFFFFAB00)),
        Triple(R.drawable.sun_max_fill, R.string.afternoon, Color(0xFFDD2C00)),
        Triple(R.drawable.cloud_moon_fill, R.string.evening, Color(0xFF2962FF)),
        Triple(R.drawable.circle_hexagongrid_fill, R.string.snack, Color(0xFF00BFA5))
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(intrinsicSize = IntrinsicSize.Min)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { }
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(standardPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.padding(
                    top = standardPadding,
                    start = standardPadding / 2,
                    end = standardPadding / 2
                )
            ) {
                itemPopupList.forEach { (icon, title) ->
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .clip(MaterialTheme.shapes.extraLarge)
                            .clickable {
                                when (title) {
                                    R.string.exercise ->
                                        activity?.let {
                                            val intent =
                                                Intent(it, ExerciseActivity::class.java)
                                            it.startActivity(intent)
                                        }

                                    R.string.weight ->
                                        activity?.let {
                                            val intent =
                                                Intent(it, UpdateWeightActivity::class.java)
                                            it.startActivity(intent)
                                        }
                                }
                                onDismissPopup()
                            },
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        MainCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(standardPadding / 2)
                        ) {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    painter = painterResource(icon),
                                    contentDescription = stringResource(R.string.activity),
                                    modifier = Modifier
                                        .padding(standardPadding)
                                        .size(standardPadding * 2),
                                    tint = MaterialTheme.colorScheme.background
                                )
                            }
                        }

                        Text(
                            text = stringResource(title),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    start = standardPadding,
                                    end = standardPadding,
                                    bottom = standardPadding / 2
                                ),
                            color = MaterialTheme.colorScheme.onBackground,
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clip(MaterialTheme.shapes.extraLarge)
                        .clickable {
                            val intent = Intent(context, AIChatbotActivity::class.java)
                            context.startActivity(intent)

                            onDismissPopup()
                        },
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier.padding(standardPadding / 2)
                    ) {
                        BlinkingGradientBox(
                            alpha = 0.5f,
                            shape = MaterialTheme.shapes.extraLarge
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.ic_chatbot_ai),
                                    contentDescription = stringResource(R.string.activity),
                                    modifier = Modifier
                                        .size(standardPadding * 4),
                                    tint = if (isSystemInDarkTheme()) {
                                        Color(0xFFB388FF)
                                    } else {
                                        Color(0xFF6200EA)
                                    }
                                )
                            }
                        }
                    }

                    Column(
                        modifier = Modifier.padding(
                            start = standardPadding,
                            end = standardPadding,
                            bottom = standardPadding / 2
                        ),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = stringResource(R.string.ai_assistant_bionix),
                            color = MaterialTheme.colorScheme.onBackground,
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }

            Row(
                modifier = Modifier.padding(
                    start = standardPadding / 2,
                    end = standardPadding / 2,
                    bottom = standardPadding
                )
            ) {
                sessionPopupList.forEach { (icon, title, color) ->
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .clip(MaterialTheme.shapes.extraLarge)
                            .clickable {
                                activity?.let {
                                    val intent = Intent(it, AddActivity::class.java)
                                    intent.putExtra(
                                        "SESSION_TITLE", when (title) {
                                            R.string.morning -> R.string.morning
                                            R.string.afternoon -> R.string.afternoon
                                            R.string.evening -> R.string.evening
                                            else -> R.string.snack
                                        }
                                    )
                                    it.startActivity(intent)
                                }

                                onDismissPopup()
                            },
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        SubCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(standardPadding / 2)
                        ) {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    painter = painterResource(icon),
                                    contentDescription = stringResource(R.string.activity),
                                    modifier = Modifier
                                        .padding(standardPadding)
                                        .size(standardPadding * 2),
                                    tint = color
                                )
                            }
                        }

                        Text(
                            text = stringResource(title),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    start = standardPadding,
                                    end = standardPadding,
                                    bottom = standardPadding / 2
                                ),
                            color = MaterialTheme.colorScheme.onBackground,
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_UNDEFINED)
@Composable
private fun ActionPopupDarkModePreview() {
    BioFitTheme {
        AnimatedVisibility(
            visible = true,
            modifier = Modifier.background(MaterialTheme.colorScheme.background),
            enter = slideInVertically { it } + fadeIn() + expandVertically(),
            exit = slideOutVertically { it } + fadeOut() + shrinkVertically()
        ) {
            ActionPopup(
                userData = UserDTO(
                    userId = 0,
                    fullName = "John",
                    email = "john@email.com",
                    gender = 0,
                    height = 180f,
                    weight = 70f,
                    targetWeight = 75f,
                    dateOfBirth = "2004-01-01",
                    avatar = "",
                    createdAccount = "2025-01-01"
                ),
                onDismissPopup = { },
                standardPadding = getStandardPadding().first
            )
        }
    }
}

@Preview
@Composable
private fun ActionPopupPreview() {
    BioFitTheme {
        AnimatedVisibility(
            visible = true,
            modifier = Modifier.background(MaterialTheme.colorScheme.background),
            enter = slideInVertically { it } + fadeIn() + expandVertically(),
            exit = slideOutVertically { it } + fadeOut() + shrinkVertically()
        ) {
            ActionPopup(
                userData = UserDTO(
                    userId = 0,
                    fullName = "John",
                    email = "john@email.com",
                    gender = 0,
                    height = 180f,
                    weight = 70f,
                    targetWeight = 75f,
                    dateOfBirth = "2004-01-01",
                    avatar = "",
                    createdAccount = "2025-01-01"
                ),
                onDismissPopup = { },
                standardPadding = getStandardPadding().first
            )
        }
    }
}