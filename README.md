# Trabajo Práctico Especial 2: Peatones

Aplicación que permite procesar los datos de peatones de la ciudad de Melbourne.

## Descargar dataset

```
scp user@pampero.itba.edu.ar:/afs/it.itba.edu.ar/pub/pod/file.csv local_path
```

## Build

Dentro de la carpeta /POD-tp2, correr el script:

	$ ./run.sh

## Correr el servidor

Dentro de la carpeta /POD-tp2, correr el script:

	$ ./run-server.sh

## Ejecutar las queries
En otra terminal:

```
cd client/target/tpe2-g10-client-1.0-SNAPSHOT
chmod -R +x ./queryX
```

X es el número de query que se desea correr (de 1 a 5)

### Parámetros

###### Obligatorios para todas las queries
- **-Daddresses**=xx.xx.xx.xx:yyyy: xx.xx.xx.xx es el server address e yyyy es el puerto del servidor
- **-DinPath**=.: path al directorio donde se encuentran los archivos de entrada 
- **-DoutPath**=.: path al directorio donde se encuentran los archivos de salida

###### Adicionales para query 3
- **-Dmin**=10000: cota mínima de medición tomada

###### Adicionales para query 4
- **-Dn**=3: cantidad de resultados requeridos
- **-Dyear**=2021: año de medición

## Query 1

Cantidad total de peatones registrados por cada sensor.

	$ ./query1.sh -Daddresses=127.0.0.1 -DinPath=. -DoutPath=.

## Query 2

Cantidad total de peatones registrados por todos los sensores por cada año durante los weekdays, 
la cantidad total de peatones registrados por todos los sensores durante los weekends y 
la suma de ambos valores.

	$ ./query2.sh -Daddresses=127.0.0.1 -DinPath=. -DoutPath=.

## Query 3

Medición más alta de cada sensor junto con fecha y hora. 
Todas las mediciones serán mayores al mínimo que se pasa como parámetro.

	$ ./query3.sh -Daddresses=127.0.0.1 -DinPath=. -DoutPath=. -Dmin=10000

## Query 4

El mayor promedio mensual de peatones de un sensor para un año en particular. Devuelve n sensores.

	$ ./query4.sh -Daddresses=127.0.0.1 -DinPath=. -DoutPath=. -Dn=3 -Dyear=2021

## Query 5

Pares de sensores que registran la misma cantidad de millones de peatones.

	$ ./query5.sh -Daddresses=127.0.0.1 -DinPath=. -DoutPath=. 
