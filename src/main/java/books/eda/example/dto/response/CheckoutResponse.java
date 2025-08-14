package books.eda.example.dto.response;

public class CheckoutResponse {

    private boolean success;
    private String message;
    private String bookId;
    private String owner;

    public CheckoutResponse() {
    }

    public CheckoutResponse(boolean success, String message, String bookId, String owner) {
        this.success = success;
        this.message = message;
        this.bookId = bookId;
        this.owner = owner;
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

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }
}
