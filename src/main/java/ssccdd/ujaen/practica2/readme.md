---

# ENUNCIADO

## Ejercicio

Para la solución de la práctica se utilizará como herramienta de concurrencia el desarrollo de monitores.
Hay que tener presenta a la hora de implementar la solución que Java no dispone de esta herramienta.
Para ello el alumno podrá utilizar cualquier utilidad de concurrencia para diseñar la clase que representa al monitor de
la solución teórica. En la implementación del monitor se deberán respetar las características que presenta el monitor
que se ha presentado en teoría. También se utilizará la factoría `Executors` y la interface `ExecutorService` para la
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

# ANALISIS

## Requisitos:

1. El sistema debe admitir múltiples trabajos de impresión y almacenarlos en una cola.
2. El sistema debe ser capaz de manejar múltiples impresoras que procesan trabajos de impresión simultáneamente.
3. Los trabajos de impresión deben procesarse en función de su orden de llegada.
4. El sistema debe poder simular la impresión de un trabajo con diferentes tiempos de espera según la ubicación del
   trabajo (almacenamiento primario o secundario).

## Análisis:

Para cumplir con estos requisitos, se ha ideado una división del sistema en las siguientes clases:

1. *Clase `ImpresoraJob`*: Esta clase representará un trabajo de impresión individual. Contiene información sobre la
   hoja que se va a imprimir, un identificador único y el orden de llegada del trabajo.
2. *Clase `MonitorImpresora`*: esta clase se encarga de gestionar los trabajos de impresión y coordinar las impresoras.
   Utilizará dos colas para almacenar trabajos de impresión: un almacenamiento primario de tamaño fijo y un
   almacenamiento secundario ilimitado. Los trabajos se almacenarán en el almacenamiento primario hasta que se llena, y
   luego se en el secundario. La clase utilizará un `ReentrantLock` y objetos `Condition` para garantizar la
   seguridad de los hilos al acceder a las colas. Habrá funciones para agregar trabajos de impresión y recuperar el
   siguiente trabajo de impresión en función del orden de llegada y del almacenamiento en el que se encuentran y un
   método para simular la impresión de un trabajo de impresión con diferentes tiempos de espera según el tipo
   almacenamiento del trabajo.
3. *Clase `Main`*: Esta clase se encarga de la creación y envío de trabajos de impresión al `MonitorImpresora`. Se
   utilizará un ExecutorService con un número fijo de hilos para simular múltiples impresoras que procesan trabajos de
   impresión simultáneamente. Después de que todos los trabajos de impresión han sido procesados.
4. *Clase `Impresora`*: Esta clase implementa la interfaz Runnable y es utilizada por los hilos en el `ExecutorService`
   para procesar simular los trabajos de impresión. La clase obtiene el siguiente trabajo de impresión
   del `MonitorImpresora`, simula la impresión del trabajo y actualiza el CountDownLatch.

## Variables compartidas y mecanismos de sincronización:

* **Variables compartidas**:

    1. almacenamientoPrimario: Una cola que representa el almacenamiento primario rápido y de capacidad limitada.
    2. almacenamientoSecundario: Una cola que representa el almacenamiento secundario más lento pero de capacidad
       ilimitada.
    3. lock: objetos ReentrantLock para garantizar la seguridad de los hilos al acceder a las colas.
    4. conditionPrimario y conditionSecundario: Dos objetos Condition asociados a cada ReentrantLock para controlar el
       acceso a las colas.

* **Mecanismos de sincronización**:
    1. Los métodos `agregarTrabajo` y `obtenerSiguienteTrabajo` en la clase `MonitorImpresora` utilizarán los
       ReentrantLock y los objetos Condition para sincronizar el acceso a las colas de almacenamiento. De esta forma se
       garantiza que múltiples hilos puedan leer de las colas simultáneamente, pero sólo un hilo puede escribir en ellas
       al mismo tiempo.
    2. La clase `MonitorImpresora` puede utilizar el `ReentrantLock` y objetos Condition para coordinar el acceso a
       las colas de almacenamiento primario y secundario, garantizando que los trabajos se procesen en función de su
       orden de llegada y del almacenamiento en el que se encuentran.
    3. Un `CountDownLatch` para controlar cuándo todos los trabajos de impresión han sido procesados.

### Interacciones y relaciones entre las clases y componentes:

* La clase `Main` crea y envía trabajos de impresión al `MonitorImpresora`. Después, crea y controla
  el `ExecutorService`  que utiliza la clase `Impresora` para procesar los trabajos de impresión.
* La clase `MonitorImpresora` coordina la gestión de los trabajos de impresión, garantizará la sincronización entre los
  hilos y el acceso a los almacenamientos primario y secundario utilizando `ReentrantLock` y `Condition`.
* La clase `ImpresoraJob` representa un trabajo de impresión individual y contiene la información necesaria para
  procesar el trabajo de impresión.
* La clase `Impresora` es utilizada por los hilos en el `ExecutorService` para procesar trabajos de impresión en
  paralelo. Interactúa con la clase `MonitorImpresora` para obtener y procesar trabajos de impresión. Se actualizará el
  `CountDownLatch` después de procesar cada trabajo de impresión.