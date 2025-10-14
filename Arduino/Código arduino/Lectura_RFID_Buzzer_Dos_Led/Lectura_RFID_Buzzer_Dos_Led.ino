#include <WiFi.h>
#include <HTTPClient.h>
#include <SPI.h>
#include <MFRC522.h>

// Pines RC522 (ajusta si tus conexiones difieren)
#define SS_PIN  5
#define RST_PIN 22

// Pines de salida
#define LED_ROJO 2
#define LED_VERDE 4
#define BUZZER 15

MFRC522 rfid(SS_PIN, RST_PIN);

// Credenciales WiFi
const char* ssid = "Wifi-Pachorrudo";
const char* password = "Angel2023Martin2025";

// URL de tu API Spring Boot (ajusta IP y puerto)
const char* serverUrl = "http://192.168.1.50:20000/card/read";

void setup() {
  Serial.begin(115200);

  // Inicializar pines
  pinMode(LED_ROJO, OUTPUT);
  pinMode(LED_VERDE, OUTPUT);
  pinMode(BUZZER, OUTPUT);
  digitalWrite(LED_ROJO, HIGH);
  digitalWrite(LED_VERDE, LOW);

  // Inicializar RFID
  SPI.begin(18, 19, 23, SS_PIN); // SCK, MISO, MOSI, SS
  rfid.PCD_Init();

  // Conexión WiFi
  Serial.println("Conectando a WiFi...");
  WiFi.begin(ssid, password);
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }
  Serial.println("\n✅ WiFi conectado");
  Serial.println(WiFi.localIP());

  Serial.println("ESP32 + RC522 listo. Acerca una tarjeta...");
}

void playBuzzerSuccess() {
  tone(BUZZER, 1500, 100);
  delay(150);
  tone(BUZZER, 2000, 150);
  delay(200);
  noTone(BUZZER);
}

void playBuzzerError() {
  tone(BUZZER, 500, 300);
  delay(350);
  noTone(BUZZER);
}

// 🔹 Función para enviar UID al backend
void sendUidToServer(String uid) {
  if (WiFi.status() != WL_CONNECTED) {
    Serial.println("⚠️ WiFi desconectado, no se puede enviar UID.");
    playBuzzerError();
    return;
  }

  HTTPClient http;
  http.begin(serverUrl);
  http.addHeader("Content-Type", "application/json");

  String payload = "{\"uid\":\"" + uid + "\"}";
  Serial.println("➡️ Enviando: " + payload);

  int httpResponseCode = http.POST(payload);

  if (httpResponseCode > 0) {
    String response = http.getString();
    Serial.println("✅ Respuesta: " + response);
    playBuzzerSuccess();
  } else {
    Serial.printf("❌ Error HTTP (%d)\n", httpResponseCode);
    playBuzzerError();
  }

  http.end();
}

// 🔹 Función para obtener UID sin espacios ni guiones
String getCardUid() {
  String uid = "";
  for (byte i = 0; i < rfid.uid.size; i++) {
    // Asegura que siempre sean 2 caracteres (ej. 0A, 1F, etc.)
    if (rfid.uid.uidByte[i] < 0x10) uid += "0";
    uid += String(rfid.uid.uidByte[i], HEX);
  }
  uid.toUpperCase();  // Estilo estándar en hex
  return uid;
}

void loop() {
  if (!rfid.PICC_IsNewCardPresent()) return;
  if (!rfid.PICC_ReadCardSerial()) return;

  String uid = getCardUid();
  Serial.println("💳 UID leído: " + uid);

  digitalWrite(LED_ROJO, LOW);
  digitalWrite(LED_VERDE, HIGH);

  sendUidToServer(uid);

  digitalWrite(LED_VERDE, LOW);
  digitalWrite(LED_ROJO, HIGH);

  rfid.PICC_HaltA();
  delay(1000);  // Evita lecturas múltiples
}
