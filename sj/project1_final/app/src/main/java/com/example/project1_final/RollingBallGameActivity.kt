package com.example.project1_final

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.ColorFilter
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.project1_final.adapter.RecordCursorAdapter
import com.example.project1_final.model.Record
import com.example.project1_final.model.RecordDatabase
import kotlinx.android.synthetic.main.activity_rolling_ball_game.*
import java.lang.Integer.parseInt
import java.text.SimpleDateFormat
import java.util.*
import kotlin.random.Random.Default.nextFloat
import kotlin.random.Random.Default.nextInt


class RollingBallGameActivity: AppCompatActivity(), SensorEventListener{

    private var sensorManager: SensorManager? = null

    //private val colorList = mutableListOf(Color.MAGENTA, Color.GREEN, Color.DKGRAY, Color.CYAN)

    private var a_x:Float = 0f
    private var a_y:Float = 0f
    private var a_z:Float = 0f

    lateinit var mainHandler: Handler
    lateinit var gameView: GameView

    lateinit var restartButton: Button
    lateinit var quitButton: Button

    private var nameText = ""
    private var startTime: Date? = Date()
    private var endTime: Date? = null

    private var freezeTime:Int = 0

    private var timer = 0

    private var num_bullet = 5f


    private val colorList = listOf<Int>(
        Color.parseColor("#E1BEE7"),
        Color.parseColor("#CE93D8"),
        Color.parseColor("#BA68C8"),
        Color.parseColor("#AB47BC"),
        Color.parseColor("#9C27B0"),
        Color.parseColor("#8E24AA"),
        Color.parseColor("#7B1FA2"),
        Color.parseColor("#6A1B9A"),
        Color.parseColor("#4A148C"))


    private fun editName(){
        val edittext = EditText(this)

        val builder = AlertDialog.Builder(this)
        builder.setTitle("What's your name?")
        builder.setMessage("Name for ranking. If you cancel, you're record will be stored as Unknown's record")
        builder.setView(edittext)
        builder.setPositiveButton(
            "OK"){ dialog, which ->
            nameText = edittext.text.toString()
            dialog.dismiss()
            mainHandler.post(updateDBandShowLeadearboard)
        }

        builder.setNegativeButton(
            "Cancel"
        ) { dialog, which ->
            nameText = "Unkown"
            dialog.dismiss()
            mainHandler.post(updateDBandShowLeadearboard)
        }
        builder.show()
    }

    private val endGame: Runnable = object:Runnable {
        override fun run() {
            editName()
        }
    }

    private val updateDBandShowLeadearboard = Runnable {
        val record = Record().apply{
            name = nameText
            score = gameView.score
            time = (endTime!!.minutes - startTime!!.minutes)*60 + (endTime!!.seconds-startTime!!.seconds)
        }


        // update database
        val recordDatabase = RecordDatabase.getInstance(applicationContext)
        recordDatabase?.insert(record)

        // update view
        records.layoutManager = LinearLayoutManager(applicationContext)
        records.adapter = RecordCursorAdapter(
            applicationContext,
            recordDatabase?.cursor!!
        )

        records.visibility = View.VISIBLE


    }

    val yellow = Color.parseColor("#FF9800")
    val red = Color.parseColor("#F6412D")


    private val updatePosition = object : Runnable {
        fun normalUpdate(){
            if (freezeTime > 0){
                gameView.bulletfrozen = true
                freezeTime -= 1
            }
            else gameView.bulletfrozen = false

            gameView.updateCharacter(a_x, a_y)
            gameView.invalidate()

            if (nextFloat()>0.98 && !gameView.bulletfrozen){
                gameView.loadRandomBullet(nextInt(1, num_bullet.toInt()))
            }
            mainHandler.postDelayed(this, 10)
        }

        override fun run() {
            timer ++

            if (timer.rem(500)==0) {
                gameView.speed += 0.5f
                val level=((gameView.speed-1)*2 +1).toInt()
                num_bullet = num_bullet+0.3f
                levelText.text = "Level ${level}"
                if (!gameView.isFever) rollingballframe.setBackgroundColor(colorList[(level-1).rem(colorList.size)])
            }

            val level=((gameView.speed-1)*2 +1).toInt()

            if (gameView.gage == gameView.gageMax){
                feverBtn.setColorFilter(red)
                feverBtn.setBackgroundColor(yellow)
            }

            if (gameView.feverTime == 0 && gameView.isFever){
                gameView.isFever = false
                rollingballframe.setBackgroundColor(colorList[(level-1).rem(colorList.size)])
            }



            when (gameView.life) {
                3 -> {
                    normalUpdate()
                    scoreText.text = "Score : ${gameView.score}"
                }
                2 -> {
                    normalUpdate()
                    heart3.setColorFilter(Color.BLACK)
                    scoreText.text = "Score : ${gameView.score}"
                }
                1 -> {
                    normalUpdate()
                    heart2.setColorFilter(Color.BLACK)
                    scoreText.text = "Score : ${gameView.score}"
                }
                else -> {
                    endTime = Date()
                    // get result
                    gameView.invalidate()
                    heart1.setColorFilter(Color.BLACK)
                    restartButton.visibility = View.VISIBLE
                    quitButton.visibility = View.VISIBLE
                    runOnUiThread(endGame)
                }
            }
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event!!.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            a_x = event.values[0]
            a_y = event.values[1]
            a_z = event.values[2]

//            val accel_magnitude = sqrt(a_x*a_x + a_y*a_y + a_z*a_z)/9.8
//
//            if (accel_magnitude>2.5){
//                colorList.shuffle()
//                gameView.bgColor = colorList[0]
//                gameView.invalidate()
//            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //val gameView = GameView(this)
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager


        setContentView(R.layout.activity_rolling_ball_game)
        rollingballframe.setBackgroundColor(Color.parseColor("#F3E5F5"))
        gameView = findViewById(R.id.gameView)
        gameView.character = intent.getIntExtra("character", -1)
        when(gameView.character){
            0->{
                gameView.friction_coef = 0.05f*9.8f
            }
            1 -> {
                gameView.friction_coef = 0.05f*9.8f*9
            }
        }

        quitButton = quit
        restartButton = restart

        restartButton.setOnClickListener {
            finish()
            overridePendingTransition(0, 0)
            val intent = Intent(this, RollingBallGameActivity::class.java)
            intent.putExtra("character", gameView.character)
            startActivity(intent)
            overridePendingTransition(0, 0)
        }

        quitButton.setOnClickListener {
            finish()
        }

        bombItem.setOnClickListener {
            if (gameView.num_bomb > 0){
                gameView.bombtrue = true
                gameView.num_bomb -= 1
                bombText.text = gameView.num_bomb.toString()
            }
        }

        freezeItem.setOnClickListener {
            if (gameView.num_freeze > 0){
                freezeTime = 400
                gameView.num_freeze -= 1
                freezeText.text = gameView.num_freeze.toString()
            }
        }

        feverBtn.setOnClickListener {
            if (gameView.gage == gameView.gageMax){
                gameView.feverTime = 500
                gameView.isFever = true
                gameView.gage = 0f
                rollingballframe.setBackgroundColor(yellow)
                feverBtn.setColorFilter(Color.BLACK)
                feverBtn.setBackgroundColor(Color.GRAY)
            }
        }

        mainHandler = Handler(Looper.getMainLooper())

    }

    override fun onResume() {
        super.onResume()
        sensorManager!!.registerListener(
            this,
            sensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
            SensorManager.SENSOR_DELAY_NORMAL
        )
        mainHandler.post(updatePosition)
    }

    override fun onPause() {
        super.onPause()
        sensorManager!!.unregisterListener(this)
        mainHandler.removeCallbacks(updatePosition)
    }




}
