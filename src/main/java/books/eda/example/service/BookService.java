package books.eda.example.service;

import books.eda.example.model.Book;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;
import software.amazon.awssdk.enhanced.dynamodb.model.ScanEnhancedRequest;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbClientBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class BookService {

    private final DynamoDbEnhancedClient enhancedClient;
    private final DynamoDbTable<Book> bookTable;

    public BookService(
        @Value("${DYNAMO_ENDPOINT:}") String endpoint,
        @Value("${app.dynamo.region:us-east-1}") String region,
        @Value("${app.dynamo.tableName:book_list}") String tableName
    ) {
        DynamoDbClientBuilder builder = DynamoDbClient.builder()
                .region(Region.of(region))
                .credentialsProvider(DefaultCredentialsProvider.create());

        if (endpoint != null && !endpoint.isBlank()) {
            builder.endpointOverride(URI.create(endpoint));
        }

        DynamoDbClient client = builder.build();

        this.enhancedClient = DynamoDbEnhancedClient.builder()
                .dynamoDbClient(client)
                .build();

        this.bookTable = enhancedClient.table(tableName, TableSchema.fromBean(Book.class));
    }

    // ---------- Queries ----------
    public List<Book> getAllBooks() {
        List<Book> list = new ArrayList<>();
        bookTable.scan().items().forEach(list::add);
        return list;
    }

    // Idempotent seed helper: true if table has no items
    public boolean tableIsEmpty() {
        PageIterable<Book> pages = bookTable.scan(
                ScanEnhancedRequest.builder().limit(1).build()
        );
        return pages.stream()
                .findFirst()
                .map(p -> !p.items().iterator().hasNext())
                .orElse(true);
    }

    // ---------- Mutations ----------
    // Save (existing method)
    public void saveBook(Book book) {
        bookTable.putItem(book);
    }

    // Upsert (used by seeding code)
    public void upsert(Book book) {
        saveBook(book);
    }

    // Convenience create, if you need it elsewhere
    public Book create(String title, String author) {
        Book book = new Book();
        book.setBookId(UUID.randomUUID().toString());
        book.setTitle(title);
        book.setAuthor(author);
        book.setStatus(Book.BookStatus.AVAILABLE);
        saveBook(book);
        return book;
    }

    // ---------- Helpers ----------

    private Book getBookById(String bookId) {
        Book key = new Book();
        key.setBookId(bookId);
        Book book = bookTable.getItem(key);
        if (book == null) throw new RuntimeException("Book not found with id: " + bookId);
        return book;
    }

    // ---------- Business Ops ----------

    public Book checkoutBook(String bookId, String owner) {
        System.out.println("CheckoutBook called for bookId=" + bookId + " owner=" + owner);
        Book book = getBookById(bookId);
        if (book.getStatus() != Book.BookStatus.AVAILABLE)
            throw new RuntimeException("Book not available for checkout");
        book.setStatus(Book.BookStatus.CHECKED_OUT);
        book.setOwner(owner);
        saveBook(book);
        return book;
    }

    public Book buyBook(String bookId, String buyer, Double offeredPrice) {
        System.out.println("buyBook called for bookId=" + bookId + " buyer=" + buyer + " offeredPrice=" + offeredPrice););
        Book book = getBookById(bookId);
        if (book.getStatus() != Book.BookStatus.AVAILABLE)
            throw new RuntimeException("Book not available for purchase");
        book.setStatus(Book.BookStatus.SOLD);
        book.setOwner(buyer);
        saveBook(book);
        return book;
    }

    public Book returnBook(String bookId) {
        System.out.println("returnBook called for bookId=" + bookId);
        Book book = getBookById(bookId);
        book.setStatus(Book.BookStatus.AVAILABLE);
        book.setOwner(null);
        saveBook(book);
        return book;
    }

    public Book sellBook(String bookId, String seller, double askingPrice) {
        System.out.println("sellBook called for bookId=" + bookId + " seller=" + seller + " askingPrice=" + askingPrice);
        Book book = getBookById(bookId);
        book.setStatus(Book.BookStatus.AVAILABLE);
        book.setOwner(null);
        book.setAskingPrice(askingPrice);
        saveBook(book);
        return book;
    }
}
