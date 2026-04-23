package zelimkhan.magomadov.contactsrevive.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import zelimkhan.magomadov.contactsrevive.ui.theme.ComfortaaFamily
import zelimkhan.magomadov.contactsrevive.ui.theme.Cream
import zelimkhan.magomadov.contactsrevive.ui.theme.Cream10

@Composable
fun ContactAvatar(
    name: String,
    modifier: Modifier = Modifier,
    size: Dp = 40.dp,
    fontSize: TextUnit = 15.sp,
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(Cream10),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = name.firstOrNull()?.uppercaseChar()?.toString() ?: "?",
            color = Cream,
            fontSize = fontSize,
            fontWeight = FontWeight.SemiBold,
            fontFamily = ComfortaaFamily,
        )
    }
}