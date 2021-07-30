/**
 * Course:      Data Structures and Algorithms for Computational Linguistics II SS2021
 * Assignment:  (Final KWIC Project)
 * Authors:      (Hoyeon Lee, Nkonye Gbadegoye, Dewi Surya, Jacqueline Becker, Felix Redfox)
 * Description: (Project to extract text from url and find/save needle typed results on it with their POS information)
 *
 * Honor Code:  We pledge that this program represents our own work.
 *  we did not receive help from anyone in designing and debugging this program.
 */

package file;

import com.sun.xml.txw2.output.IndentingXMLStreamWriter;
import kwic.POSResult;
import kwic.SearchType;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.List;


/**
 * Saves text in POS results format into local XML file in given format
 */
public class Saver {
    private String url;
    private String needle;
    private SearchType type;
    private List<POSResult> results;

    /**
     * Initialize saver operation with required data for XML
     * @param url site were body is obtained
     * @param needle searched needle on text
     * @param type type of search executed on text
     * @param results results obtained from search
     */
    public Saver(String url, String needle, SearchType type, List<POSResult> results) {
        this.url = url;
        this.type = type;
        this.needle = needle;
        this.results = results;
    }

    /**
     * Saves results into XML file format
     */
    public void save() {
        // Create writer for XML
        try {
            FileWriter fw = new FileWriter(new File(needle + "Results" + type.toString().toUpperCase() + "Output.xml"));
            //Generate xml with writer
            generateXML(fw);
            fw.close();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    /**
     * Generates xml tree on results
     * @param w xml writer
     */
    private void generateXML(Writer w) {
        //Init output factory
        XMLOutputFactory output = XMLOutputFactory.newInstance();
        XMLStreamWriter writer = null;
        try {
            // Create an XML writer with proper indentation
            writer = new IndentingXMLStreamWriter(output.createXMLStreamWriter(w));
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }
        //Build the actual tree according to format
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
