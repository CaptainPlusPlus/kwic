package file;

import com.sun.xml.internal.txw2.output.IndentingXMLStreamWriter;
import kwic.POSResult;
import kwic.SearchType;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.List;

public class Saver {
    private String url;
    private String needle;
    private SearchType type;
    private List<POSResult> results;

    public Saver(String url, String needle, SearchType type, List<POSResult> results) {
        this.url = url;
        this.type = type;
        this.needle = needle;
        this.results = results;
    }

    public void save() {
        try {
            FileWriter fw = new FileWriter(new File(needle + "Results" + type.toString().toUpperCase() + "Output.xml"));
            generateXML(fw);
            fw.close();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void generateXML(Writer w) {
        XMLOutputFactory output = XMLOutputFactory.newInstance();
        XMLStreamWriter writer = null;
        try {
            // Create an XML writer with proper indentation
            writer = new IndentingXMLStreamWriter(output.createXMLStreamWriter(w));
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }
        buildXMLTree(writer);
    }

    //<searchresult>
    //  <from url="aUrl"/>
    //  <searchTerm value="needle" type="pos|lemma|word"/>
    //      <results>
    //          <result>
    //              <t word="word" lemma="itsLemma" pos="itsPos"/>
    //              . .
    //          </result>
    //          <result> . . </result>
    //           . .
    //      </results>
    //</searchresult>
    private void buildXMLTree(XMLStreamWriter writer) {

        try {
            // <searchresult>
            writer.writeStartElement("searchresult");

            //  <from url="aUrl"/>
            writer.writeEmptyElement("from");
            writer.writeAttribute("url", url);

            //  <searchTerm value="needle" type="pos|lemma|word"/>
            writer.writeEmptyElement("searchTerm");
            writer.writeAttribute("value", needle);
            writer.writeAttribute("type", type.toString());

            // <results>
            writer.writeStartElement("results");
            for (POSResult searchResult : results) {

                // <result>
                writer.writeStartElement("result");

                // <t word="word" lemma="itsLemma" pos="itsPos"/>
                writer.writeEmptyElement("t");
                writer.writeAttribute("word", searchResult.getWord());
                writer.writeAttribute("lemma", searchResult.getLemma());
                writer.writeAttribute("pos", searchResult.getPos());

                writer.writeCharacters("\n");

                //3 xml indenter delimiters
                writer.writeCharacters("      ");
                writer.writeCharacters(String.join(" ", searchResult.getSentence()));
                writer.writeCharacters("\n");

                //2 xml indenter delimiters
                writer.writeCharacters("    ");
                writer.writeEndElement();
                // </result>
            }
            //newline for final root closing tag
            writer.writeCharacters("\n");
            //1 xml indenter delimiters
            writer.writeCharacters("  ");
            writer.writeEndElement();
            // </results>


            writer.writeCharacters("\n");
            writer.writeEndDocument();
            // </searchresult>

            writer.flush();

        } catch (XMLStreamException e) {
            e.printStackTrace();
        }
    }
}
