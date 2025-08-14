package books.eda.example.dto.response;

public class BuyResponse {

    private boolean success;
    private String message;
    private String bookId;
    private String buyer;
    private double pricePaid;

    public BuyResponse() {
    }

    public BuyResponse(boolean success, String message, String bookId, String buyer, double pricePaid) {
        this.success = success;
        this.message = message;
        this.bookId = bookId;
        this.buyer = buyer;
        this.pricePaid = pricePaid;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getBuyer() {
        return buyer;
    }

    public void setBuyer(String buyer) {
        this.buyer = buyer;
    }

    public double getPricePaid() {
        return pricePaid;
    }

    public void setPricePaid(double pricePaid) {
        this.pricePaid = pricePaid;
    }
}
