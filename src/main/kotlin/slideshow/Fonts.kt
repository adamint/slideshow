package slideshow

import javafx.scene.text.Font
import javafx.scene.text.FontWeight

enum class Fonts(val font: Font) {
    TITLE(Font.font(defaultFontFamily, FontWeight.BOLD, 50.0)),
    H2(Font.font(defaultFontFamily, FontWeight.NORMAL, 30.0))
}