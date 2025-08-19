package books.eda.example.dto;

public class BookEvent {
    private String event;
    private String bookId;
    private String title;
    private String actor;
    private Double offeredPrice;

    // Getters and Setters
    public String getEvent() { return event; }
    public void setEvent(String event) { this.event = event; }

    public String getBookId() { return bookId; }
    public void setBookId(String bookId) { this.bookId = bookId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getActor() { return actor; }
    public void setActor(String actor) { this.actor = actor; }

    public Double getOfferedPrice() { return offeredPrice; }
    public void setOfferedPrice(Double offeredPrice) { this.offeredPrice = offeredPrice; }
}
