// -------------------- LIBRERÍAS --------------------

// Permite conectar el ESP32 a redes WiFi
#include <WiFi.h>

// Permite hacer peticiones HTTP (GET, POST, etc.) desde el ESP32
#include <HTTPClient.h>

// Librería de comunicación SPI (Serial Peripheral Interface), necesaria para el lector RC522
#include <SPI.h>

// Librería específica para el lector RFID RC522
// Proporciona funciones para detectar tarjetas, leer UID, etc.
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

/*const char* NOMBRE_RED     = "iPhone de Mateo";
const char* CONTRASENA_RED = "Clave1652C";
const char* API_KEY = "12345-ESP32-SECRET;"
*/
const char* NOMBRE_RED     = "Wifi-Pachorrudo";
const char* CONTRASENA_RED = "Angel2023Martin2025";
const char* API_KEY = "12345-ESP32-SECRET";

// Dirección del servidor backend donde se enviará el UID de la tarjeta leída
/*const char* URL_SERVIDOR = "http://172.20.10.6:20000/card/read";*/
const char* URL_SERVIDOR = "http://192.168.1.50:20000/card/read";
// -------------------- INSTANCIAS --------------------

// Crea un objeto del tipo MFRC522 para controlar el lector RFID
MFRC522 lectorRFID(PIN_SS, PIN_RST);

// -------------------- FUNCIONES DE CONFIGURACIÓN --------------------

// Inicializa la conexión WiFi del ESP32
void configurarWiFi() {
  Serial.println("Conectando a la red WiFi...");
  WiFi.begin(NOMBRE_RED, CONTRASENA_RED);  // Inicia conexión WiFi

  // Espera hasta que se conecte
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }

  // Muestra la IP local obtenida
  Serial.println("\nConectado a WiFi. IP asignada: " + WiFi.localIP().toString());
}

// Inicializa la comunicación SPI y el módulo RC522
void inicializarLectorRFID() {
  // Inicia el bus SPI con pines específicos para ESP32
  SPI.begin(18, 19, 23, PIN_SS);  // SCK, MISO, MOSI, SS

  // Inicializa el módulo RFID RC522
  lectorRFID.PCD_Init();

  Serial.println("Lector RFID inicializado.");
}

// Configura los pines de salida para LEDs y buzzer
void configurarSalidas() {
  pinMode(PIN_LED_ROJO, OUTPUT);
  pinMode(PIN_LED_VERDE, OUTPUT);
  pinMode(PIN_BUZZER, OUTPUT);

  // Estado inicial: LED rojo encendido (sistema en espera)
  digitalWrite(PIN_LED_ROJO, HIGH);
  digitalWrite(PIN_LED_VERDE, LOW);
}

// -------------------- FUNCIONES DE RESPUESTA AUDITIVA --------------------

// Reproduce un sonido corto indicando operación exitosa
void emitirBipExito() {
  tone(PIN_BUZZER, 1500, 100);  // Tono agudo
  delay(150);
  tone(PIN_BUZZER, 2000, 150);  // Tono más agudo
  delay(200);
  noTone(PIN_BUZZER);          // Detiene el sonido
}

// Reproduce un sonido indicando error o fallo
void emitirBipError() {
  tone(PIN_BUZZER, 500, 300);  // Tono grave
  delay(350);
  noTone(PIN_BUZZER);
}

// -------------------- FUNCIONES DE PROCESO --------------------

// Extrae y devuelve el UID de la tarjeta en formato de texto hexadecimal
String obtenerUIDTarjeta() {
  String uid = "";

  // Recorre los bytes del UID leído por el lector
  for (byte i = 0; i < lectorRFID.uid.size; i++) {
    // Asegura que cada byte tenga dos dígitos
    if (lectorRFID.uid.uidByte[i] < 0x10) uid += "0";
    uid += String(lectorRFID.uid.uidByte[i], HEX);
  }

  uid.toUpperCase();  // Convierte a mayúsculas (formato estándar)
  return uid;
}

// Envía el UID al servidor backend mediante una petición HTTP POST
void enviarUIDAlServidor(const String& uid) {
  // Verifica conexión WiFi antes de enviar
  if (WiFi.status() != WL_CONNECTED) {
    Serial.println("Error: No hay conexión WiFi.");
    emitirBipError();
    return;
  }

  HTTPClient http;                      // Crea cliente HTTP
  http.begin(URL_SERVIDOR);            // Establece la URL de destino
  http.addHeader("Content-Type", "application/json");
  http.addHeader("x-api-key", API_KEY);  // Indica que se enviará JSON

  // Construye el cuerpo del mensaje en formato JSON
  String cuerpo = "{\"uid\":\"" + uid + "\"}";

  Serial.println("Enviando UID al servidor: " + cuerpo);

  // Realiza la petición POST y guarda el código de respuesta
  int codigoRespuesta = http.POST(cuerpo);

  if (codigoRespuesta > 0) {
    // Si la petición tuvo éxito, se puede leer la respuesta
    String respuesta = http.getString();
    Serial.println("Respuesta del servidor: " + respuesta);
    emitirBipExito();
  } else {
    // Si hubo un error al enviar
    Serial.printf("Error HTTP al enviar UID (código %d)\n", codigoRespuesta);
    emitirBipError();
  }

  http.end();  // Libera recursos del cliente HTTP
}

// Función principal que gestiona la detección de tarjeta, lectura del UID y envío al servidor
void procesarTarjeta() {
  // Verifica si hay una nueva tarjeta presente
  if (!lectorRFID.PICC_IsNewCardPresent()) return;

  // Intenta leer el número de serie (UID)
  if (!lectorRFID.PICC_ReadCardSerial()) return;

  // Extrae el UID
  String uid = obtenerUIDTarjeta();
  Serial.println("Tarjeta detectada. UID: " + uid);

  // Indicador visual de lectura
  digitalWrite(PIN_LED_ROJO, LOW);
  digitalWrite(PIN_LED_VERDE, HIGH);

  // Envía el UID al servidor backend
  enviarUIDAlServidor(uid);

  // Restaura estado de LEDs
  digitalWrite(PIN_LED_VERDE, LOW);
  digitalWrite(PIN_LED_ROJO, HIGH);

  // Detiene la comunicación con la tarjeta (importante)
  lectorRFID.PICC_HaltA();

  delay(1000);  // Espera un segundo para evitar lecturas repetidas
}

// -------------------- FUNCIONES PRINCIPALES --------------------

// Se ejecuta una sola vez al iniciar el ESP32
void setup() {
  Serial.begin(115200);        // Inicia comunicación serial con el PC
  configurarSalidas();         // Configura LEDs y buzzer
  configurarWiFi();            // Conecta a la red WiFi
  inicializarLectorRFID();     // Prepara el lector RFID

  Serial.println("Sistema listo. Acerca una tarjeta RFID.");
}

// Se ejecuta continuamente en bucle
void loop() {
  procesarTarjeta();           // Gestiona la lectura de tarjetas
}
