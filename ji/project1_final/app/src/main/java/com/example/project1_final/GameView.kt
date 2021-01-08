package com.example.project1_final

import android.content.Context
import android.graphics.*
import android.os.Build
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.annotation.RequiresApi
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sqrt
import kotlin.random.Random.Default.nextFloat

class GameView@JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : View(context, attrs, defStyle){

    var posX:Float
    var posY:Float
    var v_x: Float = 10f
    var v_y: Float = 10f
    var a_x:Float = 0f
    var a_y: Float = 0f
    var friction_coef = 0.05f*9.8f
    var bgColor = Color.CYAN
    var screenMaxX = 1080f
    var screenMaxY = 1920f


    var life = 3
    var score = 0
    var character = 0

    var bulletList = mutableListOf<Bullet>()

    var bombtrue = false
    var num_bomb = 3

    var bulletfrozen = false
    var num_freeze = 3

    var speed:Float = 1f

    var feverTime = 0
    var isFever = false

    //@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    //val airplane = VectorDrawableCompat.create(getContext().getResources(), R.drawable.airplane, null).apply { setBounds() }

//    val textPaint:Paint = Paint().apply{
//        isFilterBitmap = true
//        isAntiAlias = true
//        color = Color.WHITE
//        textSize = 80f
//    }


    init {
        posX = 500f
        posY = 600f

        when(character){
            0->{
                friction_coef = 0.05f*9.8f
            }
            1 -> {
                friction_coef = 0.05f*9.8f*9
            }
        }
    }

    val blackPaint = Paint().apply{
        isFilterBitmap = true
        isAntiAlias = true
        color = Color.BLACK
    }
    val goodbulletPaint:Paint = Paint().apply {
        isFilterBitmap = true
        isAntiAlias = true
        color = Color.parseColor("#87ceeb")
    }
    val badbulletPaint: Paint = Paint().apply {
        isFilterBitmap = true
        isAntiAlias = true
        color = Color.parseColor("#4A412A")
    }
    val paint: Paint = Paint().apply {
        isFilterBitmap = true
        isAntiAlias = true
        color = Color.parseColor("#50bcdf")
    }
//    val gagePaint = Paint().apply {
//        isFilterBitmap = true
//        isAntiAlias = true
//        color = Color.parseColor("#50bcdf")
//    }

    val feverbulletpaint: Paint = Paint().apply {
        isFilterBitmap = true
        isAntiAlias = true
        color = Color.parseColor("#FFEC19")
    }
    val feverCharacterPaint: Paint = Paint().apply {
        isFilterBitmap = true
        isAntiAlias = true
        color = Color.parseColor("#F6412D")
    }


    var gage = 0f
    val gageMax = 5f


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun draw(canvas: Canvas?) {
        super.draw(canvas)
        //canvas?.drawColor(bgColor)
        canvas?.drawRoundRect(510f, 1650f, 940f, 1750f,35f, 35f, blackPaint)
        canvas?.drawRoundRect(520f, 1660f, 520f+410f*(gage/gageMax), 1740f,30f, 30f, feverbulletpaint)
        when (character){
            0 -> {

                if (isFever){
                    canvas?.drawCircle(posX, posY, 50f, feverCharacterPaint)
                    for (bullet in bulletList){
                        canvas?.drawCircle(bullet.posX, bullet.posY, 25f, feverbulletpaint)
                        if (!bulletfrozen){
                            bullet.posY += bullet.velocity
                        }

                        if (sqrt((bullet.posX-posX)*(bullet.posX-posX)+(bullet.posY-posY)*(bullet.posY-posY))<75f){
                            score += 3
                        }
                    }
                    feverTime -= 1
                }
                else{
                    canvas?.drawCircle(posX, posY, 50f, paint)
                    for (bullet in bulletList){
                        canvas?.drawCircle(bullet.posX, bullet.posY, 25f, if (bullet.good) goodbulletPaint else badbulletPaint)
                        if (!bulletfrozen){
                            bullet.posY += bullet.velocity
                        }

                        if (sqrt((bullet.posX-posX)*(bullet.posX-posX)+(bullet.posY-posY)*(bullet.posY-posY))<75f){
                            if (bullet.good){
                                score += 1
                                gage = min(gage+1f,gageMax)
                            }
                            else {
                                life -= 1
                            }
                        }
                    }
                }

                if (bombtrue){
                    score += bulletList.size
                    bulletList = mutableListOf()
                    bombtrue = false
                }
                else{
                    bulletList = bulletList.filter{ bullet ->
                        sqrt((bullet.posX-posX)*(bullet.posX-posX)+(bullet.posY-posY)*(bullet.posY-posY))>75f
                                && bullet.posY<1920} as MutableList<Bullet>
                }

            }
            1->{

                val checkBulletinHitmap: (Bullet) -> Boolean = {bullet ->
                    ((bullet.posX<(posX+20) && bullet.posX>(posX-20))
                            && (bullet.posY<(posY+75) && bullet.posY>(posY-75)))
                            ||((bullet.posX<(posX+150f) && bullet.posX>(posX-150f))
                            && (bullet.posY<(posY+8.5f) && bullet.posY>(posY-10f)))}

                if (isFever){
                    canvas?.drawArc(posX - 150f, posY-10f, posX+50f, posY+145f,220f,80f, false, feverCharacterPaint)
                    canvas?.drawArc(posX - 50f, posY-10f, posX+150f, posY+145f,240f,80f, false, feverCharacterPaint)
                    canvas?.drawOval(posX-20f, posY-75f, posX+20f, posY+75, feverCharacterPaint)
                    canvas?.drawArc(posX-30f, posY+65f, posX+30f, posY+125,180f,180f, false, feverCharacterPaint)

                    for (bullet in bulletList){
                        canvas?.drawCircle(bullet.posX, bullet.posY, 25f, feverbulletpaint)
                        if (!bulletfrozen){
                            bullet.posY += bullet.velocity
                        }
                        if (checkBulletinHitmap(bullet)){
                            score += 3
                        }
                    }
                    feverTime -= 1
                }
                else{
                    canvas?.drawArc(posX - 150f, posY-10f, posX+50f, posY+145f,220f,80f, false, paint)
                    canvas?.drawArc(posX - 50f, posY-10f, posX+150f, posY+145f,240f,80f, false, paint)
                    canvas?.drawOval(posX-20f, posY-75f, posX+20f, posY+75, paint)
                    canvas?.drawArc(posX-30f, posY+65f, posX+30f, posY+125,180f,180f, false, paint)

                    for (bullet in bulletList){
                        canvas?.drawCircle(bullet.posX, bullet.posY, 25f, if (bullet.good) goodbulletPaint else badbulletPaint)
                        if (!bulletfrozen){
                            bullet.posY += bullet.velocity
                        }
                        if (checkBulletinHitmap(bullet)){
                            if (bullet.good){
                                score += 1
                                gage = min(gage+1f,gageMax)
                            }
                            else {
                                life -= 1
                            }
                        }
                    }
                }


                if (bombtrue){
                    score += bulletList.size
                    bulletList = mutableListOf()
                    bombtrue = false
                }
                else{
                    bulletList = bulletList.filter{ bullet ->
                        !checkBulletinHitmap(bullet)
                                && bullet.posY<1920} as MutableList<Bullet>
                }
            }
        }







    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        posX = event!!.x
        posY = event!!.y
//        v_x = -v_x
//        v_y = -v_y
        invalidate()
        return true
    }

    fun updateCharacter(aX: Float, aY: Float){
        when(character){
            0->{
                a_x = aX
                a_y = aY

                val velocity_magnitude = sqrt(v_x*v_x + v_y*v_y)

                v_x -= a_x + friction_coef*v_x/velocity_magnitude
                v_y += a_y - friction_coef*v_y/velocity_magnitude

                if (posX < 50f  || posX > screenMaxX-50f){
                    v_x = -0.8f*v_x
                    if (posX < 50f){
                        posX = 50f
                    }
                    else {
                        posX = screenMaxX-50f
                    }
                }

                if (posY < 50f || posY > screenMaxY-100f){
                    v_y = -0.8f*v_y

                    if (posY < 50f){
                        posY = 50f
                    }
                    else {
                        posY = screenMaxY-100f
                    }
                }

                posX += 0.05f*v_x
                posY += 0.05f*v_y
            }
            1->{
                a_x = aX
                a_y = aY

                val velocity_magnitude = sqrt(v_x*v_x + v_y*v_y)

                v_x -= a_x + friction_coef*v_x/velocity_magnitude
                v_y += a_y - friction_coef*v_y/velocity_magnitude

                if (posX < 50f  || posX > screenMaxX-50f){
                    //v_x = 0f
                    if (posX < 50f){
                        posX = 50f
                    }
                    else {
                        posX = screenMaxX-50f
                    }
                }

                if (posY < 50f || posY > screenMaxY-100f){
                    //v_y = 0f

                    if (posY < 50f){
                        posY = 50f
                    }
                    else {
                        posY = screenMaxY-100f
                    }
                }

                posX += 0.1f*v_x
                posY += 0.1f*v_y
            }
        }



    }

    fun loadRandomBullet(n : Int){
        for (i in 1..n){
            val randX = kotlin.math.min(nextFloat() * 1080f + 30f,1050f)
            bulletList.add(Bullet(randX, speed))
        }
    }


}

class Bullet(initX:Float, speed:Float){
    val good = nextFloat()>0.5
    var posY:Float = 0f
    var posX:Float = -9999f
    var velocity: Float = -100f

    init{
        posX = initX
        velocity = min(speed*nextFloat()*5f + 2,speed*0.2f+2)
    }
}