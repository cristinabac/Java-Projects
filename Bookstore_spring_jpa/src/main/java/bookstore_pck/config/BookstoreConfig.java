package bookstore_pck.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan({"bookstore_pck.domain.validators", "bookstore_pck.repository", "bookstore_pck.service", "bookstore_pck.ui"})
public class BookstoreConfig {
}
