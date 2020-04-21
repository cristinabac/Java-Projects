package domain;

import domain.validators.BookValidator;
import domain.validators.Validator;
import domain.validators.ValidatorException;
import org.junit.Test;

public class BookValidatorTests {

    @Test
    public void nullIDValue(){
        Book falseBook = new Book();
        Validator<Book> validator = new BookValidator();

        try{
            validator.validate(falseBook);
            assert false;
        }
        catch (ValidatorException exc){
            assert true;
        }
    }

    @Test
    public void negativeIDValueTest(){
        Book falseBook = new Book();
        falseBook.setId((long)-1);
        Validator<Book> validator = new BookValidator();

        try{
            validator.validate(falseBook);
            assert false;
        }
        catch (ValidatorException exc){
            assert true;
        }
    }


    @Test
    public void negativePriceValueTest(){
        Book falseBook = new Book();
        falseBook.setPrice(-33);
        falseBook.setId((long)3);
        falseBook.setTitle("da");
        falseBook.setAuthor("fff");
        Validator<Book> validator = new BookValidator();

        try{
            validator.validate(falseBook);
            assert false;
        }
        catch (ValidatorException exc){
            assert true;
        }
    }
    @Test
    public void nullTitleValueTest(){
        Book falseBook = new Book();
        falseBook.setPrice(33);
        falseBook.setId((long)3);
        Validator<Book> validator = new BookValidator();

        try{
            validator.validate(falseBook);
            assert false;
        }
        catch (ValidatorException exc){
            assert true;
        }
    }

    @Test
    public void emptyNameValueTest(){
        Book falseBook = new Book();
        falseBook.setPrice(33);
        falseBook.setId((long)3);
        falseBook.setTitle("");
        Validator<Book> validator = new BookValidator();

        try{
            validator.validate(falseBook);
            assert false;
        }
        catch (ValidatorException exc){
            assert true;
        }
    }


    @Test
    public void nullAuthorValueTest(){
        Book falseBook = new Book();
        falseBook.setPrice(33);
        falseBook.setId((long)3);
        falseBook.setTitle("Chemarea Strabunilor");
        Validator<Book> validator = new BookValidator();

        try{
            validator.validate(falseBook);
            assert false;
        }
        catch (ValidatorException exc){
            assert true;
        }
    }


    @Test
    public void emptyAuthorValueTest(){
        Book falseBook = new Book();
        falseBook.setPrice(33);
        falseBook.setId((long)3);
        falseBook.setTitle("Chemarea Strabunilor");
        falseBook.setAuthor("");
        Validator<Book> validator = new BookValidator();

        try{
            validator.validate(falseBook);
            assert false;
        }
        catch (ValidatorException exc){
            assert true;
        }
    }



}
