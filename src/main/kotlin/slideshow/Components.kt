package slideshow

import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Node
import javafx.scene.control.Label
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.BorderPane
import javafx.scene.layout.Pane
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.scene.text.Font
import javafx.scene.text.TextAlignment

abstract class Component(
    val size: Size,
    val margin: AllSidesDimensions<out Dimension>? = null,
    val padding: AllSidesDimensions<out Dimension>? = null,
    val background: Background? = null,
    val alignment: Pos? = null,
    val position: Position? = null,
    val parent: Component? = null,
    val slide: Slide
) {
    abstract fun toComponent(parent: VBox): Node
    fun getRenderableComponent(): Node {
        val vbox = VBox()
        val component = toComponent(vbox)
        position?.xPercent?.let { vbox.translateX = slide.getWidth() * it}
        position?.yPercent?.let { vbox.translateY = slide.getHeight() * it  }

        vbox.children.add(component)

        background?.let { vbox.background = it.toJavaFxBackground() }
        getWidth()?.let { vbox.prefWidth = it }
        getHeight()?.let { vbox.prefHeight = it }

        margin?.let {
            VBox.setMargin(
                component, Insets(
                    margin.vertical?.top?.getValue(this) ?: 0.0,
                    margin.horizontal?.right?.getValue(this) ?: 0.0,
                    margin.vertical?.bottom?.getValue(this) ?: 0.0,
                    margin.horizontal?.left?.getValue(this) ?: 0.0
                )
            )
        }

        padding?.let {
            vbox.padding = Insets(
                padding.vertical?.top?.getValue(this) ?: 0.0,
                padding.horizontal?.right?.getValue(this) ?: 0.0,
                padding.vertical?.bottom?.getValue(this) ?: 0.0,
                padding.horizontal?.left?.getValue(this) ?: 0.0
            )
        }

        return vbox
    }

    fun getWidth() = size.getWidthValue(this)
    fun getHeight() = size.getHeightValue(this)
}

class ImageComponent(
    val url: String? = null,
    val image: Image? = null,
    val imageView: ImageView = url?.let { ImageView(it) } ?: ImageView(image!!),
    val sizeMultiplier: Double = 1.0,
    size: Size = AbsoluteSize((imageView.image.width * sizeMultiplier).toXYDimension(), (imageView.image.height * sizeMultiplier).toXYDimension()),
    slide: Slide,
    val center: Boolean = false,
    position: Position? = null,
    margin: AllSidesDimensions<out Dimension>? = null,
    padding: AllSidesDimensions<out Dimension>? = null,
    parent: Component? = null
) : Component(size, margin,padding,null,null, position, parent,slide) {
    override fun toComponent(parent: VBox): Node {
        val component= imageView.apply {
             getWidth()?.let { fitWidth = it }
            getHeight()?.let { fitHeight = it }
        }

        return if (center) BorderPane(component) else component
    }
}

class TextComponent(
    val text: String,
    size: Size,
    slide: Slide,
    val image: Image? = null,
    val font: Font? = null,
    val color: Color? = null,
    val textAlignment: TextAlignment? = null,
    position: Position? = null,
    alignment: Pos? = null,
    margin: AllSidesDimensions<out Dimension>? = null,
    padding: AllSidesDimensions<out Dimension>? = null,
    background: Background? = null,
    parent: Component? = null
) : Component(size, margin, padding, background, alignment, position, parent, slide) {
    override fun toComponent(parent: VBox): Node {
        return Label(text, ImageView(image)).apply {
            this@TextComponent.color?.let { textFill = it }
            this@TextComponent.font?.let { font = it }
            this@TextComponent.textAlignment?.let { textAlignment = it }
            this@TextComponent.alignment?.let { alignment = it }
            this@TextComponent.getWidth()?.let { prefWidth = it }
            this@TextComponent.getHeight()?.let { prefHeight = it }
        }
    }
}