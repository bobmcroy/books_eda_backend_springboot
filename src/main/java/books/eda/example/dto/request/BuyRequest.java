package books.eda.example.dto.request;

public class BuyRequest {

    private String bookId;
    private String buyer;
    private double offeredPrice;

    public BuyRequest() {
    }

    public BuyRequest(String bookId, String buyer, double offeredPrice) {
        this.bookId = bookId;
        this.buyer = buyer;
        this.offeredPrice = offeredPrice;
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

    public double getOfferedPrice() {
        return offeredPrice;
    }

    public void setOfferedPrice(double offeredPrice) {
        this.offeredPrice = offeredPrice;
    }

    @Override
    public String toString() {
        return "BuyRequest{" +
                "bookId='" + bookId + '\'' +
                ", buyer='" + buyer + '\'' +
                ", offeredPrice='" + offeredPrice +
                '}';
    }
}
