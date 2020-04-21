package domain.validators;

import domain.Book;

import java.util.Optional;

public class BookValidator implements Validator<Book> {

    @Override
    public void validate(Book entity) throws ValidatorException {
        Optional.ofNullable(entity.getId()).orElseThrow(() -> new ValidatorException("The id can not be null"));
        /*
        if (entity.getId() <= 0) throw new ValidatorException("\tId must be > 0");
        if (entity.getPrice() <= 0) throw new ValidatorException("\tPrice must be > 0");
        if (entity.getAuthor().matches(".*[,?/!:;<>0-9].*"))
            throw new ValidatorException("\tAuthor must not contain digits or symbols");
        if (entity.getTitle().equals("") | entity.getAuthor().equals(""))
            throw new ValidatorException("Author or title must have more characters");

         */

        Optional.of(entity.getId()).filter((e)->e<=0).ifPresent(i->{throw new ValidatorException("Id must be >0"); });


        Optional.of(entity.getPrice()).filter((e)->e<=0).ifPresent(i->{throw new ValidatorException("Price must be >0"); });
        //author name...

        Optional.ofNullable(entity.getTitle()).orElseThrow(()->new ValidatorException("Title can not be null"));
        Optional.of(entity.getTitle()).filter((e)->e.equals("")).ifPresent(i->{throw new ValidatorException("Title can not be empty"); });

        Optional.ofNullable(entity.getAuthor()).orElseThrow(()->new ValidatorException("Author can not be null!"));
        Optional.of(entity.getAuthor()).filter((e)->e.equals("")).ifPresent(i->{throw new ValidatorException("Author can not be null"); });
    }


}
