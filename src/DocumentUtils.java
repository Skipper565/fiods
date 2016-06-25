import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Petr Zeman on 13.05.2016.
 */
public class DocumentUtils {

    private static final String TAG_TABLE_TABLE = "table:table";
    private static final String TAG_TABLE_NAME = "table:name";
    private static final String TAG_TABLE_TABLE_CELL = "table:table-cell";
    private static final String TAG_TEXT_P = "text:p";
    private static final String TAG_TABLE_ROW = "table:table-row";
    private static final String TEMP_DIR = "java.io.tmpdir";

    public Document getDocument(String ods) throws SAXException, ParserConfigurationException,
            IOException {
        UnZip unzip = new UnZip();
        unzip.unZipIt(ods, System.getProperty(TEMP_DIR));
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        return builder.parse(System.getProperty(TEMP_DIR) + "/content.xml");
    }

    public NodeList getTables(Document doc) {
        NodeList idList = doc.getElementsByTagName(TAG_TABLE_TABLE);

        return idList;
    }

    public Element getTable(NodeList list, String tableName) {
        for(int i = 0; i < list.getLength(); ++i) {
            Element idElement = (Element) list.item(i);
            if (idElement.getAttribute(TAG_TABLE_NAME).equals(tableName)) {
                return idElement;
            }
        }

        return null;
    }

    public List<Element> getRows(Element table, String value) {
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
}
