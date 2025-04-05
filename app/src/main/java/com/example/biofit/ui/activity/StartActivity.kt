package com.example.biofit.ui.activity

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import com.example.biofit.R
import com.example.biofit.ui.components.getStandardPadding
import com.example.biofit.ui.theme.BioFitTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

class StartActivity : ComponentActivity() {
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

    val standardPadding = getStandardPadding().first

    Surface(
        modifier = Modifier.fillMaxSize(), // tuỳ chỉnh kích thước của Surface
        // shape = , // tuỳ chỉnh hình dạng của Surface
        color = MaterialTheme.colorScheme.background, // tuỳ chỉnh màu nền của Surface
        // contentColor = , // tuỳ chỉnh màu của nội dung bên trong Surface
        // tonalElevation = , // tuỳ chỉnh độ nổi của Surface
        // shadowElevation = , // tuỳ chỉnh độ getJSONShadow của Surface
        // border = , // tuỳ chỉnh viền của Surface
    ) {
        Box/*(
            modifier = , // tuỳ chỉnh kích thước và vị trí của Box
            contentAlignment = , // tuỳ chỉnh vị trí của nội dung bên trong Box
            propagateMinConstraints = , // tuỳ chỉnh xem Box có kế thừa các ràng buộc min hay không
        )*/ {
            StartScreenBackgroundImage(standardPadding)
            StartScreenContent(
                screenWidth,
                screenHeight,
                standardPadding
            )
        } // nội dung bên trong Box
    } // nội dung bên trong Surface
}

@Composable
fun StartScreenBackgroundImage(standardPadding: Dp) {
    /*Image(
        painter = painterResource(id = R.drawable.bg_start_screen), // nguồn ảnh
        contentDescription = "Start screen background", // mô tả nội dung của ảnh
        modifier = Modifier.fillMaxSize(), // tuỳ chỉnh kích thước của hình ảnh
        // alignment = , // tuỳ chỉnh vị trí của hình ảnh trong Box
        contentScale = ContentScale.FillBounds,
        // alpha = , // tuỳ chỉnh độ mờ của hình ảnh
        // colorFilter = , // tuỳ chỉnh màu sắc của hình ảnh
    )*/

    val images = listOf(
        R.drawable.bg_start_screen1,
        R.drawable.bg_start_screen2,
        R.drawable.bg_start_screen3
    )

    val pagerState = rememberPagerState(
        initialPage = 500,
        initialPageOffsetFraction = 0f,
        pageCount = { Int.MAX_VALUE }
    ) // Bắt đầu từ giữa danh sách
    val coroutineScope = rememberCoroutineScope()

    // Auto-scroll effect
    LaunchedEffect(Unit) {
        while (true) {
            delay(3000) // Chuyển ảnh sau mỗi 3 giây
            coroutineScope.launch {
                pagerState.animateScrollToPage(pagerState.currentPage + 1)
            }
        }
    }

    HorizontalPager(
        state = pagerState,
        modifier = Modifier.fillMaxSize(),
        userScrollEnabled = false
    ) { page ->
        val index = page % images.size
        Image(
            painter = painterResource(id = images[index]),
            contentDescription = "Background Image $index",
            modifier = Modifier
                .fillMaxSize()
                .blur(
                    radius = standardPadding * 4,
                    edgeTreatment = BlurredEdgeTreatment.Rectangle
                ),
            contentScale = ContentScale.FillBounds
        )
    }
}

@Composable
fun StartScreenContent(
    screenWidth: Int,
    screenHeight: Int,
    standardPadding: Dp
) {
    val images = listOf(
        R.drawable.bg_start_screen1,
        R.drawable.bg_start_screen2,
        R.drawable.bg_start_screen3
    )

    val pagerState = rememberPagerState(
        initialPage = 500,
        initialPageOffsetFraction = 0f,
        pageCount = { Int.MAX_VALUE }
    ) // Bắt đầu từ giữa danh sách
    val coroutineScope = rememberCoroutineScope()



    // Auto-scroll effect
    LaunchedEffect(Unit) {
        while (true) {
            delay(3000) // Chuyển ảnh sau mỗi 3 giây
            coroutineScope.launch {
                pagerState.animateScrollToPage(pagerState.currentPage + 1)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                top = WindowInsets.safeDrawing.asPaddingValues().calculateTopPadding(),
                start = standardPadding,
                end = standardPadding,
                bottom = WindowInsets.safeDrawing.asPaddingValues().calculateBottomPadding(),
            ), // tuỳ chỉnh kích thước và vị trí của Column
        verticalArrangement = Arrangement.SpaceBetween, // sắp xếp các phần tử theo chiều dọc
        horizontalAlignment = Alignment.CenterHorizontally, // sắp xếp các phần tử theo chiều ngang
    ) {
        AppTitleAndDescription()
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            pageSpacing = standardPadding * 2,
            userScrollEnabled = false
        ) { page ->
            val index = page % images.size
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(standardPadding * 2),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = images[index]),
                    contentDescription = "Background Image $index",
                    modifier = Modifier
                        .fillMaxSize()
                        .shadow(
                            elevation = standardPadding,
                            shape = MaterialTheme.shapes.extraLarge
                        ),
                    contentScale = ContentScale.FillBounds
                )
            }
        }
        WelcomeSection(standardPadding)
        Spacer(modifier = Modifier.height(standardPadding))
        ActionButtons(standardPadding)
    } // nội dung bên trong Column
}

@Composable
fun AppTitleAndDescription() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row/*(
            modifier = , // tuỳ chỉnh kích thước và vị trí của Row
            horizontalArrangement = , // tuỳ chỉnh cách sắp xếp các phần tử theo chiều ngang
            verticalAlignment = , // tuỳ chỉnh cách sắp xếp các phần tử theo chiều dọc
        )*/ {
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
                style = MaterialTheme.typography.displaySmall.copy(
                    fontWeight = FontWeight.Black
                ) // tuỳ chỉnh kiểu chữ
            )
            Text(
                text = "FIT",
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.displaySmall.copy(
                    fontWeight = FontWeight.Black
                )
            )
        } // nội dung bên trong Row
        Text(
            text = stringResource(R.string.des_app_name),
            color = MaterialTheme.colorScheme.onPrimary,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
fun WelcomeSection(standardPadding: Dp) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(standardPadding),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.welcome_to_app),
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.displaySmall.copy(
                fontWeight = FontWeight.Bold
            )
        )

        Text(
            text = stringResource(R.string.start_title_1),
            color = MaterialTheme.colorScheme.onPrimary,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold
            )
        )

        Text(
            text = stringResource(R.string.start_title_2),
            color = MaterialTheme.colorScheme.onPrimary,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleSmall
        )
    }
}

@Composable
fun ActionButtons(standardPadding: Dp) {
    val context = LocalContext.current
    val activity = context as? Activity

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(standardPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        GetStartedButton(
            onCLick = {
                activity?.let {
                    val intent = Intent(it, LoginActivity::class.java)
                    it.startActivity(intent)
                    it.finish()
                }
            }
        )
        Spacer(modifier = Modifier.height(standardPadding))
    }
}

@Composable
fun GetStartedButton(
    onCLick: () -> Unit = {}
) {
    // GET STARTED button
    ElevatedButton(
        onClick = onCLick, // xử lý sự kiện khi nút được nhấn
        // modifier = Modifier, // tuỳ chỉnh kích thước và vị trí của nút
        // enabled = true, // trạng thái kích hoạt của nút (mặc định true)
        // shape = MaterialTheme.shapes.extraLarge, // hình dạng của nút
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary, // màu nền của nút (mặc định màu primary)
            // contentColor = MaterialTheme.colorScheme.onPrimary, // màu của nội dung bên trong nút (mặc định màu onPrimary)
            // disabledContainerColor: màu nền của nút khi vô hiệu hóa
            // disabledContentColor: màu của nội dung bên trong nút khi vô hiệu hóa
        ), // cấu hình màu của nút
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
        Text(text = stringResource(id = R.string.get_started))
    } // nội dung bên trong nút thường là Text hoặc Icon
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