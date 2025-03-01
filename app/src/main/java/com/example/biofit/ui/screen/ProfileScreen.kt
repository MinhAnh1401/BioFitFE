package com.example.biofit.ui.screen

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.biofit.R
import com.example.biofit.ui.activity.LoginActivity
import com.example.biofit.navigation.OverviewActivity
import com.example.biofit.ui.activity.SettingActivity
import com.example.biofit.ui.activity.TargetActivity
import com.example.biofit.ui.components.DefaultDialog
import com.example.biofit.ui.components.getStandardPadding
import com.example.biofit.ui.theme.BioFitTheme

@Composable
fun ProfileScreen() {
    val standardPadding = getStandardPadding().first
    val modifier = getStandardPadding().second

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
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ProfileContent(
                standardPadding,
                modifier
            )
        }
    }
}

@Composable
fun ProfileContent(
    standardPadding: Dp,
    modifier: Modifier
) {
    val context = LocalContext.current
    val activity = context as? Activity

    var showSignOutDialog by rememberSaveable { mutableStateOf(false) }
    var showDeleteAccountDialog by rememberSaveable { mutableStateOf(false) }
    var showDeleteDataDialog by rememberSaveable { mutableStateOf(false) }

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(standardPadding * 2),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Row(
                modifier = modifier.fillMaxWidth(),
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
                    onClick = {
                        activity?.let {
                            val intent = Intent(it, SettingActivity::class.java)
                            it.startActivity(intent)
                        }
                    },
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
                modifier = modifier
            ) {
                Text(
                    text = stringResource(R.string.target),
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.titleSmall
                )

                Card(
                    modifier = Modifier
                        .padding(top = standardPadding)
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.25f),
                            shape = MaterialTheme.shapes.large
                        ),
                    shape = MaterialTheme.shapes.large,
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(standardPadding)
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

                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.5f)
                    )

                    Column(
                        modifier = Modifier.clickable {
                            activity?.let {
                                val intent = Intent(it, TargetActivity::class.java)
                                it.startActivity(intent)
                            }
                        }
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(standardPadding),
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

                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                contentDescription = "Arrow right icon",
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }

                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.5f)
                    )

                    Column(
                        modifier = Modifier.clickable {
                            activity?.let {
                                val intent = Intent(it, OverviewActivity::class.java)
                                it.startActivity(intent)
                            }
                        }
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(standardPadding),
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

        item {
            Column(
                modifier = modifier
            ) {
                Text(
                    text = stringResource(R.string.term_of_use)
                            + " " + stringResource(R.string.and)
                            + " " + stringResource(R.string.privacy_policy),
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.titleSmall
                )

                Card(
                    modifier = Modifier
                        .padding(top = standardPadding)
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.25f),
                            shape = MaterialTheme.shapes.large
                        ),
                    shape = MaterialTheme.shapes.large,
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Column(
                        modifier = Modifier.clickable {
                            TODO()
                        }
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(standardPadding),
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

                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                contentDescription = "Arrow right icon",
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }

                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.5f)
                    )

                    Column(
                        modifier = Modifier.clickable {
                            TODO()
                        }
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(standardPadding),
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

        item {
            Column(
                modifier = modifier
            ) {
                Text(
                    text = stringResource(R.string.account),
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.titleSmall
                )

                Card(
                    modifier = Modifier
                        .padding(top = standardPadding)
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.25f),
                            shape = MaterialTheme.shapes.large
                        ),
                    shape = MaterialTheme.shapes.large,
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Column(
                        modifier = Modifier.clickable { showDeleteDataDialog = true }
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(standardPadding),
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

                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                contentDescription = "Arrow right icon",
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }

                    if (showDeleteDataDialog) {
                        DefaultDialog(
                            title = R.string.are_you_sure_you_want_to_delete_your_data,
                            description = R.string.des_delete_data,
                            actionTextButton = R.string.delete,
                            actionTextButtonColor = MaterialTheme.colorScheme.onErrorContainer,
                            actionButtonColor = MaterialTheme.colorScheme.errorContainer,
                            onClickActionButton = {
                                if (activity != null) {
                                    deleteData(context, activity)
                                }
                            },
                            onDismissRequest = { showDeleteDataDialog = false },
                            standardPadding = standardPadding
                        )
                    }

                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.5f)
                    )

                    Column(
                        modifier = Modifier.clickable { showDeleteAccountDialog = true }
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(standardPadding),
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

                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                contentDescription = "Arrow right icon",
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }

                    if (showDeleteAccountDialog) {
                        DefaultDialog(
                            title = R.string.are_you_sure_you_want_to_delete_your_account,
                            description = R.string.des_delete_account,
                            actionTextButton = R.string.delete,
                            actionTextButtonColor = MaterialTheme.colorScheme.onError,
                            actionButtonColor = MaterialTheme.colorScheme.error,
                            onClickActionButton = {
                                if (activity != null) {
                                    deleteAccount(context, activity)
                                }
                            },
                            onDismissRequest = { showDeleteAccountDialog = false },
                            standardPadding = standardPadding
                        )
                    }
                }
            }
        }

        item {
            Button(
                onClick = { showSignOutDialog = true },
                modifier = Modifier
                    .padding(top = standardPadding)
                    .widthIn(min = standardPadding * 10),
                shape = MaterialTheme.shapes.extraLarge,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                )
            ) {
                Text(
                    text = stringResource(R.string.sign_out),
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    style = MaterialTheme.typography.labelLarge
                )
            }

            if (showSignOutDialog) {
                DefaultDialog(
                    title = R.string.are_you_sure_you_want_to_sign_out,
                    description = null,
                    actionTextButton = R.string.sign_out,
                    actionTextButtonColor = MaterialTheme.colorScheme.onSecondary,
                    actionButtonColor = MaterialTheme.colorScheme.secondary,
                    onClickActionButton = {
                        if (activity != null) {
                            signOut(context, activity)
                        }
                    },
                    onDismissRequest = { showSignOutDialog = false },
                    standardPadding = standardPadding
                )
            }
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

fun signOut(
    context: Context,
    activity: Activity
) {
    val sharedPreferences = activity.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    // Xóa dữ liệu đăng nhập (SharedPreferences)
    sharedPreferences.edit().clear().apply()

    // Đăng xuất Firebase (nếu dùng Firebase)
    //FirebaseAuth.getInstance().signOut()

    // Đăng xuất OAuth / API backend nếu có
    // AuthManager.logout()

    // Chuyển về màn hình đăng nhập và xóa toàn bộ backstack
    activity.let {
        val intent = Intent(it, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        it.startActivity(intent)
        it.finish() // Kết thúc activity hiện tại
    }

    Toast.makeText(
        activity,
        context.getString(R.string.signed_out_successfully),
        Toast.LENGTH_SHORT
    ).show()
}

fun deleteAccount(
    context: Context,
    activity: Activity
) {
    val sharedPreferences = activity.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    sharedPreferences.edit().clear().apply()
    activity.let {
        val intent = Intent(it, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        it.startActivity(intent)
        it.finish()
    }
    Toast.makeText(
        activity,
        context.getString(R.string.deleted_account_successfully),
        Toast.LENGTH_SHORT
    ).show()
}

fun deleteData(
    context: Context,
    activity: Activity
) {
    val sharedPreferences = activity.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    sharedPreferences.edit().clear().apply()
    activity.let {
        val intent = Intent(it, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        it.startActivity(intent)
        it.finish()
    }
    Toast.makeText(
        activity,
        context.getString(R.string.deleted_data_successfully),
        Toast.LENGTH_SHORT
    ).show()
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
private fun ProfilePortraitScreenPreviewInLargePhone() {
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