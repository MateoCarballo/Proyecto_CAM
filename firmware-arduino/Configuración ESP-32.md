# 🛠️ Guía de Instalación y Configuración para ESP32 + RC522 + Arduino IDE

Esta guía te ayudará a preparar un entorno desde cero en un ordenador nuevo para cargar el proyecto `ESP32 + RC522 + WiFi + API`.

---

## 📦 Requisitos previos

- Tener instalado el **Arduino IDE** (recomendado: versión 2.0 o superior).
- Conexión a Internet.
- Tu placa **ESP32** conectada por USB.

---

## 1️⃣ Instalar soporte para placas ESP32

1. Abre Arduino IDE.
2. Ve a: **Archivo → Preferencias**.
3. En el campo **"Gestor de URLs Adicionales de Tarjetas"**, añade la siguiente URL:

https://raw.githubusercontent.com/espressif/arduino-esp32/gh-pages/package_esp32_index.json


4. Luego ve a: **Herramientas → Placa → Gestor de placas...**
5. Busca "`esp32`" e instala el paquete desarrollado por **Espressif Systems**.

---

## 2️⃣ Seleccionar tu placa ESP32

Ve a **Herramientas → Placa → ESP32 Arduino** y selecciona tu modelo específico, por ejemplo:

ESP32 Dev Module


Asegúrate de que el **puerto COM** esté correctamente seleccionado:
**Herramientas → Puerto → COMx**

---

## 3️⃣ Instalar Librerías Necesarias

Ve a **Programa → Incluir Librería → Gestionar Bibliotecas...** y busca e instala estas librerías:

| Librería          | Autor/Descripción                          |
|-------------------|---------------------------------------------|
| `WiFi`            | Ya incluida con el ESP32 (no instalar)     |
| `HTTPClient`      | Ya incluida con el ESP32 (no instalar)     |
| `MFRC522`         | de **Miguel Balboa**                       |
| `SPI`             | Incluida por defecto                       |

### Cómo instalar:

1. Abre el **Gestor de Librerías**.
2. Busca: `MFRC522`
3. Instala la versión más reciente de **Miguel Balboa**.

---

## 4️⃣ Conexiones del RC522 al ESP32

Conecta tu módulo **RC522 RFID** al ESP32 usando los siguientes pines (ajusta si es necesario):

| RC522 Pin | ESP32 Pin |
|-----------|-----------|
| SDA (SS)  | D5        |
| SCK       | D18       |
| MOSI      | D23       |
| MISO      | D19       |
| RST       | D22       |
| 3.3V      | 3.3V      |
| GND       | GND       |

🟡 **Importante:** El RC522 trabaja con 3.3V, **NO** lo conectes a 5V.

---

## 5️⃣ Cargar tu Script

1. Abre el Arduino IDE.
2. Pega el código completo del proyecto (como el que compartiste).
3. Haz clic en **Verificar (✔)**.
4. Luego en **Subir (→)**.

---

## 6️⃣ Solución de Problemas

- Si la compilación da errores de librerías: revisa que todas estén instaladas como se indica.
- Si no se conecta al WiFi: asegúrate de que el nombre y contraseña del WiFi estén bien escritos y dentro de rango.
- Si no detecta el RC522: revisa las conexiones y que uses los pines correctos en `SPI.begin()`.

---

## ✅ Recursos Extra

- Página oficial ESP32 Arduino:  
  https://docs.espressif.com/projects/arduino-esp32/en/latest/installing.html

- GitHub MFRC522:  
  https://github.com/miguelbalboa/rfid

---

## ✍️ Autor

Guía generada por [ChatGPT - Asistente Técnico].

Última actualización: Octubre 2025
