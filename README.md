# alfresco-webscripts-demo

Incluye dos ejemplos utilizados en formaciones impartidas sobre desarrollo y extensión del gestor documental Alfresco. Los aspectos, metadatos o referencias a tipos utilizados son todos ficticios.

## java-amp-alfrescosdk3
Ejemplo de AMP para Alfresco con webscripts JAVA. Se utiliza [Alfresco SDK 3](https://docs.alfresco.com/5.2/concepts/sdk-intro.html).

El proyecto permite levantar una instancia de Alfresco con el amp ya instalado. El amp incluye tres webscript Java
 - **helloWorld** 
 - **crearDocumento** 
 - **getDocumento**

## javascript-webscripts
Incluye tres webscripts desarrollados con javascript. Para el despliegue en el gestor documental deberían simplemente ser incluídos en la carpeta:

 - **Data Dictionary > Web Scripts**

Los scripts incluídos permiten:
- **delete-node** (incluye log en user home)
- **search-document-title**
- **update-content** (acceso mediante post y get)


