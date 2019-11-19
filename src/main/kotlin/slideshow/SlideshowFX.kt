package slideshow

import javafx.application.Application
import javafx.application.Platform
import javafx.event.EventHandler
import javafx.scene.Node
import javafx.scene.Scene
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import javafx.scene.layout.VBox
import javafx.stage.Screen.getPrimary
import javafx.stage.Stage

lateinit var primaryStage: Stage

class SlideshowApplication : Application() {
    override fun start(primaryStage: Stage) {
        slideshow.primaryStage = primaryStage
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            launch(SlideshowApplication::class.java)
        }
    }

}

fun ((Stage) -> Slideshow).createPresenter() = Presenter(this)

class Presenter(val producer: (Stage) -> Slideshow) {
    lateinit var scene: Scene
    lateinit var stage: Stage
    lateinit var presentation: Presentation

    init {
        Thread {
            while (!::primaryStage.isInitialized) {
                Thread.sleep(10)
            }

            Platform.runLater {
                val slideshow = producer(primaryStage)
                val presentation = Presentation(slideshow)
                val scene = Scene(presentation)
                primaryStage.scene = scene
                primaryStage.width = slideshow.options.size.width.number
                primaryStage.height = slideshow.options.size.height.number

                presentation.begin()
                primaryStage.show()

                this.scene = scene
                this.stage = primaryStage
                this.presentation = presentation
            }
        }.start()
        Thread {
            SlideshowApplication.main(arrayOf())
        }.start()

        while(!::scene.isInitialized || !::stage.isInitialized || !::presentation.isInitialized) {
            Thread.sleep(50)
        }
    }
}

class Presentation(val slideshow: Slideshow) : VBox() {
    var index = -1
    val slides = slideshow.slides.zip(slideshow.slides.map { lazy { it.toComponent(primaryStage) } })

    var currentSlide: Pair<Slide, Lazy<Node>>? = null
    var keyHandler = EventHandler<KeyEvent> { event ->
        if (event.code == KeyCode.LEFT) previousSlide()
        else if (event.code == KeyCode.RIGHT) nextSlide()
    }

    fun previousSlide() {
        if (index > 0) index--
        setSlide(index)
    }

    fun nextSlide() {
        if (index < slideshow.slides.size - 1) index++
        setSlide(index)
    }

    fun setSlide(index: Int) {
        currentSlide?.let { (_, node) ->
            children.remove(node.value)
            node.value.removeEventHandler(KeyEvent.KEY_PRESSED, keyHandler)
        }

        currentSlide = slides[index]
        with(currentSlide!!) {
            this.second.value.addEventHandler(KeyEvent.KEY_PRESSED, keyHandler)
            children.add(slides[index].second.value)
            this.second.value.requestFocus()
        }

    }

    fun begin() {
        nextSlide()
    }

    private fun scaleToFit() {
        val screenBounds = getPrimary().getBounds()
        val prefWidth = (currentSlide!!.second as VBox).prefWidth
        val prefHeight = (currentSlide!!.second as VBox).prefHeight
        val scaleX = screenBounds.width / prefWidth
        val scaleY = screenBounds.height / prefHeight
        val centerX = screenBounds.width / 2 - prefWidth / 2
        val centerY = screenBounds.height / 2 - prefHeight / 2
        translateX = centerX
        translateY = centerY
        setScaleX(scaleX)
        setScaleY(scaleY)
    }
}

typealias JavaFXBackground = javafx.scene.layout.Background