package logan.example.com.trianglesolver

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import java.lang.Math.*
import kotlin.math.tan

class Triangle(internal var context: Context, attrs: AttributeSet) : View(context, attrs){
    private val mPath = Path()
    private val mPaint = Paint()

    var sideA: Double? = null
    var sideB: Double? = null
    var sideC: Double? = null
    var angleA: Double? = null
    var angleB: Double? = null
    var angleC: Double? = null

    var area = 0.0
    var perimeter = 0.0

    private val pointA = Point()
    private val pointB = Point()
    private var pointC= Point()

    private var nodraw = true

    init {
        mPaint.isAntiAlias = true
        mPaint.color = Color.BLACK
        mPaint.style = Paint.Style.STROKE
        mPaint.strokeJoin = Paint.Join.ROUND
        mPaint.strokeWidth = 4f
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        mPaint.color = android.graphics.Color.WHITE
        canvas.drawPaint(mPaint)

        mPaint.color = android.graphics.Color.GREEN
        mPaint.style = Paint.Style.STROKE
        mPaint.isAntiAlias = true

        pointA.x = width / 6
        pointA.y = 5 * height / 6
        pointB.x = 5 * width / 6
        pointB.y = 5 * height / 6

        if (!nodraw) {
            mPath.fillType = Path.FillType.EVEN_ODD
            mPath.setLastPoint(pointA.x.toFloat(), pointA.y.toFloat())
            mPath.lineTo(pointB.x.toFloat(), pointB.y.toFloat())
            mPath.lineTo(pointC.x.toFloat(), pointC.y.toFloat())
            mPath.lineTo(pointA.x.toFloat(), pointA.y.toFloat())
            mPath.close()

            canvas.drawPath(mPath, mPaint)
            nodraw = true
        }
    }

    fun solveTriangle() {
        //gets total number of non-zero sides & angles
        val sides: Int = toInt(sideA != null) + toInt(sideB != null) + toInt(sideC != null)
        val angles: Int = toInt(angleA != null) + toInt(angleB != null) + toInt(angleC != null)

        var a: Double = sideA ?: 0.0
        var b: Double = sideB ?: 0.0
        var c: Double = sideC ?: 0.0
        var A: Double = angleA ?: 0.0
        var B: Double = angleB ?: 0.0
        var C: Double = angleC ?: 0.0

        //check for invalid cases
        if (sides + angles > 3) {
            throw Exception(context.getString(R.string.too_much_data))
        }
        else if (sides + angles < 3) {
            throw Exception(context.getString(R.string.not_enough_data))
        }
        else if (sides == 0) {
            throw Exception(context.getString(R.string.min_one_side_len))
        }
        else if (A + B + C > 180 || a + b <= c || b + c <= a || a + c <= b) {
            throw Exception(context.getString(R.string.impossible_triangle))
        }

        if (sides == 3) {
            //SSS
            A = cosineSolveAngle(a, b, c)
            B = cosineSolveAngle(b, c, a)
            C = cosineSolveAngle(c, a, b)
        }
        else if (angles == 2) {
            //ASA
            if (A == 0.0) A = angleSumSolveAngle(B, C)
            if (B == 0.0) B = angleSumSolveAngle(A, C)
            if (C == 0.0) C = angleSumSolveAngle(A, B)

            if (a != 0.0) {
                c = sineSolveSide(a, A, C)
                b = sineSolveSide(a, A, B)
            }
            else if (b != 0.0) {
                a = sineSolveSide(b, B, A)
                c = sineSolveSide(b, B, C)
            }
            else {
                a = sineSolveSide(c, C, A)
                b = sineSolveSide(c, C, B)
            }
        }
        else if (A != 0.0 && a == 0.0 || B != 0.0 && b == 0.0 || C != 0.0 && c == 0.0) {
            //SAS
            if (a == 0.0) a = cosineSolveSide(b, c, A)
            if (b == 0.0) b = cosineSolveSide(a, c, B)
            if (c == 0.0) c = cosineSolveSide(a, b, C)

            if (A != 0.0) {
                B = cosineSolveAngle(b, c, a)
                C = angleSumSolveAngle(A, B)
            }
            else if (B != 0.0) {
                A = cosineSolveAngle(a, b, c)
                C = angleSumSolveAngle(A, B)
            }
            else {
                A = cosineSolveAngle(a, b, c)
                B = angleSumSolveAngle(A, C)
            }
        }
        else if (sides == 2) {
            //SSA (to do later)
        }

        sideA = a
        sideB = b
        sideC = c
        angleA = A
        angleB = B
        angleC = C

        //gets position of 3rd point
        pointC = calcPointC()

        perimeter = a + b + c

        //heron formula
        val s = (a + b + c) / 2
        area = sqrt(s * (s-a) * (s - b) * (s - c))
    }

    //solves side C using cosine law
    fun cosineSolveSide(a: Double, b: Double, C: Double): Double {
        return sqrt(b * b  + a * a - 2 * b * a * cos(C * PI / 180))
    }

    //solves side C using sine law
    fun sineSolveSide(a: Double, A: Double, C: Double): Double {
        return sin(C * PI / 180) * a / sin(A * PI / 180)
    }

    //solves angle A using cosine law
    fun cosineSolveAngle(a: Double, b: Double, c: Double): Double {
        return acos((a * a - b * b - c * c) / (-2 * b * c)) * 180 / PI
    }

    //solves angle C using angle sum
    fun angleSumSolveAngle(A: Double, B: Double): Double {
        return 180 - A - B
    }

    fun calcPointC(): Point {
        var drawSideA: Double = sideA ?: throw Exception("Side A length is null!")
        var drawSideB: Double = sideB ?: throw Exception("Side B length is null!")
        var drawSideC: Double = sideC ?: throw Exception("Side C length is null!")
        var drawAngleA: Double = angleA ?: throw Exception("Angle A length is null!")
        var drawAngleB: Double = angleB ?: throw Exception("Angle B length is null!")
        var drawAngleC: Double = angleC ?: throw Exception("Angle C length is null!")
        when {
            drawSideA >= drawSideB && drawSideA >= drawSideC -> {
                //moves side a to base; rotates everything else
                val tempSide = drawSideA
                val tempAngle = drawAngleA
                drawSideA = drawSideB
                drawAngleA = drawAngleB
                drawSideB = drawSideC
                drawAngleB = drawAngleC
                drawSideC = tempSide
                drawAngleC = tempAngle
            }
            drawSideB >= drawSideA && drawSideB >= drawSideC -> {
                //moves side b to base; rotates everything else
                val tempSide = drawSideB
                val tempAngle = drawAngleB
                drawSideB = drawSideA
                drawAngleB = drawSideB
                drawSideA = drawSideC
                drawAngleA = drawAngleC
                drawSideC = tempSide
                drawAngleC = tempAngle
            }
            //otherwise longest side is already at the base
        }

        val slopeA: Double = -tan(drawAngleA * PI / 180)
        val slopeB: Double = -tan((180 - drawAngleB) * PI / 180)
        val yIntA: Double = 5 * height.toDouble() / 6 - slopeA * width.toDouble() / 6
        val yIntB: Double = 5 * height.toDouble() / 6 - slopeB * 5 * width.toDouble() / 6

        val x = (yIntB - yIntA) / (slopeA - slopeB)
        val y = slopeA * (yIntB - yIntA) / (slopeA - slopeB) + yIntA

        return Point(round(x).toInt(),round(y).toInt())
    }

    fun redraw() {
        nodraw = false
        mPath.reset()
        invalidate()
    }

    fun clearCanvas() {
        sideA = 0.0
        sideB = 0.0
        sideC = 0.0
        angleA = 0.0
        angleB = 0.0
        angleC = 0.0

        nodraw = true
        mPath.reset()
        invalidate()
    }

    fun toInt(b: Boolean): Int = if (b) 1 else 0
}