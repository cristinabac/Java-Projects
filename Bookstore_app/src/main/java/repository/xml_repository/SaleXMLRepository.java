package repository.xml_repository;

import domain.Sale;
import domain.validators.BookstoreException;
import domain.validators.SaleValidator;
import domain.validators.Validator;
import domain.validators.ValidatorException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import repository.InMemoryRepository;

import javax.xml.crypto.dsig.Transform;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;

public class SaleXMLRepository extends InMemoryRepository<Long, Sale> {
    private String filePath;

    public SaleXMLRepository(Validator<Sale> validator, String filePath){
        super(validator);
        this.filePath=filePath;
        loadData();
    }

    /**
     * Reads data from xml file
     * @throws BookstoreException if reading operation fails or file structe is wrong
     */
    private void loadData() throws BookstoreException {
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

            Document document = documentBuilder.parse(filePath);
            Element root = document.getDocumentElement();
            NodeList nodes = root.getChildNodes();

            for(int index = 0; index < nodes.getLength(); index++){
                Node saleNode = nodes.item(index);
                if(saleNode instanceof Element)
                    super.save(createSale((Element)saleNode));
            }
        }
        catch (ParserConfigurationException | SAXException | IOException e) {
            throw new BookstoreException(e.getMessage());
        }
    }

    /**
     * Creates a Sale entity from an object of type Element
     * @param saleNode - Element
     * @return Sale
     */
    private Sale createSale(Element saleNode) {
        Sale  sale = new Sale();

        Long saleId = Long.parseLong(saleNode.getAttribute("id"));
        Long bookId = Long.parseLong(getTextFromTag(saleNode,"bookid"));
        Long clientId = Long.parseLong(getTextFromTag(saleNode,"clientid"));

        sale.setId(saleId);
        sale.setBookId(bookId);
        sale.setClientId(clientId);

        return sale;
    }

    /**
     * Returns the text from associated to a tag of an XML Sale object
     * @param saleNode - Element
     * @param tagName - String
     * @return String
     */
    private String getTextFromTag(Element saleNode, String tagName) {
        NodeList elems = saleNode.getElementsByTagName(tagName);
        return elems.item(0).getTextContent();
    }


    @Override
    public Optional<Sale> save(Sale entity) throws ValidatorException {
        Optional<Sale> res =  super.save(entity);
        res.ifPresentOrElse(v-> System.out.println(""),()->saveInternal(entity));
        return res;
    }

    /**
     * Saves a Sale object to the XML file
     * @param res - Sale object
     */
    private void saveInternal(Sale res) {
        try {
            Document document = DocumentBuilderFactory
                    .newInstance()
                    .newDocumentBuilder()
                    .parse(filePath);

            Element root = document.getDocumentElement();
            Element saleElement = document.createElement("sale");
            saleElement.setAttribute("id", String.valueOf(res.getId()));
            root.appendChild(saleElement);
            appendChildWithText(document, saleElement, "bookid", String.valueOf(res.getBookId()));
            appendChildWithText(document,saleElement,"clientid",String.valueOf(res.getClientId()));

            Transformer transformer = TransformerFactory
                    .newInstance()
                    .newTransformer();
            transformer.transform(new DOMSource(root), new StreamResult(new FileOutputStream(filePath)));

        } catch (SAXException | ParserConfigurationException | IOException | TransformerException e) {
            throw new BookstoreException(e.getMessage());
        }


    }

    /**
     * Adds a new child having to a node.
     * It sets the tag name and its textual content
     * @param document Documet
     * @param parent Node
     * @param tagName String
     * @param textCont String
     */
    private void appendChildWithText(Document document, Node parent, String tagName, String textCont) {
        Element element = document.createElement(tagName);
        element.setTextContent(textCont);
        parent.appendChild(element);
    }

    /**
     * Deletes a sale object identified by ID
     * @param id Long
     *            must not be null.
     * @return Optional
     */
    @Override
    public Optional<Sale> delete(Long id) {
        Optional<Sale> result = super.delete(id);
        deleteInternal(id);
        return result;
    }

    /**
     * Removes from XML file a Sale object represented
     * by its ID
     * @param id - Long
     * @throws BookstoreException if working on file fails
     */
    private void deleteInternal(Long id) throws BookstoreException {
        try{
            Document document = DocumentBuilderFactory
                    .newInstance()
                    .newDocumentBuilder()
                    .parse(filePath);

            Element root = document.getDocumentElement();
            Integer index = 0;
            while(index < root.getElementsByTagName("sale").getLength()){
                Element element = (Element) root.getElementsByTagName("sale").item(index);
                if(element.getAttribute("id").equals(String.valueOf(id))) {
                    root.removeChild(element);
                }
                else
                    index++;

            }

            Transformer transformer = TransformerFactory.newInstance()
                    .newTransformer();
            transformer.transform(new DOMSource(root),
                    new StreamResult(new FileOutputStream(filePath)));
        } catch (SAXException | ParserConfigurationException | IOException | TransformerException e) {
            throw new BookstoreException(e.getMessage());
        }
    }

    /**
     * Updates a sale in both repository and XML file
     * @param entity Sale
     *            must not be null.
     * @return an {@code Optional} - null if the entity was updated otherwise (e.g. id does not exist) returns the
     *      *          entity.
     * @throws ValidatorException - if entity is not valid
     * @throws BookstoreException - if operation on XML file fails
     */
    @Override
    public Optional<Sale> update(Sale entity) throws ValidatorException ,BookstoreException{
        Optional<Sale> res =  super.update(entity);
        updateInternal(entity);
        return res;
    }

    /**
     * Update a sale entity in the XML file
     * @param entity Sale
     * @throws BookstoreException if operations on XML file fail
     */
    private void updateInternal(Sale entity)throws BookstoreException{
        try{
            Document document = DocumentBuilderFactory
                    .newInstance()
                    .newDocumentBuilder()
                    .parse(filePath);
            Element saleElement = document.createElement("sale");
            saleElement.setAttribute("id",String.valueOf(entity.getId()));
            appendChildWithText(document,saleElement,"bookid",String.valueOf(entity.getBookId()));
            appendChildWithText(document,saleElement,"cleintid",String.valueOf(entity.getClientId()));

            Element root = document.getDocumentElement();
            for(int index = 0; index < root.getElementsByTagName("sale").getLength(); index++){
                Element element = (Element) root.getElementsByTagName("sale").item(index);
                if(element.getAttribute("id").equals(String.valueOf(entity.getId())))
                    root.replaceChild(saleElement,element);
            }
        } catch (SAXException | ParserConfigurationException | IOException e) {
            throw new BookstoreException(e.getMessage());
        }
    }
}
