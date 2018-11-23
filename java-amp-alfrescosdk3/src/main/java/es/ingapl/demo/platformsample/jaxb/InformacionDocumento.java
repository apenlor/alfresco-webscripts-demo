package es.ingapl.demo.platformsample.jaxb;

import java.util.HashMap;
import java.util.Map;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author ingapl
 */
@XmlRootElement(name = "InformacionDocumento", namespace = "http://www.ejemplo.com")
public class InformacionDocumento {

    public static final javax.xml.namespace.QName ELEMENT_QNAME = new javax.xml.namespace.QName("http://www.ejemplo.com", "InformacionDocumento");

    private String nodeRef;
    private Map<String, String> propiedades;

    public InformacionDocumento() {
        propiedades = new HashMap<String, String>();
    }

    @XmlElement(name = "nodeRef")
    public String getNodeRef() {
        return nodeRef;
    }

    public void setNodeRef(String nodeRef) {
        this.nodeRef = nodeRef;
    }

    @XmlElementWrapper(name = "metadatos")
    public Map<String, String> getPropiedades() {
        return propiedades;
    }

    public void setPropiedades(Map<String, String> propiedades) {
        this.propiedades = propiedades;
    }

    @Override
    public String toString() {
        return "InformacionDocumento{" + "nodeRef=" + nodeRef + ", propiedades=" + propiedades + '}';
    }

}
