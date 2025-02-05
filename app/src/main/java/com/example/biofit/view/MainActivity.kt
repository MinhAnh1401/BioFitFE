package com.example.biofit.view

import android.content.res.Configuration
import android.os.Bundle
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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.biofit.R
import com.example.biofit.ui.theme.BioFitTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BioFitTheme {
                StartScreen()
            }
        }
    }

    // thay đổi chế độ hiển thị khi thiết bị xoay màn hình
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        recreate()
    }
}

@Composable
fun StartScreen() {
    val screenWidth = LocalConfiguration.current.screenWidthDp
    val screenHeight = LocalConfiguration.current.screenHeightDp
    val standardPadding = ((screenWidth + screenHeight) / 2).dp * 0.02f

    Surface(
        modifier = Modifier.fillMaxSize(), // tuỳ chỉnh kích thước của Surface
        // shape = , // tuỳ chỉnh hình dạng của Surface
        color = MaterialTheme.colorScheme.background, // tuỳ chỉnh màu nền của Surface
        // contentColor = , // tuỳ chỉnh màu của nội dung bên trong Surface
        // tonalElevation = , // tuỳ chỉnh độ nổi của Surface
        // shadowElevation = , // tuỳ chỉnh độ getJSONShadow của Surface
        // border = , // tuỳ chỉnh viền của Surface
    ) {
        Box(
            // modifier = , // tuỳ chỉnh kích thước và vị trí của Box
            // contentAlignment = , // tuỳ chỉnh vị trí của nội dung bên trong Box
            // propagateMinConstraints = , // tuỳ chỉnh xem Box có kế thừa các ràng buộc min hay không
        ) {
            StartScreenBackgroundImage()
            StartScreenContent(
                screenWidth,
                screenHeight,
                standardPadding
            )
        } // nội dung bên trong Box
    } // nội dung bên trong Surface
}

@Composable
fun StartScreenBackgroundImage() {
    Image(
        painter = painterResource(id = R.drawable.bg_start_screen), // nguồn ảnh
        contentDescription = "Start screen background", // mô tả nội dung của ảnh
        modifier = Modifier.fillMaxSize(), // tuỳ chỉnh kích thước của hình ảnh
        // alignment = , // tuỳ chỉnh vị trí của hình ảnh trong Box
        contentScale = ContentScale.FillBounds,
        // alpha = , // tuỳ chỉnh độ mờ của hình ảnh
        // colorFilter = , // tuỳ chỉnh màu sắc của hình ảnh
    )
}

@Composable
fun StartScreenContent(
    screenWidth: Int,
    screenHeight: Int,
    standardPadding: Dp
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                top = WindowInsets.safeDrawing.asPaddingValues().calculateTopPadding(),
                start = standardPadding,
                end = standardPadding,
            ), // tuỳ chỉnh kích thước và vị trí của Column
        verticalArrangement = Arrangement.SpaceBetween, // sắp xếp các phần tử theo chiều dọc
        horizontalAlignment = Alignment.CenterHorizontally, // sắp xếp các phần tử theo chiều ngang
    ) {
        AppTitleAndDescription()
        WelcomeSection(
            screenWidth,
            screenHeight,
            standardPadding
        )
        ActionButtons(standardPadding)
    } // nội dung bên trong Column
}

@Composable
fun AppTitleAndDescription() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            // modifier = , // tuỳ chỉnh kích thước và vị trí của Row
            // horizontalArrangement = , // tuỳ chỉnh cách sắp xếp các phần tử theo chiều ngang
            // verticalAlignment = , // tuỳ chỉnh cách sắp xếp các phần tử theo chiều dọc
        ) {
            Text(
                text = "BIO",
                // modifier = , // tuỳ chỉnh kích thước và vị trí của Text
                color = MaterialTheme.colorScheme.primary,
                // fontSize = , // tuỳ chỉnh kích thước chữ
                // fontStyle = , // tuỳ chỉnh font chữ
                // fontWeight = , // tuỳ chỉnh độ đậm của chữ
                // fontFamily = , // tuỳ chỉnh font chữ
                // letterSpacing = , // tuỳ chỉnh khoảng cách chữ
                // textDecoration = , // tuỳ chỉnh kiểu chữ
                // textAlign = , // tuỳ chỉnh căn giữa chữ
                // lineHeight = , // tuỳ chỉnh chiều cao chữ
                // overflow = , // tuỳ chỉnh cách xử lý khi chữ vượt quá giới hạn
                // softWrap = , // tuỳ chỉnh cách xử lý khi chữ vượt quá giới hạn
                // maxLines = , // tuỳ chỉnh số lượng dòng tối đa
                // minLines = , // tuỳ chỉnh số lượng dòng tối thiểu
                // onTextLayout = , // tuỳ chỉnh hành vi khi layout chữ
                style = MaterialTheme.typography.displaySmall, // tuỳ chỉnh kiểu chữ
            )
            Text(
                text = "FIT",
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.displaySmall,
            )
        } // nội dung bên trong Row
        Text(
            text = stringResource(R.string.description),
            color = MaterialTheme.colorScheme.onPrimary,
            style = MaterialTheme.typography.bodySmall,
        )
    }
}

@Composable
fun WelcomeSection(
    screenWidth: Int,
    screenHeight: Int,
    standardPadding: Dp
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(standardPadding),
        horizontalAlignment = if (screenWidth > screenHeight || screenWidth > 450) {
            Alignment.CenterHorizontally
        } else {
            Alignment.Start
        },
    ) {
        Text(
            text = stringResource(R.string.welcome_to_biofit),
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.displaySmall,
        )
        Text(
            text = stringResource(R.string.start_title_1),
            color = MaterialTheme.colorScheme.onPrimary,
            style = MaterialTheme.typography.titleSmall,
        )
        Text(
            text = stringResource(R.string.start_title_2),
            color = MaterialTheme.colorScheme.onPrimary,
            style = MaterialTheme.typography.bodySmall,
        )
    }
}

@Composable
fun ActionButtons(standardPadding: Dp) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(standardPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        GetStartedButton(
            onCLick = { TODO() }
        )
        SignInAndSignUpButtons(standardPadding)
        Spacer(modifier = Modifier.height(standardPadding))
    }
}

@Composable
fun GetStartedButton(onCLick: () -> Unit = {}) {
    // GET STARTED button
    Button(
        onClick = onCLick, // xử lý sự kiện khi nút được nhấn
        // modifier = Modifier, // tuỳ chỉnh kích thước và vị trí của nút
        // enabled = true, // trạng thái kích hoạt của nút (mặc định true)
        shape = MaterialTheme.shapes.large, // hình dạng của nút
        /* colors = ButtonDefaults.buttonColors(
            // containerColor = MaterialTheme.colorScheme.primary, // màu nền của nút (mặc định màu primary)
            // contentColor = MaterialTheme.colorScheme.onPrimary, // màu của nội dung bên trong nút (mặc định màu onPrimary)
            // disabledContainerColor: màu nền của nút khi vô hiệu hóa
            // disabledContentColor: màu của nội dung bên trong nút khi vô hiệu hóa
        ), // cấu hình màu của nút */
        /* elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 16.dp, // độ cao của nút
            pressedElevation = 8.dp, // độ cao khi nút được nhấn
            disabledElevation = 0.dp // độ cao khi nút vô hiệu hóa
        ), // cấu hình độ cao của nút */
        /* border = BorderStroke(
            // width: độ dày của viền
            // color: màu viền
        ), // cấu hình viền của nút */
        // contentPadding = PaddingValues(), // quy định khoảng cách giữa nội dung và viền của nút
        // interactionSource = MutableInteractionSource(), // đối tượng lưu trữ trạng thái tương tác của nút
    ) {
        Text(
            text = stringResource(R.string.get_started),
            color = MaterialTheme.colorScheme.onPrimary,
            style = MaterialTheme.typography.labelLarge
        )
    } // nội dung bên trong nút thường là Text hoặc Icon
}

@Composable
fun SignInAndSignUpButtons(standardPadding: Dp) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(standardPadding / 2),
    ) {
        TextButton(
            onClick = { /* TODO */ },
            shape = MaterialTheme.shapes.large,
        ) {
            Text(
                text = stringResource(R.string.sign_in),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onPrimary
            )
        } // có các tham số tương tự như Button
        TextButton(
            onClick = { /* TODO */ },
            shape = MaterialTheme.shapes.large,
        ) {
            Text(
                text = stringResource(R.string.sign_up),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

// xem trước layout mà không cần chạy ứng dụng
@Preview(
    device = "id:pixel",
    showSystemUi = true,
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL,
    locale = "vi"
)
@Composable
private fun StartPortraitScreenDarkModePreviewInSmallPhone() {
    BioFitTheme {
        StartScreen()
    }
}

@Preview(
    device = "id:pixel_9_pro_xl",
    showSystemUi = true,
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
private fun StartPortraitScreenPreviewInLargePhone() {
    BioFitTheme {
        StartScreen()
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
private fun StartPortraitScreenPreviewInTablet() {
    BioFitTheme {
        StartScreen()
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
private fun StartLandscapeScreenDarkModePreviewInSmallPhone() {
    BioFitTheme {
        StartScreen()
    }
}

@Preview(
    device = "spec:parent=pixel_9_pro_xl,orientation=landscape",
    showSystemUi = true,
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
private fun StartLandscapeScreenPreviewInLargePhone() {
    BioFitTheme {
        StartScreen()
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
private fun StartLandscapeScreenPreviewInTablet() {
    BioFitTheme {
        StartScreen()
    }
}