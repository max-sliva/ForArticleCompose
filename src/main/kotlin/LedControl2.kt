import jssc.SerialPort
import jssc.SerialPortEvent
import jssc.SerialPortException
import jssc.SerialPortList
import java.awt.BorderLayout
import java.awt.event.ActionEvent
import javax.swing.JCheckBox
import javax.swing.JComboBox
import javax.swing.JFrame

object LedControl2 {
    var totalStr = ""
    var serialPort: SerialPort? = null
    @JvmStatic
    fun main(args: Array<String>) {
        init()
    }

    private fun init() {
        val myFrame = JFrame("ArduinoControl")
        myFrame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        val check = JCheckBox("LED on/off") // чекбокс, который будет отвечать за
        //вкл/выкл светодиода на плате Arduino
        check.isEnabled = false //делаем неактивным чекбокс, пока не выберем порт для связи с
        //Arduino
        val portNames = SerialPortList.getPortNames() // получаем список портов
        val comPorts = JComboBox(portNames) //создаем комбобокс с этим
        //списком
        comPorts.selectedIndex = -1 //чтоб не было выбрано ничего в комбобоксе
        comPorts.addActionListener { arg: ActionEvent? ->  //слушатель выбора порта в комбобоксе
// получаем название выбранного порта
            val choosenPort = comPorts.getItemAt(comPorts.selectedIndex)
            //если serialPort еще не связана с портом или текущий порт не равен выбранному в комбо-боксе
            if (serialPort == null || !serialPort!!.portName.contains(choosenPort)) {
                serialPort = SerialPort(choosenPort) //задаем выбранный порт
                check.isEnabled = true //активируем чек-бокс
                try { //тут секция с try...catch для работы с портом
                    serialPort!!.openPort() //открываем порт
                    //задаем параметры порта, 9600 - скорость, такую же нужно задать для Serial.begin в Arduino
                    serialPort!!.setParams(9600, 8, 1, 0) //остальные параметры стандартные
                    //слушатель порта для приема сообщений от ардуино
                    serialPort!!.addEventListener { event: SerialPortEvent ->
                        if (event.isRXCHAR) { // если есть данные для приема
                            try { //тут секция с try...catch для работы с портом
//считываем данные из порта в строку
                                var str = serialPort!!.readString()
                                //убираем лишние символы (типа пробелов, которые могут быть в принятой строке)
                                str = str.trim { it <= ' ' }
                                if (!str.contains("\n ;") && str!="") {
//                    println("received $str") //выводим принятую строку
                                    totalStr+=str
                                    println("!!!totalStr = $totalStr")
                                }
                                if (str.contains("\n") || str.contains(";")) {
                                    println("totalStr = $totalStr")
                                    if (totalStr.contains("recv=1")) check.isSelected = true
                                    if (totalStr.contains("recv=0")) check.isSelected = false
                                    println("totalStr = $totalStr") //выводим принятую строку
                                    totalStr = ""
                                }
//                                str.trim{it=='\n'}
//                                str.replace("\n", "")
                                //проверяем принятую строку, и либо ставим, либо убираем галочку в чек-боксе
//                                if (str.contains("recv=1")) check.isSelected = true
//                                if (str.contains("recv=0")) check.isSelected = false
//                                println("str = $str") //выводим принятую строку
                            } catch (ex: SerialPortException) { //для обработки возможных ошибок
                                println(ex)
                            }
                        }
                    }
                    serialPort!!.writeString("+")
                    println("sent +")
                    serialPort!!.writeString("+")
                    println("sent +")
                } catch (e: SerialPortException) { //для обработки возможных ошибок
                    e.printStackTrace()
                }
            } else println("Same port!!") //если выбрали в списке тот же порт, что и
        }
        //до этого
        check.addActionListener { arg0: ActionEvent? ->  //обработка нажатия на чек-бокс
            try { //тут секция с try...catch для работы с портом
                if (check.isSelected) { //если стоит галочка
                    println("LED on") //то выводим текст
                    serialPort!!.writeString("+") // и отправляем символ "1" в порт
                } else {
                    println("LED off")
                    serialPort!!.writeString("0") // иначе отправляем символ "0" в порт
                }
            } catch (e: SerialPortException) { //для обработки возможных ошибок
                e.printStackTrace()
            }
        }
        myFrame.add(comPorts, BorderLayout.NORTH) //добавляем всё на форму
        myFrame.add(check, BorderLayout.CENTER)
        myFrame.setLocationRelativeTo(null)
        myFrame.pack()
        myFrame.isVisible = true
    }
}