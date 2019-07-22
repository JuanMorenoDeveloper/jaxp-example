package com.proitc.joox;

import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import javax.xml.transform.TransformerFactoryConfigurationError;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.xmlunit.assertj.XmlAssert.assertThat;

/**
 * Unit test for simple {@link JooxTransformer}.
 */
public class JooxProcessorUnitTest {

    @Test
    public void givenXmlWithAttributes_whenModifyAttribute_thenGetXmlUpdated() throws IOException, SAXException, TransformerFactoryConfigurationError {
        String path = getClass()
          .getResource("/xml/attribute.xml")
          .toString();
        JooxTransformer transformer = new JooxTransformer(path);
        String attribute = "customer";
        String oldValue = "true";
        String newValue = "false";

        String result = transformer.modifyAttribute(attribute, oldValue, newValue);

        assertThat(result).hasXPath("//*[contains(@customer, 'false')]");
    }

    @Test
    public void givenTwoXml_whenModifyAttribute_thenGetSimilarXml() throws IOException, TransformerFactoryConfigurationError, URISyntaxException, SAXException {
        String path = getClass()
          .getResource("/xml/attribute.xml")
          .toString();
        JooxTransformer transformer = new JooxTransformer(path);
        String attribute = "customer";
        String oldValue = "true";
        String newValue = "false";
        String expectedXml = new String(Files.readAllBytes((Paths.get(getClass()
          .getResource("/xml/attribute_expected.xml")
          .toURI())))).replaceAll("(?m)^[ \t]*\r?\n", "");
        //This replace is need it only in Java 8+, see https://bugs.openjdk.java.net/browse/JDK-8215543;

        String result = transformer.modifyAttribute(attribute, oldValue, newValue).replaceAll("(?m)^[ \t]*\r?\n", "");

        assertThat(result)
          .and(expectedXml)
          .areSimilar();
    }

}
