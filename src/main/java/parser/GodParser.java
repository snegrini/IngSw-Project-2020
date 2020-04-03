package parser;

import model.God;
import model.effects.Effect;
import model.enumerations.XMLName;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GodParser {

    public static final String filePath = "xml/gods.xml";

    private GodParser() { }

    /**
     *
     * @return
     */
    public static List<God> parseGods() {
        List<God> gods = new ArrayList<>();

        File file = new File(GodParser.class.getClassLoader().getResource(filePath).getFile());

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = null;
        Document doc = null;

        try {
            db = dbf.newDocumentBuilder();
            dbf.setValidating(false);
            doc = db.parse(file);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            // TODO: throw exception, stop execution. ??
            e.printStackTrace();
        }

        Element root = doc.getDocumentElement();

        // Retrieve all <god> nodes
        NodeList godNodeList = root.getChildNodes();

        for (int i = 0; i < godNodeList.getLength(); i++) {
            Node godNode = godNodeList.item(i);
            if (godNode.getNodeType() == Node.ELEMENT_NODE) {
                gods.add(buildGodObject((Element) godNode));
            }
        }
        return gods;
    }

    private static God buildGodObject(Element godElement) {
        // TODO: use GodBuilder to setup a God object.
        godElement.getAttribute(XMLName.ID.getText());

        God god = null;// = new God();

        return god;
    }

/*    private static Effect buildEffectObject(Node effectNode) {
        // TODO
    }*/
}
