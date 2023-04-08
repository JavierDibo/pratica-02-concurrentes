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


--------------------------------


Ahora que las clases están adecuadamente descritas, tu trabajo es analizar y modificar, si fuese necesario, el código que te he proporcionado para
que se ajuste a la descripción dada por este ejercicio, exponiendo tu razonamiento:
"## Resolución con monitores

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
   intercambio de datos entre los almacenamientos.
3. Los datos han de imprimirse en el orden en que llegaron las peticiones. Al iniciar una acción de
   impresión hay que determinar el almacenamiento donde hay que tomar el siguiente dato. Obsérvese la situación en el
   siguiente ejemplo, que muestra cómo los datos pueden estar repartidos entre uno y otro almacenamiento de manera
   irregular: Peticiones: `[ G F E D C B A ]` --> Búfer prim./sec.: `[F C]` `[G E D]`  --> Datos ya impresos: `[B A]`

4. Cada almacenamiento requiere acceso exclusivo. No pueden hacerse dos operaciones a la vez sobre el mismo
   almacenamiento.

5. Durante el acceso al almacenamiento secundario no debe estar bloqueada la aceptación de peticiones ni el acceso al
   almacenamiento primario. La solución debe estar debidamente justificada en las decisiones que se pueden tomar. Para
   la implementación deberá simularse la velocidad relativa de las operaciones que se realicen en cada uno de los
   almacenamientos."