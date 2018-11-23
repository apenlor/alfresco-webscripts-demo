
package es.ingapl.demo.platformsample;

import es.ingapl.demo.platformsample.jaxb.InformacionDocumento;
import java.io.IOException;
import java.io.Serializable;
import java.util.Map;
import java.util.Map.Entry;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.alfresco.service.namespace.QName;
import org.apache.commons.lang3.StringUtils;
import org.springframework.extensions.webscripts.WebScriptRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.extensions.webscripts.AbstractWebScript;
import org.springframework.extensions.webscripts.WebScriptException;
import org.springframework.extensions.webscripts.WebScriptResponse;
import org.springframework.http.HttpStatus;


/**
 * 
 * @author ingapl
 */
public class GetDocumento extends AbstractWebScript {

    private static Log logger = LogFactory.getLog(GetDocumento.class);

    private NodeService nodeService;
    private QName DOCUMENT_TYPE = QName.createQName("http://www.ejemplo.com", "document");

    @Override
    public void execute(WebScriptRequest request, WebScriptResponse response) throws IOException {

        String oidDocumento = request.getParameter("oidDocumento");

        NodeRef nodeRefDocumento = validarOidDocumento(oidDocumento);

        generarRespuesta(nodeRefDocumento, response);

    }

    /**
     * Valida que el oid aportado existe en el sistema y se recupera el noderef
     * asociado
     *
     * @param oidDocumento
     * @return
     */
    private NodeRef validarOidDocumento(String oidDocumento) {
        if (StringUtils.isBlank(oidDocumento)) {
            throw new WebScriptException(HttpStatus.BAD_REQUEST.value(), "El parametro oidDocumento no acepta valores nulos o vacios");
        }
        if (!NodeRef.isNodeRef(oidDocumento)) {
            throw new WebScriptException(HttpStatus.BAD_REQUEST.value(), "El parametro oidDocumento no encaja con el patron de un nodeRef de Alfresco");
        }
        NodeRef nodeRef = new NodeRef(oidDocumento);
        if (!nodeService.exists(nodeRef)) {
            throw new WebScriptException(HttpStatus.BAD_REQUEST.value(), "El nodo aportado no existe en el sistema: " + nodeRef);
        }
        QName tipoDocumento = nodeService.getType(nodeRef);
        if (!DOCUMENT_TYPE.equals(tipoDocumento)) {
            throw new WebScriptException(HttpStatus.BAD_REQUEST.value(), "El nodo aportado no es del tipo documental esperado: " + tipoDocumento + ". Se espera: " + DOCUMENT_TYPE);
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
        for (Entry<QName, Serializable> entry : propiedades.entrySet()) {
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

    public void setNodeService(NodeService nodeService) {
        this.nodeService = nodeService;
    }

}
