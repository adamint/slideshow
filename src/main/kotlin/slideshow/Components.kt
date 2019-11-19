package slideshow

import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Node
import javafx.scene.control.Label
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
    val parent: Component? = null,
    val slide: Slide?
) {
    abstract fun toComponent(parent: VBox): Node
    fun getRenderableComponent(): Node {
        val vbox = VBox()
        val component = toComponent(vbox)
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

class TextComponent(
    val text: String,
    size: Size,
    slide: Slide,
    val font: Font? = null,
    val color: Color? = null,
    val textAlignment: TextAlignment? = null,
    alignment: Pos? = null,
    margin: AllSidesDimensions<out Dimension>? = null,
    padding: AllSidesDimensions<out Dimension>? = null,
    background: Background? = null,
    parent: Component? = null
) : Component(size, margin, padding, background, alignment, parent, slide) {
    override fun toComponent(parent: VBox): Node {
        return Label(text).apply {
            this@TextComponent.color?.let { textFill = it }
            this@TextComponent.font?.let { font = it }
            this@TextComponent.textAlignment?.let { textAlignment = it }
            this@TextComponent.alignment?.let { alignment = it }
            this@TextComponent.getWidth()?.let { prefWidth = it }
            this@TextComponent.getHeight()?.let { prefHeight = it }

        }
    }
}