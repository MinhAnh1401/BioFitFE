package com.example.biofit.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun getStandardPadding(): Pair<Dp, Modifier> {
    val screenWidth = LocalConfiguration.current.screenWidthDp
    val screenHeight = LocalConfiguration.current.screenHeightDp
    val standardPadding = ((screenWidth + screenHeight) / 2).dp * 0.02f
    val modifier = if (screenWidth > screenHeight) {
        Modifier.width(((screenWidth + screenHeight) / 2).dp)
    } else {
        Modifier.fillMaxWidth()
    }

    return Pair(standardPadding, modifier)
}