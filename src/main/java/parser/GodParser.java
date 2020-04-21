package parser;

import model.God;
import model.effects.*;
import model.enumerations.EffectType;
import model.enumerations.XMLName;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;


/**
 * Provides the methods for processing the XML document with the gods configuration.
 */
public class GodParser {

    public static final String filePath = "xml/gods.xml";

    private GodParser() { }

    /**
     * Parses the XML file into a list of {@link God} objects.
     *
     * @return the list of gods parsed from the XML file.
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
            // TODO throw exception, stop execution. ??
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

    /**
     * Builds and returns a god object by parsing the given god Element.
     * If the given Element has any effect, the method {@link #buildEffectObject(Element)} is called.
     *
     * @param godElement the Element of the XML god node.
     * @return the built God object.
     */
    private static God buildGodObject(Element godElement) {
        String name = godElement.getElementsByTagName(XMLName.NAME.getText()).item(0).getTextContent();
        String caption = godElement.getElementsByTagName(XMLName.CAPTION.getText()).item(0).getTextContent();
        String description = godElement.getElementsByTagName(XMLName.DESCRIPTION.getText())
                .item(0).getTextContent();

        List<Effect> effects = new ArrayList<>();

        // Retrieve <effect> nodes
        Node effectsNode = godElement.getElementsByTagName(XMLName.EFFECTS.getText()).item(0);
        NodeList effectNodeList = effectsNode.getChildNodes();

        // Build effect list
        for (int i = 0; i < effectNodeList.getLength(); i++) {
            Node effectNode = effectNodeList.item(i);
            if (effectNode.getNodeType() == Node.ELEMENT_NODE) {
                effects.add(buildEffectObject((Element) effectNode));
            }
        }

        return new God.Builder(name)
                .withCaption(caption)
                .withDescription(description)
                .withEffects(effects)
                .build();
    }

    /**
     * Builds and returns a effect object by parsing the given effect Element.
     *
     * @param effectElement the Element of the XML god node.
     * @return the built Effect object.
     */
    private static Effect buildEffectObject(Element effectElement) {
        String effectType = effectElement.getAttribute(XMLName.TYPE.getText());
        Effect effect = new SimpleEffect(EffectType.valueOf(effectType));

        NodeList reqNodeList = effectElement.getElementsByTagName(XMLName.REQUIREMENTS.getText());
        NodeList parNodeList = effectElement.getElementsByTagName(XMLName.PARAMETERS.getText());

        Map<String, String> requirements = Map.of();
        Map<String, String> parameters = Map.of();

        // Retrieve only the child nodes of the first element found
        if (reqNodeList.getLength() > 0) {
            reqNodeList = reqNodeList.item(0).getChildNodes();
            requirements = toMap(reqNodeList);
        }
        if (parNodeList.getLength() > 0) {
            parNodeList = parNodeList.item(0).getChildNodes();
            parameters = toMap(parNodeList);
        }

        // FIXME
        switch (effect.getEffectType()) {
            case YOUR_MOVE:
                effect = decorateMove(effect, requirements, parameters);
                break;
            case YOUR_BUILD:
                effect = decorateBuild(effect, requirements, parameters);
                break;
        }
        return effect;
    }

    private static Effect decorateBuild(Effect effect, Map<String, String> requirements,
                                        Map<String, String> parameters) {

        if (parameters.containsKey(XMLName.BUILD.getText())) {
            effect = new BuildDecorator(effect, requirements, parameters);
        }

        return effect;
    }

    private static Effect decorateMove(Effect effect, Map<String, String> requirements,
                                       Map<String, String> parameters) {
        if (parameters.containsKey(XMLName.MOVE.getText() + XMLName.OVER.getText())) {
            effect = new MoveOverDecorator(effect, requirements, parameters);
        }

        if (parameters.containsKey(XMLName.MOVE.getText() + XMLName.AGAIN.getText())) {
            effect = new MoveAgainDecorator(effect, requirements, parameters);
        }

        return effect;
    }

    private static Effect decorateWin(Effect effect, Map<String, String> requirements,
                                      Map<String, String> parameters) {
        effect = new WinDownDecorator(effect, requirements, parameters);

        return effect;
    }

    /**
     * Returns an immutable map containing all of the elements and attributes in the given nodeList.
     * The root Element of the NodeList will be ignored.
     * Only elements within a depth level of 1 will be considered.
     * Other nested elements will be ignored.
     *
     * @param nodeList the NodeList to transform into a map.
     * @return Returns an immutable map containing all of the elements and attributes in the given nodeList.
     *         Returns an immutable empty map if the nodeList is empty.
     */
    private static Map<String, String> toMap(NodeList nodeList) {
        Map<String, String> map = new HashMap<>();

        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                map.put(node.getNodeName(), node.getTextContent());

                if (node.hasAttributes()) {
                    NamedNodeMap attributes = node.getAttributes();

                    for (int j = 0; j < attributes.getLength(); j++) {
                        Node attr = attributes.item(j);
                        map.put(node.getNodeName() + attr.getNodeName(), attr.getNodeValue());
                    }
                }
            }
        }
        return Collections.unmodifiableMap(map);
    }
}
