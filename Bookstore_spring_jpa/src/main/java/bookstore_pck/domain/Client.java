package bookstore_pck.domain;

import javax.persistence.Entity;

@Entity
public class Client extends BaseEntity<Long> {

    private String name;
    private int moneySpent = 0;

    public Client() {}

    public Client(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMoneySpent() {
        return moneySpent;
    }

    public void setMoneySpent(int moneySpent) {
        this.moneySpent = moneySpent;
    }

    @Override
    public String toString() {
        return "Client{" +
                "name='" + name + '\'' +
                ", moneySpent=" + moneySpent +
                '}'+ super.toString();
    }
}
