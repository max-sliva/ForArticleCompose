import java.awt.BorderLayout
import javax.swing.*

object SwingTester {
    @JvmStatic
    fun main(args: Array<String>) {
        val frame = JFrame("Swing Tester").apply { defaultCloseOperation = JFrame.EXIT_ON_CLOSE }
        val upperBox = Box(BoxLayout.X_AXIS)
        val textField = JTextField(15)
        val btn = JButton("Hello").apply {
            addActionListener {
                textField.text = "Hi"
            }
        }
        upperBox.add(btn)
        upperBox.add(textField)
        frame.add(upperBox, BorderLayout.NORTH)
        frame.setSize(300, 100)
        frame.isVisible = true
    }
}

//    private fun createWindow() {
//        val frame = JFrame("Swing Tester")
//        frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
////        createUI2(frame)
//        frame.setSize(300, 100)
//        frame.setLocationRelativeTo(null)
//        frame.isVisible = true
//    }
//
//
//

//    private fun createUI(frame: JFrame) {
//        val panel = JPanel()
//        val layout: LayoutManager = FlowLayout()
//        panel.layout = layout
//
//        val jEditorPane = JEditorPane()
//        jEditorPane.isEditable = false
//        val kit = HTMLEditorKit()
//        jEditorPane.editorKit = kit
//        val styleSheet: StyleSheet = kit.styleSheet
//        styleSheet.addRule("h2 {color: blue;}");
//        styleSheet.addRule("img {float:right;}");
//
//        val url: URL = SwingTester::class.java.getResource("floppy3_5.html")
//
//        try {
//            jEditorPane.setPage(url)
//        } catch (e: IOException) {
//            jEditorPane.contentType = "text/html"
//            jEditorPane.text = "<html>Page not found.</html>"
//        }
//
//        val jScrollPane = JScrollPane(jEditorPane)
//        jScrollPane.preferredSize = Dimension(1400, 1000)
//
//        panel.add(jScrollPane)
//        frame.contentPane.add(panel, BorderLayout.CENTER)
//    }
//}