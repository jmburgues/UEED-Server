# UEED-Server

TRABAJO PRÁCTICO FINAL 2021
PROGRAMACIÓN AVANZADA I / BASE DE DATOS II

Nuestra compañía UDEE (UTN Distribucion de Energia Electrica) es una compañia de
distribucion de energia electrica con sede en la Ciudad de Mar del Plata.
Nuestra empresa se dedica a la facturación y mantenimiento de los sistemas eléctricos de la
ciudad. Con este fin ha instalado en cada casa/departamento de la ciudad un aparato que
nos va enviando información del consumo eléctrico de cada domicilio cada 5 minutos
mediante la tecnología 4G, a un servicio web que debemos desarrollar.
¿ Cómo funciona esto ?
Nuestro medidor arranca en 0 cuando se instala (Tomemos este momento como
01/01/2021 00:00:00). En los próximos cinco minutos , en ese domicilio se van a consumir
0.5 Kw/h, por lo tanto nuestro medidor nos enviara la siguiente información :
Si nuestro domicilio consume 1.0 Kw/h entre 01/01/2021 00:05:00 y 01/01/2021 00:10:00 ,
a está última hora enviará la siguiente información :
La medición se va incrementando conforme el domicilio va consumiendo energía. Cada uno
de los medidores va enviando información cada 5 minutos del número de la medición, pero
no el consumo . El consumo debe ser calculado por nuestro Web Service en base a las
distintas mediciones enviadas por cada medidor. Cada domicilio puede tener un solo
medidor.
El medidor se identifica en el sistema con un número de serie que no puede repetirse. A su
vez de cada medidor debemos tener que marca y que modelo es.
Fecha y hora Medición
01/01/2021 00:00:00 0.0
01/01/2021 00:05:00 0.5
Fecha y hora Medición
01/01/2021 00:10:00 1.0
Cada domicilio tiene un solo medidor y tiene asociada una tarifa por cada Kwh consumidos .
Estas tarifas deben ser configuradas por nuestro servicio Web.
Cada primer día de mes a las 00:00 se corre automáticamente un proceso automático que
nos genera la facturación de cada uno de los domicilios. Para saber cuánto debe pagar el
cliente, se toman en cuenta todas aquellas mediciones que no han sido facturadas hasta el
momento , es decir que debemos de alguna manera saber qué mediciones han sido tomadas
en cuenta a la hora de generar una factura de consumo.
Estas facturas deben tener la siguiente información :
● Cliente
● Domicilio
● Numero de medidor
● Medición inicial
● Medición final
● Consumo total en Kwh
● Fecha y hora medición inicial
● Fecha y hora medición final
● Tipo de tarifa
● Total a pagar (Consumo * Tarifa)
TRABAJO PRÁCTICO PROGRAMACIÓN AVANZADA I
UDEE ha contratado los servicios de dos empresas expertas en la realización de
plataformas web. Con el fin de distribuir la carga de trabajo entre las dos empresas, una
de ellas se encargará del desarrollo Web y Aplicación Celular, mientras a nuestra
compañía se encargará del desarrollo de una API REST para el mantenimiento de la
información de consumos y mediciones (llamado a partir de ahora como Backoffice) y
también para dar soporte a la Aplicación Web y Aplicación Android desde donde cada
cliente podrá ingresar y consultar información.
● El portal de usuarios y aplicacion Android deberá permitir :
1) Login de clientes
2) Consulta de facturas por rango de fechas.
3) Consulta de deuda (Facturas impagas)
4) Consulta de consumo por rango de fechas (el usuario va a ingresar un rango
de fechas y quiere saber cuánto consumió en ese periodo en Kwh y dinero)
5) Consulta de mediciones por rango de fechas
● Desde el sistema de Backoffice, se debe permitir :
1) Login de empleados.
2) Alta, baja y modificación de tarifas.
3) Alta, baja y modificación de domicilios y medidores.
4) Consulta de facturas impagas por cliente y domicilio.
5) Consulta 10 clientes más consumidores en un rango de fechas.
6) Consulta de mediciones de un domicilio por rango de fechas
● Con un login especial se debe crear un endpoint para que nuestros medidores nos
envien la informacion de consumo cada 5 minutos. Cada medidor nos enviará
Número de medidor, medición, fecha y hora de la medición.
Para la completitud del TP se considera :
● Seguir los fundamentos de API REST.
● División en capas mostradas en clase (Controller, Service, Persistence/Dao)
● Unit tests con cobertura de al menos 70% del código.
El no cumplimiento de alguno de los requisitos significa la desaprobación del TP con la
consiguiente desaprobación de la materia.
Fecha de entrega : 8-6-2021 / 15-6-2021 / 22-6-2021 / 29-6-2021
TRABAJO PRÁCTICO BASE DE DATOS II
Se debe generar un diseño de base de datos relacional que permita dar soporte al
escenario planteado anteriormente, y a su vez todo el trabajo que se requiera para
cumplimentar lo siguiente :
1) Generar las estructuras necesarias para dar soporte a 4 sistemas diferentes :
a) BACKOFFICE , que permitirá el manejo de clientes, medidores y tarifas.
b) CLIENTES , que permitirá consultas de mediciones y facturación.
c) MEDIDORES, , que será el sistema que enviará la información de
mediciones a la base de datos.
d) FACTURACIÓN , proceso automático de facturación.
2) La facturación se realizará por un proceso automático en la base de datos. Se
debe programar este proceso para el primer día de cada mes y debe generar una
factura por medidor y debe tomar en cuenta todas las mediciones no facturadas
para cada uno de los medidores, sin tener en cuenta su fecha. La fecha de vencimiento de
esta factura será estipulado a 15 días.
3) Generar las estructuras necesarias para el cálculo de precio de cada medición y las
inserción de la misma. Se debe tener en cuenta que una modificación en la tarifa debe
modificar el precio de cada una de estas mediciones en la base de datos y generar una
factura de ajuste a la nueva medición de cada una de las mediciones involucradas con esta
tarifa.
4) Generar las estructuras necesarias para dar soporte a las consultas de mediciones
por fecha y por usuario , debido a que tenemos restricción de que estas no pueden demorar
más de dos segundos y tenemos previsto que tendremos 500.000.000 de mediciones en el
sistema en el mediano plazo. Este reporte incluirá :
● Cliente
● Medidor
● Fecha medición
● Medicion
● Consumo Kwh
● Consumo precio
Como PLAN B , generar una estructura de base de datos NoSQL de su preferencia
para dar soporte al problema planteado.
Fechas de entrega : 10-6-2021 / 17-6-2021 / 24-6-2021 / 1-7-2021
