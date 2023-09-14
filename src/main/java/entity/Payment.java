package entity;


public class Payment extends Entity {

    private Integer amount;

    private User user;

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
}
