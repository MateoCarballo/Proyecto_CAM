#include <SPI.h>
#include <MFRC522.h>

#define SS_PIN  5    // CS del RC522
#define RST_PIN 22   // RST del RC522

// Pines de salida
#define LED_ROJO 2
#define LED_VERDE 4
#define BUZZER 15

MFRC522 rfid(SS_PIN, RST_PIN);

void setup() {
  Serial.begin(115200);

  // Configurar pines de salida
  pinMode(LED_ROJO, OUTPUT);
  pinMode(LED_VERDE, OUTPUT);
  pinMode(BUZZER, OUTPUT);

  // Estado inicial
  digitalWrite(LED_ROJO, HIGH);   // LED inicial encendido
  digitalWrite(LED_VERDE, LOW);

  // Inicializar SPI y RC522
  SPI.begin(18, 19, 23, SS_PIN);  // SCK, MISO, MOSI, SS
  rfid.PCD_Init();

  Serial.println("ESP32 + MFRC522 listo. Acerca una tarjeta MIFARE...");
}

void playBuzzer() {
  // Secuencia de 3 tonos ascendentes cortos
  tone(BUZZER, 1000, 150); // 1 kHz 150ms
  delay(200);
  tone(BUZZER, 1200, 150); // 1.2 kHz 150ms
  delay(200);
  tone(BUZZER, 1400, 150); // 1.4 kHz 150ms
  delay(200);
  noTone(BUZZER); // Apagar buzzer
}

void loop() {
  // Esperar por nueva tarjeta
  if (!rfid.PICC_IsNewCardPresent()) return;
  if (!rfid.PICC_ReadCardSerial()) return;

  // Mostrar UID en monitor serie
  Serial.print("UID:");
  for (byte i = 0; i < rfid.uid.size; i++) {
    if (rfid.uid.uidByte[i] < 0x10) Serial.print(" 0");
    else Serial.print(" ");
    Serial.print(rfid.uid.uidByte[i], HEX);
  }
  Serial.println();

  // Feedback visual y sonoro
  digitalWrite(LED_ROJO, LOW);      // Apagar LED inicial
  digitalWrite(LED_VERDE, HIGH);    // Encender LED de lectura
  playBuzzer();                      // Activar secuencia de buzzer
  digitalWrite(LED_VERDE, LOW);     // Apagar LED de lectura
  digitalWrite(LED_ROJO, HIGH);     // Volver al LED inicial

  rfid.PICC_HaltA();
  delay(200);  // Pequeño retraso para evitar lecturas dobles
}
