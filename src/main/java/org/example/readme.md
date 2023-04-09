## Resolución con monitores

Para la solución de la práctica se utilizará como herramienta de concurrencia el desarrollo de monitores.
Hay que tener presenta a la hora de implementar la solución que Java no dispone de esta herramienta.
Para ello el alumno podrá utilizar cualquier utilidad de concurrencia para diseñar la clase que representa al monitor de
la solución teórica. En la implementación del monitor se deberán respetar las características que presenta el monitor
que se ha presentado en teoría. También se utilizará la factoría `Executors` y la interface `ExecutorService` para la
ejecución de las tareas concurrentes que compondrán la solución de la práctica.

## Problema a resolver

Se trata de organizar una cola de impresora con doble sistema de almacenamiento. Existirá un almacenamiento primario
rápido, con capacidad limitada, y un almacenamiento secundario, de capacidad prácticamente ilimitada pero mucho más
lento. Cuando un proceso solicite una operación de impresión los datos se copiarán a uno de los almacenamientos, y
posteriormente un proceso servidor de impresión irá tomando e imprimiendo los datos disponibles en los almacenamientos.

Diseñar la aplicación que cumpla las siguientes restricciones:

1. Cuando se pida una operación de impresión los datos se copiarán al almacenamiento primario, si hay sitio, y si no al
   secundario.
2. Cuando un dato se guarda en un almacenamiento, permanece en él hasta el momento de imprimirlo, es decir, no hay
   intercambio de datos
   entre los almacenamientos.
3. Los datos han de imprimirse en el orden en que llegaron las peticiones. Al iniciar una acción de
   impresión hay que determinar el almacenamiento donde hay que tomar el siguiente dato. Obsérvese la situación en el
   siguiente ejemplo, que muestra cómo los datos pueden estar repartidos entre uno y otro almacenamiento de manera
   irregular:

   Peticiones: `[ G F E D C B A ]` --> Búfer prim./sec.: `[F C]` `[G E D]`  --> Datos ya impresos: `[B A]`

4. Cada almacenamiento requiere acceso exclusivo. No pueden hacerse dos operaciones a la vez sobre el mismo
   almacenamiento.

5. Durante el acceso al almacenamiento secundario no debe estar bloqueada la aceptación de peticiones ni el acceso al
   almacenamiento primario. La solución debe estar debidamente justificada en las decisiones que se pueden tomar. Para
   la implementación deberá simularse la velocidad relativa de las operaciones que se realicen en cada uno de los
   almacenamientos.

-------------------------------

### Constantes

Para la solución del problema de la cola de impresión, tenemos que tener presentes las siguientes constantes:

    NUM_PRINT_JOBS : es la cantidad de trabajos de impresión que se deben procesar

    NUM_PRINTERS : es el número de impresoras disponibles para realizar trabajos de impresión

### Análisis

El problema consiste en gestionar correctamente los trabajos de impresión y asignarlos a las impresoras disponibles
utilizando una cola de prioridad. Para ello, tenemos los siguientes elementos de sincronización:

* Añadir trabajos de impresión: Los trabajos de impresión se agregan a la cola de prioridad, que tiene un almacenamiento
  primario y secundario. Cuando el almacenamiento primario está lleno, los trabajos se añaden al almacenamiento
  secundario.
* Asignar trabajos de impresión a las impresoras: Las impresoras toman los trabajos de impresión de la cola de prioridad
  y los imprimen simulando un tiempo de espera.
* Concurrencia y bloqueo: Se utilizan bloqueos de lectura y escritura para garantizar la sincronización y el acceso
  seguro a las colas de prioridad.

### Variables compartidas

Por el análisis de las condiciones de sincronización, se utilizan dos colas de prioridad para manejar los trabajos de
impresión:

    primaryStorage : almacenamiento primario para los trabajos de impresión
    
    secondaryStorage : almacenamiento secundario para los trabajos de impresión cuando el almacenamiento primario está lleno

--------------------------------

# Primera Práctica

Para la resolución de esta primera práctica se utilizará como herramienta de concurrencia los semáforos.

## Problema a resolver

Santa Claus pasa su tiempo de descanso, durmiendo, en su casa del Polo Norte. Para poder despertarlo, se ha de cumplir
una de las dos condiciones siguientes:

1. Que todos los renos de los que dispone, nueve en total, hayan vuelto de vacaciones.
2. Que algunos de sus duendes necesiten su ayuda para fabricar un juguete.

Para permitir que Santa Claus pueda descansar, los duendes han acordado despertarle si tres de ellos tienen problemas a
la hora de fabricar un juguete. En el caso de que un grupo de tres duendes estén siendo ayudados por Santa, el resto de
los duendes con problemas tendrán que esperar a que Santa termine de ayudar al primer grupo.

En caso de que haya duendes esperando y todos los renos hayan vuelto de vacaciones, entonces Santa Claus decidirá
preparar el trineo y repartir los regalos, ya que su entrega es más importante que la fabricación de otros juguetes que
podría esperar al año siguiente. El último reno en llegar ha de despertar a Santa mientras el resto de renos esperan
antes de ser enganchados al trineo.

Para **solucionar** este problema hay que definir los siguientes procesos:

- Santa Claus
- Duende
- Reno.
- Hilo Principal para la realización de la prueba.

*Ayuda* : para los eventos de sincronización, será necesario disponer de mecanismos para despertar a Santa Claus,
notificar a los renos que se han de enganchar al trineo y controlar la espera por parte de los duendes cuando otro grupo
de duendes esté siendo ayudado por Santa Claus.

## Solución

### Constantes

Para la solución del problema tenemos que tener presentes las siguientes constantes.

    NUM_RENOS : es la cantidad de renos que dispone Santa para repartir los regalos

    NUM_DUENDES : es el número de duendes con problemas para avisar a Santa
    
    AVISO : {REGALOS, PROBLEMAS} : permite priorizar a Santa el tipo de aviso 

### Análisis

El problema consiste en sincronizar correctamente los tres procesos implicados en el problema: Santa, Renos, Duendes.
Para ello tenemos los siguientes elementos de sincronización:

- *El Reno vuelve de vacaciones* : En esta condición se deben contabilizar los Renos que han vuelto de vacaciones. El
  último Reno que vuelve de vacaciones deberá avisar a Santa que están disponibles para el reparto de los regales. Para
  ello deberán contabilizarse en una variable compartida y así comprobar quién es el último y que deberá avisar a Santa.
- *Esperar a repartir los regalos* : Lo Renos esperan a que Santa monte el trineo y está disponible con todos los Renos
  para repartir los regalos.
- *Duendes con problemas* : Si un número establecido de Duendes tienen problemas hay que avisar a Santa para que los
  ayude. Se necesita una variable compartida para llevar la cuenta de los Duendes que tienen problemas para que el que
  se compute como el último avise a Santa.
- *Esperar a Santa* : Los Duendes deberán esperar a Santa a que les ayude a resolver los problemas. Una vez que esto
  ocurra deberán ir computando individualmente que han sido ayudados y así permitir que otros Duendes puedan recibir
  ayuda si la necesitan.
- *Esperar ayuda* : Si un grupo de Duendes están recibiendo ayuda de Santa los duendes tienen que esperar a que se
  complete la operación antes de que puedan ello solicitar ayuda a Santa.
- *Descanso de Santa* : Si los Renos no han vuelto de vacaciones o no se ha alcanzado el número requerido de Duendes con
  problemas Santa estará descansando.

Como restricción adicional Santa dará prioridad al reparto de los regalos antes de resolver problemas a los Duendes.

### Variables compartidas

Por el análisis de las condiciones de sincronización van a ser necesarias dos variables compartidas para que los Renos y
Duendes puedan establecer sus condiciones de sincronización de forma apropiada:

    regresoVacaciones : número de Renos que han vuelto de vacaciones
    duendesConProblemas : número de duendes que tienen problemas hasta el momento

### Semáforos

Como herramienta de concurrencia para resolver el problema serán necesarios definir diferentes semáforos que garantices
las condiciones de sincronización y el uso seguro de las variables compartidas.

- `exm` : Semáforo de exclusión mutua para garantizar el acceso a las variables compartidas. Estará inicializado a 1
- `repartoRegalos` :  El semáforo controla que los Renos esperan a Santa antes de empezar el reparto de los regalos.
  Este semáforo estará inicializado a 0.
- `esperarAyuda` : El semáforo controla que los Duendes con problemas esperan a Santa para recibir la ayuda que
  necesitan. Estará inicializado a 0.
- `esperaDuende` : El semáforo controla si un grupo establecido de duendes está recibiendo ayuda de Santa. Este semáforo
  se inicializa a 1.
- `descansoSanta` : El semáforo controla el descanso de Santa y hasta que los Renos no hayan vuelto de vacaciones o un
  grupo de Duendes tenga problemas Santa estará descansando. El semáforo estará inicializado a 0.

### Diseño

Para simular las operaciones de los diferentes proceso se utilizan los siguientes métodos:

- `vacaciones()` : Este método simula el tiempo que el Reno está de vacaciones.
- `prepararTrineo()` : Este método simula las operaciones necesarias que tendrá que realizar Santa antes de empezar el
  reparto de los regalos.
- `repartirRegalos()` : Este método estará disponible para Santa y los Renos para simular que están repartiendo los
  regalos.
- `hacerJuguete()` : Este método simula el trabajo de un Duende haciendo juguetes.
- `prepararAyuda()` : Este método simula las operaciones que hace Santa antes de prestar la ayuda a los duendes.
- `resolverAyuda()` : Este método estará disponible para Santa y los Duendes para completar la ayuda que cada uno de
  ellos precisa.

El diseño de los diferentes proceso será el siguientes:

#### Proceso Santa

	Variables
		AVISO aviso

Ejecución :

```
while ( Hasta ser interrumpido ) {
	descansoSanta.wait()

	// Resolver el aviso
	exm.wait()
	if ( regresoVacaciones = NUM_RENOS ) {
		aviso = REGALOS
		regresoVacaciones = NINGUNO
	} else {
		aviso = PROBLEMAS
	}
	exm.signal()

	if ( aviso = REGALOS ) {
		prepararTrineo()
		for( i = 0; i < NUM_RENOS; i++ )
			repartoRegalos.signal()
		repartirRegalos()
	} else {
		prepararAyuda()
		for( i = 0; i < NUM_DUENDES; i++ )
			esperarAyuda.signal()
		resolverAyuda()
	}
}
```

#### Proceso Reno(id)

Ejecución :

```
while ( Hasta ser interrumpido ) {
	vacaciones()
	
	exm.wait()
	regresoVacaciones++
	if ( regrosoVacaciones = NUM_RENOS )
		descansoSanta.signal() // Se avisa a Santa
	exm.signal()

	// Se espera a Santa
	repartoRegalos.wait()
	repartirRegalos()
}
```

#### Proceso Duende(id)

Ejecución :

```
while ( Hasta ser interrumpido ) {
	hacerJuguete()
	
	esperaDuende.wait()
	exm.wait()
	duendesConProblemas++
	if ( duendesConProblemas = NUM_DUENDES )
		descansoSanta.signal() // Se avisa a Santa
	else
		esperaDuende.signal()
	exm.signal()

	esperarAyuda.wait()
	resolverAyuda()

	// Ya se ha completado la ayuda de Santa
	exm.wait()
	duendesConProblemas--
	if ( duendesConProblemas = NINGUNO )
		esperarDuende.signal()
	exm.signal()
}
``` 
