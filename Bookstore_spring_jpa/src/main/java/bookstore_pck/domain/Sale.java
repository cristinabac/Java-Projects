package bookstore_pck.domain;

import javax.persistence.Entity;

@Entity
public class Sale extends BaseEntity<Long> {

    private Long bookId;
    private Long clientId;
    private static Long pid = 0L;

    public Sale() {this.setId(++pid);}

    public Sale(Long bookId, Long clientId) {
        this.bookId = bookId;
        this.clientId = clientId;
        this.setId(++pid);
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    @Override
    public String toString() {
        return
                "bookId=" + bookId + ", clientId=" + clientId +" "+ super.toString();
    }
}
