package bookstore_pck.domain.validators;


public interface Validator<T> {
    void validate(T entity) throws ValidatorException;
}
