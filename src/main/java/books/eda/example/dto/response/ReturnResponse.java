package books.eda.example.dto.response;

public class ReturnResponse {

    private boolean success;
    private String message;
    private String bookId;
    private String owner;
    private String returnDate;

    public ReturnResponse() {
    }

    public ReturnResponse(boolean success, String message, String bookId, String owner, String returnDate) {
        this.success = success;
        this.message = message;
        this.bookId = bookId;
        this.owner = owner;
        this.returnDate = returnDate;
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

    public String getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }
}
