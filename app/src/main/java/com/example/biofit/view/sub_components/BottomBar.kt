package com.example.biofit.view.sub_components

import android.content.res.Configuration
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import com.example.biofit.R
import com.example.biofit.view.activity.getStandardPadding
import com.example.biofit.view.ui_theme.BioFitTheme

@Composable
fun BottomBar(
    onItemSelected: (String) -> Unit,
    showPopup: Boolean,
    onDismissPopup: () -> Unit,
    standardPadding: Dp
) {
    val items = listOf(
        stringResource(R.string.home),
        stringResource(R.string.plan),
        "Add",
        stringResource(R.string.knowledge),
        stringResource(R.string.profile)
    )
    val selectedItem = remember { mutableStateOf(items.first()) }

    val icons = listOf(
        R.drawable.ic_home,
        R.drawable.ic_plan,
        R.drawable.ic_add,
        R.drawable.ic_knowledge,
        R.drawable.ic_person
    )

    val rotationAngle by animateFloatAsState(
        targetValue = if (showPopup) 45f else 0f,
        animationSpec = tween(durationMillis = 300)
    )

    BottomAppBar(
        containerColor = MaterialTheme.colorScheme.primaryContainer
    ) {
        items.forEachIndexed { index, label ->
            Spacer(modifier = Modifier.weight(1f))
            IconButton(
                onClick = {
                    if (label != "Add") {
                        onDismissPopup()
                        selectedItem.value = label
                    }
                    onItemSelected(label)
                },
                modifier = Modifier.size(standardPadding * 3.5f)
            ) {
                Icon(
                    painter = painterResource(id = icons[index]),
                    contentDescription = label,
                    modifier = Modifier
                        .size(if (index != 2) standardPadding * 2f else standardPadding * 3.5f)
                        .graphicsLayer {
                            rotationZ = if (label == "Add") rotationAngle else 0f
                        },
                    tint = if (selectedItem.value == label && label != "Add") {
                        MaterialTheme.colorScheme.onSecondaryContainer
                    } else {
                        MaterialTheme.colorScheme.primary
                    }
                )
            }
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_UNDEFINED)
@Composable
private fun BottomBarDarkModePreview() {
    BioFitTheme {
        BottomBar(
            onItemSelected = {},
            showPopup = false,
            onDismissPopup = {},
            standardPadding = getStandardPadding().first
        )
    }
}

@Preview
@Composable
private fun BottomBarPreview() {
    BioFitTheme {
        BottomBar(
            onItemSelected = {},
            showPopup = false,
            onDismissPopup = {},
            standardPadding = getStandardPadding().first
        )
    }
}