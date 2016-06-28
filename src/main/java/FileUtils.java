import javax.servlet.http.Part;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by Stanislav Cerny on 18.06.2016.
 */
public class FileUtils {

    private static final String ODS = "opendocument.spreadsheet";
    private static final String CONTENT_FILE_NAME = "content.xml";

    /**
     * Extracts content.xml file from .ods file to the given target
     * @param odsFile input ods file
     * @param xmlFile output xml file
     */
    public static void odsToXML(File odsFile, File xmlFile) throws IOException {
        byte[] buffer = new byte[1024];

        //get the zip file content
        ZipInputStream zis = new ZipInputStream(new FileInputStream(odsFile));
        //get the zipped file list entry
        ZipEntry zipEntry = zis.getNextEntry();

        while(zipEntry != null) {
            String fileName = zipEntry.getName();
            if(fileName.equals(CONTENT_FILE_NAME)) {
                FileOutputStream fos = new FileOutputStream(xmlFile);
                int len;
                while ((len = zis.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }
                fos.close();
                break;
            } else {
                zipEntry = zis.getNextEntry();
            }
        }
        zis.closeEntry();
        zis.close();
        odsFile.delete();
    }

    public static void partToFile(Part part, File file) throws IOException {
        file.setReadable(true, false);
        file.setExecutable(true, false);
        file.setWritable(true, false);
        part.write(file.getAbsolutePath());
    }

    public static void checkODSFileSize(Part part) {
        if(part.getSize() == 0) {
            throw new IllegalArgumentException("No file selected");
        }
        if(!part.getContentType().contains(ODS)) {
            throw new IllegalArgumentException("Incorrect file type");
        }
    }
}