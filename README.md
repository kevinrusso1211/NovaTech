# 🛒 NovaTech - Backend

Backend de una tienda online desarrollado con **Spring Boot** y **MongoDB**, implementando autenticación JWT, gestión de productos, carrito de compras, pedidos, reseñas, favoritos, cupones de descuento y panel administrativo.

## 🚀 Tecnologías utilizadas

- Java 21
- Spring Boot 3
- Spring Security
- JWT (JSON Web Token)
- Spring Data MongoDB
- MongoDB
- Maven
- Lombok
- Swagger / OpenAPI
- JUnit 5
- Mockito

---

# 📌 Características

## 🔐 Autenticación y Seguridad

- Registro de usuarios
- Inicio de sesión con JWT
- Roles:
  - ADMIN
  - CLIENTE
- Contraseñas cifradas con BCrypt
- Endpoints protegidos mediante Spring Security

---

## 📦 Gestión de Productos

- Crear producto
- Actualizar producto
- Eliminar producto
- Consultar productos
- Buscar por nombre
- Buscar por categoría
- Buscar por rango de precios
- Control de stock
- Carga de imágenes

---

## 🛒 Carrito de Compras

- Agregar productos
- Actualizar cantidad
- Eliminar productos
- Vaciar carrito
- Calcular total automáticamente

---

## ❤️ Favoritos

- Agregar favoritos
- Eliminar favoritos
- Listar favoritos del usuario autenticado

---

## 📋 Pedidos

- Crear pedidos desde el carrito
- Descuento automático de stock
- Historial de pedidos
- Consulta por usuario
- Consulta por ID
- Flujo de estados:

```
PENDIENTE
   ↓
 PAGADO
   ↓
 ENVIADO
   ↓
ENTREGADO
```

Si un pedido es cancelado:

- Se restaura automáticamente el stock.

---

## 🎟️ Sistema de Cupones

- CRUD completo
- Activar / Desactivar cupones
- Validación por fechas
- Máximo de usos
- Aplicación automática del descuento
- Integrado con la creación de pedidos

---

## ⭐ Reseñas

- Solo usuarios que compraron el producto
- Una reseña por usuario
- Calificación de 1 a 5 estrellas
- Comentarios
- Promedio de calificaciones por producto

---

## 📊 Dashboard Administrativo

Estadísticas disponibles:

- Total de ventas
- Total de pedidos
- Total de productos
- Total de usuarios

---

## 🧪 Pruebas Unitarias

Se implementaron pruebas utilizando:

- JUnit 5
- Mockito

Cobertura para:

- PedidoService
- CuponService

---

# 📖 Documentación API

Swagger disponible en:

```
http://localhost:8080/swagger-ui/index.html
```

---

# ⚙️ Instalación

## Clonar repositorio

```bash
git clone https://github.com/TU_USUARIO/tienda-online.git
```

Entrar al proyecto

```bash
cd tienda-online
```

Ejecutar

```bash
mvn spring-boot:run
```

---

# 🗄️ Base de datos

Configurar en:

```
application.properties
```

Ejemplo:

```properties
spring.data.mongodb.uri=mongodb://localhost:27017/novatech
```

---

# 📂 Arquitectura

```
src
│
├── config
├── controller
├── dto
│   ├── request
│   └── response
├── exception
├── model
├── repository
├── security
├── service
├── utils
└── validation
```

---

# 🔒 Seguridad

- JWT Authentication
- BCrypt
- Spring Security
- Roles
- Endpoints protegidos

---

# 👨‍💻 Autor

**Kevin Russo Emiliany**

Proyecto desarrollado como práctica Full Stack utilizando Spring Boot.

---

# 🚀 Próximas mejoras

- Frontend en React
- Panel administrativo completo
- Pasarela de pagos
- Despliegue en la nube
- Docker
- CI/CD con GitHub Actions

---

# 📄 Licencia

Proyecto con fines educativos y de portafolio.