package logan.example.com.trianglesolver

import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.constraint.Group
import android.support.v7.app.AppCompatActivity
import android.view.View;
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast


class MainActivity : AppCompatActivity() {

    private lateinit var t: Triangle
    private lateinit var sideAEdit: EditText
    private lateinit var sideBEdit: EditText
    private lateinit var sideCEdit: EditText
    private lateinit var angleAEdit: EditText
    private lateinit var angleBEdit: EditText
    private lateinit var angleCEdit: EditText
    private lateinit var areaText: TextView
    private lateinit var perimeterText: TextView

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        t = findViewById(R.id.triangle_canvas)
        sideAEdit = findViewById(R.id.side_a_edit)
        sideBEdit = findViewById(R.id.side_b_edit)
        sideCEdit = findViewById(R.id.side_c_edit)
        angleAEdit = findViewById(R.id.angle_a_edit)
        angleBEdit = findViewById(R.id.angle_b_edit)
        angleCEdit = findViewById(R.id.angle_c_edit)
        areaText = findViewById(R.id.area_text)
        perimeterText = findViewById(R.id.perimeter_text)
    }

    fun resetCanvas(view: View) {
        sideAEdit.text.clear()
        sideAEdit.invalidate()
        sideBEdit.text.clear()
        sideBEdit.invalidate()
        sideCEdit.text.clear()
        sideCEdit.invalidate()
        angleAEdit.text.clear()
        angleAEdit.invalidate()
        angleBEdit.text.clear()
        angleBEdit.invalidate()
        angleCEdit.text.clear()
        angleCEdit.invalidate()

        areaText.text = ""
        perimeterText.text = ""

        t.clearCanvas()
    }

    fun solveTriangle(view: View) {
        t.sideA = sideAEdit.text.toString().toDoubleOrNull()
        t.sideB = sideBEdit.text.toString().toDoubleOrNull()
        t.sideC = sideCEdit.text.toString().toDoubleOrNull()
        t.angleA = angleAEdit.text.toString().toDoubleOrNull()
        t.angleB = angleBEdit.text.toString().toDoubleOrNull()
        t.angleC = angleCEdit.text.toString().toDoubleOrNull()

        try {
            t.solveTriangle()
            t.redraw()

            sideAEdit.setText(String.format("%.3f", t.sideA))
            sideBEdit.setText(String.format("%.3f", t.sideB))
            sideCEdit.setText(String.format("%.3f", t.sideC))
            angleAEdit.setText(String.format("%.3f", t.angleA))
            angleBEdit.setText(String.format("%.3f", t.angleB))
            angleCEdit.setText(String.format("%.3f", t.angleC))

            areaText.text = getString(R.string.area, t.area)
            perimeterText.text = getString(R.string.perimeter, t.perimeter)


        }
        catch (e: Exception) {
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
        }
    }
}