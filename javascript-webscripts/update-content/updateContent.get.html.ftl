<html>
    <head>
        <title>Webscript para modificar el contenido de un fichero</title>
        <link rel="stylesheet" href="${url.context}/css/main.css" TYPE="text/css">
    </head>
    <body>
        <table>
            <tr>
                <td><img src="${url.context}/images/logo/AlfrescoLogo32.png" alt="Alfresco" /></td>
                <td><nobr><span class="mainTitle">Update Content Web Script</span></nobr></td>                
    </tr>
    <tr><td><td>Alfresco ${server.edition} v${server.version}
    <tr><td><td>Selecciona el fichero con el contenido queremos cargar en Alfresco e inserta la 'uuid' del fichero cuyo contenido se va a modificar.
    <tr><td><td>La 'uuid' es el identificador único del archivo. ejemplo: 'ed20bccf-817e-11de-9b3d-51ac4af5bcb9'
</table>
<p>
<table>     
    <form action="${url.service}" method="post" enctype="multipart/form-data" acceptcharset="utf-8">
        <tr><td>File: <td><input type="file" name="file">
        <tr><td>uuid: <td><input name="uuidReemplazado">
        <tr><td><td>
        <tr><td><td><input type="submit" name="submit" value="Ejecutar">
    </form>
</table>
</body>
</html>
