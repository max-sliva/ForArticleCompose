import java.awt.BorderLayout;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import jssc.SerialPort;
import jssc.SerialPortException;
import jssc.SerialPortList;
public class LedControl {
    static SerialPort serialPort = null;
    public static void main(String[] args) {
        init();
    }
    private static void init() {
        JFrame myFrame = new JFrame("ArduinoControl");
        myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JCheckBox check = new JCheckBox("LED on/off"); // чекбокс, который будет отвечать за
//вкл/выкл светодиода на плате Arduino
        check.setEnabled(false); //делаем неактивным чекбокс, пока не выберем порт для связи с
//Arduino
        String[] portNames = SerialPortList.getPortNames(); // получаем список портов
        JComboBox<String> comPorts = new JComboBox<>(portNames); //создаем комбобокс с этим
//списком
        comPorts.setSelectedIndex(-1); //чтоб не было выбрано ничего в комбобоксе
        comPorts.addActionListener(arg -> { //слушатель выбора порта в комбобоксе
// получаем название выбранного порта
            String choosenPort = comPorts.getItemAt(comPorts.getSelectedIndex());
//если serialPort еще не связана с портом или текущий порт не равен выбранному в комбо-боксе
            if (serialPort == null || !serialPort.getPortName().contains(choosenPort)) {
                serialPort = new SerialPort(choosenPort); //задаем выбранный порт
                check.setEnabled(true); //активируем чек-бокс
                try { //тут секция с try...catch для работы с портом
                    serialPort.openPort(); //открываем порт
//задаем параметры порта, 9600 - скорость, такую же нужно задать для Serial.begin в Arduino
                    serialPort.setParams(9600, 8, 1, 0); //остальные параметры стандартные
//слушатель порта для приема сообщений от ардуино
                    serialPort.addEventListener(event -> {
                        if (event.isRXCHAR()) {// если есть данные для приема
                            try { //тут секция с try...catch для работы с портом
//считываем данные из порта в строку
                                String str = serialPort.readString();
//убираем лишние символы (типа пробелов, которые могут быть в принятой строке)
                                str = str.trim();
//проверяем принятую строку, и либо ставим, либо убираем галочку в чек-боксе
                                if (str.contains("recv=1")) check.setSelected(true);
                                if (str.contains("recv=0")) check.setSelected(false);
                                System.out.println(str); //выводим принятую строку
                            } catch (SerialPortException ex) { //для обработки возможных ошибок
                                System.out.println(ex);
                            }
                        }
                    });
                } catch (SerialPortException e) {//для обработки возможных ошибок
                    e.printStackTrace();
                }
            } else
                System.out.println("Same port!!"); //если выбрали в списке тот же порт, что и
        });
//до этого
        check.addActionListener(arg0 -> { //обработка нажатия на чек-бокс
            try { //тут секция с try...catch для работы с портом
                if (check.isSelected()) { //если стоит галочка
                    System.out.println("LED on"); //то выводим текст
                    serialPort.writeString("1");// и отправляем символ "1" в порт
                } else {
                    System.out.println("LED off");
                    serialPort.writeString("0");// иначе отправляем символ "0" в порт
                }
            } catch (SerialPortException e) { //для обработки возможных ошибок
                e.printStackTrace();
            }
        });
        myFrame.add(comPorts, BorderLayout.NORTH); //добавляем всё на форму
        myFrame.add(check, BorderLayout.CENTER);
        myFrame.setLocationRelativeTo(null);
        myFrame.pack();
        myFrame.setVisible(true);
    }
}