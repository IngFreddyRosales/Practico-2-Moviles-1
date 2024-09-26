package com.example.practico2.Field

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.example.practico2.MainActivity
import com.example.practico2.Objets.Snake
import kotlin.math.floor
import kotlin.random.Random

class Panel(context: Context?, attrs: AttributeSet?) : View(context, attrs) {



    // Inicializamos snake sin valores, para asignarlo en 'onSizeChanged'
    private lateinit var snake: Snake

    private val snakePaint = Paint().apply {
        color = Color.GREEN
        style = Paint.Style.FILL_AND_STROKE
    }

    private val foodPaint = Paint().apply {
        color = Color.RED
        style = Paint.Style.FILL_AND_STROKE
    }

    private var food = Pair(10, 5)

    // Este método es llamado cuando la vista cambia de tamaño (o cuando se dibuja por primera vez)
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        // Inicializamos la serpiente con las dimensiones correctas
        snake = Snake(40, w, h)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Dibujar la serpiente
        for (bloc in snake.getBody()) {
            canvas.drawRect(
                (bloc.first * 40).toFloat(),
                (bloc.second * 40).toFloat(),
                (bloc.first * 40 + 40).toFloat(),
                (bloc.second * 40 + 40).toFloat(),
                snakePaint
            )
        }

        // Dibujar la comida
        canvas.drawCircle(
            (food.first * 40 + 30).toFloat(),
            (food.second * 40 + 30).toFloat(),
            20f,
            foodPaint
        )

        // Mover la serpiente
        snake.move()

        // Comprobar colisión con la comida
        if (snake.getBody()[0] == food) {
            snake.addBloc()
            spawnFood()
            startFoodCollisionAnimation()
        }

        // Comprobar colisión consigo misma
        if (snake.checkSelfCollision()) {
            resetGame()
        }

        // Forzar la actualización del juego después de 200 ms
        postInvalidateDelayed(200)
    }

    private fun spawnFood() {
        food = Pair(floor(Math.random() * (width / 40)).toInt(), floor(Math.random() * (height / 40)).toInt())
    }

    private fun startFoodCollisionAnimation() {
        val scaleX = ObjectAnimator.ofFloat(this, "scaleX", 1f, 1.5f, 1f)
        val scaleY = ObjectAnimator.ofFloat(this, "scaleY", 1f, 1.5f, 1f)
        val animatorSet = AnimatorSet()
        animatorSet.playTogether(scaleX, scaleY)
        animatorSet.duration = 300
        animatorSet.start()
    }

    private fun resetGame() {
        AlertDialog.Builder(context)
            .setTitle("Game Over")
            .setMessage("You lost")
            .setPositiveButton("Restart") { _, _ ->
                val intent = (context as MainActivity).intent
                (context as MainActivity).finish()
                context.startActivity(intent)
            }
            .setNegativeButton("Exit") { _, _ ->
                (context as MainActivity).finish()
            }
            .show()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event?.action == MotionEvent.ACTION_DOWN) {
            val touchX = event.x
            val touchY = event.y

            val width = this.width
            val height = this.height

            val topHeight = height * 0.2
            val bottomHeight = height * 0.2
            val leftWidth = width * 0.5
            val rightWidth = width * 0.5

            val currentDireccion = snake.getCurrentDirection()

            if (touchY < topHeight && currentDireccion != Pair(0, 1)) { // hacia arriba
                snake.setDirection(Pair(0, -1))
            } else if (touchY > height - bottomHeight && currentDireccion != Pair(0, -1)) { // hacia abajo
                snake.setDirection(Pair(0, 1))
            } else if (touchX < leftWidth && currentDireccion != Pair(1, 0)) { // hacia la izquierda
                snake.setDirection(Pair(-1, 0))
            } else if (touchX > width - rightWidth && currentDireccion != Pair(-1, 0)) { // hacia la derecha
                snake.setDirection(Pair(1, 0))
            }
        }

        return true
    }
}
