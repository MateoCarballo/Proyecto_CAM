# Proyecto Final de Ciclo: Conexión y Configuración de ESP32 con MFRC522

## 1. Microcontrolador utilizado
- **Modelo:** ESP32 Dev Module (ESP-32S / WROOM-32)
- **Puerto de conexión:** USB-C mediante CP2102
- **Velocidad de carga:** 115200 / 921600 baud (según configuración)
- **Placa seleccionada en Arduino IDE:** ESP32 Dev Module

## 2. Librerías utilizadas
- **SPI**: Librería estándar de Arduino para comunicación SPI.
- **MFRC522**: Librería de Miguel Balboa para manejo del lector RFID.
  - Repositorio: [https://github.com/miguelbalboa/rfid](https://github.com/miguelbalboa/rfid)

## 3. Configuración de pines
Se han usado los pines del **bus VSPI del ESP32** para asegurar compatibilidad y funcionamiento estable:

| RC522 | ESP32 | Función |
|-------|-------|---------|
| VCC   | 3.3 V | Alimentación |
| GND   | GND   | Tierra común |
| SDA/SS | GPIO 5 | Chip Select |
| SCK   | GPIO 18 | Clock SPI |
| MOSI  | GPIO 23 | Master Out Slave In |
| MISO  | GPIO 19 | Master In Slave Out |
| RST   | GPIO 22 | Reset del RC522 |

**Notas:**
- No usar 5V en el RC522 para evitar daños.
- Es recomendable verificar que el pin GND del RC522 esté conectado al GND del ESP32.

## 4. Código de prueba utilizado
```cpp
#include <SPI.h>
#include <MFRC522.h>

#define SS_PIN  5    // CS del RC522
#define RST_PIN 22   // RST del RC522

MFRC522 rfid(SS_PIN, RST_PIN);

void setup() {
  Serial.begin(115200);
  SPI.begin(18, 19, 23, SS_PIN);  // SCK, MISO, MOSI, SS
  rfid.PCD_Init();
  Serial.println("ESP32 + MFRC522 listo. Acerca una tarjeta MIFARE...");
}

void loop() {
  if (!rfid.PICC_IsNewCardPresent()) return;
  if (!rfid.PICC_ReadCardSerial()) return;

  Serial.print("UID:");
  for (byte i = 0; i < rfid.uid.size; i++) {
    if (rfid.uid.uidByte[i] < 0x10) Serial.print(" 0");
    else Serial.print(" ");
    Serial.print(rfid.uid.uidByte[i], HEX);
  }
  Serial.println();

  rfid.PICC_HaltA();
  delay(500);
}
```

## 5. Configuraciones realizadas
- Inicialización explícita del **bus SPI** con pines VSPI: `SPI.begin(18, 19, 23, SS_PIN)`.
- Selección de **pines de reset y chip-select** según tabla de conexiones.
- Ganancia de antena configurada al máximo mediante: `rfid.PCD_SetAntennaGain(rfid.RxGain_max)`.
- Monitor serie configurado a **115200 baud** para lectura de UIDs.
- Configuración de placa y puerto en Arduino IDE para ESP32 Dev Module.

## 6. Observaciones
- Solo se pueden leer tarjetas **MIFARE 13.56 MHz ISO14443A**.
- Asegurarse de usar **cables USB de datos** de buena calidad para alimentación estable y comunicación.
- Verificar la continuidad y correcta soldadura de todos los pines del módulo RC522.
- En caso de fallo persistente, probar con otro módulo RC522 conocido funcional.