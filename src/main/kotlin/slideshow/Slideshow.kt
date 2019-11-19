package slideshow

import javafx.geometry.Insets
import javafx.scene.Group
import javafx.scene.Node
import javafx.scene.image.Image
import javafx.scene.layout.*
import javafx.scene.layout.Background
import javafx.scene.paint.Color
import javafx.stage.Stage

val defaultFontFamily = "Helvetica"

data class Slideshow(val options: SlideshowOptions) {
    val slides: MutableList<Slide> = mutableListOf()

    fun withSlide(slide: Slideshow.() -> Slide): Slideshow = apply { slides.add(slide()) }
}

data class SlideshowOptions(val size: AbsoluteSize)

interface Dimension {
    fun getValue(component: Component): Double?
    fun getValue(slide: Slide): Double?
}

data class XYDimension(val number: Double) : Dimension {
    override fun getValue(slide: Slide): Double? {
        return number
    }

    override fun getValue(component: Component): Double? {
        return number
    }
}

data class PercentDimension(
    val percent: Double,
    val dimensionOf: DimensionOf = DimensionOf.SLIDESHOW,
    val dimensionType: DimensionType = DimensionType.WIDTH
) : Dimension {
    override fun getValue(slide: Slide): Double? {
        return when (dimensionType) {
            DimensionType.WIDTH -> slide.slideshow.options.size.width.number * percent
            DimensionType.HEIGHT -> slide.slideshow.options.size.height.number * percent
        }
    }

    override fun getValue(component: Component): Double? {
        return when (dimensionOf) {
            DimensionOf.PARENT -> {
                if (dimensionType == DimensionType.WIDTH) component.parent!!.getWidth()?.times(percent)
                else component.parent!!.getHeight()?.times(percent)
            }
            DimensionOf.SLIDESHOW -> {
                if (dimensionType == DimensionType.WIDTH) component.slide.slideshow.options.size.width.number * percent
                else component.slide.slideshow.options.size.height.number * percent
            }
        }
    }
}

class FitContent(val width: Dimension? = null, val height: Dimension? = null) : Dimension, Size {
    override fun getValue(slide: Slide): Double? {
        return null
    }

    override fun getValue(component: Component): Double? {
        return null
    }

    override fun getWidthValue(component: Component): Double? {
        return width?.getValue(component)
    }

    override fun getHeightValue(component: Component): Double? {
        return height?.getValue(component)
    }
}

enum class DimensionOf {
    SLIDESHOW, PARENT
}

enum class DimensionType {
    WIDTH, HEIGHT
}

interface Size {
    fun getWidthValue(component: Component): Double?
    fun getHeightValue(component: Component): Double?
}

data class AbsoluteSize(val width: XYDimension, val height: XYDimension) : Size {
    override fun getWidthValue(component: Component): Double? {
        return width.number
    }

    override fun getHeightValue(component: Component): Double? {
        return height.number
    }

    fun timesPercent(percent: Double) =
        AbsoluteSize(XYDimension(width.number * percent), XYDimension(height.number * percent))
}

data class PercentSize(val width: PercentDimension, val height: PercentDimension) : Size {
    override fun getWidthValue(component: Component): Double? {
        return when (width.dimensionOf) {
            DimensionOf.PARENT -> {
                component.parent!!.size.getWidthValue(component.parent)
            }
            DimensionOf.SLIDESHOW -> {
                component.slide.slideshow.options.size.width.number
            }
        }
    }

    override fun getHeightValue(component: Component): Double? {
        return when (height.dimensionOf) {
            DimensionOf.PARENT -> {
                component.parent!!.size.getHeightValue(component.parent)
            }
            DimensionOf.SLIDESHOW -> {
                component.slide.slideshow.options.size.height.number
            }
        }
    }
}

data class VerticalDimensions<T : Dimension>(val top: T? = null, val bottom: T? = null)
data class HorizontalDimensions<T : Dimension>(val left: T? = null, val right: T? = null)
data class AllSidesDimensions<T : Dimension>(
    val horizontal: HorizontalDimensions<T>? = null,
    val vertical: VerticalDimensions<T>? = null
)

interface Background {
    fun toJavaFxBackground(): JavaFXBackground
}

data class UrlBackground(val url: String) : slideshow.Background {
    override fun toJavaFxBackground(): JavaFXBackground {
        return ImageBackground(Image(url)).toJavaFxBackground()
    }
}

data class ImageBackground(val image: Image) : slideshow.Background {
    override fun toJavaFxBackground(): JavaFXBackground {
        return Background(
            BackgroundImage(
                image,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT
            )
        )
    }
}

data class ColorBackground(val color: Color) : slideshow.Background {
    override fun toJavaFxBackground(): JavaFXBackground {
        return Background(
            BackgroundFill(color, CornerRadii.EMPTY, Insets.EMPTY)
        )
    }
}

data class Position(val xPercent: Double? = null, val yPercent: Double? = null)

class Slide(
    val background: slideshow.Background? = null,
    val padding: AllSidesDimensions<out Dimension>? = null,
    val slideshow: Slideshow
) {
    val components: MutableList<Component> = mutableListOf()

    fun withComponent(component: Slide.() -> Component) = apply { components.add(component()) }

    fun toComponent(stage: Stage): Node {
        val group = Group()
        val vbox = VBox()
        group.children.add(vbox)

        vbox.prefWidth = stage.width
        vbox.prefHeight = stage.height

        padding?.let {
            vbox.padding = Insets(
                padding.vertical?.top?.getValue(this) ?: 0.0,
                padding.horizontal?.right?.getValue(this) ?: 0.0,
                padding.vertical?.bottom?.getValue(this) ?: 0.0,
                padding.horizontal?.left?.getValue(this) ?: 0.0
            )
        }
        components.forEach { component ->
            val node = component.getRenderableComponent()
            if (component.position?.xPercent != null || component.position?.yPercent != null) {
                group.children.add(node)
            } else vbox.children += node
        }
        background?.let { vbox.background = it.toJavaFxBackground() }

        return group
    }

    fun getWidth() = slideshow.options.size.width.number
    fun getHeight() = slideshow.options.size.height.number
}