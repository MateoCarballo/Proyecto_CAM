# Guía para probar endpoints de Control de Acceso con Postman

Esta guía explica cómo probar los endpoints de la API de Control de Acceso local usando Postman. Se asume que el servidor está corriendo en `localhost` en el puerto `20000` y que se utiliza la API Key real del dispositivo ESP32.

---

## 1. Endpoint: Leer tarjeta

**URL:**  
```
POST http://localhost:20000/card/read
```

**Headers:**

| Key       | Value                                   |
|-----------|-----------------------------------------|
| x-api-key | 12345-ESP32-SECRET          |
| Content-Type | application/json                    |

**Body (raw JSON):**

```json
{
  "uid": "B3AB430E"
}
```

> Nota: `uid` debe coincidir con la tarjeta registrada en la base de datos.

---

## 2. Respuesta esperada

Si el UID es válido y autorizado, la API devolverá:

```json
{
    "uid": "B3AB430E",
    "authorized": true,
    "message": "Acceso registrado correctamente (salida)",
    "timestamp": "2025-10-30T18:51:43.5756541"
}
```

**Campos:**

| Campo       | Descripción                                               |
|-------------|-----------------------------------------------------------|
| uid         | UID de la tarjeta leída                                   |
| authorized  | Booleano que indica si el acceso está autorizado         |
| message     | Mensaje descriptivo del resultado                        |
| timestamp   | Fecha y hora exacta del registro del acceso              |

---

## 3. Notas importantes

1. Asegúrate de que el endpoint `/card/read` está protegido con la API Key (`x-api-key`).  
2. El `Content-Type` debe ser `application/json`.  
3. El UID debe existir en la tabla `tarjetas` de la base de datos.  
4. En caso de UID no válido o sin API Key, la respuesta será un error 401 con el mensaje:  
   ```
   API Key inválida o no proporcionada
   ```

---

## 4. Pruebas adicionales

Puedes modificar el `uid` para simular tarjetas no autorizadas:

```json
{
  "uid": "00000000"
}
```

Respuesta esperada:

```json
{
    "uid": "00000000",
    "authorized": false,
    "message": "Acceso denegado",
    "timestamp": "2025-10-30T18:55:00.0000000"
}
```

---

**Registro**

URL: POST ```http://localhost:20000/auth/register```  ```  ```

Body JSON:
```json
{
  "empleadoId": 2,
  "email": "usuario@empresa.com",
  "password": "miPassword"
}
```

Posibles respuestas:

200 OK:
```json
{ "success": true, "mensaje": "Usuario registrado correctamente" }
```

400 Bad Request (ejemplos):
```json
{ "success": false, "mensaje": "Empleado no encontrado" }
{ "success": false, "mensaje": "El empleado ya tiene un usuario registrado" }
{ "success": false, "mensaje": "El email ya está registrado" }
```

**Login**

URL: POST ```http://localhost:20000/auth/login```

Body JSON:
```json
{
  "email": "usuario@empresa.com",
  "password": "miPassword"
}
```

Respuestas:

200 OK:
```json
{
  "token": "eyJhbGciOi...",
  "tipo": "Bearer",
  "mensaje": "Login exitoso"
}
```

400 / 401:
```json
{ "token": null, "mensaje": "Usuario no encontrado" }
{ "token": null, "mensaje": "Contraseña incorrecta" }
{ "token": null, "mensaje": "Usuario inactivo" }
```

**Obtener registros**

URL: POST ```http://localhost:20000/horarios/registros```

Body (JSON):
```json
{
  "email": "usuario@empresa.com",
  "fechaInicio": "2025-10-01",
  "fechaFin": "2025-10-30"
}
```