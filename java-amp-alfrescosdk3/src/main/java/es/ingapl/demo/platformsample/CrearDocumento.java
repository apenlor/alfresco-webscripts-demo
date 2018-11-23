package es.ingapl.demo.platformsample;

import es.ingapl.demo.platformsample.jaxb.InformacionDocumento;
import es.ingapl.demo.platformsample.predicates.PredicateFormField;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import org.alfresco.model.ContentModel;
import org.alfresco.service.cmr.model.FileFolderService;
import org.alfresco.service.cmr.repository.ContentService;
import org.alfresco.service.cmr.repository.ContentWriter;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.alfresco.service.namespace.QName;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.extensions.webscripts.WebScriptRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.extensions.webscripts.AbstractWebScript;
import org.springframework.extensions.webscripts.WebScriptException;
import org.springframework.extensions.webscripts.WebScriptResponse;
import org.springframework.extensions.webscripts.servlet.FormData;
import org.springframework.http.HttpStatus;


/**
 * 
 * @author ingapl
 */
public class CrearDocumento extends AbstractWebScript {

    private static Log logger = LogFactory.getLog(CrearDocumento.class);

    private FileFolderService fileFolderService;
    private NodeService nodeService;
    private ContentService contentService;
    private QName DOCUMENT_TYPE = QName.createQName("http://www.ejemplo.com", "documento");
    private QName PROP_TITULO = QName.createQName("http://www.ejemplo.com", "titulo");
    private QName PROP_RESUMEN = QName.createQName("http://www.ejemplo.com", "resumen");
    
    @Override
    public void execute(WebScriptRequest request, WebScriptResponse response) throws IOException {

        CrearDocumentoDTO crearDocumento = getDatosRequestCrearDocumentoDTO(request);

        //Creacion del nodo
        NodeRef documento = fileFolderService.create(crearDocumento.destino, crearDocumento.titulo.concat(".pdf"), DOCUMENT_TYPE).getNodeRef();
        //Set propiedades
        nodeService.setProperty(documento, PROP_TITULO, crearDocumento.titulo);
        nodeService.setProperty(documento, PROP_RESUMEN, crearDocumento.resumen);
        //Contenido
        ContentWriter contentWriter = contentService.getWriter(documento, ContentModel.PROP_CONTENT, true);
        contentWriter.setEncoding("UTF-8");
        contentWriter.setMimetype("application/pdf");
        contentWriter.putContent(crearDocumento.stream);

        //Respuesta identica al script de consulta
        generarRespuesta(documento, response);
    }

    /**
     * Extrae los parametros de la peticion y genera un objeto con ellos para
     * facilitar su manejo
     *
     * @param request
     * @return
     */
    private CrearDocumentoDTO getDatosRequestCrearDocumentoDTO(WebScriptRequest request) {
        CrearDocumentoDTO crearDocumento = new CrearDocumentoDTO();
        FormData formData = (FormData) request.parseContent();
        FormData.FormField[] fields = formData.getFields();
        List<FormData.FormField> formFieldList = Arrays.asList(fields);

        FormData.FormField contenido = (FormData.FormField) CollectionUtils.find(formFieldList, new PredicateFormField("contenido"));
        FormData.FormField titulo = (FormData.FormField) CollectionUtils.find(formFieldList, new PredicateFormField("titulo"));
        FormData.FormField resumen = (FormData.FormField) CollectionUtils.find(formFieldList, new PredicateFormField("resumen"));
        FormData.FormField oidCarpetaDestino = (FormData.FormField) CollectionUtils.find(formFieldList, new PredicateFormField("oidCarpetaDestino"));

        if (contenido == null || contenido.getInputStream() == null) {
            throw new WebScriptException(HttpStatus.BAD_REQUEST.value(), "El parametro contenido no acepta valores nulos o vacios");
        }
        if (titulo == null || StringUtils.isBlank(titulo.getValue())) {
            throw new WebScriptException(HttpStatus.BAD_REQUEST.value(), "El parametro titulo no acepta valores nulos o vacios");
        }
        if (resumen == null || StringUtils.isBlank(resumen.getValue())) {
            throw new WebScriptException(HttpStatus.BAD_REQUEST.value(), "El parametro resumen no acepta valores nulos o vacios");
        }
        if (resumen == null || StringUtils.isBlank(oidCarpetaDestino.getValue())) {
            throw new WebScriptException(HttpStatus.BAD_REQUEST.value(), "El parametro oidCarpetaDestino no acepta valores nulos o vacios");
        }

        crearDocumento.stream = contenido.getInputStream();
        crearDocumento.titulo = titulo.getValue();
        crearDocumento.resumen = resumen.getValue();
        crearDocumento.destino = recuperarNodoDestino(oidCarpetaDestino.getValue());

        return crearDocumento;
    }

    /**
     * Valida que el nodo aportado existe y es una carpeta
     *
     * @param oidCarpetaDestino
     * @return
     */
    private NodeRef recuperarNodoDestino(String oidCarpetaDestino) {
        if (!NodeRef.isNodeRef(oidCarpetaDestino)) {
            throw new WebScriptException(HttpStatus.BAD_REQUEST.value(), "El parametro oidCarpetaDestino no encaja con el patron de un nodeRef de Alfresco");
        }
        NodeRef nodeRef = new NodeRef(oidCarpetaDestino);
        if (!nodeService.exists(nodeRef)) {
            throw new WebScriptException(HttpStatus.BAD_REQUEST.value(), "El nodo destino aportado no existe en el sistema: " + nodeRef);
        }
        QName tipoDocumento = nodeService.getType(nodeRef);
        if (!ContentModel.TYPE_FOLDER.equals(tipoDocumento)) {
            throw new WebScriptException(HttpStatus.BAD_REQUEST.value(), "El nodo destino aportado no es del tipo documental esperado: " + tipoDocumento + ". Se espera: " + ContentModel.TYPE_FOLDER);
        }
        return nodeRef;
    }

    /**
     * Recupera toda la iformacion del nodo y la escribe en el response
     *
     * @param nodeRefDocumento
     * @param response
     */
    private void generarRespuesta(NodeRef nodeRefDocumento, WebScriptResponse response) {
        Map<QName, Serializable> propiedades = nodeService.getProperties(nodeRefDocumento);

        InformacionDocumento informacionDocumento = new InformacionDocumento();
        informacionDocumento.setNodeRef(String.valueOf(nodeRefDocumento));
        for (Map.Entry<QName, Serializable> entry : propiedades.entrySet()) {
            informacionDocumento.getPropiedades().put(String.valueOf(entry.getKey()), String.valueOf(entry.getValue()));
        }

        try {
            JAXBContext jaxbc = JAXBContext.newInstance(new Class[]{InformacionDocumento.class});
            Marshaller marshaller = jaxbc.createMarshaller();
            marshaller.marshal(new JAXBElement(InformacionDocumento.ELEMENT_QNAME, InformacionDocumento.class, informacionDocumento), response.getOutputStream());
        } catch (JAXBException ex) {
            throw new WebScriptException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error en la generacion del contexto de jaxb", ex);
        } catch (IOException ex) {
            throw new WebScriptException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "IOException durante la escritura de la respuesta en WebScriptResponse", ex);
        }
    }

    /**
     * Clase interna para la gestion de los parametros de la peticion
     */
    private class CrearDocumentoDTO {

        private InputStream stream;
        private String titulo;
        private String resumen;
        private NodeRef destino;

        public CrearDocumentoDTO() {
        }

    }

    public void setNodeService(NodeService nodeService) {
        this.nodeService = nodeService;
    }

    public void setFileFolderService(FileFolderService fileFolderService) {
        this.fileFolderService = fileFolderService;
    }

    public void setContentService(ContentService contentService) {
        this.contentService = contentService;
    }

}
