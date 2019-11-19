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
                size = getScreenSize().timesPercent(.5)
            )
        ).withSlide {
            Slide(
                slideshow = this,
                background = ColorBackground(Color.WHITE)
            ).withComponent {
                TextComponent(
                    text = "Presentation",
                    font = Fonts.TITLE.font,
                    size = FitContent(width = PercentDimension(1.0)),
                    alignment = Pos.CENTER,
                    margin = AllSidesDimensions(
                        vertical = VerticalDimensions(
                            top = PercentDimension(
                                .3,
                                dimensionType = DimensionType.HEIGHT
                            )
                        )
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
                    size = FitContent(width = PercentDimension(1.0)),
                    margin = AllSidesDimensions(
                        vertical = VerticalDimensions(
                            top = PercentDimension(
                                .02,
                                dimensionType = DimensionType.WIDTH
                            )
                        )
                    ),
                    alignment = Pos.CENTER,
                    padding = null,
                    background = null,
                    parent = null,
                    slide = this
                )
            }
        }.withSlide {
            Slide(
                slideshow = this
            ).withComponent {
                TextComponent(
                    text = "Image",
                    font = Fonts.TITLE.font,
                    size = FitContent(width = PercentDimension(1.0)),
                    alignment = Pos.CENTER,
                    margin = AllSidesDimensions(
                        vertical = VerticalDimensions(
                            top = PercentDimension(
                                .2,
                                dimensionType = DimensionType.HEIGHT
                            )
                        )
                    ),
                    padding = null,
                    background = null,
                    parent = null,
                    slide = this
                )
            }.withComponent {
                ImageComponent(
                    url = "https://www.google.com/images/branding/googlelogo/2x/googlelogo_color_272x92dp.png",
                    sizeMultiplier = .5,
                    position = Position(.7, .7),
                    slide = this
                )
            }
        }
    }.createPresenter()

    val scene = presenter.scene
}