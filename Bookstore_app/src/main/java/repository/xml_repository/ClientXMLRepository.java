package repository.xml_repository;

import domain.Client;
import domain.validators.BookstoreException;
import domain.validators.Validator;
import domain.validators.ValidatorException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import repository.InMemoryRepository;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.stream.StreamSupport;

public class ClientXMLRepository extends InMemoryRepository<Long, Client> {
    private String fileName;

    public ClientXMLRepository(Validator<Client> clientValidator,String fileName){
        super(clientValidator);
        this.fileName=fileName;
        loadData();
    }

    /**
     * Reads data from xml file
     * @throws BookstoreException if reading operation fails or file structe is wrong
     */
    private void loadData() throws BookstoreException{
        try{
            DocumentBuilderFactory documentBuilderFactory =
                    DocumentBuilderFactory.newInstance();

            DocumentBuilder documentBuilder =
                    documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(fileName);
            Element root = document.getDocumentElement();

            NodeList nodes = root.getChildNodes();

            for(int index = 0; index < nodes.getLength(); index++){
                Node clientNode = nodes.item(index);
                if(clientNode instanceof Element)
                    super.save(createClient((Element)clientNode));
            }
        }
        catch (ParserConfigurationException | IOException | SAXException e) {
            throw new BookstoreException(e.getMessage());
        }
    }

    /**
     * Returns a client from an object of type Element
     * @param clientNode Element
     * @return Client
     */
    private Client createClient(Element clientNode) {
        Client client = new Client();

        Long clientID = Long.parseLong(clientNode.getAttribute("id"));
        String clientName = getTextFromTag(clientNode,"name");
        Integer moneySpent = Integer.parseInt(getTextFromTag(clientNode,"moneySpent"));
        client.setId(clientID);
        client.setName(clientName);
        client.setMoneySpent(moneySpent);
        return client;
    }

    /**
     * Returns the string value asssociated to a tag
     * @param clientNode Element
     * @param name String
     * @return String
     */
    private String getTextFromTag(Element clientNode, String name) {
        String result;
        NodeList elems = clientNode.getElementsByTagName(name);
        return elems.item(0).getTextContent();
    }


    /**
     * Saves a client in the repository and the corresponding XML file
     * @param entity Client
     *            must not be null.
     * @return {@code Optional} - null if the entity was updated otherwise (e.g. id does not exist) returns the
     *              entity.
     * @throws BookstoreException if writing to XML file fails or a validation error will be thrown
     */
    @Override
    public Optional<Client> save(Client entity)throws BookstoreException{
        Optional<Client> result = super.save(entity);
        result.ifPresentOrElse((v)-> System.out.println(""),()->{saveInternal(entity);});
        return result;
    }

    /**
     * Saves a client in the corresponding XML file
     * @param entity Client
     * @throws BookstoreException if writing to XML fails
     */
    private void saveInternal(Client entity)throws BookstoreException{
        try {
            Document document = DocumentBuilderFactory
                    .newInstance()
                    .newDocumentBuilder()
                    .parse(fileName);
            Element root = document.getDocumentElement();

            Element clientElement = document.createElement("client");
            clientElement.setAttribute("id", String.valueOf(entity.getId()));
            root.appendChild(clientElement);

            appendChildWithText(document, clientElement, "name", entity.getName());
            appendChildWithText(document, clientElement, "moneySpent", String.valueOf(entity.getMoneySpent()));

            Transformer transformer =
                    TransformerFactory.newInstance().newTransformer();
            transformer.transform(new DOMSource(root),
                    new StreamResult(new FileOutputStream(
                            fileName)));
        } catch (IOException | ParserConfigurationException | SAXException | TransformerException e) {
            throw new BookstoreException(e.getMessage());
        }

    }


    /**
     * Adds to a Document a new Child
     * @param document Document
     * @param parent Node - an XML node
     * @param tagName String - name of an XML tag
     * @param textContent String - text associated to tag
     */
    private static void appendChildWithText(Document document,
                                            Node parent, String tagName, String textContent) {
        Element element = document.createElement(tagName);
        element.setTextContent(textContent);
        parent.appendChild(element);
    }


    /**
     * Removes the Client described by the value of the id
     * for its id field
     * @param id Long
     *            must not be null.
     * @return an {@code Optional} - null if there is no entity with the given id, otherwise the removed entity.
     * @throws BookstoreException if deleteInternal fails
     */
    @Override
    public Optional<Client> delete(Long id)throws BookstoreException{
        Optional<Client> result = super.delete(id);
        deleteInternal(id);
        return result;
    }

    /**
     * Removes from the XML file the client corresponding
     * to the id value given as parameter
     * @param id Long
     * @throws BookstoreException if removing from XML file fails
     */
    private void deleteInternal(Long id)throws BookstoreException{
        try {
            Document document = DocumentBuilderFactory
                    .newInstance()
                    .newDocumentBuilder()
                    .parse(fileName);

            Element root = document.getDocumentElement();
            int i = 0;
            while (i < root.getElementsByTagName("client").getLength()) {
                Element element = (Element) root.getElementsByTagName("client").item(i);
                if (element.getAttribute("id").equals(String.valueOf(id)))
                    root.removeChild(element);
                else
                    i++;
            }

            Transformer transformer =
                    TransformerFactory.newInstance().newTransformer();
            transformer.transform(new DOMSource(root),
                    new StreamResult(new FileOutputStream(
                            fileName)));
        }
        catch (IOException | ParserConfigurationException | SAXException | TransformerException e) {
            throw new BookstoreException(e.getMessage());
        }

    }

    /**
     * Updates a client in both repository and XML file
     * @param entity Client
     *            must not be null.
     * @return an {@code Optional} - null if the entity was updated otherwise (e.g. id does not exist) returns the
     *      *          entity.
     * @throws ValidatorException - if entity is not valid
     * @throws BookstoreException - if operation on XML file fails
     */
    @Override
    public Optional<Client> update(Client entity)throws ValidatorException,BookstoreException {
        Optional<Client> result = super.update(entity);
        updateInternal(entity);
        return result;
    }

    /**
     * Updates a client in the XML file
     * @param newClient Client
     * @throws BookstoreException if operations on XML file fail
     */
    private void updateInternal(Client newClient)throws BookstoreException {
        try {
            Document document = DocumentBuilderFactory
                    .newInstance()
                    .newDocumentBuilder()
                    .parse(fileName);

            Element clientElement = document.createElement("client");
            clientElement.setAttribute("id", String.valueOf(newClient.getId()));

            appendChildWithText(document, clientElement, "name", newClient.getName());
            appendChildWithText(document, clientElement, "moneySpent", String.valueOf(newClient.getMoneySpent()));

            Element root = document.getDocumentElement();
            for (int i = 0; i < root.getElementsByTagName("client").getLength(); i++) {
                Element element = (Element) root.getElementsByTagName("client").item(i);
                if (element.getAttribute("id").equals(String.valueOf(newClient.getId())))
                    root.replaceChild(clientElement, element);
            }

            Transformer transformer =
                    TransformerFactory.newInstance().newTransformer();
            transformer.transform(new DOMSource(root),
                    new StreamResult(new FileOutputStream(
                            fileName)));
        } catch (IOException | ParserConfigurationException | SAXException | TransformerException e) {
            throw new BookstoreException(e.getMessage());
        }
    }
}


