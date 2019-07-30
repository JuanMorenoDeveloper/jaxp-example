package com.proitc.transformation;

import org.junit.jupiter.api.Test;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.xmlunit.assertj.XmlAssert.assertThat;

public class TransformationUnitTest {

    @Test
    public void givenXML_whenTransform_theGetSameXML() throws TransformerException, IOException, URISyntaxException {
        URI path = getClass()
          .getResource("/xml/simplecdata.xml")
          .toURI();
        String input = new String(Files.readAllBytes(Paths.get(path)));

        StreamSource source = new StreamSource(new StringReader(input));
        Transformer transformer = TransformerFactory
          .newInstance()
          .newTransformer();

        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        StreamResult result = new StreamResult(new StringWriter());
        transformer.transform(source, result);
        String output = result
          .getWriter()
          .toString()
          .replaceAll("(?m)^[ \t]*\r?\n", "")
          .replaceAll(">\\s*(<\\!\\[CDATA\\[.*?]]>)\\s*</", ">$1</");

        assertThat(input)
          .and(output)
          .areSimilar();
    }
}
