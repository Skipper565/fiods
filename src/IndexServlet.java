import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Petr Zeman on 13.05.2016.
 */
//@WebServlet(name = "IndexServlet", urlPatterns = {"/index"})
@WebServlet("/index/*")
@MultipartConfig
public class IndexServlet extends HttpServlet {

    private static final String ODS = "opendocument.spreadsheet";
    private NodeList nodeList = null;
    private String tableName = null;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getPathInfo();
        req.setCharacterEncoding("utf-8");
        resp.setContentType("text/html;charset=UTF-8");

        switch (action) {
            case "/step1":
                Part filePart = req.getPart("file");

// check file size
                if((filePart.getSize() == 0) || !(filePart.getContentType().contains(ODS))) {
                    req.setAttribute("error", "You have to choose ODS file!");
                    initializePage(req, resp);
                    return;
                }

                //System.out.println(System.getProperty("java.io.tmpdir"));

                // create temp file
                File directory = new File(getServletContext().getRealPath("/"));
                File file = File.createTempFile("tmp", ".ods", null);
                file.setReadable(true, false);
                file.setExecutable(true, false);
                file.setWritable(true, false);
                req.setAttribute("path", file.getAbsolutePath());

                filePart.write(file.getAbsolutePath());

                List<String> tables = new ArrayList<>();
                try {
                    //NodeList
                    //String path = "/home/sarhan/Documents/School/fiods/src/Albums.ods";
                    nodeList = getDocumentUtils().getTables(getDocumentUtils().getDocument(file));

                    for(int i = 0; i < nodeList.getLength(); ++i) {
                        Element e = (Element) nodeList.item(i);
                        tables.add(e.getAttribute("table:name"));
                    }
                    req.setAttribute("tables", tables);
                    req.setAttribute("step", "step2");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                initializePage(req, resp);
                break;
            case "/step2":
                tableName = req.getParameter("table");
                req.setAttribute("step", "step3");
                /*huhu*/
                Element sheet1 = getDocumentUtils().getTable(nodeList, tableName);
                List<Element> rows1 = getDocumentUtils().getRows(sheet1, "");
                List<List<String>> rowStrings1 = getDocumentUtils().NodesToStrings(rows1);
                req.setAttribute("albs", rowStrings1);
                /*huhu*/
                initializePage(req, resp);
                break;
            case "/step3":
                String searchValue = req.getParameter("value");
                Element sheet = getDocumentUtils().getTable(nodeList, tableName);
                List<Element> rows = getDocumentUtils().getRows(sheet, searchValue);
                List<List<String>> rowStrings = getDocumentUtils().NodesToStrings(rows);
                req.setAttribute("rows", rowStrings);
                req.setAttribute("step", "step4");
                initializePage(req, resp);
                break;
            case "/step4":
                req.setAttribute("step", "step1");
                initializePage(req, resp);
                break;

        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        /*response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");

        try (PrintWriter writer = response.getWriter()) {

            writer.println("<!DOCTYPE html><html>");
            writer.println("<head>");
            writer.println("<meta charset=\"UTF-8\" />");
            writer.println("<title>MyServlet.java:doGet(): Servlet code!</title>");
            writer.println("</head>");
            writer.println("<body>");

            writer.println("<h1>This is a simple java servlet.</h1>");

            writer.println("</body>");
            writer.println("</html>");
        }

        if (voiceXMLClient != null) {
            voiceXMLClient.disconnected();
            System.out.println("Previous text client disconneted.");
        }

        voiceXMLClient = new JVoiceXMLClient();
        */
        String fiods = "fiods";
        request.setAttribute("title", "Find in ODS {fiods}");
        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }

    private DocumentUtils getDocumentUtils() {
        return (DocumentUtils) getServletContext().getAttribute("documentUtils");
    }

    private UnZip getUnzip() {
        return (UnZip) getServletContext().getAttribute("unzip");
    }

    private void initializePage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }
    


}
