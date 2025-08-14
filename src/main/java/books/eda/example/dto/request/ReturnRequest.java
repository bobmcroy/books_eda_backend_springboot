package books.eda.example.dto.request;

public class ReturnRequest {

    private String bookId;
    private String owner;

    public ReturnRequest() {
    }

    public ReturnRequest(String bookId, String owner) {
        this.bookId = bookId;
        this.owner = owner;
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

    @Override
    public String toString() {
        return "ReturnRequest{" +
                "bookId='" + bookId + '\'' +
                ", owner='" + owner + '\'' +
                '}';
    }
}
