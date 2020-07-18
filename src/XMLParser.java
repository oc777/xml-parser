import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class XMLParser {
    private List<String> lines;
    private Document doc;
    private Element currentPerson;
    private Element currentFamily;

    /**
     * Constructor
     * @param inputFilePath - file to txt file to be parsed
     */
    public XMLParser() {}

    /**
     * Parses data from txt file to xml
     * @throws ParserConfigurationException
     * @throws TransformerException
     */
    public void parse(String inputFilePath) throws ParserConfigurationException, TransformerException {
        System.out.println("Parsing text file to XML");

        init(inputFilePath);

        doc = createXMLDocument();
        createXMLStructure(doc);
        writeToXML(doc);

        System.out.println("XML file is ready.");
    }

    /**
     * Prepares the data to be parsed
     * @param inputFilePath
     */
    private void init(String inputFilePath) {
        try {
            lines = readInputFile(inputFilePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Reads lines of data from file into a List
     * @param file - path of the file to be parsed
     * @return List of lines to be parsed
     * @throws IOException if input file not found
     */
    private List<String> readInputFile(String file) throws IOException {
        List<String> result = Files.readAllLines(Paths.get(file));
        return result;
    }

    /**
     * Prepares the document to write to
     * @return the Document object
     * @throws ParserConfigurationException
     */
    private Document createXMLDocument() throws ParserConfigurationException {
        // prepare document builder
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        Document d = docBuilder.newDocument();

        return d;
    }

    /**
     * Prettifies the parsed XML and writes to a file
     * @param document
     * @throws TransformerException
     */
    private void writeToXML(Document document) throws TransformerException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();

        // prettify output
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(new File("result.xml"));

        transformer.transform(source, result);
    }

    /**
     * Create XML elements and hierarchy
     * @param doc
     */
    private void createXMLStructure(Document doc) {
        // people root element
        Element rootElement = doc.createElement("people");
        doc.appendChild(rootElement);
    
        for (String line : lines) {
            String[] lineElements = line.trim().split("[|]");
            // for (String s:lineElements) System.out.println(s);

            switch (lineElements[0]) {
                // create Person element
                case "P":
                    Element person = doc.createElement("person");
                    rootElement.appendChild(person);

                    currentPerson = person;
                    currentFamily = null;

                    createChildElement("firstname", lineElements[1], person);
                    createChildElement("lastname", lineElements[2], person);

                    break;

                // create Phone element
                case "T":
                    Element phone = doc.createElement("phone");

                    if (currentFamily != null) currentFamily.appendChild(phone);
                    else currentPerson.appendChild(phone);

                    createChildElement("mobile", lineElements[1], phone);
                    createChildElement("fixed", lineElements[2], phone);

                    break;

                // create Address element
                case "A":
                    Element address = doc.createElement("address");
                    
                    if (currentFamily != null) currentFamily.appendChild(address);
                    else currentPerson.appendChild(address);

                    createChildElement("street", lineElements[1], address);
                    createChildElement("city", lineElements[2], address);
                    createChildElement("zip", lineElements[3], address);

                    break;

                // create Family element
                case "F":
                    Element family = doc.createElement("family");
                    currentPerson.appendChild(family);
                    currentFamily = family;
                    
                    createChildElement("name", lineElements[1], family);
                    createChildElement("born", lineElements[2], family);

                    break;

            }
        }
    }

    /**
     * Creates XML element
     * @param tagName   String  - element name
     * @param text      String  - text content of the element
     * @param parent    Element - parent element to append to
     */
    private void createChildElement(String tagName, String text, Element parent) {
        Element el = doc.createElement(tagName);
        el.appendChild(doc.createTextNode(text));
        parent.appendChild(el);
    }

}