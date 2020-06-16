package it.polimi.ingsw.parser;

import it.polimi.ingsw.model.God;
import it.polimi.ingsw.model.effects.*;
import it.polimi.ingsw.model.enumerations.MoveType;
import it.polimi.ingsw.model.enumerations.PhaseType;
import it.polimi.ingsw.model.enumerations.TargetType;
import it.polimi.ingsw.network.server.Server;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.*;

import static it.polimi.ingsw.model.enumerations.XMLName.*;


/**
 * Provides the methods for processing the XML document with the gods configuration.
 */
public class GodParser {

    public static final String FILE_PATH = "xml/gods.xml";

    private GodParser() {
    }

    /**
     * Parses the XML file into a list of {@link God} objects.
     *
     * @return the list of gods parsed from the XML file.
     */
    public static List<God> parseGods() {
        List<God> gods = new ArrayList<>();
        DocumentBuilder db;
        Document doc = null;

        File file = new File(GodParser.class.getClassLoader().getResource(FILE_PATH).getFile());
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        try {
            dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            dbf.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
            dbf.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");

            db = dbf.newDocumentBuilder();
            dbf.setValidating(false);
            doc = db.parse(file);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            Server.LOGGER.severe("failed to read gods.xml file.");
            System.exit(1);
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
        String name = godElement.getElementsByTagName(NAME.getText()).item(0).getTextContent();
        String caption = godElement.getElementsByTagName(CAPTION.getText()).item(0).getTextContent();
        String description = godElement.getElementsByTagName(DESCRIPTION.getText())
                .item(0).getTextContent();

        List<Effect> effects = new ArrayList<>();

        // Retrieve <effect> nodes
        Node effectsNode = godElement.getElementsByTagName(EFFECTS.toString()).item(0);
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
        String effectType = effectElement.getAttribute(PHASE.getText());
        Effect effect = new SimpleEffect(PhaseType.valueOf(effectType));

        NodeList reqNodeList = effectElement.getElementsByTagName(REQUIREMENTS.getText());
        NodeList parNodeList = effectElement.getElementsByTagName(PARAMETERS.getText());

        Map<String, String> requirements = Map.of();
        Map<String, String> parameters = Map.of();

        // Retrieve only the child nodes of the first element found
        if (reqNodeList.getLength() > 0) {
            reqNodeList = reqNodeList.item(0).getChildNodes();
            requirements = toMap(reqNodeList);
            //TargetType reqTargetType = TargetType.valueOf(requirements.get(TARGET.getText()));
            //effect.addTargetType(REQUIREMENTS, reqTargetType);
        }
        if (parNodeList.getLength() > 0) {
            parNodeList = parNodeList.item(0).getChildNodes();
            parameters = toMap(parNodeList);
            TargetType parTargetType = TargetType.valueOf(parameters.get(TARGET.getText()));
            effect.addTargetType(PARAMETERS, parTargetType);
        }

        return parseEffectDecorators(effect, requirements, parameters);
    }

    /**
     * Decorates the effect argument.
     *
     * @param effect       the effect to decorate.
     * @param requirements the map of settings to be satisfied in order to apply the effect.
     * @param parameters   the map of settings applied by the effect.
     * @return the decorated effect.
     */
    private static Effect parseEffectDecorators(Effect effect, Map<String, String> requirements,
                                                Map<String, String> parameters) {

        if (parameters.containsKey(MOVE.getText())) {
            effect = decorateMove(effect, requirements, parameters);
        }

        if (parameters.containsKey(BUILD.getText())) {
            effect = decorateBuild(effect, requirements, parameters);
        }

        return effect;
    }

    private static Effect decorateBuild(Effect effect, Map<String, String> requirements,
                                        Map<String, String> parameters) {
        if (Boolean.parseBoolean(parameters.get(BUILD.getText() + AGAIN.getText()))) {
            boolean sameSpace = Boolean.parseBoolean(parameters.get(BUILD.getText() + SAME_SPACE.getText()));
            boolean dome = Boolean.parseBoolean(parameters.get(BUILD.getText() + DOME.getText()));
            boolean forceSameSpace = Boolean.parseBoolean(parameters.get(BUILD.getText() + FORCE_SAME_SPACE.getText()));

            effect = new BuildAgainDecorator(effect, requirements, sameSpace, dome, forceSameSpace);
        }

        if (Boolean.parseBoolean(parameters.get(BUILD.getText() + FORCE_DOME.getText()))) {
            effect = new BuildDomeDecorator(effect, requirements);
        }

        return effect;
    }

    private static Effect decorateMove(Effect effect, Map<String, String> requirements,
                                       Map<String, String> parameters) {
        if (Boolean.parseBoolean(parameters.get(MOVE.getText() + AGAIN.getText()))) {
            boolean goBack = Boolean.parseBoolean(parameters.get(MOVE.getText() + GO_BACK.getText()));
            effect = new MoveAgainDecorator(effect, requirements, goBack);
        }

        if (Boolean.parseBoolean(parameters.get(MOVE.getText() + OVER.getText()))) {
            boolean pushBack = Boolean.parseBoolean(parameters.get(MOVE.getText() + PUSH_BACK.getText()));
            boolean swapSpace = Boolean.parseBoolean(parameters.get(MOVE.getText() + SWAP_SPACE.getText()));
            effect = new MoveOverDecorator(effect, requirements, pushBack, swapSpace);
        }

        if (Boolean.parseBoolean(parameters.get(MOVE.getText() + LOCK.getText()))) {
            MoveType moveType = MoveType.valueOf(parameters.get(MOVE.getText()));
            effect = new MoveLockDecorator(effect, requirements, moveType);
        }

        if (Boolean.parseBoolean(parameters.get(MOVE.getText() + WIN.getText()))) {
            MoveType moveType = MoveType.valueOf(parameters.get(MOVE.getText()));
            int levels = Integer.parseInt(parameters.get(MOVE.getText() + LEVEL.getText()));
            effect = new MoveWinDecorator(effect, requirements, moveType, levels);
        }

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
     * Returns an immutable empty map if the nodeList is empty.
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
