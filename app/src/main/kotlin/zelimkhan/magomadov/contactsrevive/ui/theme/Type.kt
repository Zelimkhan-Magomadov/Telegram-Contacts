package zelimkhan.magomadov.contactsrevive.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import zelimkhan.magomadov.contactsrevive.R

val ComfortaaFamily = FontFamily(
    Font(R.font.comfortaa_regular, FontWeight.Normal),
    Font(R.font.comfortaa_medium, FontWeight.Medium),
    Font(R.font.comfortaa_semi_bold, FontWeight.SemiBold),
    Font(R.font.comfortaa_bold, FontWeight.Bold),
)

val AppTypography = Typography(
    displayLarge = TextStyle(
        fontFamily = ComfortaaFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp,
        letterSpacing = (-0.5).sp
    ),
    displayMedium = TextStyle(
        fontFamily = ComfortaaFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp
    ),
    displaySmall = TextStyle(
        fontFamily = ComfortaaFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp
    ),
    headlineLarge = TextStyle(
        fontFamily = ComfortaaFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = ComfortaaFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 17.sp
    ),
    headlineSmall = TextStyle(
        fontFamily = ComfortaaFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 15.sp
    ),
    titleLarge = TextStyle(
        fontFamily = ComfortaaFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp
    ),
    titleMedium = TextStyle(
        fontFamily = ComfortaaFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        letterSpacing = 0.1.sp
    ),
    titleSmall = TextStyle(
        fontFamily = ComfortaaFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        letterSpacing = 0.1.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = ComfortaaFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 15.sp,
        lineHeight = 22.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = ComfortaaFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 13.sp,
        lineHeight = 20.sp
    ),
    bodySmall = TextStyle(
        fontFamily = ComfortaaFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 18.sp
    ),
    labelLarge = TextStyle(
        fontFamily = ComfortaaFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp
    ),
    labelMedium = TextStyle(
        fontFamily = ComfortaaFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        letterSpacing = 0.3.sp
    ),
    labelSmall = TextStyle(
        fontFamily = ComfortaaFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 10.sp,
        letterSpacing = 0.3.sp
    ),
)