
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Properties;
import javax.naming.Context;
import javax.naming.InitialContext;
import java.net.UnknownHostException;
import org.jvoicexml.ConnectionInformation;
import org.jvoicexml.JVoiceXml;
import org.jvoicexml.Session;
import org.jvoicexml.client.text.TextListener;
import org.jvoicexml.client.text.TextServer;
import org.jvoicexml.event.ErrorEvent;
import org.jvoicexml.xml.ssml.SsmlDocument;

/**
 * Created by Petr Zeman on 20.06.2016.
 */
public class JVoiceXMLClient extends Thread implements TextListener {

    private TextServer txtserver;
    private Session session;
    private boolean connected;

    public JVoiceXMLClient() {
        JVoiceXml jvxml = null;
        Context context = null;
        Properties props = new Properties();

        try {
            props.load(new FileInputStream("jndi.properties"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        try {
            context = new InitialContext(props);
            jvxml = (JVoiceXml) context.lookup("JVoiceXml");
        } catch (Exception ne) {
            System.out.println("JVoiceXML interprer was not found.");
            ne.printStackTrace();
        }

        txtserver = new TextServer(4242);
        txtserver.start();
        txtserver.addTextListener(this);

        try {
            ConnectionInformation client = txtserver.getConnectionInformation();
            session = jvxml.createSession(client);
            //session.call(uri);
        } catch (UnknownHostException ex) {
            System.out.println("Error while establishing client connection.");
            ex.printStackTrace();
        } catch (ErrorEvent ex) {
            System.out.println("Error while establishing session.");
            ex.printStackTrace();
        }

        try {
            txtserver.waitConnected();
        } catch (IOException ex) {
            System.out.println("Error while waiting for textserver.");
            ex.printStackTrace();
        }

        System.out.println("Session created, interpretation started.");
    }

    @Override
    public void started() {
        System.out.println("JVoiceXMLClient started on port 4242.");
    }

    @Override
    public void connected(InetSocketAddress inetSocketAddress) {
        connected = true;
        System.out.println("Text client connected to JVoiceXML interpreter.");
    }

    @Override
    public void outputSsml(SsmlDocument document) {
        //outputText(document.getSpeak().getTextContent());
    }

    @Override
    public void expectingInput() {
        System.out.println("Expecting input.");
    }

    @Override
    public void inputClosed() {
        System.out.println("Input closed.");
    }

    @Override
    public void disconnected() {
        if (connected) {
            connected = false;
            txtserver.stopServer();
            String text = "Interpretation ended, you are disconnected from the interpreter.";

            try {
                this.sleep(2000);
            } catch (InterruptedException ex) {
                System.out.println(ex);
            }

            //outputText(text);
            System.out.println("Session ended, interpretation ended.");
            System.out.println("Text client disconnected from JVoiceXML interpreter, interpretation ended.");

            //dialogManagerServlet.setWritingReady(true);
        }
    }
}
