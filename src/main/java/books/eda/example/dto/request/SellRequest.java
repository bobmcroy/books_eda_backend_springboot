package books.eda.example.dto.request;

public class SellRequest {

    private String bookId;
    private String seller;
    private double askingPrice;

    public SellRequest() {
    }

    public SellRequest(String bookId, String seller, double askingPrice) {
        this.bookId = bookId;
        this.seller = seller;
        this.askingPrice = askingPrice;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public double getAskingPrice() {
        return askingPrice;
    }

    public void setAskingPrice(double askingPrice) {
        this.askingPrice = askingPrice;
    }

    @Override
    public String toString() {
        return "SellRequest{" +
                "bookId='" + bookId + '\'' +
                ", seller='" + seller + '\'' +
                ", askingPrice='" + askingPrice +
                '}';
    }
}
