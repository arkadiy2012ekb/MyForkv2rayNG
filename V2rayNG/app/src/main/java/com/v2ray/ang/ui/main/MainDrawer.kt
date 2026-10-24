package com.v2ray.ang.ui.main

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DrawerState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextGeometricTransform
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.v2ray.ang.R
import com.v2ray.ang.ui.compose.LocalDarkTheme
import com.v2ray.ang.ui.compose.verticalScrollbar

// Шрифт Source Sans 3 SemiBold Italic (похож на Myriad Pro Italic): файл res/font/source_sans3_semibold_italic.ttf
private val TitleFontFamily = FontFamily(
    Font(R.font.source_sans3_semibold_italic, FontWeight.SemiBold, FontStyle.Italic)
)

enum class MainDestination(
    @DrawableRes val iconRes: Int,
    @StringRes val labelRes: Int? = null,
    val label: String? = null
) {
    Subscriptions(R.drawable.ic_subscriptions_24dp, R.string.title_sub_setting),
    PerAppProxy(R.drawable.ic_per_apps_24dp, R.string.per_app_proxy_settings),
    Routing(R.drawable.ic_routing_24dp, R.string.routing_settings_title),
    UserAssets(R.drawable.ic_file_24dp, R.string.title_user_asset_setting),
    Settings(R.drawable.ic_settings_24dp, R.string.title_settings),
    Logcat(R.drawable.ic_logcat_24dp, R.string.title_logcat),
    VulnCheck(R.drawable.ic_promotion_24dp, label = "Проверка уязвимостей"),
    CheckUpdate(R.drawable.ic_check_update_24dp, R.string.update_check_for_update),
    BackupRestore(R.drawable.ic_restore_24dp, R.string.title_configuration_backup_restore),
    About(R.drawable.ic_about_24dp, R.string.title_about)
}

private val primaryDrawerItems = listOf(
    MainDestination.Subscriptions,
    MainDestination.PerAppProxy,
    MainDestination.Routing,
    MainDestination.UserAssets,
    MainDestination.Settings
)

private val drawerItems = primaryDrawerItems + listOf(
    MainDestination.Logcat,
    MainDestination.VulnCheck,
    MainDestination.BackupRestore,
    MainDestination.About
)

@Composable
fun MainDrawerContent(drawerState: DrawerState, onNavigate: (MainDestination) -> Unit) {
    val drawerScrollState = rememberScrollState()
    val isDarkTheme = LocalDarkTheme.current
    val itemColor = MaterialTheme.colorScheme.onSurfaceVariant
    // Фон меню: нейтральный серый, 95% от белого (#F2F2F2), без оттенка (светлая тема)
    val containerColor = if (isDarkTheme) MaterialTheme.colorScheme.surface else Color(0xFFF2F2F2)
    // Тёмная тема: края шапки как раньше
    val headerBaseColor = containerColor
    // Цвет надписи и ника (светлая тема: #48454E с макета), тень: чистый белый в светлой теме, тёмная в тёмной
    val titleColor = if (isDarkTheme) MaterialTheme.colorScheme.onSurface else Color(0xFF48454E)
    val titleShadowColor = if (isDarkTheme) Color.Black.copy(alpha = 0.35f) else Color.White

    ModalDrawerSheet(
        drawerState = drawerState,
        modifier = Modifier
            // Чуть шире (+примерно 3 dp), чтобы закрыть элементы, выступающие у края
            .fillMaxWidth(0.843f)
            .windowInsetsPadding(WindowInsets.systemBars),
        drawerContainerColor = containerColor,
        windowInsets = WindowInsets(0, 0, 0, 0)
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(drawerScrollState)
                .verticalScrollbar(drawerScrollState)
        ) {
            // Шапка: значок, название и ник
            val glowColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.30f)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .drawBehind {
                        if (isDarkTheme) {
                            // Тёмная тема: без изменений
                            drawRect(
                                Brush.radialGradient(
                                    colors = listOf(glowColor, headerBaseColor),
                                    center = center,
                                    radius = size.width * 0.6f
                                )
                            )
                        } else {
                            // Светлая тема: один сплошной цвет, без градиента (без полос)
                            drawRect(Color(0xFF71A5AF))
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Значок: V в ровном светлом круге (95% от белого) с плавной тенью «на 5 часов»
                    Box(
                        modifier = Modifier.size(100.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                // Сдвиг круга влево, подальше от надписи
                                .offset(x = (-12).dp)
                                .size(78.dp)
                                // Плавная тень, спрятанная под круг: малый сдвиг вправо-вниз, короткое затухание
                                .drawBehind {
                                    val circleRadius = size.minDimension / 2f
                                    val blur = 3.dp.toPx()
                                    val shadowRadius = circleRadius + blur
                                    val shadowCenter = Offset(
                                        center.x + 2.dp.toPx(),
                                        center.y + 3.dp.toPx()
                                    )
                                    val inner = ((circleRadius - 3.dp.toPx()) / shadowRadius)
                                        .coerceIn(0f, 0.95f)
                                    val span = 1f - inner
                                    val a = 0.30f
                                    drawCircle(
                                        brush = Brush.radialGradient(
                                            0.0f to Color.Black.copy(alpha = a),
                                            inner to Color.Black.copy(alpha = a),
                                            inner + span * 0.33f to Color.Black.copy(alpha = a * 0.60f),
                                            inner + span * 0.66f to Color.Black.copy(alpha = a * 0.22f),
                                            1.0f to Color.Transparent,
                                            center = shadowCenter,
                                            radius = shadowRadius
                                        ),
                                        radius = shadowRadius,
                                        center = shadowCenter
                                    )
                                }
                                .background(
                                    color = if (isDarkTheme) Color(0xFF2B2B2B) else Color(0xFFF2F2F2),
                                    shape = CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(R.mipmap.ic_launcher_foreground),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(54.dp)
                                    .scale(2.3f),
                                colorFilter = if (isDarkTheme) {
                                    ColorFilter.tint(Color.White, BlendMode.SrcIn)
                                } else {
                                    null
                                }
                            )
                        }
                    }
                    // Ник над названием (по правому краю) и название: Source Sans 3 SemiBold Italic
                    // с дополнительным наклоном (как у Myriad Pro Italic) и белой тенью
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "@arkadiy2012ekb",
                            fontSize = 11.sp,
                            fontFamily = TitleFontFamily,
                            fontStyle = FontStyle.Italic,
                            fontWeight = FontWeight.SemiBold,
                            color = titleColor,
                            style = TextStyle(
                                textGeometricTransform = TextGeometricTransform(scaleX = 1f, skewX = -0.07f),
                                shadow = Shadow(
                                    color = titleShadowColor,
                                    offset = Offset(3f, 4f),
                                    blurRadius = 2f
                                )
                            ),
                            modifier = Modifier.offset(x = 4.dp, y = 16.dp)
                        )
                        Text(
                            text = stringResource(R.string.app_name),
                            fontSize = 47.sp,
                            fontFamily = TitleFontFamily,
                            fontStyle = FontStyle.Italic,
                            fontWeight = FontWeight.SemiBold,
                            color = titleColor,
                            style = TextStyle(
                                textGeometricTransform = TextGeometricTransform(scaleX = 1f, skewX = -0.07f),
                                shadow = Shadow(
                                    color = titleShadowColor,
                                    offset = Offset(4f, 5f),
                                    blurRadius = 3f
                                )
                            ),
                            modifier = Modifier.offset(y = 7.dp)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            drawerItems.forEachIndexed { index, item ->
                if (index == primaryDrawerItems.size) {
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 28.dp, vertical = 8.dp),
                        color = itemColor.copy(alpha = 0.7f)
                    )
                }
                // Свой пункт меню: меньше отступов, поэтому подписи помещаются в одну строку
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp)
                        .clip(RoundedCornerShape(24.dp))
                        .clickable { onNavigate(item) }
                        .heightIn(min = 48.dp)
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(item.iconRes),
                        contentDescription = null,
                        tint = itemColor
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = item.labelRes?.let { stringResource(it) } ?: item.label.orEmpty(),
                        fontSize = 15.sp,
                        letterSpacing = 0.1.sp,
                        fontWeight = FontWeight.Medium,
                        color = itemColor
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}
