package repository.xml_repository;

import domain.Book;
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
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;

public class BookXMLRepository extends InMemoryRepository<Long, Book> {
    private String fileName;

    public BookXMLRepository(Validator<Book> validator, String filename) {
        super(validator);
        this.fileName = filename;
        this.loadData();
    }

    /**
     * Reads data from xml file
     * @throws BookstoreException if reading operation fails or file structure is wrong
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
                Node bookNode = nodes.item(index);
                if(bookNode instanceof Element)
                    super.save(createBook((Element)bookNode));
            }
        }
        catch (ParserConfigurationException | IOException | SAXException e) {
            throw new BookstoreException(e.getMessage());
        }
    }


    /**
     * Returns a book from an object of type Element
     * @param bookNode Element
     * @return Book
     */
    private Book createBook(Element bookNode) {
        Book book = new Book();

        Long bookID = Long.parseLong(bookNode.getAttribute("id"));
        String bookTitle = getTextFromTag(bookNode,"title");
        String bookAuthor = getTextFromTag(bookNode,"author");
        Integer bookPrice = Integer.parseInt(getTextFromTag(bookNode,"price"));

        book.setId(bookID);
        book.setTitle(bookTitle);
        book.setAuthor(bookAuthor);
        book.setPrice(bookPrice);

        return book;
    }


    /**
     * Returns the string value associated to a tag
     * @param bookNode Element
     * @param name String
     * @return String
     */
    private String getTextFromTag(Element bookNode, String name) {
        String result;
        NodeList elems = bookNode.getElementsByTagName(name);
        return elems.item(0).getTextContent();
    }

    /**
     * Saves a book in the repository and the corresponding XML file
     * @param entity Book
     *            must not be null.
     * @return {@code Optional} - null if the entity was updated otherwise (e.g. id does not exist) returns the
     *              entity.
     * @throws BookstoreException if writing to XML file fails or a validation error will be thrown
     */
    @Override
    public Optional<Book> save(Book entity)throws BookstoreException{
        Optional<Book> result = super.save(entity);
        result.ifPresentOrElse((v)-> System.out.println(""),()->{saveInternal(entity);});
        return result;
    }

    /**
     * Saves a book in the corresponding XML file
     * @param entity Book
     * @throws BookstoreException if writing to XML fails
     */
    private void saveInternal(Book entity)throws BookstoreException{
        try {
            Document document = DocumentBuilderFactory
                    .newInstance()
                    .newDocumentBuilder()
                    .parse(fileName);
            Element root = document.getDocumentElement();

            Element bookElement = document.createElement("book");
            bookElement.setAttribute("id", String.valueOf(entity.getId()));
            root.appendChild(bookElement);

            appendChildWithText(document, bookElement, "title", entity.getTitle());
            appendChildWithText(document, bookElement, "author", entity.getAuthor());
            appendChildWithText(document, bookElement, "price", String.valueOf(entity.getPrice()));

            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.transform(new DOMSource(root), new StreamResult(new FileOutputStream(fileName)));

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
     * Removes the Book described by the value of the id
     * for its id field
     * @param id Long
     *            must not be null.
     * @return an {@code Optional} - null if there is no entity with the given id, otherwise the removed entity.
     * @throws BookstoreException if deleteInternal fails
     */
    @Override
    public Optional<Book> delete(Long id)throws BookstoreException{
        Optional<Book> result = super.delete(id);
        deleteInternal(id);
        return result;
    }


    /**
     * Removes from the XML file the book corresponding
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
            while (i < root.getElementsByTagName("book").getLength()) {
                Element element = (Element) root.getElementsByTagName("book").item(i);
                if (element.getAttribute("id").equals(String.valueOf(id)))
                    root.removeChild(element);
                else
                    i++;
            }

            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.transform(new DOMSource(root), new StreamResult(new FileOutputStream(fileName)));
        }
        catch (IOException | ParserConfigurationException | SAXException | TransformerException e) {
            throw new BookstoreException(e.getMessage());
        }

    }


    /**
     * Updates a book in both repository and XML file
     * @param entity Book
     *            must not be null.
     * @return an {@code Optional} - null if the entity was updated otherwise (e.g. id does not exist) returns the
     *      *          entity.
     * @throws ValidatorException - if entity is not valid
     * @throws BookstoreException - if operation on XML file fails
     */
    @Override
    public Optional<Book> update(Book entity)throws ValidatorException,BookstoreException {
        Optional<Book> result = super.update(entity);
        updateInternal(entity);
        return result;
    }

    /**
     * Updates a book in the XML file
     * @param newBook Book
     * @throws BookstoreException if operations on XML file fail
     */
    private void updateInternal(Book newBook)throws BookstoreException {
        try {
            Document document = DocumentBuilderFactory
                    .newInstance()
                    .newDocumentBuilder()
                    .parse(fileName);

            Element bookElement = document.createElement("book");
            bookElement.setAttribute("id", String.valueOf(newBook.getId()));

            appendChildWithText(document, bookElement, "title", newBook.getTitle());
            appendChildWithText(document, bookElement, "author", newBook.getAuthor());
            appendChildWithText(document, bookElement, "price", String.valueOf(newBook.getPrice()));

            Element root = document.getDocumentElement();
            for (int i = 0; i < root.getElementsByTagName("book").getLength(); i++) {
                Element element = (Element) root.getElementsByTagName("book").item(i);
                if (element.getAttribute("id").equals(String.valueOf(newBook.getId())))
                    root.replaceChild(bookElement, element);
            }

            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.transform(new DOMSource(root), new StreamResult(new FileOutputStream(fileName)));
        } catch (IOException | ParserConfigurationException | SAXException | TransformerException e) {
            throw new BookstoreException(e.getMessage());
        }
    }
}
