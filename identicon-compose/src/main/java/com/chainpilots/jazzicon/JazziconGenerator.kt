package com.chainpilots.jazzicon

import android.util.Log
import com.chainpilots.jazzicon.model.HSL
import com.chainpilots.jazzicon.model.Shape
import com.chainpilots.jazzicon.utils.MersenneTwister
import com.chainpilots.jazzicon.utils.colors
import kotlin.math.*

class JazziconGenerator{
    private var remainingColors = mutableListOf<HSL>()

    fun generate(
        address: String = "0xffffffff",
        diameter: Int = 100,
        customColors: List<HSL> = listOf()
    ): List<Shape> {
        val seed = address
            .slice(IntRange(2, 9))
            .lowercase()
            .toUInt(16)

        val generator = MersenneTwister(seed)

        remainingColors = if (customColors.isEmpty() || customColors.size < 4) {
            hueShift(colors, generator).toMutableList()
        }else {
            hueShift(customColors, generator).toMutableList()
        }

        val shapes = mutableListOf<Shape>()
        shapes.add(
            Shape(
                x = 0F,
                y = 0F,
                rotation = 0F,
                fill = genColor(generator)
            )
        )
        for ( i in 0 until 3) {
            shapes.add(genShape(generator, diameter.toFloat(), i, 3))
        }
        return shapes.toList()
    }

    private fun genShape(
        generator: MersenneTwister,
        diameter: Float,
        i: Int,
        total: Int
    ): Shape {
        val firstRot = generator.random()
        val angle = PI * 2 * firstRot
        val velocity = diameter / total * generator.random() + (i * diameter / total)

        val tx = cos(angle) * velocity
        val ty = sin(angle) * velocity

        val secondRot = generator.random()
        val rot = (firstRot * 360) + secondRot * 180

        val fill = genColor(generator)

        return Shape(
            x = tx.toFloat(),
            y = ty.toFloat(),
            rotation = rot.toFloat(),
            fill = fill
        )
    }

    private fun genColor(generator: MersenneTwister): HSL {
        generator.random()
        val rand = generator.random()
        val size = remainingColors.size
        Log.i("JAZZICON", "$size * $rand = ${size*rand}")
        val idx = floor(remainingColors.size * rand).toInt()
        Log.i("JAZZICON", "index: $idx")
        val color = remainingColors[idx]
        remainingColors.removeAt(idx)
        return color
    }

    private fun hueShift(colors: List<HSL>, generator: MersenneTwister): List<HSL> {
        val wobble = 30
        val amount = ((generator.random() * 30) - ( wobble / 2)).toFloat()
        return colors.map {
            colorRotate(it, amount)
        }
    }

    private fun colorRotate(hsl: HSL, degrees: Float): HSL {
        var hue = hsl.hue
        hue = (hue + degrees) % 360
        hue = if (hue < 0) 360 + hue else hue
        return HSL(
            hue = hue,
            saturation = hsl.saturation,
            lightness = hsl.lightness
        )
    }
}