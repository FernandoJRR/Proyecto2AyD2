# Configuración de Servicios con Systemd para Microservicios Java

Este documento describe cómo configurar servicios de tipo `systemd` para ejecutar microservicios Spring Boot en formato `.jar`.

## 📁 Ubicación de los archivos de servicio

Todos los archivos `.service` deben guardarse en:

```
/etc/systemd/system
````

Ejemplo de archivo: `/etc/systemd/system/employee-service.service`

---

## 🛠 Estructura del archivo `.service`

Cada microservicio tendrá un archivo con la siguiente estructura:

```ini
[Unit]
Description=Nombre del Servicio
After=network.target

[Service]
User=usuario_instancia
ExecStart=/usr/bin/java -jar /var/api/project2AyD2/nombre-del-archivo.jar --spring.profiles.active=entrono-de-trabajo
SuccessExitStatus=143
Restart=always
RestartSec=5

[Install]
WantedBy=multi-user.target
````

### 🔁 Detalles importantes:

* **User**: Usuario que ejecutará el servicio.
* **ExecStart**: Ruta completa al archivo `.jar`.
* **Restart=always**: Reinicia el servicio si se detiene inesperadamente.
* **RestartSec=5**: Espera 5 segundos antes de reiniciar.

---

## 🚀 Habilitar y arrancar los servicios

Después de crear o modificar los archivos de servicio, ejecuta los siguientes comandos:

```bash
sudo systemctl daemon-reexec
sudo systemctl daemon-reload
sudo systemctl enable nombre-del-servicio
sudo systemctl start nombre-del-servicio
```

> 🔄 `daemon-reexec` es útil si se ha actualizado systemd.
> 🔁 `daemon-reload` recarga los archivos de servicio nuevos o modificados.
> ⚙️ `enable` activa el servicio para que inicie al arrancar el sistema.
> ▶️ `start` inicia el servicio de inmediato.

---

## ✅ Verificar estado

Para verificar que un servicio se esté ejecutando correctamente:

```bash
sudo systemctl status nombre-del-servicio
```

---

## 🧪 Ejemplo

```bash
sudo systemctl enable employee-service.service
sudo systemctl start employee-service.service
sudo systemctl status employee-service.service
```
