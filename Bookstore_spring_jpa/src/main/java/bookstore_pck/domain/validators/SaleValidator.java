package bookstore_pck.domain.validators;

import bookstore_pck.domain.Sale;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class SaleValidator implements Validator<Sale> {
    @Override
    public void validate(Sale entity) throws ValidatorException {
        Optional.ofNullable(entity.getId()).orElseThrow(() -> new ValidatorException("The id must not be null"));
        Optional.ofNullable(entity.getBookId()).orElseThrow(()-> new ValidatorException("Book id must not be null"));
        Optional.ofNullable(entity.getClientId()).orElseThrow(()-> new ValidatorException("Client id must not be null"));
        Optional.of(entity.getBookId()).filter((i)->i<0).ifPresent((i)->{throw new ValidatorException("Invalid value for book ID!");});
        Optional.of(entity.getClientId()).filter((i)->i<0).ifPresent((i)->{throw new ValidatorException("Invalid value for client ID!");});
    }
}
