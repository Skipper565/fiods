import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.servlet.http.Part;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
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
    private static final String CONTENT_DISPOSITION_HEADER = "content-disposition";
    private static final String SPLIT_SEPARATOR = ";";
    private static final String FILENAME = "filename";

    public static Document getDocument(File odsFile, File xmlFile) throws SAXException, ParserConfigurationException,
            IOException {
        FileUtils.odsToXML(odsFile, xmlFile);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        return builder.parse(xmlFile);
    }

    public static NodeList getTables(Document doc) {
        NodeList idList = doc.getElementsByTagName(TAG_TABLE_TABLE);
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
        List<Element> rows = new ArrayList<Element>();
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
                    if(e.getTextContent().toLowerCase().contains(value.toLowerCase())) {
                        rows.add(idElement);
                        break;
                    }
                }
            }
        }
        return rows;
    }

    public static List<List<String>> elementsToStringLists(List<Element> rows){
        List<List<String>> strings = new ArrayList<List<String>>();
        for (Element row: rows) {
            NodeList children = row.getChildNodes();
            List<String> rowStrings = new ArrayList<String>();
            for(int i = 0; i<children.getLength(); i++)
            {
                rowStrings.add(i, children.item(i).getTextContent());
            }
            strings.add(rowStrings);
        }
        return strings;
    }

    public static String getFileName(final Part part) {
        for (String content : part.getHeader(CONTENT_DISPOSITION_HEADER).split(SPLIT_SEPARATOR)) {
            if (content.trim().startsWith(FILENAME)) {
                return content.substring(
                        content.indexOf('=') + 1).trim().replace("\"", "");
            }
        }
        return null;
    }

    public static List<String> nodeListToStringList(NodeList nodeList) {
        List<String> tables = new ArrayList<String>();
        for(int i = 0; i < nodeList.getLength(); i++) {
            Element e = (Element) nodeList.item(i);
            tables.add(e.getAttribute(TAG_TABLE_NAME));
        }
        return tables;
    }

    public static List<List<String>> searchForValue(NodeList nodeList, String tableName, String searchValue) {
        Element sheet = DocumentUtils.getTable(nodeList, tableName);
        List<Element>rows = DocumentUtils.getRows(sheet, searchValue);
        return DocumentUtils.elementsToStringLists(rows);
    }
}
