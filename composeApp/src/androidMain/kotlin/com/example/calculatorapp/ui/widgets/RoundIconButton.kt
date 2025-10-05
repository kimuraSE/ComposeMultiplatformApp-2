package com.example.calculatorapp.ui.widgets


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


class RoundIconButton {

    private val IconButtonSizeModifier = Modifier.then(Modifier.padding(8.dp))

    @Composable
    fun RoundIconBtn(
        modifier: Modifier,
        imageVector: ImageVector,
        onClick: () -> Unit,
        tint: Color = Color.Black.copy(alpha = 0.6f),
        backgroundColor: Color = Color.White,
        customElvation: Dp = 4.dp,
    ) {

        Card(
            modifier = modifier
                .padding(all = 4.dp)
                .clickable {
                    onClick.invoke()
                }
                .then(IconButtonSizeModifier),
            shape = CircleShape,
            elevation = CardDefaults.cardElevation(
                defaultElevation = customElvation,
                pressedElevation = 0.dp,
                focusedElevation = 0.dp,
                hoveredElevation = 0.dp,
            ),
            colors = CardDefaults.cardColors(
                containerColor = backgroundColor
            )
        ) {
            Icon(
                imageVector = imageVector,
                contentDescription = "plus or minus icon",
                tint = tint,
                modifier = Modifier
                    .padding(4.dp)
                    .then(IconButtonSizeModifier).background(backgroundColor)
            )

        }

    }
}