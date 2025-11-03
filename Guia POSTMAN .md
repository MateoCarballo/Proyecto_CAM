<!DOCTYPE html>
<html lang="es">
<head>
<meta charset="UTF-8">
<title>Guion de Pruebas Postman - Moderno</title>
<style>
    /* Tipografía moderna y ligera */
    @import url('https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600&display=swap');
    
    body {
        font-family: 'Inter', sans-serif;
        background-color: #f2f4f8;
        color: #1c1e21;
        margin: 0;
        padding: 20px;
    }

    h1 {
        text-align: center;
        font-weight: 600;
        color: #0a0a0a;
        margin-bottom: 40px;
    }

    .step {
        background: #ffffff;
        border-radius: 12px;
        box-shadow: 0 4px 20px rgba(0,0,0,0.08);
        margin-bottom: 20px;
        overflow: hidden;
        transition: transform 0.2s ease, box-shadow 0.2s ease;
    }

    .step:hover {
        transform: translateY(-3px);
        box-shadow: 0 8px 30px rgba(0,0,0,0.12);
    }

    .step-header {
        background: linear-gradient(90deg, #4a90e2 0%, #0078d7 100%);
        color: #fff;
        padding: 15px 20px;
        font-weight: 500;
        font-size: 18px;
        cursor: pointer;
        user-select: none;
        display: flex;
        justify-content: space-between;
        align-items: center;
    }

    .step-header:hover {
        background: linear-gradient(90deg, #357ab8 0%, #0056a3 100%);
    }

    .step-content {
        display: none;
        padding: 20px;
        border-top: 1px solid #e0e0e0;
        animation: fadeIn 0.3s ease-in-out;
    }

    @keyframes fadeIn {
        from { opacity: 0; transform: translateY(-10px); }
        to { opacity: 1; transform: translateY(0); }
    }

    pre {
        background-color: #f7f9fc;
        padding: 15px;
        border-radius: 8px;
        overflow-x: auto;
        font-size: 14px;
        line-height: 1.5;
        border-left: 4px solid #4a90e2;
    }

    code {
        color: #c0392b;
        font-weight: 600;
    }

    ul {
        padding-left: 20px;
    }

    .note {
        background-color: #e8f0fe;
        padding: 10px 15px;
        border-left: 4px solid #4a90e2;
        border-radius: 6px;
        margin-top: 10px;
    }

    .arrow {
        transition: transform 0.3s ease;
    }

    .arrow.open {
        transform: rotate(90deg);
    }

</style>
</head>
<body>

<h1>Guion de Pruebas - API Control de Acceso</h1>

<div class="step">
    <div class="step-header">Paso 1: Login como Admin <span class="arrow">&#9654;</span></div>
    <div class="step-content">
        <p><strong>Endpoint:</strong> POST <code>http://localhost:20000/auth/login</code></p>
        <p>Body (JSON):</p>
        <pre>{
  "email": "admin@example.com",
  "password": "1234"
}</pre>
        <p>Respuesta esperada:</p>
        <pre>{
    "token": "eyJhbGciOiJIUzI1NiJ9....",
    "tipo": "Bearer",
    "mensaje": "Login exitoso"
}</pre>
    </div>
</div>

<div class="step">
    <div class="step-header">Paso 2: Login como Usuario <span class="arrow">&#9654;</span></div>
    <div class="step-content">
        <p><strong>Endpoint:</strong> POST <code>http://localhost:20000/auth/login</code></p>
        <p>Body (JSON):</p>
        <pre>{
  "email": "fernando.alonso@astonmartin.com",
  "password": "1234"
}</pre>
        <p>Respuesta esperada:</p>
        <pre>{
    "token": "eyJhbGciOiJIUzI1NiJ9....",
    "tipo": "Bearer",
    "mensaje": "Login exitoso"
}</pre>
    </div>
</div>

<div class="step">
    <div class="step-header">Paso 3: Obtener registros (Admin) <span class="arrow">&#9654;</span></div>
    <div class="step-content">
        <p><strong>Endpoint:</strong> POST <code>http://localhost:20000/horarios/registros</code></p>
        <p>Headers:</p>
        <pre>
Authorization: Bearer &lt;token_admin&gt;
Content-Type: application/json
        </pre>
        <p>Body (opcional):</p>
        <pre>{
  "email": null,
  "fechaInicio": null,
  "fechaFin": null
}</pre>
        <p>Respuesta esperada:</p>
        <pre>{
  "empleados": [
    {
      "nombreEmpleado": "Lewis Hamilton",
      "numeroTarjeta": "B70E5006",
      "registrosHorariosPorDia": [...]
    },
    {
      "nombreEmpleado": "Fernando Alonso",
      "numeroTarjeta": "B3AB430E",
      "registrosHorariosPorDia": [...]
    },
    ...
  ]
}</pre>
    </div>
</div>

<div class="step">
    <div class="step-header">Paso 4: Obtener registros (Usuario) <span class="arrow">&#9654;</span></div>
    <div class="step-content">
        <p><strong>Endpoint:</strong> POST <code>http://localhost:20000/horarios/registros</code></p>
        <p>Headers:</p>
        <pre>
Authorization: Bearer &lt;token_usuario&gt;
Content-Type: application/json
        </pre>
        <p>Body (opcional):</p>
        <pre>{
  "email": null,
  "fechaInicio": null,
  "fechaFin": null
}</pre>
        <p>Respuesta esperada:</p>
        <pre>{
  "empleado": {
    "nombreEmpleado": "Fernando Alonso",
    "numeroTarjeta": "B3AB430E",
    "registrosHorariosPorDia": [...]
  }
}</pre>
    </div>
</div>

<div class="note">
    <strong>Notas importantes:</strong>
    <ul>
        <li>Reemplaza <code>&lt;token_admin&gt;</code> y <code>&lt;token_usuario&gt;</code> por los tokens JWT recibidos al hacer login.</li>
        <li>Si el <code>email</code> en el body es <code>null</code>, el sistema toma automáticamente el email del usuario autenticado.</li>
        <li>Para probar filtros por fechas, completa <code>fechaInicio</code> y <code>fechaFin</code> en formato <code>YYYY-MM-DD</code>.</li>
        <li>Usuarios <code>admin</code> ven todos los registros, mientras que <code>usuario</code> ve solo los suyos.</li>
    </ul>
</div>

<script>
    const headers = document.querySelectorAll('.step-header');
    headers.forEach(header => {
        header.addEventListener('click', () => {
            const content = header.nextElementSibling;
            const arrow = header.querySelector('.arrow');
            content.style.display = content.style.display === 'block' ? 'none' : 'block';
            arrow.classList.toggle('open');
        });
    });
</script>

</body>
</html>
