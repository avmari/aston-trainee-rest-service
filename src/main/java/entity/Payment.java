package entity;


import java.util.Objects;

public class Payment extends Entity {

    private Integer amount;

    private User user;

    public Payment(Integer amount, User user) {
        this.amount = amount;
        this.user = user;
    }

    public Payment() {}

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getAmount() {
        return amount;
    }

    public User getUser() {
        return user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Payment payment)) return false;
        return Objects.equals(getId(), payment.getId()) && Objects.equals(amount, payment.amount) && Objects.equals(user.getId(), payment.user.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), amount, user.getId());
    }
}
