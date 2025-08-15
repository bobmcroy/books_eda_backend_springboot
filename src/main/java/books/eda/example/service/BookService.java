package books.eda.example.service;

import books.eda.example.model.Book;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.regions.Region;

import javax.annotation.PostConstruct;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Service
public class BookService {

    private final DynamoDbEnhancedClient enhancedClient;
    private final DynamoDbTable<Book> bookTable;

    public BookService() {
        DynamoDbClient client = DynamoDbClient.builder()
                .region(Region.US_EAST_1)
                .endpointOverride(URI.create("http://localhost:8000"))
                .build();

        this.enhancedClient = DynamoDbEnhancedClient.builder()
                .dynamoDbClient(client)
                .build();

        this.bookTable = enhancedClient.table("book_list", TableSchema.fromBean(Book.class));
    }

    // 🔹 Optional: Seed local DynamoDB with initial books
    @PostConstruct
    public void initData() {
        if (bookTable.scan().items().iterator().hasNext()) {
            return; // already seeded
        }

        List<Book> books = new ArrayList<>();

        Book b1 = new Book();
        b1.setBookId("1");
        b1.setTitle("Effective Java Third Edition");
        b1.setAuthor("Joshua Bloch");
        b1.setPublisher("Addison-Wesley Professional");
        b1.setPublicationDate("December 27, 2017");
        b1.setPageCount(416);
        b1.setWeight("1.5");
        b1.setWeightUom(Book.BookWeightUOM.POUNDS);
        b1.setSizeUom(Book.BookSizeUOM.INCHES);
        b1.setDimensions("7.4 x 0.9 x 9");
        b1.setIsbn("0134685997");
        b1.setLanguage("English");
        b1.setPrice(37.67);
        b1.setType(Book.BookMediaType.HARDCOVER);
        b1.setStatus(Book.BookStatus.AVAILABLE);
        b1.setCondition(Book.BookCondition.NEW);
        books.add(b1);

        Book b2 = new Book();
        b2.setBookId("2");
        b2.setTitle("Learn React Hooks");
        b2.setAuthor("Daniel Bugl");
        b2.setPublisher("Packt Publishing");
        b2.setPublicationDate("May 23, 2025");
        b2.setPageCount(372);
        b2.setWeight("1.41");
        b2.setWeightUom(Book.BookWeightUOM.POUNDS);
        b2.setSizeUom(Book.BookSizeUOM.INCHES);
        b2.setDimensions("7.5 x 0.84 x 9.25");
        b2.setIsbn("1836209177");
        b2.setLanguage("English");
        b2.setPrice(35.99);
        b2.setType(Book.BookMediaType.PAPERBACK);
        b2.setStatus(Book.BookStatus.AVAILABLE);
        b2.setCondition(Book.BookCondition.LIKE_NEW);
        books.add(b2);

        for (Book book : books) {
            bookTable.putItem(book);
        }
    }

    // 🔹 Fetch all books
    public List<Book> getAllBooks() {
        List<Book> list = new ArrayList<>();
        bookTable.scan().items().forEach(list::add);
        return list;
    }

    // 🔹 Helper to get book by ID
    private Book getBookById(String bookId) {
        Book key = new Book();
        key.setBookId(bookId);
        Book book = bookTable.getItem(key);
        if (book == null) throw new RuntimeException("Book not found with id: " + bookId);
        return book;
    }

    // 🔹 Checkout
    public Book checkoutBook(String bookId, String owner) {
        Book book = getBookById(bookId);
        if (book.getStatus() != Book.BookStatus.AVAILABLE)
            throw new RuntimeException("Book not available for checkout");

        book.setStatus(Book.BookStatus.CHECKED_OUT);
        book.setOwner(owner);
        bookTable.putItem(book);
        return book;
    }

    // 🔹 Buy
    public Book buyBook(String bookId, String buyer) {
        Book book = getBookById(bookId);
        if (book.getStatus() != Book.BookStatus.AVAILABLE)
            throw new RuntimeException("Book not available for purchase");

        book.setStatus(Book.BookStatus.SOLD);
        book.setOwner(buyer);
        bookTable.putItem(book);
        return book;
    }

    // 🔹 Return
    public Book returnBook(String bookId) {
        Book book = getBookById(bookId);
        book.setStatus(Book.BookStatus.AVAILABLE);
        book.setOwner(null);
        bookTable.putItem(book);
        return book;
    }

    // 🔹 Sell
    public Book sellBook(String bookId, String seller, double askingPrice) {
        Book book = getBookById(bookId);
        book.setStatus(Book.BookStatus.AVAILABLE);
        book.setOwner(null);
        book.setAskingPrice(askingPrice);
        bookTable.putItem(book);
        return book;
    }
}
