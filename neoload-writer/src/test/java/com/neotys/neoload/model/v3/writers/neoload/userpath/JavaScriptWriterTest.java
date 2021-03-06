package com.neotys.neoload.model.v3.writers.neoload.userpath;

import com.google.common.io.Files;
import com.neotys.neoload.model.v3.project.userpath.JavaScript;
import com.neotys.neoload.model.v3.writers.neoload.WriterUtils;
import com.neotys.neoload.model.v3.writers.neoload.WrittingTestUtils;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

public class JavaScriptWriterTest {

    @Test
    public void writeJavaScriptTest() throws ParserConfigurationException, TransformerException, IOException {
        final Document doc = WrittingTestUtils.generateEmptyDocument();
        final Element root = WrittingTestUtils.generateTestRootElement(doc);
        final JavaScript javascript = new JavaScript.Builder().name("myJS").script("// Get variable value from VariableManager\n" +
                "var myVar = context.variableManager.getValue(\"myVar\");\n" +
                "\n" +
                "\n" +
                "// Do some computation using the methods\n" +
                "// you defined in the JS Library\n" +
                "var computedValue = myLibraryFunction(myVar);\n" +
                "logger.debug(\"ComputedValue=\"+computedValue);\n" +
                "\n" +
                "// Inject the computed value in a runtime variable\n" +
                "context.variableManager.setValue(\"computedVar\",computedValue);").description("myDescription").build();

        final String expectedStart = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>"
                + "<test-root><js-action filename=\"scripts/jsAction_"+WriterUtils.getElementUid(javascript)+".js\" name=\"myJS\" ts=\"";
        final String expectedEnd = "\" uid=\"" + WriterUtils.getElementUid(javascript)+ "\"><description>myDescription</description></js-action>"
                + "</test-root>";
        String tmpDirPath = Files.createTempDir().getAbsolutePath();
        JavaScriptWriter.of(javascript).writeXML(doc, root, tmpDirPath);
        final String generatedResult = WrittingTestUtils.getXmlString(doc);
        Assertions.assertThat(generatedResult).startsWith(expectedStart);
        // There is a timestamp in the middle
        Assertions.assertThat(generatedResult).endsWith(expectedEnd);
        File scriptFile = new File(tmpDirPath+"/scripts/jsAction_"+WriterUtils.getElementUid(javascript)+".js");
        Assertions.assertThat(scriptFile.exists()).isTrue();
        List<String> lines = Files.readLines(scriptFile, Charset.defaultCharset());
        Assertions.assertThat(lines.size()).isEqualTo(11);
        Assertions.assertThat(lines.get(6)).isEqualTo("var computedValue = myLibraryFunction(myVar);");
    }
}
