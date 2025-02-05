package com.example.biofit.view

import android.content.res.Configuration
import android.os.Bundle
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.biofit.R
import com.example.biofit.ui.theme.BioFitTheme

class ProfileActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BioFitTheme {
                ProfileScreen()
            }
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        recreate()
    }
}

@Composable
fun ProfileScreen() {
    val screenWidth = LocalConfiguration.current.screenWidthDp
    val screenHeight = LocalConfiguration.current.screenHeightDp
    val standardPadding = ((screenWidth + screenHeight) / 2).dp * 0.02f
    val modifier = if (screenWidth > screenHeight) {
        Modifier.width(((screenWidth + screenHeight) / 2).dp)
    } else {
        Modifier.fillMaxWidth()
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column {
            Row(
                modifier = Modifier.weight(1f)
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
                    ProfileContent(
                        standardPadding,
                        modifier
                    )
                }
            }

            Row {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.Bottom
                ) {
                    BottomBar(
                        onItemSelected = { TODO() },
                        standardPadding = standardPadding
                    )
                }
            }
        }
    }
}

@Composable
fun ProfileContent(
    standardPadding: Dp,
    modifier: Modifier
) {
    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Spacer(modifier = Modifier.padding(standardPadding))
        }

        item {
            Row(
                modifier = modifier,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(R.drawable.fake_avatar), // Thay avatar của người dùng từ database
                    contentDescription = "Avatar",
                    modifier = Modifier
                        .size(standardPadding * 5)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = standardPadding)
                ) {
                    Text(
                        text = stringResource(R.string.full_name), // Thay tên người dùng từ database
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.titleSmall
                    )

                    Row {
                        Text(
                            text = stringResource(R.string.gender), // Thay giới tính từ database
                            color = MaterialTheme.colorScheme.onBackground,
                            style = MaterialTheme.typography.bodySmall
                        )

                        Text(
                            text = " | ",
                            color = MaterialTheme.colorScheme.onBackground,
                            style = MaterialTheme.typography.bodySmall
                        )

                        Text(
                            text = stringResource(R.string.age), // Tính tuổi từ nằm sinh trong database
                            color = MaterialTheme.colorScheme.onBackground,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }

                IconButton(
                    onClick = { TODO() },
                ) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = stringResource(R.string.profile),
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        }

        item {
            Column(
                modifier = modifier.padding(top = standardPadding)
            ) {
                Text(
                    text = stringResource(R.string.target),
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.titleSmall
                )

                Card(
                    modifier = Modifier.padding(top = standardPadding),
                    shape = MaterialTheme.shapes.large,
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(standardPadding)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = standardPadding)
                        ) {
                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = stringResource(R.string.starting_weight),
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    style = MaterialTheme.typography.bodySmall
                                )

                                Text(
                                    text = "xx kg", // Thay cân nặng từ database
                                    modifier = Modifier.padding(top = standardPadding),
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    style = MaterialTheme.typography.titleSmall
                                )
                            }

                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = stringResource(R.string.target_weight),
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    style = MaterialTheme.typography.bodySmall
                                )

                                Text(
                                    text = "xx kg", // Thay mục tiêu cân nặng từ database
                                    modifier = Modifier.padding(top = standardPadding),
                                    color = MaterialTheme.colorScheme.onSecondary,
                                    style = MaterialTheme.typography.titleSmall
                                )
                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_target),
                                contentDescription = stringResource(R.string.target),
                                tint = MaterialTheme.colorScheme.onPrimary
                            )

                            Text(
                                text = stringResource(R.string.target),
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(start = standardPadding),
                                color = MaterialTheme.colorScheme.onPrimary,
                                style = MaterialTheme.typography.bodySmall
                            )

                            IconButton(
                                onClick = { TODO() },
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                    contentDescription = "Arrow right icon",
                                    tint = MaterialTheme.colorScheme.onPrimary
                                )
                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_nutrition_report),
                                contentDescription = stringResource(R.string.nutrition_report),
                                tint = MaterialTheme.colorScheme.onPrimary
                            )

                            Text(
                                text = stringResource(R.string.nutrition_report),
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(start = standardPadding),
                                color = MaterialTheme.colorScheme.onPrimary,
                                style = MaterialTheme.typography.bodySmall
                            )

                            IconButton(
                                onClick = { TODO() },
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                    contentDescription = "Arrow right icon",
                                    tint = MaterialTheme.colorScheme.onPrimary
                                )
                            }
                        }
                    }
                }

                Text(
                    text = stringResource(R.string.term_of_use)
                            + " " + stringResource(R.string.and)
                            + " " + stringResource(R.string.privacy_policy),
                    modifier = Modifier.padding(top = standardPadding),
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.titleSmall
                )

                Card(
                    modifier = Modifier.padding(top = standardPadding),
                    shape = MaterialTheme.shapes.large,
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(standardPadding)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_term_of_use),
                                contentDescription = stringResource(R.string.term_of_use),
                                tint = MaterialTheme.colorScheme.onPrimary
                            )

                            Text(
                                text = stringResource(R.string.term_of_use),
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(start = standardPadding),
                                color = MaterialTheme.colorScheme.onPrimary,
                                style = MaterialTheme.typography.bodySmall
                            )

                            IconButton(
                                onClick = { TODO() },
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                    contentDescription = "Arrow right icon",
                                    tint = MaterialTheme.colorScheme.onPrimary
                                )
                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_privacy_policy),
                                contentDescription = stringResource(R.string.privacy_policy),
                                tint = MaterialTheme.colorScheme.onPrimary
                            )

                            Text(
                                text = stringResource(R.string.privacy_policy),
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(start = standardPadding),
                                color = MaterialTheme.colorScheme.onPrimary,
                                style = MaterialTheme.typography.bodySmall
                            )

                            IconButton(
                                onClick = { TODO() },
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                    contentDescription = "Arrow right icon",
                                    tint = MaterialTheme.colorScheme.onPrimary
                                )
                            }
                        }
                    }
                }

                Text(
                    text = stringResource(R.string.account),
                    modifier = Modifier.padding(top = standardPadding),
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.titleSmall
                )

                Card(
                    modifier = Modifier.padding(top = standardPadding),
                    shape = MaterialTheme.shapes.large,
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(standardPadding)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_delete_data),
                                contentDescription = stringResource(R.string.delete_data),
                                tint = MaterialTheme.colorScheme.onPrimary
                            )

                            Text(
                                text = stringResource(R.string.delete_data),
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(start = standardPadding),
                                color = MaterialTheme.colorScheme.onPrimary,
                                style = MaterialTheme.typography.bodySmall
                            )

                            IconButton(
                                onClick = { TODO() },
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                    contentDescription = "Arrow right icon",
                                    tint = MaterialTheme.colorScheme.onPrimary
                                )
                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_delete_account),
                                contentDescription = stringResource(R.string.delete_account),
                                tint = MaterialTheme.colorScheme.onPrimary
                            )

                            Text(
                                text = stringResource(R.string.delete_account),
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(start = standardPadding),
                                color = MaterialTheme.colorScheme.onPrimary,
                                style = MaterialTheme.typography.bodySmall
                            )

                            IconButton(
                                onClick = { TODO() },
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                    contentDescription = "Arrow right icon",
                                    tint = MaterialTheme.colorScheme.onPrimary
                                )
                            }
                        }
                    }
                }
            }
        }

        item {
            Button(
                onClick = { TODO() },
                modifier = Modifier.padding(top = standardPadding),
                shape = MaterialTheme.shapes.large,
            ) {
                Text(
                    text = stringResource(R.string.sign_out),
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }

        item {
            Spacer(
                modifier = Modifier.padding(
                    bottom = WindowInsets.safeDrawing.asPaddingValues().calculateBottomPadding() * 2
                )
            )
        }
    }
}

@Composable
fun BottomBar(
    onItemSelected: (String) -> Unit,
    standardPadding: Dp
) {
    val selectedItem = remember { mutableStateOf(value = "Profile") }
    val items = listOf(
        stringResource(R.string.home),
        stringResource(R.string.plan),
        "Add",
        stringResource(R.string.knowledge),
        stringResource(R.string.profile)
    )
    val icons = listOf(
        R.drawable.ic_home,
        R.drawable.ic_plan,
        R.drawable.ic_add,
        R.drawable.ic_knowledge,
        R.drawable.ic_person
    )

    BottomAppBar(
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.onPrimary
    ) {
        items.forEachIndexed { index, label ->
            if (label == "Add") {
                Spacer(modifier = Modifier.weight(1f))
                FloatingActionButton(
                    onClick = { onItemSelected(label) },
                    containerColor = MaterialTheme.colorScheme.primary
                ) {
                    Icon(
                        painter = painterResource(id = icons[index]),
                        contentDescription = label,
                        tint = MaterialTheme.colorScheme.primaryContainer
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
            } else {
                Spacer(modifier = Modifier.weight(1f))
                IconButton(
                    onClick = {
                        selectedItem.value = label
                        onItemSelected(label)
                    },
                    modifier = Modifier.size(standardPadding * 3)
                ) {
                    Icon(
                        painter = painterResource(id = icons[index]),
                        contentDescription = label,
                        modifier = Modifier.size(standardPadding * 2.5f),
                        tint = if (selectedItem.value == label) {
                            MaterialTheme.colorScheme.onPrimaryContainer
                        } else {
                            MaterialTheme.colorScheme.primary
                        }
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
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
private fun ProfilePortraitScreenDarkModePreviewInSmallPhone() {
    BioFitTheme {
        ProfileScreen()
    }
}

@Preview(
    device = "id:pixel_9_pro_xl",
    showSystemUi = true,
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
private fun Profile() {
    BioFitTheme {
        ProfileScreen()
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
private fun ProfilePortraitScreenPreviewInTablet() {
    BioFitTheme {
        ProfileScreen()
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
private fun ProfileLandscapeScreenDarkModePreviewInSmallPhone() {
    BioFitTheme {
        ProfileScreen()
    }
}

@Preview(
    device = "spec:parent=pixel_9_pro_xl,orientation=landscape",
    showSystemUi = true,
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
private fun ProfileLandscapeScreenPreviewInLargePhone() {
    BioFitTheme {
        ProfileScreen()
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
private fun ProfileLandscapeScreenPreviewInTablet() {
    BioFitTheme {
        ProfileScreen()
    }
}