/*
 *  
 *  
 */
package es.ingapl.demo.platformsample.predicates;

import org.apache.commons.collections.Predicate;
import org.springframework.extensions.webscripts.servlet.FormData.FormField;

/**
 * Predicate utilizado para filtrar FormFields en base a su id
 *
 * @author ingapl
 */
public class PredicateFormField implements Predicate {

    private final String id;

    public PredicateFormField(String id) {
        this.id = id;
    }

    @Override
    public boolean evaluate(Object o) {
        if (o instanceof FormField) {
            FormField f = (FormField) o;
            if (f.getName().equalsIgnoreCase(id)) {
                return true;
            }
        }
        return false;
    }
}
