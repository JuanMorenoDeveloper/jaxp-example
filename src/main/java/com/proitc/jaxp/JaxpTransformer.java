package com.proitc.jaxp;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

public class JaxpTransformer {
    private final Document input;

    public JaxpTransformer(String resourcePath) throws SAXException, IOException, ParserConfigurationException {
        // 1- Build the doc from the XML file
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
        input = factory
          .newDocumentBuilder()
          .parse(resourcePath);
    }

    public String modifyAttribute(String attribute, String oldValue, String newValue) throws XPathExpressionException, TransformerFactoryConfigurationError, TransformerException {
        // 2- Locate the node(s) with xpath
        XPath xpath = XPathFactory
          .newInstance()
          .newXPath();
        String expr = String.format("//*[contains(@%s, '%s')]", attribute, oldValue);
        NodeList nodes = (NodeList) xpath.evaluate(expr, input, XPathConstants.NODESET);
        // 3- Make the change on the selected nodes
        for (int i = 0; i < nodes.getLength(); i++) {
            Element value = (Element) nodes.item(i);
            value.setAttribute(attribute, newValue);
        }
        //Stream api syntax
        //        IntStream
        //          .range(0, nodes.getLength())
        //          .mapToObj(i -> (Element) nodes.item(i))
        //          .forEach(value -> value.setAttribute(attribute, newValue));
        // 4- Save the result to a new XML doc
        TransformerFactory factory = TransformerFactory.newInstance();
        factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
        Transformer xformer = factory.newTransformer();
        xformer.setOutputProperty(OutputKeys.INDENT, "yes");
        Writer output = new StringWriter();
        xformer.transform(new DOMSource(input), new StreamResult(output));
        return output.toString().replaceAll("[\r\n]+", "\n");
        //These replace is need it only in Java 8+, see https://bugs.openjdk.java.net/browse/JDK-8215543
    }
}
