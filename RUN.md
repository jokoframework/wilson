# Wilson

## Proyecto partido del Joko Backend Starter Kit
https://github.com/jokoframework/joko_backend_starter_kit

## Requisitos para ejecutar el servicio

* Maven 4.15 o mayor
* Java 11 (JDK11)
* Base de datos PostgreSQL 10.10 o mayor con el nombre *wilson*
* Base de datos MongoDB 4.2 o mayor


## Clonar proyecto

Debe clonar el proyecto del repositorio:

```shell
$ git clone https://github.com/jokoframework/joko_backend_starter_kit
$ cd ./wilson
```

## Configuración
El proyecto posee un conjunto de scripts que nos permiten automatizar el ciclo de vida de la base de datos. Con esto se puede crear facilmente toda la BD desde la linea de comandos. Para actualizar hay que seguir los siguientes pasos:
  
### Step 1) Crear el directorio PROFILE_DIR
El directorio de profile con su archivo application.properties que tiene la configuracion necesaria para lanzar la aplicacion spring-boot.
 
La convencion utilizada es tener un directorio (`/opt/wilson` en nuestros ejemplos), dentro del cual existan varios PROFILE_DIR segun se requiera. Por ejemplo:

```shell
/opt/wilson
├── dev
│   └── application.properties
└── qa
    └── application.properties
```
 
En el anterior ejemplo existen dos PROFILE_DIR dentro de `/opt/wilson`, el primero para development y el segundo para quality assurance.
 
Para referirse al PROFILE_DIR como parametro se usa "ext.prop.dir".
 
Obs.: Un archivo de ejemplo para el application.properties se encuentra en `src/main/resources/application.properties.examples`
  
### Step 2) Configuración del archivo "development.vars"
  
Se debe configurar un archivo "development.vars" por cada PROFILE_DIR, que servirá para la ejecucion de liquibase. Este es un archivo bash que debe tener dos variables: 
* MVN_SETTINGS - Archivo de configuracion de perfil Maven. En caso de utilizar el Artifactory interno, sería el recien descargado. Ej: `$HOME/.m2/settings.xml` puede utilizar de ejemplo el archivo que se encuentra en `src/main/resources/settings.xml.example` copiando a `$HOME/.m2/settings.xml`
* `PROFILE_DIR`: Directorio de perfil creado en el punto inicial. Ej: `/opt/wilson/dev`
  
Luego, el arbol de directorios quedaria así:
A continuación se muestra un esquema del arbol de directorio:
```shell
/opt/wilson/
├── dev
│   ├── application.properties
│   └── development.vars
└── qa
    ├── application.properties
    └── development.vars
```

Un ejemplo de este archivo se encuenta en `src/main/resources/development.vars`.
  
### Step 3) Configuración de variables de entorno
Exportar variable del PROFILE asignando a la variable ENV_VARS la ruta al archivo development.vars del PROFILE a utilizar:
```shell
export BASE_DIR="/opt/wilson";
export ENV_VARS="${BASE_DIR}/dev/development.vars";
```
**OJO**: Esto se debe realizar por cada terminal que se tenga abierta, no se guarda entre sesiones.

# Step 4) Instalar librerias Joko
	
Clonar los proyectos (no dentro de la misma carpeta backend o PROFILE)
	
https://github.com/jokoframework/joko-utils

https://github.com/jokoframework/security

Entrar en el directorio de jada proyecto y hacer lo siguiente:

* Para Joko-utils solo ejecutar 'mvn install', en caso de tener problemas descargando las dependencias ejecute
 
 `mvn install -Ddownloader.quick.query.timestamp=false`

* Para security hay que entrar al proyecto en github y seguir sus instrucciones de instalacion. Esto deja instaladas las librerías que son dependencias para el Backend.

# Step 5) Ejecutar Liquibase.
  
1.- Crea la schema de cero.
  ```shell
    $ ./scripts/updater fresh
  ```
2.- Inicializa datos básicos (o reinicializa)
  ```shell
    $ ./scripts/updater seed src/main/resources/db/sql/seed-data.sql
    $ ./scripts/updater seed src/main/resources/db/sql/seed-config.sql
  ```
  **OJO**:
* El parámetro `fresh` elimina la base de datos que está configurada en el `application.properties` y la vuelve a crear desde cero con la última versión del schema
  
* Los parámetros `seed <file>` cargan datos indicados en el archivo `<file>`, para los casos en que se ejecute `fresh` siempre debe ir seguido de un `seed` con el archivo que (re)inicializa los datos básicos del sistema 
  
* Los datos básicos del sistema estan en dos archivos:

`seed-data.sql`: Todos la configuracion base que es independiente al ambiente
`[ambiente]-config`. Por ejemplo: `dev-config.sql` . Posee los parametros de configuracion adecuados  para el ambiente de desarrollo. Tambien existe `qa-config` y `prod-config`
  
3.- Para correr el liquibase en modo de actualización ejecute:  
```
    $ ./scripts/updater update
  ```
    
# Step 6) Preparar base de datos MongoDB.
Para guardar los datos sobre que servicios se desea que Wilson aplique sus funcionalidades de Cache y para guardar el cache mismo se utiliza
una base de datos MongoDB, la configuración de los parametros de conexión a esta base de datos se deben configurar en el application.properties

En el archivo de ejemplo para el application.properties que se encuentra en `src/main/resources/application.properties.examples` se detallan
campos para definir el HOST, PORT y NAME de la base de datos de MongoDB a la que se conectara Wilson

## Correr desde una consola con `mvn` (Maven)

Si es una consola nueva exportar las variables primero:
```shell
export BASE_DIR="/opt/wilson";
export ENV_VARS="${BASE_DIR}/dev/development.vars";
```

Luego asumiendo que ya instalo Maven, solo queda correr el proyecto como una aplicación de Spring Boot desde su terminal:
```shell
  $ mvn spring-boot:run -Dext.prop.dir=/opt/wilson/dev -Dspring.config.location=file:///opt/wilson/dev/application.properties
```

El usuario/password default que se crea con la base de datos, es admin/123456

## Correr con Spring Tool Suite 3 o Spring Tools 4
Para poder levantar la apliación desde el IDE se debe crear un "Run Configuration", del menú principal seleccionar: `Run>Run Configurations...`, de la lista de tipos de configuraciones crear una nueva del tipo "Spring Boot App" y configurar lo siguiente:

En la pestaña "Spring Boot" de su archivo de configuración:
* Name - Nombre de la configuración
* Project - Seleccionar el proyecto "wilson" de su workspace
* Main Type - Seleccionar el archivo "Application" que lanza la applicación (Archivo sugerido por el menú de busqueda)
* Profile - Seleccionar el PROFILE_DIR a utilizar (Ej: dev o qa)

En la pestaña "Arguments" de su archivo de configuración:
* VM arguments - Añadir el parámetro `-Dspring.config.location=file://` con la ruta al archivo application.properties (Ej: -Dspring.config.location=file:///opt/wilson/application.properties -Dext.prop.dir=/opt/wilson).

Esta guía es especifica para estos dos programas pero la mayoría de los IDEs soportan ejecución de aplicaciones tipo Spring Boot o permiten configurar ejecuciones customizadas de maven.

## Swagger API
El proyecto cuenta con documentación del API accesible desde el swagger-ui. URI al swagger:

http://localhost:8080/swagger/index.html
