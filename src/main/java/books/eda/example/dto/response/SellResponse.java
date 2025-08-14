package books.eda.example.dto.response;

public class SellResponse {

    private boolean success;
    private String message;
    private String bookId;
    private String seller;
    private double profit;

    public SellResponse() {
    }

    public SellResponse(boolean success, String message, String bookId, String seller, double profit) {
        this.success = success;
        this.message = message;
        this.bookId = bookId;
        this.seller = seller;
        this.profit = profit;
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

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public double getProfit() {
        return profit;
    }

    public void setProfit(double profit) {
        this.profit = profit;
    }
}
