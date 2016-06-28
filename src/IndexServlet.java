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

/**
 * Created by Petr Zeman on 13.05.2016.
 */
@WebServlet("/index/*")
@MultipartConfig
public class IndexServlet extends HttpServlet {

    private NodeList nodeList = null;
    private String tableName = null;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getPathInfo();
        req.setCharacterEncoding("utf-8");
        resp.setContentType("text/html;charset=UTF-8");

        try {
            if (action.equals("/step1")) {
                Part filePart = req.getPart("file");
                FileUtils.checkODSFileSize(filePart);
                req.setAttribute("fileName", DocumentUtils.getFileName(filePart));

                // create temp files
                File tmpODSFile = File.createTempFile("tmp", ".ods", null);
                File tmpXMLFile = File.createTempFile("tmp", ".xml", null);
                FileUtils.partToFile(filePart, tmpODSFile);
                nodeList = DocumentUtils.getTables(DocumentUtils.getDocument(tmpODSFile, tmpXMLFile));

                req.setAttribute("tables", DocumentUtils.nodeListToStringList(nodeList));
                req.setAttribute("step", "step2");
                initializePage(req, resp);
            } else if (action.equals("/step2")) {
                tableName = req.getParameter("table");
                // Search for all rows. Need for VoiceXML.
                req.setAttribute("albs", DocumentUtils.searchForValue(nodeList, tableName, ""));
                req.setAttribute("step", "step3");
                initializePage(req, resp);
            } else if (action.equals("/step3")) {
                String searchValue = req.getParameter("value");
                req.setAttribute("rows", DocumentUtils.searchForValue(nodeList, tableName, searchValue));
                req.setAttribute("step", "step4");
                initializePage(req, resp);
            } else if (action.equals("/step4")) {
                req.setAttribute("step", "step1");
                initializePage(req, resp);
            }
        } catch (Exception e) {
            req.setAttribute("error", e.getMessage());
            initializePage(req, resp);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        initializePage(request, response);
    }

    private void initializePage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }


}
