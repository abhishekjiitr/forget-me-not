#include<SoftwareSerial.h>
SoftwareSerial bluetooth(9,10);
int disc=0;
int status = 0;
void setup() {
  // put your setup code here, to run once:
  
  Serial.begin(9600);
  bluetooth.begin(9600);
  pinMode(A5,OUTPUT);
}

void loop() {
  // put your main code here, to run repeatedly:
  if(bluetooth.available()>0){
    disc = 0;
    status = 1;
    char in= bluetooth.read();
    Serial.print(in);
    
   // Serial.print("Connected\n");
    delay(500);
    }

   else{
    disc++;
    
    //Serial.print("Disconnected!!!\n");
    delay(500);
    }
    if ( disc > 14){
      Serial.print("DISCONNECTED\n\n");
      digitalWrite(13,HIGH);
      digitalWrite(A5,HIGH);
      status = 0;
    }
    else{
      Serial.print("CONNECTED\n\n");  
      digitalWrite(13,LOW);
      digitalWrite(A5,LOW);
    } 
}
