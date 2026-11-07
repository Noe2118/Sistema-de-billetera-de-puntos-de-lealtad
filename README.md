# Sistema de billetera de puntos de lealtad

Aplicación para registrar clientes, acumular puntos por compras,
canjear recompensas y procesar devoluciones completas de compras.

## Requisitos

- Java Development Kit (JDK) 8 o posterior, con `javac` y `java` disponibles
  desde la terminal.
- No se requieren dependencias externas.

## Compilar y ejecutar

Abre una terminal en la carpeta raíz del proyecto y ejecuta:

```sh
mkdir -p bin
javac -d bin $(find src -name '*.java')
java -cp bin Sistema.Main
```

## Funcionalidades implementadas

- **Nivel 1:** registro de clientes, compras, catálogo, canjes, consulta de saldo
  e historial, con validaciones de datos y errores.
- **Nivel 2:** Se icluyen niveles de los clientes y sus respectivos multiplicadores 
  de puntos,respectivamente los puntos por compra se redondean hacia abajo. Los
  montos aceptan hasta dos decimales.
- **Nivel 3 (opcional):** Devolución completa de una compra.Se identifica por cliente y     referencia, solo se permite una devolución dentro de los 30 días siguientes a la compra.
La devolución revierte los puntos originales y recalcula los
  puntos acumulados para el nivel. Si los puntos ya se canjearon, el saldo puede
  quedar negativo; las compras futuras primero cubren esa deuda y los canjes no
  se permiten si el saldo no alcanza.

## Instancias 

- `Cliente` Representa los datos de una persona, su saldo actual, los puntos
  acumulados que determinan su nivel y su historial.
- `Movimiento` registra cada acumulación, canje o devolución con puntos, fecha y
  referencia. El historial conserva el orden de las operaciones y permite
  rastrear y revertir una compra.
- `SistemaLealtad` mantiene en memoria las listas de clientes y recompensas,
  asigna referencias consecutivas a compras y aplica toda la logica.
- `Recompensa` representa cada premio con su costo en puntos y stock disponible.
- `Nivel` define los puntos para Bronce, Plata y Oro.

Se usan las colecciones como listas en memoria para guardar los datos sin base de datos.

## Limitaciones y mejoras posibles

- Se podría agregar almacenamiento en archivo o una base de datos para guardar los datos 
- Se podría mejorar el manejo de errores para cubrir de mejor forma las
    entrada y evitar datos inválidos en las operaciones.

## Herramientas de apoyo

- Visual Studio Code para editar el proyecto.
- JDK para compilar y ejecutar el programa.
- Git para consultar los cambios del proyecto.
