# 🧪 Guía práctica: Probar autenticación JWT y API Key con Postman

Esta guía te explica paso a paso cómo probar la autenticación **JWT** (para app Android o Postman) y la autenticación por **API Key** (para el dispositivo ESP32) en el proyecto **ControlAccesoPYME**.

---

## ⚙️ Configuración base del servidor

- **URL base:** `http://localhost:20000`
- **Puerto:** `20000`
- **Clave JWT:** `MiClaveSuperSecretaParaFirmarJWT12345`
- **API Key (ESP32):** `12345-ESP32-SECRET`

---

## 🪪 1. Login — Obtener token JWT

**Método:** `POST`  
**URL:** `http://localhost:20000/auth/login`

**Headers:**
```
Content-Type: application/json
```

**Body (raw JSON):**
```json
{
  "email": "fernando.alonso@email.com",
  "password": "abc123"
}
```

**Respuesta esperada (200 OK):**
```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJmZXJuYW5kby5hbG9uc29AZW1haWwuY29tIiwiaWF0IjoxNzMwNTY4NjAwLCJleHAiOjE3MzA2NTUwMDB9.Zd_NY5U5-8bP_n5y3vH9z..."
}
```

💡 **Consejo:** Copia el valor del campo `token`, lo necesitarás en el siguiente paso.

---

## 🔐 2. Probar endpoint protegido con JWT

**Método:** `POST`  
**URL:** `http://localhost:20000/horarios/registros`

**Headers:**
```
Authorization: Bearer <pega_aquí_tu_token>
Content-Type: application/json
```

**Body (opcional):**
```json
{
  "email": "fernando.alonso@email.com"
}
```

**Respuesta esperada:**
```json
{
  "empleado": "Fernando Alonso",
  "registros": [
    { "fecha": "2025-10-30", "hora": "08:00:00", "tipo": "entrada" },
    { "fecha": "2025-10-30", "hora": "13:30:00", "tipo": "salida" },
    { "fecha": "2025-10-30", "hora": "15:00:00", "tipo": "entrada" },
    { "fecha": "2025-10-30", "hora": "18:00:00", "tipo": "salida" }
  ]
}
```

✅ Si obtienes esta respuesta, el **token JWT es válido** y el usuario fue autenticado correctamente.

---

## 🔑 3. Probar autenticación con API Key (ESP32)

**Método:** `GET`  
**URL:** `http://localhost:20000/card/read`

**Headers:**
```
x-api-key: 12345-ESP32-SECRET
```

**Respuesta esperada (200 OK):**
```
Lectura de tarjeta aceptada
```

**Respuesta esperada (401 Unauthorized):**
```
API Key inválida o no proporcionada
```

---

## 🧩 4. Errores comunes

| Error | Causa probable | Solución |
|-------|----------------|-----------|
| `401 Unauthorized` | Token expirado o inválido | Vuelve a hacer login y usa el nuevo token |
| `403 Forbidden` | Endpoint protegido sin token o API Key | Verifica que estás enviando el header correcto |
| `500 Internal Server Error` | Token malformado o clave incorrecta | Comprueba `jwt.secret` en `application.properties` |

---

## 🧠 Conclusión

Con esta guía puedes:
- Generar un token JWT válido desde Postman.
- Usar ese token para acceder a endpoints protegidos.
- Probar autenticación dual (JWT + API Key) sin necesidad de la app Android.

> 💬 Si vas a subir esta guía a GitHub, guárdala como `Guía_Postman_JWT.md` en la raíz del proyecto.
