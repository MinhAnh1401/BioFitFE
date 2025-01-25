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
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
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

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        recreate()
    }
}

@Composable
fun StartScreen() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Box {
            BackgroundImage()
            Content()
        }
    }
}

@Composable
fun BackgroundImage() {
    Image(
        painter = painterResource(id = R.drawable.bg_start_screen),
        contentDescription = "Start Screen Background",
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Crop
    )
}

@Composable
fun Content() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(WindowInsets.safeDrawing.asPaddingValues())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        AppTitleAndDescription()
        WelcomeSection()
        ActionButtons()
    }
}

@Composable
fun AppTitleAndDescription() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row {
            Text(
                text = "BIO",
                style = MaterialTheme.typography.displaySmall,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "FIT",
                style = MaterialTheme.typography.displaySmall,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
        Text(
            text = stringResource(R.string.description),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}

@Composable
fun WelcomeSection() {
    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = stringResource(R.string.welcome_to_biofit),
            style = MaterialTheme.typography.displaySmall,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = stringResource(R.string.start_title_1),
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onPrimary
        )
        Text(
            text = stringResource(R.string.start_title_2),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}

@Composable
fun ActionButtons() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        GetStartedButton()
        SignInAndSignUpButtons()
    }
}

@Composable
fun GetStartedButton() {
    Button(
        onClick = { /* TODO */ }, // xử lý sự kiện khi nút được nhấn
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
        content = {
            Text(
                text = stringResource(R.string.get_started),
                style = MaterialTheme.typography.labelLarge
            )
        } // nội dung bên trong nút thường là Text hoặc Icon
    )
}

@Composable
fun SignInAndSignUpButtons() {
    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        TextButton(
            onClick = { /* TODO */ },
            shape = MaterialTheme.shapes.large,
            content = {
                Text(
                    text = stringResource(R.string.sign_in),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        ) // có các tham số tương tự như Button
        TextButton(
            onClick = { /* TODO */ },
            shape = MaterialTheme.shapes.large,
            content = {
                Text(
                    text = stringResource(R.string.sign_up),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        )
    }
}

@Preview(
    device = "id:pixel_9_pro_xl",
    showSystemUi = true,
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
private fun StartScreenPreviewInLargePhone() {
    BioFitTheme {
        StartScreen()
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
private fun StartScreenDarkModePreviewInSmallPhone() {
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
private fun StartScreenPreviewInTablet() {
    BioFitTheme {
        StartScreen()
    }
}