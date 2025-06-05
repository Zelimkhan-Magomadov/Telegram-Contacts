package zelimkhan.magomadov.contactsrevive.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import zelimkhan.magomadov.contactsrevive.R

val ComfortaaFontFamily = FontFamily(
    Font(R.font.comfortaa_regular, FontWeight.Normal),
    Font(R.font.comfortaa_medium, FontWeight.Medium),
    Font(R.font.comfortaa_semi_bold, FontWeight.SemiBold),
    Font(R.font.comfortaa_bold, FontWeight.Bold)
)

val Typography = Typography(
    displayLarge = TextStyle(
        fontFamily = ComfortaaFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp,
    ),
    displayMedium = TextStyle(
        fontFamily = ComfortaaFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp,
    ),
    displaySmall = TextStyle(
        fontFamily = ComfortaaFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 20.sp,
    ),
    labelLarge = TextStyle(
        fontFamily = ComfortaaFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
    ),
    labelMedium = TextStyle(
        fontFamily = ComfortaaFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
    ),
    labelSmall = TextStyle(
        fontFamily = ComfortaaFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 10.sp,
    ),
    titleLarge = TextStyle(
        fontFamily = ComfortaaFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
    ),
)