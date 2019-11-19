package slideshow

import javafx.geometry.Pos
import javafx.scene.control.Label
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.stage.Stage

fun main() {
    val presenter = { stage: Stage ->
        Slideshow(
            SlideshowOptions(
                size = getScreenSize().timesPercent(50.0)
            )
        ).withSlide {
            Slide(
                slideshow = this,
                background = ColorBackground(Color.WHITE)
            ).withComponent {
                TextComponent(
                    text = "Presentation",
                    font = Fonts.TITLE.font,
                    size = FitContent(width = PercentDimension(100.0)),
                    alignment = Pos.CENTER,
                    margin = AllSidesDimensions(
                        vertical = VerticalDimensions(top = PercentDimension(30.0, dimensionType = DimensionType.HEIGHT))
                    ),
                    padding = null,
                    background = null,
                    parent = null,
                    slide = this
                )
            }.withComponent {
                TextComponent(
                    text = "Adam Ratzman",
                    font = Fonts.H2.font,
                    size = FitContent(width = PercentDimension(100.0)),
                    margin = AllSidesDimensions(vertical = VerticalDimensions(top = PercentDimension(2.0, dimensionType = DimensionType.WIDTH))),
                    alignment = Pos.CENTER,
                    padding = null,
                    background = null,
                    parent = null,
                    slide = this
                )
            }
        }.withSlide {
            Slide(
                slideshow = this,
                background = ColorBackground(Color.RED)
            ).withComponent {
                TextComponent(
                    text = "Presentation",
                    font = Fonts.TITLE.font,
                    size = FitContent(width = PercentDimension(100.0)),
                    alignment = Pos.CENTER,
                    margin = AllSidesDimensions(
                        vertical = VerticalDimensions(top = PercentDimension(30.0, dimensionType = DimensionType.HEIGHT))
                    ),
                    padding = null,
                    background = null,
                    parent = null,
                    slide = this
                )
            }.withComponent {
                TextComponent(
                    text = "Adam Ratzman",
                    font = Fonts.H2.font,
                    size = FitContent(width = PercentDimension(100.0)),
                    margin = AllSidesDimensions(vertical = VerticalDimensions(top = PercentDimension(2.0, dimensionType = DimensionType.WIDTH))),
                    alignment = Pos.CENTER,
                    padding = null,
                    background = null,
                    parent = null,
                    slide = this
                )
            }
        }
    }.createPresenter()

    val scene = presenter.scene
    val label = (((scene.focusOwner) as VBox).children[0] as VBox).children[0] as Label
    println(label.width)
    println(label.textAlignment)
}