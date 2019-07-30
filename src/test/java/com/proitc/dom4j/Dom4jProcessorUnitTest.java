package com.proitc.dom4j;

import org.dom4j.DocumentException;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.xmlunit.assertj.XmlAssert.assertThat;

/**
 * Unit test for simple {@link Dom4jTransformer}.
 */
public class Dom4jProcessorUnitTest {

    @Test
    public void givenXmlWithAttributes_whenModifyAttribute_thenGetXmlUpdated() throws TransformerFactoryConfigurationError, TransformerException, DocumentException, SAXException {
        String path = getClass()
          .getResource("/xml/attribute.xml")
          .toString();
        Dom4jTransformer transformer = new Dom4jTransformer(path, true);
        String attribute = "customer";
        String oldValue = "true";
        String newValue = "false";

        String result = transformer.modifyAttribute(attribute, oldValue, newValue);

        assertThat(result).hasXPath("//*[contains(@customer, 'false')]");
    }

    @Test
    public void givenTwoXml_whenModifyAttribute_thenGetSimilarXml() throws IOException, TransformerFactoryConfigurationError, TransformerException, URISyntaxException, DocumentException, SAXException {
        String path = getClass()
          .getResource("/xml/attribute.xml")
          .toString();
        Dom4jTransformer transformer = new Dom4jTransformer(path, true);
        String attribute = "customer";
        String oldValue = "true";
        String newValue = "false";
        String expectedXml = new String(Files.readAllBytes((Paths.get(getClass()
          .getResource("/xml/attribute_expected.xml")
          .toURI()))));

        String result = transformer
          .modifyAttribute(attribute, oldValue, newValue)
          .replaceAll("(?m)^[ \t]*\r?\n", "");
        //This replace is need it only in Java 8+, see https://bugs.openjdk.java.net/browse/JDK-8215543

        assertThat(result)
          .and(expectedXml)
          .areSimilar();
    }

    @Test
    public void givenXmlXee_whenInit_thenThrowException() throws IOException, SAXException, ParserConfigurationException, XPathExpressionException, TransformerFactoryConfigurationError, TransformerException {
        String path = getClass()
          .getResource("/xml/xee_attribute.xml")
          .toString();

        assertThatThrownBy(() -> {

            new Dom4jTransformer(path, true);

        }).isInstanceOf(DocumentException.class);
    }

    @Test
    public void givenXmlXee_whenAsHtml_thenGetHtmlForm() throws SAXException, DocumentException, IOException, URISyntaxException {
        URI xhtmlPath = getClass()
          .getResource("/xml/basic.xhtml")
          .toURI();
        URI htmlPath = getClass()
          .getResource("/html/basic.html")
          .toURI();
        String html = new String(Files.readAllBytes(Paths.get(htmlPath)), StandardCharsets.UTF_8);
        Dom4jTransformer transformer = new Dom4jTransformer(xhtmlPath.getPath(), false);

        String result = transformer.asHTML();

        assertThat(result).isEqualTo(html);
    }

}
