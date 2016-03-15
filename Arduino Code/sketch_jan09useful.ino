#include <SoftwareSerial.h>

SoftwareSerial BTSerial(9,3); // RX | TX

void setup()
{
  Serial.begin(9600);
  pinMode(13,OUTPUT);
  digitalWrite(13,LOW);
  Serial.println("Initialised!");
  BTSerial.begin(9600);
}

void loop()
{

  // Keep reading from HC-05 and send to Arduino Serial Monitor
  if (BTSerial.available()){
    Serial.write(BTSerial.read());
    //int a = BTSerial.read();
    //if (a == 'b'){
    //digitalWrite(13,HIGH);
    //delay(1000);
    //digitalWrite(13,LOW);
   // delay(100);
  }
  // Keep reading from Arduino Serial Monitor and send to HC-05
  if (Serial.available())
    BTSerial.print(Serial.read());
    //if(Serial.available()==0
}
