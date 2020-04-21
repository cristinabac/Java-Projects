package domain;

import domain.validators.ClientValidator;
import domain.validators.Validator;
import domain.validators.ValidatorException;
import org.junit.Test;

public class ClientValidatorTests {


    @Test
    public void invalidIdTest(){
        Validator<Client> clientValidator = new ClientValidator();
        Client falseClient = new Client();
        try{
            clientValidator.validate(falseClient);
            assert false;
        }
        catch (ValidatorException exc){
            assert true;
        }
    }

    @Test
    public void invalidIDValue(){
        Validator<Client> clientValidator = new ClientValidator();
        Client falseClient = new Client();
        falseClient.setId((long) -1);
        try{
            clientValidator.validate(falseClient);
            assert false;
        }
        catch (ValidatorException exc){
            assert true;
        }
    }

    @Test
    public void invalidNameTest(){
        Validator<Client> clientValidator = new ClientValidator();
        Client falseClient = new Client();
        falseClient.setId((long)2);
        falseClient.setName("[shekmau23'");
        try{
            clientValidator.validate(falseClient);
            assert false;
        }
        catch (ValidatorException exc){
            assert true;
        }

    }

    @Test
    public void nullNameTest(){
        Validator<Client> clientValidator = new ClientValidator();
        Client falseClient = new Client();
        falseClient.setId((long)2);
        try{
            clientValidator.validate(falseClient);
            assert false;
        }
        catch (ValidatorException exc){
            assert true;
        }
    }

    @Test
    public void invalidSpentMoneyTest(){
        Validator<Client> clientValidator = new ClientValidator();
        Client falseClient = new Client();
        falseClient.setId((long)1);
        falseClient.setName("false client");
        falseClient.setMoneySpent(-20);
        try{
            clientValidator.validate(falseClient);
            assert false;
        }
        catch (ValidatorException exc){
            assert true;
        }
    }
}
