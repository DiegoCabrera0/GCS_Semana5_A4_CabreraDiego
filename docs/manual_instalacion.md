# Manual de Instalación y Despliegue del Sistema de Gestión Académica
**Unidad Educativa Dr. Alfredo Pareja Diezcanseco (Ambato, Ecuador)**

---

## 1. Requisitos del Entorno

* **Sistema Operativo:** Windows 10 / 11.
* **Servidor Web y BD:** XAMPP para Windows (Apache 2.4 + MySQL/MariaDB 10.4+ / PHP 8.1+).
* **Gestor de Dependencias:** Composer 2.x instalado en el PATH del sistema.
* **IDE Desarrollo Móvil:** Android Studio Ladybug / Koala o superior con JDK 17.
* **Extensiones PHP Requeridas:** `pdo_mysql`, `openssl`, `fileinfo`, `zip`, `mbstring`.

---

## 2. Instalación y Configuración del Backend en XAMPP

### Paso 2.1: Copia de Archivos
Copiar el contenido de la carpeta `backend/` dentro del directorio `C:\xampp\apps\gestion_academica\backend\`.

Estructura requerida:
```
C:\xampp\apps\gestion_academica\backend\
  ├── config\
  ├── public\
  │     ├── index.php
  │     └── .htaccess
  ├── routes\
  ├── src\
  ├── storage\
  ├── composer.json
  └── composer.lock
```

### Paso 2.2: Instalación de Dependencias PHP mediante Composer
Abrir PowerShell o CMD y ejecutar:
```powershell
cd C:\xampp\apps\gestion_academica\backend
composer install --no-dev --optimize-autoloader
```
*Nota: Si la consola indica falta de la extensión ZIP, verificar `php.ini` descomentando `;extension=zip` a `extension=zip` y reiniciar la terminal.*

### Paso 2.3: Configuración del Alias Apache
Abrir el archivo `C:\xampp\apache\conf\extra\httpd-vhosts.conf` (o `C:\xampp\apache\conf\httpd.conf`) y agregar el siguiente bloque:

```apache
Alias /gestion_academica "C:/xampp/apps/gestion_academica/backend/public"

<Directory "C:/xampp/apps/gestion_academica/backend/public">
    Options -Indexes +FollowSymLinks
    AllowOverride All
    Require all granted

    <IfModule mod_rewrite.c>
        RewriteEngine On
        RewriteCond %{HTTP:Authorization} .
        RewriteRule .* - [E=HTTP_AUTHORIZATION:%{HTTP:Authorization}]
    </IfModule>
</Directory>

<Directory "C:/xampp/apps/gestion_academica/backend">
    Require all denied
</Directory>

<Directory "C:/xampp/apps/gestion_academica/backend/public">
    Require all granted
</Directory>
```

Asegurarse de que en `httpd.conf` estén desmarcladas las siguientes líneas:
```apache
LoadModule rewrite_module modules/mod_rewrite.so
```

Reiniciar el servicio Apache desde el panel de control de XAMPP.

---

## 3. Configuración e Importación de la Base de Datos MySQL

1. Abrir phpMyAdmin (`http://localhost/phpmyadmin/`) o la consola MySQL de XAMPP.
2. Crear la base de datos importando `database/schema.sql`:
   ```sql
   SOURCE C:/Users/USER/AndroidStudioProjects/APP_Grupo9/database/schema.sql;
   ```
3. Cargar los datos ficticios de prueba ejecutando `database/seed.sql`:
   ```sql
   SOURCE C:/Users/USER/AndroidStudioProjects/APP_Grupo9/database/seed.sql;
   ```

---

## 4. Verificación de Funcionamiento de la API REST

### Prueba de Salud y Diagnóstico
Abrir un navegador o ejecutor HTTP (PowerShell/Postman):
1. **Health Check:** `http://localhost/gestion_academica/api/v1/health`
   - *Respuesta esperada (200 OK):* `{"success":true,"message":"Servicio web REST activo..."}`
2. **Ready Check:** `http://localhost/gestion_academica/api/v1/ready`
   - *Respuesta esperada (200 OK):* `{"success":true,"message":"Servicio listo y conexión a la base de datos verificada.","data":{"database":"CONNECTED"}}`

---

## 5. Configuración y Ejecución del Cliente Android

1. Abrir Android Studio e importar la carpeta del proyecto Android.
2. La URL base está configurada en `app/build.gradle.kts`:
   - Para Emulador Android Studio: `http://10.0.2.2/gestion_academica/api/v1/`
   - Para Dispositivo Físico en Red Local: Sustituir `10.0.2.2` por la IP local del PC (ejemplo: `http://192.168.1.150/gestion_academica/api/v1/`).
3. Ejecutar el proyecto en un dispositivo AVD Android (API 24 o superior).

---

## 6. Guía de Diagnóstico de Errores

| Error HTTP | Causa Probable | Solución |
|---|---|---|
| **404 Not Found** | Configuración de Alias Apache incorrecta o `mod_rewrite` deshabilitado. | Verificar `httpd-vhosts.conf` y asegurarse de que `.htaccess` está en `public/`. |
| **401 Unauthorized** | Token JWT faltante, expirado o cabecera `Authorization` bloqueada por Apache. | Verificar envío de `Authorization: Bearer <token>` y reglas de rewrite. |
| **403 Forbidden** | Intento de acceso a endpoints sin el rol correspondiente. | Comprobar los permisos del usuario activo. |
| **500 Internal Server Error** | Error en código PHP o sintaxis SQL. | Revisar `backend/storage/logs/app.log` y el log de Apache `C:\xampp\apache\logs\error.log`. |
| **503 Service Unavailable** | Fallo de conexión de PDO a MySQL. | Verificar que el servicio MySQL en XAMPP esté encendido y que el puerto 3306 esté disponible. |
| **Cleartext Traffic Error** | Android bloqueando tráfico HTTP en desarrollo. | Incluido en `network_security_config.xml` y `AndroidManifest.xml` (`usesCleartextTraffic="true"`). |
