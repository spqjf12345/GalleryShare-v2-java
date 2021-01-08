package com.example.project1_final.fragments

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.project1_final.GameView
import com.example.project1_final.R
import com.example.project1_final.RollingBallGameActivity
import kotlinx.android.synthetic.main.fragment_game.*
import kotlin.math.abs
import kotlin.math.sqrt


class GameFragmentTab : Fragment(), SensorEventListener {
    var name = ""

    private val colorList = mutableListOf(Color.MAGENTA, Color.GREEN, Color.DKGRAY, Color.CYAN)
    private var sensorManager: SensorManager? = null

    private var a_x:Float = 0f
    private var a_y:Float = 0f
    private var a_z:Float = 0f

    lateinit var mainHandler: Handler
    lateinit var gameView: GameView

    private var character = 0

    private val updatePosition = object : Runnable {
        override fun run() {
            gameView.updateCharacter(a_x, a_y)
            gameView.invalidate()
            mainHandler.postDelayed(this, 10)
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event!!.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            a_x = event.values[0]
            a_y = event.values[1]
            a_z = event.values[2]

            val accel_magnitude = sqrt(a_x*a_x + a_y*a_y + a_z*a_z) /9.8

            if (accel_magnitude>2.5){
                colorList.shuffle()
                gameView.bgColor = colorList[0]
                gameView.invalidate()
            }

        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sensorManager = requireContext().getSystemService(Context.SENSOR_SERVICE) as SensorManager

        return inflater.inflate(R.layout.fragment_game,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        gameView = ballWindow

        gameView.viewTreeObserver.addOnGlobalLayoutListener {
            if (gameView != null){
                val width = gameView.measuredWidth
                val height = gameView.measuredHeight
                gameView.screenMaxX = width.toFloat()
                gameView.screenMaxY = height.toFloat()+50f
            }
        }

        characterChangeBtn.setOnClickListener {
            character = abs(character-1)
            gameView.character = character
            when(character){
                0->{
                    gameView.friction_coef = 0.05f*9.8f
                    characterImg.setImageDrawable(resources.getDrawable(R.drawable.ball))
                }
                1 -> {
                    gameView.friction_coef = 0.05f*9.8f*9
                    characterImg.setImageDrawable(resources.getDrawable(R.drawable.airplane))
                }
            }
        }


        //handling aniamtion on button click

        gameStartBtn.setOnClickListener {
            val intent = Intent(context, RollingBallGameActivity::class.java)
            intent.putExtra("character", character)
            startActivity(intent)
        }



        mainHandler = Handler(Looper.getMainLooper())

    }

    override fun onResume() {
        super.onResume()
        sensorManager!!.registerListener(this, sensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL)
        mainHandler.post(updatePosition)

    }

    override fun onPause() {
        super.onPause()
        sensorManager!!.unregisterListener(this)
        mainHandler.removeCallbacks(updatePosition)
    }

}