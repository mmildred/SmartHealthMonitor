package mx.utng.smarthealthmonitor.wear.watchface

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Typeface
import android.view.SurfaceHolder
import androidx.wear.watchface.Renderer
import androidx.wear.watchface.WatchState
import androidx.wear.watchface.style.CurrentUserStyleRepository
import mx.utng.smarthealthmonitor.data.SmartHealthRepository
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class SmartHealthRenderer(
    surfaceHolder: SurfaceHolder,
    currentUserStyleRepository: CurrentUserStyleRepository,
    watchState: WatchState,
    canvasType: Int
) : Renderer.CanvasRenderer2<Renderer.SharedAssets>(
    surfaceHolder,
    currentUserStyleRepository,
    watchState,
    canvasType,
    1000L,
    true
) {
    private val timePaint = Paint().apply {
        color = Color.WHITE
        textSize = 72f
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        isAntiAlias = true
        textAlign = Paint.Align.CENTER
    }

    private val secondsPaint = Paint().apply {
        color = Color.GRAY
        textSize = 22f
        isAntiAlias = true
        textAlign = Paint.Align.CENTER
    }

    private val fcPaint = Paint().apply {
        color = Color.RED
        textSize = 30f
        isAntiAlias = true
        textAlign = Paint.Align.CENTER
    }

    private val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
    private val secondsFormatter = DateTimeFormatter.ofPattern("ss")

    override fun render(canvas: Canvas, bounds: Rect, zonedDateTime: ZonedDateTime, sharedAssets: Renderer.SharedAssets) {
        canvas.drawColor(Color.BLACK)

        val centerX = bounds.centerX().toFloat()
        val centerY = bounds.centerY().toFloat()

        // Draw Time (HH:mm)
        canvas.drawText(zonedDateTime.format(timeFormatter), centerX, centerY, timePaint)

        // Draw Seconds (ss) below time
        canvas.drawText(zonedDateTime.format(secondsFormatter), centerX, centerY + 40f, secondsPaint)

        // Draw FC
        val fc = SmartHealthRepository.fcFlow.value
        if (fc > 0) {
            canvas.drawText("❤ $fc bpm", centerX, centerY + 90f, fcPaint)
        }
    }

    override fun renderHighlightLayer(canvas: Canvas, bounds: Rect, zonedDateTime: ZonedDateTime, sharedAssets: Renderer.SharedAssets) {
        // No highlight layer for now
    }

    override suspend fun createSharedAssets(): Renderer.SharedAssets {
        return object : Renderer.SharedAssets {
            override fun onDestroy() {}
        }
    }
}