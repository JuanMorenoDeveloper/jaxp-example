package com.proitc.jaxp;

import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.xml.sax.SAXParseException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.xmlunit.assertj.XmlAssert.assertThat;

/**
 * Unit test for simple {@link JaxpTransformer}.
 */
public class JaxpProcessorUnitTest {

    @Test
    public void givenXmlWithAttributes_whenModifyAttribute_thenGetXmlUpdated() throws IOException, SAXException, ParserConfigurationException, XPathExpressionException, TransformerFactoryConfigurationError, TransformerException {
        String path = getClass()
          .getResource("/xml/attribute.xml")
          .toString();
        JaxpTransformer transformer = new JaxpTransformer(path);
        String attribute = "customer";
        String oldValue = "true";
        String newValue = "false";

        String result = transformer.modifyAttribute(attribute, oldValue, newValue);

        assertThat(result).hasXPath("//*[contains(@customer, 'false')]");
    }

    @Test
    public void givenTwoXml_whenModifyAttribute_thenGetSimilarXml() throws IOException, SAXException, ParserConfigurationException, XPathExpressionException, TransformerFactoryConfigurationError, TransformerException, URISyntaxException {
        String path = getClass()
          .getResource("/xml/attribute.xml")
          .toString();
        JaxpTransformer transformer = new JaxpTransformer(path);
        String attribute = "customer";
        String oldValue = "true";
        String newValue = "false";
        String expectedXml = new String(Files.readAllBytes((Paths.get(getClass()
          .getResource("/xml/attribute_expected.xml")
          .toURI()))));

        String result = transformer.modifyAttribute(attribute, oldValue, newValue).replaceAll("(?m)^[ \t]*\r?\n", "");
        //This replace is need it only in Java 8+, see https://bugs.openjdk.java.net/browse/JDK-8215543

        assertThat(result)
          .and(expectedXml)
          .areSimilar();
    }

    @Test
    public void givenXmlXee_whenInit_thenThrowException() throws IOException, SAXException, ParserConfigurationException, XPathExpressionException, TransformerFactoryConfigurationError, TransformerException {
        String path = getClass().getResource("/xml/xee_attribute.xml")
            .toString();

        assertThatThrownBy(() -> {

            new JaxpTransformer(path);

        }).isInstanceOf(SAXParseException.class);
    }

}
