package books.eda.example.dto.request;

public class CheckoutRequest {

    private String bookId;
    private String owner;

    // Default constructor
    public CheckoutRequest() {
    }

    // All-args constructor
    public CheckoutRequest(String bookId, String owner) {
        this.bookId = bookId;
        this.owner = owner;
    }

    // Getters and Setters
    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    @Override
    public String toString() {
        return "CheckoutRequest{" +
                "bookId='" + bookId + '\'' +
                ", owner='" + owner + '\'' +
                '}';
    }
}
