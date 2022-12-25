package com.chainpilots.identicon.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.chainpilots.identicon.IdenticonGenerator


@Composable
fun Jazzicon(
    modifier: Modifier = Modifier,
    diameter: Int = 100,
    address: String = "0xffffffff"
) {
    val shapes = IdenticonGenerator()
        .generate(
            address = address,
            diameter = diameter,
        )
    val center = diameter / 2

    Canvas(
        modifier = Modifier
            .width(diameter.dp)
            .aspectRatio(1f)
            .clip(CircleShape)
            .then(modifier)
    ) {
        shapes.forEach {
            translate(it.x.dp.toPx(), it.y.dp.toPx()) {
                rotate(
                    it.rotation,
                    pivot = Offset(center.dp.toPx(), center.dp.toPx())
                ) {
                    drawRect(
                        color = Color.hsl(
                            hue = it.fill.hue,
                            saturation = it.fill.saturation,
                            lightness = it.fill.lightness
                        ),
                        size = Size(diameter.dp.toPx(), diameter.dp.toPx())
                    )
                }
            }
        }
    }

}

@Preview
@Composable
fun DefaultJazzicon() {
    Jazzicon(
        modifier = Modifier,
        diameter = 80,
        address = "0xA05d08146c5a",
    )
}