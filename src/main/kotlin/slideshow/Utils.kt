package slideshow

import javafx.stage.Screen

fun getScreenSize(): AbsoluteSize {
    val screenSize = Screen.getPrimary().bounds
    return AbsoluteSize(XYDimension(screenSize.width), XYDimension(screenSize.height))
}

fun Number.toXYDimension() = XYDimension(this.toDouble())