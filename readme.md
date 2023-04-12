---

# ENUNCIADO

## Ejercicio

Para la solución de la práctica se utilizará como herramienta de concurrencia el desarrollo de monitores.
Hay que tener presenta a la hora de implementar la solución que Java no dispone de esta herramienta.
Para ello el alumno podrá utilizar cualquier utilidad de concurrencia para diseñar la clase que representa al monitor de
la solución teórica. En la implementación del monitor se deberán respetar las características que presenta el monitor
que se ha presentado en teoría. También se utilizará la factoría `Executors` y la interface ``ExecutorService`` para la
ejecución de las tareas concurrentes que compondrán la solución de la práctica.

## Problema a resolver

Se trata de organizar una cola de impresora con doble sistema de almacenamiento. Existirá un almacenamiento primario
rápido, con capacidad limitada, y un almacenamiento secundario, de capacidad prácticamente ilimitada pero más lento.
Cuando un proceso solicite una operación de impresión los datos se copiarán a uno de los almacenamientos, y
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

4. Cada almacenamiento requiere acceso exclusivo. No pueden hacerse dos operaciones a la vez sobre el mismo
   almacenamiento.

5. Durante el acceso al almacenamiento secundario no debe estar bloqueada la aceptación de peticiones ni el acceso al
   almacenamiento primario. La solución debe estar debidamente justificada en las decisiones que se pueden tomar. Para
   la implementación deberá simularse la velocidad relativa de las operaciones que se realicen en cada uno de los
   almacenamientos.

---

## Análisis:

Este sistema de impresión en Java tiene los siguientes componentes principales:

1. Clase `TrabajoImpresora`: Esta clase representa un trabajo de impresión individual con un nombre, un identificador
   único y un orden de llegada. Además, proporciona un método estático para generar nombres de trabajos aleatorios.

2. Clase `MonitorImpresora`: Esta clase es responsable de gestionar y coordinar los trabajos de impresión entre las
   impresoras. Utiliza dos colas de trabajos de impresión (primario y secundario) y proporciona métodos para agregar
   trabajos de impresión, recuperar el siguiente trabajo de impresión según el orden de llegada y el almacenamiento en
   el que se encuentran, simular la impresión de un trabajo de impresión con diferentes tiempos de espera según el tipo
   de almacenamiento del trabajo, y marcar trabajos como completados. Además, utiliza un `CountDownLatch` para controlar
   cuándo todos los trabajos de impresión han sido procesados y dos `ReentrantLock` y objetos `Condition` para
   garantizar la seguridad de los hilos al acceder a las colas.

3. Clase `Impresora`: Esta clase implementa la interfaz Runnable y representa una impresora individual. Cada impresora
   intenta obtener trabajos de impresión del `MonitorImpresora` y simula la impresión del trabajo. Después de procesar
   cada trabajo de impresión, actualiza el `CountDownLatch` en el `MonitorImpresora`.

4. Clase `Main`: Esta clase se encarga de la creación y envío de trabajos de impresión al `MonitorImpresora`. Utiliza
   un `ExecutorService` con un número fijo de hilos para simular múltiples impresoras que procesan trabajos de impresión
   simultáneamente. Después de que todos los trabajos de impresión han sido procesados, el programa espera a que se
   complete el `CountDownLatch`.

## Interacciones y relaciones entre las clases y componentes:

* La clase `Main` crea y envía trabajos de impresión al `MonitorImpresora`. A continuación, crea y controla el
  `ExecutorService` que utiliza la clase `Impresora` para procesar los trabajos de impresión.

* La clase `MonitorImpresora` coordina la gestión de los trabajos de impresión, garantizando la sincronización entre los
  hilos y el acceso a las colas de almacenamiento primario y secundario utilizando `ReentrantLock` y
  objetos `Condition`.

* La clase `TrabajoImpresora` representa un trabajo de impresión individual y contiene la información necesaria para
  procesar el trabajo de impresión.

* La clase `Impresora` es utilizada por los hilos en el `ExecutorService` para procesar trabajos de impresión en
  paralelo. Interactúa con la clase `MonitorImpresora` para obtener y procesar trabajos de impresión y actualizar el
  `CountDownLatch` después de procesar cada trabajo de impresión.

## Mecanismos de sincronización:

1. `ReentrantLock`: La clase `MonitorImpresora` utiliza dos `ReentrantLock` para garantizar la seguridad de los hilos al
   acceder a las colas de almacenamiento primario y secundario. Los métodos `annadirTrabajo()`, `siguienteTrabajo()` y
   `completado()` utilizan este bloqueo para proteger el acceso a las colas. Esto permite que múltiples
   hilos lean las colas simultáneamente, pero solo un hilo pueda escribir en ellas al mismo tiempo.

2. `Condition`: La clase `MonitorImpresora` también utiliza dos objetos `Condition`, `conditionPrimario` y
   `conditionSecundario`, asociados con los `ReentrantLock` para controlar el acceso a las colas de almacenamiento
   primario y secundario. Esto garantiza que los trabajos se procesen en función de su orden de llegada y del
   almacenamiento en el que se encuentran. Además, permite que las impresoras esperen a que haya trabajos disponibles
   antes de intentar obtenerlos, evitando así la espera activa.

3. `CountDownLatch`: La clase `MonitorImpresora` utiliza un `CountDownLatch` para rastrear cuándo todos los trabajos de
   impresión han sido procesados. Cada vez que un trabajo de impresión se marca como completado, el `CountDownLatch` se
   actualiza. La clase `Main` espera a que el `CountDownLatch` alcance cero antes de finalizar el programa, asegurando
   que todos los trabajos de impresión hayan sido procesados antes de que el programa termine.

## Lógica interna:

1. La clase `Main` crea un objeto `MonitorImpresora` y envía trabajos de impresión a él. Luego, crea
   un `ExecutorService` con un número fijo de hilos para simular múltiples impresoras que procesan trabajos de impresión
   simultáneamente.

2. Cada hilo en el `ExecutorService` ejecuta una instancia de la clase `Impresora`. Las impresoras solicitan trabajos de
   impresión al `MonitorImpresora` utilizando el método obtenerSiguienteTrabajo. Si no hay trabajos disponibles, las
   impresoras esperan hasta que haya trabajos en las colas.

3. Una vez que una impresora obtiene un trabajo de impresión, simula el proceso de impresión con un tiempo de espera
   diferente según el almacenamiento donde se encontraba el trabajo. Después de simular la impresión, la impresora marca
   el trabajo como completado en el `MonitorImpresora`.

4. El `MonitorImpresora` actualiza el `CountDownLatch` cada vez que un trabajo de impresión se marca como completado.
   Cuando todos los trabajos han sido procesados, el `CountDownLatch` llega a cero y la clase `Main` finaliza el
   programa.