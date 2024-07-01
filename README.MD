Para un entorno de desarrollo, es necesario configurar java sfx. Para esto vamos a descargar la versión 17 del siguiente link.
https://gluonhq.com/products/javafx/. La versión debe ser la 17.0.11 y descargar la versión correspondiente al sistema operativo.

Conseguimos la ruta y la agregamos al archivo pom.xml.

```
<path.to.javafx>/usr/local/javafx-sdk-17.0.11/lib</path.to.javafx>
```

Antes de correr el aplicativo, es necesario tener abierta la instancia de la base de datos

```
docker-compose up
```

Para correr el aplicativo, se debe ejecutar el siguiente comando
    
```
java -jar
```