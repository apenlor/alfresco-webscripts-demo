var content = null;
var uuidReemplazado = "";
var queryReemplazado;
var fileToUpdate;
var nombreDelFicheroNuevo;

/*
 * Se carga el contenido del documento seleccionado
 * (el documento no se guarda en Alfresco, se desecha)
 *
 * Se localiza el fichero (a través de la uuid) cuyo contenido vamos a sustituir
 * y se le fija el contenido cargado.
 *
 * Un ejemplo de uuid sería: 'ed20bccf-817e-11de-9b3d-51ac4af5bcb9'
 */

logger.log("iniciando webscript 'updateContent'...");

try{
    for each (field in formdata.fields)
    {
        // leemos la uuid del fichero cuyo contenido vamos a reemplazar
        if (field.name == "uuidReemplazado")
        {
            uuidReemplazado = field.value;
        }
        // leemos el contenido a cargar
        else if (field.name == "file" && field.isFile)
        {
            content = field.content;
            nombreDelFicheroNuevo = field.filename;
            logger.log("Nombre nuevo del fichero: "+nombreDelFicheroNuevo);
        }
    }

    // nos aseguramos de que los campos obligatorios existan
    if (content == undefined || uuidReemplazado == undefined)
    {
        status.code = 400;
        status.message = "Uploaded file cannot be located in request";
        status.redirect = true;
    }
    else
    {
        // localizamos el fichero a reemplazar
        queryReemplazado = "@sys\\:node-uuid:\"" + uuidReemplazado +  "\"";
        fileToUpdate = search.luceneSearch(queryReemplazado)[0];

        // cargamos el contenido en el
        fileToUpdate.properties.content.write(content);
        fileToUpdate.properties["cm:name"] = nombreDelFicheroNuevo;
        fileToUpdate.save();

        // setup model for response template
        model.fileToUpdate = fileToUpdate;

        logger.log("fin de la ejecución del webscript 'fileToUpdate'");
    }
}
catch (error){
    logger.log("Se ha producido un error en el webscript. El valor de alguno de los campos no es válido o es inexistente");
    status.code = 500;
    status.message = "Se ha producido un error en la ejecución del Web Script debido a que alguno de los campos contiene un valor inválido o está vacío." +
        "<br> Recuerde que debe seleccionar un fichero del cual se cargará el contenido en Alfresco." +
        "<br> Deberá también fijar la 'uuid' del documento de Alfresco cuyo contenido vamos a reemplazar. El formato de la 'uuid' será del tipo 'ed20bccf-817e-11de-9b3d-51ac4af5bcb9'";
    status.redirect = true;
}