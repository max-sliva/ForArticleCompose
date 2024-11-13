import java.awt.BorderLayout
import java.awt.Color
import java.awt.GridLayout
import javax.swing.BorderFactory
import javax.swing.JButton
import javax.swing.JFrame
import javax.swing.JPanel
import javax.swing.JRadioButton

class JButtonBordersTest : JFrame() {
    private val button: Array<JRadioButton?>
    private val panel: JPanel

    init {
        title = "JButton Borders"
        panel = JPanel()
        panel.layout = GridLayout(7, 1)
        button = arrayOfNulls(7)
        for (count in button.indices) {
            button[count] = JRadioButton("Button " + (count + 1))
            panel.add(button[count])
        }
        button[0]!!.border = BorderFactory.createLineBorder(Color.blue)
        button[1]!!.border = BorderFactory.createBevelBorder(0)
        button[2]!!.border = BorderFactory.createBevelBorder(1, Color.red, Color.blue)
        button[3]!!.border =
            BorderFactory.createBevelBorder(1, Color.green, Color.orange, Color.red, Color.blue)
        button[4]!!.border = BorderFactory.createEmptyBorder(10, 10, 10, 10)
        button[5]!!.border = BorderFactory.createEtchedBorder(0)
        button[6]!!.border = BorderFactory.createTitledBorder("Titled Border")

        add(panel, BorderLayout.CENTER)
        setSize(400, 300)
        defaultCloseOperation = EXIT_ON_CLOSE
        setLocationRelativeTo(null)
        isVisible = true
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            JButtonBordersTest()
        }
    }
}