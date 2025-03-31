package com.example.biofit.ui.components

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import com.example.biofit.R
import com.example.biofit.navigation.MainActivity
import com.example.biofit.ui.activity.BackButton
import com.example.biofit.ui.theme.BioFitTheme

@Composable
fun TopBarScreen() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        TopBar(
            onBackClick = { },
            title = "Title",
            middleButton = {
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onBackground
                )
            },
            rightButton = {
                Icon(
                    painter = painterResource(R.drawable.ic_plus),
                    contentDescription = "Add button",
                    modifier = Modifier.size(getStandardPadding().first * 1.5f),
                    tint = MaterialTheme.colorScheme.primary
                )
            },
            standardPadding = getStandardPadding().first
        )
    }
}

@Composable
fun TopBar(
    onBackClick: (() -> Unit)? = null,
    title: String? = null,
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
            Row {
                if (onBackClick != null) {
                    BackButton(
                        onBackClick = onBackClick,
                        standardPadding = standardPadding
                    )
                }

                Spacer(modifier = Modifier.width(standardPadding))

                HomeButton(standardPadding)
            }
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                title?.let {
                    Text(
                        text = title,
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.headlineSmall
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
fun HomeButton(standardPadding: Dp) {
    val context = LocalContext.current
    val activity = context as? Activity

    IconButton(
        onClick = {
            activity?.let {
                val intent = Intent(it, MainActivity::class.java)
                it.startActivity(intent)
            }
        },
        modifier = Modifier.size(standardPadding * 1.5f),
        enabled = true,
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_home),
            contentDescription = "Back Button",
            modifier = Modifier.size(standardPadding * 1.5f),
            tint = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_UNDEFINED)
@Composable
private fun TopBarScreenDarkModePreview() {
    BioFitTheme {
        TopBarScreen()
    }
}

@Preview
@Composable
private fun TopBarScreenPreview() {
    BioFitTheme {
        TopBarScreen()
    }
}
