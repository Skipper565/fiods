import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sarhan on 6/18/16.
 */
public class Main {

    private Document doc;
    private static final String TAG_TABLE_TABLE = "table:table";
    private static final String TAG_TABLE_NAME = "table:name";
    private static final String TAG_TABLE_TABLE_CELL = "table:table-cell";
    private static final String TAG_TEXT_P = "text:p";
    private static final String TAG_TABLE_ROW = "table:table-row";
    /**
     * Vytvori novou instanci teto tridy a nacte obsah xml dokumentu se zadanym
     * URL.
     */
    public static Main newInstance(URI uri) throws SAXException,
            ParserConfigurationException, IOException {
        return new Main(uri);
    }

    /**
     * Create a new instance of this class and read the content of the given XML
     * file.
     */
    public static Main newInstance(File file)
            throws SAXException, ParserConfigurationException, IOException {
        return newInstance(file.toURI());
    }

    /**
     * Constructor creating a new instance of Uloha1 class reading from the file
     * at the given URI
     */
    private Main(URI uri) throws SAXException, ParserConfigurationException,
            IOException {
        // Vytvorime instanci tovarni tridy
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        // Pomoci tovarni tridy ziskame instanci DocumentBuilderu
        DocumentBuilder builder = factory.newDocumentBuilder();
        // DocumentBuilder pouzijeme pro zpracovani XML dokumentu
        // a ziskame model dokumentu ve formatu W3C DOM
        doc = builder.parse(uri.toString());
    }

    // <-------------------------------------------------------------->

    public NodeList getTables(String tableTag) {
        NodeList idList = doc.getElementsByTagName(tableTag);

        return idList;
    }

    public static Element getTable(NodeList list, String tableName) {
        for(int i = 0; i < list.getLength(); ++i) {
            Element idElement = (Element) list.item(i);
            if (idElement.getAttribute(TAG_TABLE_NAME).equals(tableName)) {
                return idElement;
            }
        }

        return null;
    }

    public static List<Element> getRows(Element table, String value) {
        List<Element> rows = new ArrayList<>();
        NodeList list = table.getElementsByTagName(TAG_TABLE_ROW);

        for(int i = 0; i < list.getLength(); ++i) {
            Element idElement = (Element) list.item(i);
            NodeList cells = idElement.getElementsByTagName(TAG_TABLE_TABLE_CELL);

            for(int j = 0; j < cells.getLength(); ++j) {
                Element cell = (Element) cells.item(j);
                NodeList values = cell.getElementsByTagName(TAG_TEXT_P);
                Node e;
                if(values.getLength() != 0) {
                    e = values.item(0);
                    if(value.equals(e.getTextContent())) {
                        rows.add(idElement);
                    }
                }
            }
        }
        return rows;
    }

    public static void printList(List<Element> list) {
        if(list == null) {
            throw new IllegalArgumentException("List is null");
        }

        list.forEach(Main::printElement);
    }

    public static void printElement(Element element) {
        NodeList cells = element.getElementsByTagName(TAG_TABLE_TABLE_CELL);

        for(int j = 0; j < cells.getLength(); ++j) {
            Element cell = (Element) cells.item(j);
            NodeList values = cell.getElementsByTagName(TAG_TEXT_P);
            Node e;
            if(values.getLength() != 0) {
                e = values.item(0);
                System.out.print(e.getTextContent() + ";");
            }
        }
        System.out.println();
    }

    public static void main(String[] args) throws IOException,
            SAXException, ParserConfigurationException, TransformerException {

        if (args.length < 1) {
            System.err.println("Input file name is missing.");
            return;
        }
        UnZip unZip = new UnZip();
        unZip.unZipIt(args[0], "output");

        File input = new File("output/content.xml");

        Main task1 = newInstance(input);

        NodeList list = task1.getTables(TAG_TABLE_TABLE);

        Element element = getTable(list, "USA");

        if (element == null) {
            System.out.println("Element is null");
        } else {
            System.out.println(element.getAttribute(TAG_TABLE_NAME));
            String value = "Asap Rocky";
            List<Element> found = getRows(element, value);
            System.out.println("Number of rows with given entry(for Asap Rocky should by 3): " + found.size());
            printList(found);
        }
    }
}