#include <OLED_I2C.h>
OLED  display(SDA, SCL, 8);
extern uint8_t SmallFont[];
String recvStr = "";
char recv = '0';          //переменная для приема и отправки сообщений
char lastRecv = '0';      //дополнительная переменная для хранения предыдущего значения recv
byte buttonPress = HIGH;  // переменная для определения нажата кнопка или нет, HIGH – не нажата
int butState = 0;
long time;                // переменная для таймера
void setup() {
  pinMode(3, OUTPUT);       //настраиваем пин для встроенного светодиода
  pinMode(9, INPUT_PULLUP);  //настраиваем пин для кнопки
  digitalWrite(3, LOW);     //гасим светодиод на всякий случай
 // time = millis();           //стартуем таймер
   display.begin();
  // display.drawCircle(CENTER, CENTER, 30);
  display.setFont(SmallFont);
 display.print("from kotlin = ",10,10);
  display.update();

  Serial.begin(9600);        //задаем скорость порта

}
void loop() {
  if (Serial.available() > 0) {  //если есть данные для приема из ком-порта
    recv = Serial.read();        //считываем 1 символ в переменную recv
    recvStr += recv;
    display.print(recvStr,10,30);
    display.update();
    recvStr = "";
  }                              //можно использовать Serial.readString() для чтения строк
  buttonPress = digitalRead(9);  //считываем значение пина с кнопкой
  //если кнопка нажата и с прошлого нажатия прошло не менее 500 мс
  if (buttonPress == LOW) butState = 1;
  if (buttonPress == HIGH && butState)
  {
    recv = (recv == '0') ? '1' : '0';  //то меняем значение переменной recv
    butState = 0;
    // time = millis();                   //и сбрасываем таймер
  }
  if (recv == '1') {         //в зависимости от значения переменной recv
    digitalWrite(3, HIGH);  //либо включаем светодиод
  } else {
    digitalWrite(3, LOW);  //либо выключаем светодиод
  }                         //это можно сделать и одной строчкой
  // (recv == '1') ? digitalWrite(13, HIGH) : digitalWrite(13, LOW);
  if (lastRecv != recv) {   //и если предыдущее значение recv отличается от текущего
    Serial.print("recv=");  //то отправляем в ком-порт строку со значением переменной
    Serial.print(recv);
    Serial.println(";");
    lastRecv = recv;  //и делаем одинаковыми lastRecv и recv
  }                   //такая конструкция нужна, чтобы отправить данные в порт 1 раз, а не постоянно
}