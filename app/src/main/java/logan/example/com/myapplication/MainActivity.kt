package logan.example.com.myapplication

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View;
import android.widget.EditText
import android.widget.Toast


class MainActivity : AppCompatActivity() {

    private lateinit var t: Triangle
    private lateinit var sideAEdit: EditText
    private lateinit var sideBEdit: EditText
    private lateinit var sideCEdit: EditText
    private lateinit var angleAEdit: EditText
    private lateinit var angleBEdit: EditText
    private lateinit var angleCEdit: EditText

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
    }

    fun resetCanvas(view: View) {
        sideAEdit.text.clear()
        sideBEdit.text.clear()
        sideCEdit.text.clear()
        angleAEdit.text.clear()
        angleBEdit.text.clear()
        angleCEdit.text.clear()

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
        }
        catch (e: Exception) {
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
        }
    }

}