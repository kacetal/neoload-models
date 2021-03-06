package com.neotys.neoload.model.v3.writers.neoload;

import com.neotys.neoload.model.v3.project.Element;
import org.w3c.dom.Document;

import java.util.Optional;

public class ElementWriter {

    public static final String XML_NAME_ATTR = "name";
    public static final String XML_DESCRIPTION_TAG = "description";
    public static final String XML_UID_TAG = "uid";
    
    protected Element element;
    
    public ElementWriter(Element element) {
        this.element = element;
    }

    public void writeDescription(final Document document, final org.w3c.dom.Element currentElement) {
    	writeDescription(document, currentElement, this.element.getDescription());
    }
    
    public static void writeDescription(final Document document, final org.w3c.dom.Element currentElement, final Optional<String> description) {
    	description.ifPresent(s -> {
            org.w3c.dom.Element descElement = document.createElement(ElementWriter.XML_DESCRIPTION_TAG);
            descElement.setTextContent(s);
            currentElement.appendChild(descElement);
        });
    }

   /**
    * 
    * @param document
    * @param currentElement
    * @param outputFolder NeoLoad output folder may be use for further computation in overriding classes.
    */
    public void writeXML(final Document document, final org.w3c.dom.Element currentElement, final String outputFolder) {
        currentElement.setAttribute(XML_NAME_ATTR, element.getName());
        currentElement.setAttribute(XML_UID_TAG, WriterUtils.getElementUid(element));
        writeDescription(document, currentElement);
    }

}
