package XMLreporter;

import java.lang.reflect.Method;

public class Saver {
    private static final String INDENT = "   "; //easier to append the StringBuilder with indents

    public String save(Object o) throws Exception {
        return save(o, 0);
    }

    private String save(Object o, int depth) throws Exception {
        if (o == null) { //prevent errors, if wrongly read object
            return "";
        }

        StringBuilder xml = new StringBuilder();
        Element rootElement = o.getClass().getAnnotation(Element.class);

        if (rootElement != null) {
            xml.append(indent(depth)).append('<').append(rootElement.name());
            appendElementFields(xml, o);

            
            boolean hasSubNodes = hasSubNodes(o); //hasSubNodes was a solution to keeping track of which children had additional nodes
            
            if (hasSubNodes) {
                xml.append(">\n");
                appendSubElements(xml, o, depth + 1);
                xml.append(indent(depth)).append("</").append(rootElement.name()).append(">\n");
            } else {
                xml.append("/>\n"); //closes node if it doesn't have subnodes
            }
        }

        return xml.toString();
    }
    
    private boolean hasSubNodes(Object o) throws Exception { 
        for (Method method : o.getClass().getMethods()) {
            SubElements subElements = method.getAnnotation(SubElements.class); //checks if there are subelements
            if (subElements != null) {
                Object[] children = (Object[]) method.invoke(o);
                if (children != null && children.length > 0) {
                    return true; //there are subnodes.
                }
            }
        }
        return false; //there are no subnodes.
    }



    private void appendElementFields(StringBuilder xml, Object o) throws Exception { //kept the same from last submission
        for (Method method : o.getClass().getMethods()) {
            ElementField field = method.getAnnotation(ElementField.class);
            if (field != null) {
                Object fieldValue = method.invoke(o);
                xml.append(' ').append(field.name()).append("=\"").append(fieldValue).append('"');
            }
        }
    }

    private void appendSubElements(StringBuilder xml, Object o, int depth) throws Exception { 
        for (Method method : o.getClass().getMethods()) {
            SubElements subElements = method.getAnnotation(SubElements.class);
            if (subElements != null) {
                Object[] children = (Object[]) method.invoke(o);
                if (children != null && children.length > 0) {
                    xml.append(indent(depth)).append("<").append(subElements.name()).append(">\n"); 
                    for (Object child : children) {					//the children acts as a node if it contains "grandchildren"
                        xml.append(save(child, depth + 1));			//the hasSubNodes prevents the save() from recursion
                    }
                    xml.append(indent(depth)).append("</").append(subElements.name()).append(">\n");
                }
            }
        }
    }


    private String indent(int depth) {
        return INDENT.repeat(depth);
    }
}
