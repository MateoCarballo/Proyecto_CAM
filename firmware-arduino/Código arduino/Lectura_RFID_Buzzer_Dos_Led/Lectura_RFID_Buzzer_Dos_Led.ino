// -------------------- LIBRERÍAS --------------------

// Permite conectar el ESP32 a redes WiFi
#include <WiFi.h>

// Permite hacer peticiones HTTP (GET, POST, etc.) desde el ESP32
#include <HTTPClient.h>

// Librería de comunicación SPI (Serial Peripheral Interface), necesaria para el lector RC522
#include <SPI.h>

// Librería específica para el lector RFID RC522
#include <MFRC522.h>

// -------------------- CONSTANTES --------------------

// Pines del lector RFID (RC522)
constexpr uint8_t PIN_SS   = 5;   // Pin de selección de esclavo (SDA)
constexpr uint8_t PIN_RST  = 22;  // Pin de reset del módulo RFID

// Pines de salida para LEDs y zumbador
constexpr uint8_t PIN_LED_ROJO  = 2;
constexpr uint8_t PIN_LED_VERDE = 4;
constexpr uint8_t PIN_BUZZER    = 15;

// Credenciales de la red WiFi
const char* NOMBRE_RED     = "WifiPachorrudo";
const char* CONTRASENA_RED = "Angel2023Martin2025!";
const char* API_KEY        = "Alonso!GanaLa33";

// Dirección del servidor backend donde se enviará el UID de la tarjeta leída
//const char* URL_SERVIDOR = "http://192.168.1.50:20000/card/read";
const char* URL_SERVIDOR = "http://192.168.1.134:20000/card/read";

// -------------------- INSTANCIAS --------------------

// Crea un objeto del tipo MFRC522 para controlar el lector RFID
MFRC522 lectorRFID(PIN_SS, PIN_RST);

// -------------------- PARPADEO LED ROJO --------------------
unsigned long ultimoParpadeo = 0;
const unsigned long intervaloParpadeo = 500;  // ms
bool estadoLedRojo = true;

// -------------------- FUNCIONES DE CONFIGURACIÓN --------------------

// Inicializa la conexión WiFi del ESP32
void configurarWiFi() {
  Serial.println("Conectando a la red WiFi...");
  WiFi.begin(NOMBRE_RED, CONTRASENA_RED);

  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }

  Serial.println("\nConectado a WiFi. IP asignada: " + WiFi.localIP().toString());
}

// Inicializa la comunicación SPI y el módulo RC522
void inicializarLectorRFID() {
  SPI.begin(18, 19, 23, PIN_SS);  // SCK, MISO, MOSI, SS
  lectorRFID.PCD_Init();
  Serial.println("Lector RFID inicializado.");
}

// Configura los pines de salida para LEDs y buzzer
void configurarSalidas() {
  pinMode(PIN_LED_ROJO, OUTPUT);
  pinMode(PIN_LED_VERDE, OUTPUT);
  pinMode(PIN_BUZZER, OUTPUT);

  digitalWrite(PIN_LED_ROJO, HIGH);  // LED rojo inicial encendido
  digitalWrite(PIN_LED_VERDE, LOW);
}

// -------------------- PARPADEO (NO BLOQUEANTE) --------------------
void parpadearLedRojo() {
  unsigned long ahora = millis();

  if (ahora - ultimoParpadeo >= intervaloParpadeo) {
    ultimoParpadeo = ahora;
    estadoLedRojo = !estadoLedRojo;
    digitalWrite(PIN_LED_ROJO, estadoLedRojo);
  }
}

// -------------------- RESPUESTA AUDITIVA --------------------

void emitirBipExito() {
  tone(PIN_BUZZER, 1500, 100);
  delay(150);
  tone(PIN_BUZZER, 2000, 150);
  delay(200);
  noTone(PIN_BUZZER);
}

void emitirBipError() {
  tone(PIN_BUZZER, 500, 300);
  delay(350);
  noTone(PIN_BUZZER);
}

// -------------------- PROCESO RFID --------------------

// Extrae y devuelve el UID de la tarjeta
String obtenerUIDTarjeta() {
  String uid = "";

  for (byte i = 0; i < lectorRFID.uid.size; i++) {
    if (lectorRFID.uid.uidByte[i] < 0x10) uid += "0";
    uid += String(lectorRFID.uid.uidByte[i], HEX);
  }

  uid.toUpperCase();
  return uid;
}

// Envía el UID al servidor backend mediante POST
void enviarUIDAlServidor(const String& uid) {
  if (WiFi.status() != WL_CONNECTED) {
    Serial.println("Error: No hay conexión WiFi.");
    emitirBipError();
    return;
  }

  HTTPClient http;
  http.begin(URL_SERVIDOR);
  http.addHeader("Content-Type", "application/json");
  http.addHeader("x-api-key", API_KEY);

  String cuerpo = "{\"uid\":\"" + uid + "\"}";
  Serial.println("Enviando UID al servidor: " + cuerpo);

  int codigoRespuesta = http.POST(cuerpo);

  if (codigoRespuesta > 0) {
    String respuesta = http.getString();
    Serial.println("Respuesta del servidor: " + respuesta);

    // -----------------------------------
    // ANALIZAR RESPUESTA "authorized"
    // -----------------------------------
    bool autorizado = false;

    if (respuesta.indexOf("\"authorized\":true") != -1) {
      autorizado = true;
    }

    if (autorizado) {
      Serial.println("Acceso permitido.");
      emitirBipExito();     // Bip de éxito
      digitalWrite(PIN_LED_VERDE, HIGH);
      delay(300);
      digitalWrite(PIN_LED_VERDE, LOW);

    } else {
      Serial.println("Acceso denegado (tarjeta no registrada).");
      emitirBipError();     // Bip de error
      digitalWrite(PIN_LED_ROJO, HIGH);
      delay(500);
    }

  } else {
    Serial.printf("Error HTTP (código %d)\n", codigoRespuesta);
    emitirBipError();
  }

  http.end();
}

// Procesa lectura RFID
void procesarTarjeta() {
  if (!lectorRFID.PICC_IsNewCardPresent()) return;
  if (!lectorRFID.PICC_ReadCardSerial()) return;

  String uid = obtenerUIDTarjeta();
  Serial.println("Tarjeta detectada. UID: " + uid);

  // Indicador: rojo off, verde on
  digitalWrite(PIN_LED_ROJO, LOW);
  digitalWrite(PIN_LED_VERDE, HIGH);

  enviarUIDAlServidor(uid);

  // Restaurar estado
  digitalWrite(PIN_LED_VERDE, LOW);
  digitalWrite(PIN_LED_ROJO, HIGH);
  estadoLedRojo = true; // sincroniza el parpadeo

  lectorRFID.PICC_HaltA();

  delay(1000);
}

// -------------------- PRINCIPAL --------------------

void setup() {
  Serial.begin(115200);
  configurarSalidas();
  configurarWiFi();
  inicializarLectorRFID();

  Serial.println("Sistema listo. Acerca una tarjeta RFID.");
}

void loop() {
  parpadearLedRojo();   // Nuevo: LED rojo parpadeando
  procesarTarjeta();    // Lectura normal
}
