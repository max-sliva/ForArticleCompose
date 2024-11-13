
import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.FlowLayout
import java.awt.LayoutManager
import java.io.IOException
import java.net.URL
import javax.swing.JEditorPane
import javax.swing.JFrame
import javax.swing.JPanel
import javax.swing.JScrollPane
import javax.swing.text.html.HTMLEditorKit
import javax.swing.text.html.StyleSheet


object SwingTester {
    @JvmStatic
    fun main(args: Array<String>) {
        createWindow()
    }

    private fun createWindow() {
        val frame = JFrame("Swing Tester")
        frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        createUI(frame)
        frame.setSize(1400, 1000)
        frame.setLocationRelativeTo(null)
        frame.isVisible = true
    }

    private fun createUI(frame: JFrame) {
        val panel = JPanel()
        val layout: LayoutManager = FlowLayout()
        panel.layout = layout

        val jEditorPane = JEditorPane()
        jEditorPane.isEditable = false
        val kit = HTMLEditorKit()
        jEditorPane.editorKit = kit
        val styleSheet: StyleSheet = kit.styleSheet
        styleSheet.addRule("h2 {color: blue;}");
        styleSheet.addRule("img {float:right;}");

        val url: URL = SwingTester::class.java.getResource("floppy3_5.html")

        try {
            jEditorPane.setPage(url)
        } catch (e: IOException) {
            jEditorPane.contentType = "text/html"
            jEditorPane.text = "<html>Page not found.</html>"
        }

        val jScrollPane = JScrollPane(jEditorPane)
        jScrollPane.preferredSize = Dimension(1400, 1000)

        panel.add(jScrollPane)
        frame.contentPane.add(panel, BorderLayout.CENTER)
    }
}