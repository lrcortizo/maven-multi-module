# Ejercicios Maven Multi-módulo

En este ejercicio vamos a trabajar con la herencia, composición y los perfiles de Maven.

El punto de partida es un proyecto que está dividido en tres subproyectos independientes. El objetivo será convertirlos en un único proyecto Maven con varios módulos (uno por subproyecto).

La estructura inicial de este proyecto es la siguiente:
```
maven-multi-module
 |-- domain
 |-- logic
 `-- view
```
Cada uno de los subdirectorios contiene un proyecto Maven. El contenido de cada uno de ellos es el siguiente:
* `domain`: Contiene las entidades. En este momento, solo contiene la entidad `Person`. Incluye tests de unidad básicos.
* `logic`: Contiene la lógica de negocio. En este momento, solo contiene la clase `PeopleFacade`, con una serie de métodos para gestionar el almacenamiento de las entidades `Person` en una base de datos utilizando JDBC. Incluye tests de unidad básicos.
* `view`: Contiene una clase principal temporal, que se conecta a una base de datos y utiliza la clase `PeopleFacade` para ejecutar una serie de consultas y mostrar los resultados. En un futuro, se espera que contenga una aplicación de escritorio de gestión.

## Preparación
Empieza el ejercicio clonando el repositorio del proyecto. Para ello, ejecuta:
```
git clone http://sing.ei.uvigo.es/dt/gitlab/dgss-1718/maven-multi-module.git
```

## Ejercicio 1. Ejecuta la aplicación
Antes de empezar con las modificaciones vamos a comprobar que la aplicación funciona correctamente. Para ello, en el proyecto `view` se ha incluido el plugin `org.codehaus.mojo:exec-maven-plugin`, que permite ejecutar la aplicación con el comando:
```bash
mvn exec:java
```
Ejecuta este comando en el proyecto `view`. ¿Qué ha ocurrido? ¿Se ha ejecutado correctamente? Si no ha sido así, identifica el motivo y soluciónalo.

Una vez que se haya ejecutado correctamente, edita el fichero `PersonManager` y comenta la configuración del driver H2 y descomenta la configuración del driver Derby. Ejecuta de nuevo y comprueba que obtienes los mismos resultados.

**Nota**: El driver H2 creará el fichero `target/composite.mv.db` y el driver Derby creará el directorio `target/composite`, donde almacenarán la base de datos. Por lo tanto, una ejecución de `mvn clean` eliminará ambas bases de datos.

## Ejercicio 2. Herencia y Agregación
Modifica el POM de los proyectos para conseguir que:
1. Todos usen las mismas versiones de las dependencias, plugins y de Java. En el caso de que haya diferentes versiones en los proyectos, mantén la versión más actual.
2. Con una única invocación de Maven se ejecuten el mismo comando en todos los subproyectos.

Al hacer esta modificación es importante tener en cuenta que :
* Los subproyectos no deben importar más dependencias o plugins de los que necesitan.
* Debe respetarse el `scope` de las dependencias en cada subproyecto.

## Ejercicio 3. Variables de Entorno
En el proyecto `view` la configuración de la base de datos está puesta directamente en el código, algo que no es recomendable.

Modifica este proyecto para que la configuración de la base de datos (URL, login y password) se tome de las variables del entorno.

Para realizar esta modificación el plugin `org.codehaus.mojo:exec-maven-plugin` incluye, entre sus parámetros de configuración, el elemento `<systemProperties>`, donde se pueden configurar propiedades del sistema que recibirá el programa al ejecutarse. Estas propiedaes podrán recuperarse en el programa mediante el método `System.getProperty("propiedad")`.

Utiliza los siguientes nombres de propiedades:
* `db.url`: URL de conexión con la base de datos.
* `db.login`: Login de acceso a la base de datos.
* `db.password`: Password de acceso a la base de datos.

Una vez hayas hecho este cambio, la configuración de conexión con la base de datos debería estar completamente en el POM en lugar de en el código de la aplicación.

## Ejercicio 4. Perfiles
En el código de la aplicación se incluyen dos configuraciones de conexión con la base de datos, una para H2 y otra para Derby.

Queremos seguir pudiendo utilizar ambas configuraciones, por lo que debes crear dos perfiles, uno para utilizar H2 y otro para utilizar Derby.

Por defecto, la aplicación debe utilizar la base de datos H2.

¿En qué POM has ubicado los perfiles? ¿Por qué? ¿Qué comando debes utilizar para ejecutar, desde Maven, la aplicación con H2? ¿Y con Derby?
