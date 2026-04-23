package zelimkhan.magomadov.contactsrevive.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import zelimkhan.magomadov.contactsrevive.ui.theme.OnSurfaceVariant
import zelimkhan.magomadov.contactsrevive.ui.theme.Primary

@Composable
fun AlphabeticalScroller(
    alphabet: List<String>,
    onLetterSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var selectedLetter by remember { mutableStateOf<String?>(null) }
    var itemHeight by remember { mutableFloatStateOf(0f) }
    var containerPosition by remember { mutableStateOf(0f) }
    val density = LocalDensity.current

    Box(
        modifier = modifier
            .width(40.dp)
            .fillMaxHeight()
            .pointerInput(alphabet) {
                detectVerticalDragGestures(
                    onDragStart = { offset ->
                        val index =
                            ((offset.y) / itemHeight).toInt().coerceIn(0, alphabet.lastIndex)
                        selectedLetter = alphabet[index]
                        onLetterSelected(alphabet[index])
                    },
                    onDragEnd = { selectedLetter = null },
                    onDragCancel = { selectedLetter = null },
                    onVerticalDrag = { change, _ ->
                        val index = ((change.position.y) / itemHeight).toInt()
                            .coerceIn(0, alphabet.lastIndex)
                        if (selectedLetter != alphabet[index]) {
                            selectedLetter = alphabet[index]
                            onLetterSelected(alphabet[index])
                        }
                    }
                )
            }
            .onGloballyPositioned { containerPosition = it.positionInRoot().y },
        contentAlignment = Alignment.CenterEnd
    ) {
        Column(
            modifier = Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            alphabet.forEach { letter ->
                val isSelected = selectedLetter == letter
                val scale by animateFloatAsState(if (isSelected) 1.5f else 1f, label = "")

                Text(
                    text = letter,
                    modifier = Modifier
                        .onGloballyPositioned {
                            if (itemHeight == 0f) itemHeight = it.size.height.toFloat()
                        }
                        .padding(vertical = 2.dp),
                    color = if (isSelected) Primary else OnSurfaceVariant,
                    fontSize = 10.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp * scale)
                )
            }
        }

        // Вылетающая буква слева (Magnifier effect)
        selectedLetter?.let { letter ->
            val index = alphabet.indexOf(letter)
            val yOffset = with(density) { (index * itemHeight).toDp() + (itemHeight / 2).toDp() }

            Box(
                modifier = Modifier
                    .offset(x = (-50).dp, y = yOffset - 30.dp)
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(Primary)
                    .alpha(0.9f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = letter,
                    color = Color.Black,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
