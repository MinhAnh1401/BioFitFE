package com.example.biofit.view.activity

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import com.example.biofit.R
import com.example.biofit.view.ui_theme.BioFitTheme

class InfoUserTargetActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BioFitTheme {
                InfoUserTargetScreen()
            }
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        recreate()
    }
}

@Composable
fun InfoUserTargetScreen() {
    val context = LocalContext.current
    val activity = context as? Activity

    val screenWidth = LocalConfiguration.current.screenWidthDp
    val screenHeight = LocalConfiguration.current.screenHeightDp
    val standardPadding = getStandardPadding().first
    val modifier = getStandardPadding().second

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
    ) {
        Box {
            BackgroundInfoScreen()

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = WindowInsets.safeDrawing.asPaddingValues().calculateTopPadding(),
                        start = standardPadding,
                        end = standardPadding,
                    ),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                TopBarInfoScreen(
                    onBackClick = { activity?.finish() },
                    stepColors = listOf(
                        MaterialTheme.colorScheme.primary,
                        MaterialTheme.colorScheme.primary,
                        MaterialTheme.colorScheme.primary,
                        MaterialTheme.colorScheme.primary,
                        MaterialTheme.colorScheme.primary
                    ),
                    screenWidth,
                    screenHeight,
                    standardPadding
                )
                InfoUserTargetContent(
                    standardPadding,
                    modifier
                )
            }

            NextButtonInfoScreen(
                onClick = {
                    activity?.let {
                        val intent = Intent(it, MainActivity::class.java)
                        it.startActivity(intent)
                        it.finish()
                    }
                },
                standardPadding
            )
        }
    }
}

@Composable
fun InfoUserTargetContent(
    standardPadding: Dp,
    modifier: Modifier
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        item {
            Text(
                text = stringResource(R.string.what_is_your_weight_goal),
                modifier = Modifier.padding(top = standardPadding * 3),
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.displaySmall
            )
        }

        item {
            Text(
                text = stringResource(R.string.description_target),
                modifier = Modifier.padding(bottom = standardPadding),
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodySmall
            )
        }

        item {
            var targetWeight by rememberSaveable { mutableStateOf("") }

            OutlinedTextField(
                value = targetWeight, // giá trị hiện tại của trường nhập liệu
                onValueChange = {
                    targetWeight = it
                }, // xử lý thay đổi giá trị và cập nhật trạng thái tương ứng
                modifier = modifier, // kích thước và vị trí của trường nhập liệu
                // enabled = true, // trạng thái kích hoạt của trường nhập liệu (mặc định true)
                // readOnly = false, // trạng thái chỉ đọc của trường nhập liệu (mặc định false)
                textStyle = MaterialTheme.typography.bodySmall, // kiểu chữ và kích thước của văn bản trong trường nhập liệu
                label = {
                    Text(
                        text = stringResource(R.string.target_weight),
                        style = MaterialTheme.typography.bodySmall
                    )
                }, // nhãn cho trường nhập liệu
                // placeholder = null, // văn bản gợi ý bên trong trường nhập liệu (mặc định null)
                // leadingIcon = null, // biểu tượng trước văn bản (mặc định null)
                // trailingIcon = null, // biểu tượng sau văn bản (mặc định null)
                // prefix = null, // tiền tố văn bản (mặc định null)
                suffix = {
                    Text(
                        text = stringResource(R.string.kg),
                        style = MaterialTheme.typography.bodySmall
                    )
                }, // hậu tố văn bản
                // supportingText = null, // văn bản trợ giúp dưới trường nhập liệu (mặc định null)
                isError = false, // trạng thái lỗi
                // visualTransformation = VisualTransformation.None, // biến đổi hiển thị của văn bản (mặc định VisualTransformation.None)
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal), // kiểu bàn phím
                keyboardActions = KeyboardActions(
                    onDone = { /*TODO*/ },
                    onGo = { /*TODO*/ },
                    onNext = { /*TODO*/ },
                    onPrevious = { /*TODO*/ },
                    onSearch = { /*TODO*/ },
                    onSend = { /*TODO*/ }
                ), // hành động khi nhấn phím
                singleLine = true, // chỉ cho phép nhập một dòng văn bản
                maxLines = 1, // số lượng dòng tối đa cho văn bản
                // minLines = 1, // số lượng dòng tối thiểu cho văn bản (mặc định 1)
                shape = MaterialTheme.shapes.large, // hình dạng của trường nhập liệu
                colors = OutlinedTextFieldDefaults.colors(
                    focusedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    focusedBorderColor = MaterialTheme.colorScheme.onPrimaryContainer
                ) // màu sắc của trường nhập liệu
            )
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
private fun InfoUserTargetPortraitScreenDarkModePreviewInSmallPhone() {
    BioFitTheme {
        InfoUserTargetScreen()
    }
}

@Preview(
    device = "id:pixel_9_pro_xl",
    showSystemUi = true,
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
private fun InfoUserTargetPortraitScreenPreviewInLargePhone() {
    BioFitTheme {
        InfoUserTargetScreen()
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
private fun InfoUserTargetPortraitScreenPreviewInTablet() {
    BioFitTheme {
        InfoUserTargetScreen()
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
private fun InfoUserTargetLandscapeScreenDarkModePreviewInSmallPhone() {
    BioFitTheme {
        InfoUserTargetScreen()
    }
}

@Preview(
    device = "spec:parent=pixel_9_pro_xl,orientation=landscape",
    showSystemUi = true,
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
private fun InfoUserTargetLandscapeScreenPreviewInLargePhone() {
    BioFitTheme {
        InfoUserTargetScreen()
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
private fun InfoUserTargetLandscapeScreenPreviewInTablet() {
    BioFitTheme {
        InfoUserTargetScreen()
    }
}