package books.eda.example.model;

import java.util.Objects;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@DynamoDbBean
public class Book {

    public enum BookWeightUOM {OUNCES, POUNDS, KILOGRAMS}
    public enum BookSizeUOM {INCHES, FEET, MILLIMETERS, CENTIMETERS}
    public enum BookMediaType {EBOOK, PAPERBACK, HARDCOVER}
    public enum BookStatus {AVAILABLE, CHECKED_OUT, SOLD}
    public enum BookCondition {NEW, LIKE_NEW, USED, DAMAGED}

    private String bookId;
    private String title;
    private String author;
    private String publisher;
    private String publicationDate;
    private BookWeightUOM weightUom;
    private String weight;
    private int pageCount;
    private BookSizeUOM sizeUom;
    private String dimensions;
    private String isbn;
    private String language;
    private double price;
    private String owner; // optional, who checked out or owns it
    private BookMediaType type;
    private BookStatus status;
    private BookCondition condition;
    private double askingPrice = 0.0;
    private String coverUrl;
    private String coverUrlKey;

    @DynamoDbPartitionKey
    public String getBookId() {
        return bookId;
    }
    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }
    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublisher() {
        return publisher;
    }
    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getPublicationDate() {
        return publicationDate;
    }
    public void setPublicationDate(String publicationDate) {
        this.publicationDate = publicationDate;
    }

    public BookWeightUOM getWeightUom() {
        return weightUom;
    }
    public void setWeightUom(BookWeightUOM weightUom) {
        this.weightUom = weightUom;
    }

    public String getWeight() {
        return weight;
    }
    public void setWeight(String weight) {
        this.weight = weight;
    }

    public int getPageCount() {
        return pageCount;
    }
    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public BookSizeUOM getSizeUom() {
        return sizeUom;
    }
    public void setSizeUom(BookSizeUOM sizeUom) {
        this.sizeUom = sizeUom;
    }

    public String getDimensions() {
        return dimensions;
    }
    public void setDimensions(String dimensions) {
        this.dimensions = dimensions;
    }

    public String getIsbn() {
        return isbn;
    }
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getLanguage() {
        return language;
    }
    public void setLanguage(String language) {
        this.language = language;
    }

    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }

    public String getOwner() {
        return owner;
    }
    public void setOwner(String owner) {
        this.owner = owner;
    }

    public BookMediaType getType() {
        return type;
    }
    public void setType(BookMediaType type) {
        this.type = type;
    }

    public BookStatus getStatus() {
        return status;
    }
    public void setStatus(BookStatus status) {
        this.status = status;
    }

    public BookCondition getCondition() {
        return condition;
    }
    public void setCondition(BookCondition condition) {
        this.condition = condition;
    }

    public double getAskingPrice() {
        return askingPrice;
    }
    public void setAskingPrice(double askingPrice) {
        this.askingPrice = askingPrice;
    }

    @DynamoDbAttribute("coverUrl")
    public String getCoverUrl() { 
        return coverUrl; 
    }

    public void setCoverUrl(String coverUrl) { 
        this.coverUrl = coverUrl; 
    }

    @DynamoDbAttribute("coverUrlKey")
    public String getCoverUrlKey() { 
        return coverUrlKey; 
    }

    public void setCoverUrlKey(String coverUrlKey) { 
        this.coverUrlKey = coverUrlKey; 
    }

    @Override
    public String toString() {
        return "Book{" +
               "bookId='" + bookId + '\'' +
               ", title='" + title + '\'' +
               ", author='" + author + '\'' +
               ", publisher='" + publisher + '\'' +
               ", publicationDate='" + publicationDate + '\'' +
               ", weightUom='" + weightUom + '\'' +
               ", weight='" + weight + '\'' +
               ", pageCount='" + pageCount + 
               ", sizeUom='" + sizeUom + '\'' +
               ", dimensions='" + dimensions + '\'' +
               ", isbn='" + isbn + '\'' +
               ", language='" + language + '\'' +
               ", price=" + price +
               ", owner='" + owner + '\'' +
               ", type=" + type +
               ", status=" + status +
               ", condition=" + condition +
               ", askingPrice=" + askingPrice +
               ", coverUrl='" + coverUrl + '\'' +
               ", coverUrlKey='" + coverUrlKey + '\'' +
               '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Book)) return false;
        Book book = (Book) o;
        return Integer.compare(book.pageCount, pageCount) == 0 &&
                Objects.equals(bookId, book.bookId) &&
                Objects.equals(title, book.title) &&
                Objects.equals(author, book.author) &&
                Objects.equals(publisher, book.publisher) &&
                Objects.equals(publicationDate, book.publicationDate) &&
                weightUom == book.weightUom &&
                Objects.equals(weight, book.weight) &&
                sizeUom == book.sizeUom &&
                Objects.equals(dimensions, book.dimensions) &&
                Objects.equals(isbn, book.isbn) &&
                Objects.equals(language, book.language) &&
                Double.compare(book.price, price) == 0 &&
                Objects.equals(owner, book.owner) &&
                status == book.status &&
                type == book.type &&
                condition == book.condition &&
                Double.compare(book.askingPrice, askingPrice) == 0 &&
                Objects.equals(coverUrl, book.coverUrl) &&
                Objects.equals(coverUrlKey, book.coverUrlKey);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bookId, title, author, publisher, publicationDate, weightUom, weight, pageCount, 
        sizeUom, dimensions, isbn, language, price, owner, status, type, condition, askingPrice, coverUrl, coverUrlKey);
    }
}
