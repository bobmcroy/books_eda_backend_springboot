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
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class BookService {

    private final DynamoDbEnhancedClient enhancedClient;
    private final DynamoDbTable<Book> bookTable;
    private final SnsClient snsClient;

    private final String checkoutTopicArn;
    private final String buyTopicArn;
    private final String returnTopicArn;
    private final String sellTopicArn;
    private final String listTopicArn;

    // -----------------------------------------

    public BookService(
        @Value("${app.dynamo.primaryRegion}") String primaryRegion,
        @Value("${app.dynamo.secondaryRegion:}") String secondaryRegion,
        @Value("${app.dynamo.tableName}") String tableName,
        @Value("${DYNAMO_ENDPOINT:}") String dynamoEndpoint,
        @Value("${app.sns.checkoutTopicArn}") String checkoutTopicArn,
        @Value("${app.sns.buyTopicArn}") String buyTopicArn,
        @Value("${app.sns.returnTopicArn}") String returnTopicArn,
        @Value("${app.sns.sellTopicArn}") String sellTopicArn,
        @Value("${app.sns.listTopicArn}") String listTopicArn
    ) {
        DynamoDbClient client;

        try {
            client = createClient(primaryRegion, dynamoEndpoint);
            client.listTables(); // optional: only if you want connectivity check
        } catch (Exception e) {
            if (secondaryRegion != null && !secondaryRegion.isBlank()) {
                client = createClient(secondaryRegion, dynamoEndpoint);
                // optional: client.listTables();
            } else {
                throw new RuntimeException("Failed to connect to primary DynamoDB region and no secondary defined", e);
            }
        }

        this.enhancedClient = DynamoDbEnhancedClient.builder()
                .dynamoDbClient(client)
                .build();

        this.bookTable = enhancedClient.table(tableName, TableSchema.fromBean(Book.class));

        // SNS always uses primary region
        // SNS client always points to primary region
        this.snsClient = SnsClient.builder()
                .region(Region.of(primaryRegion))
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();

        this.checkoutTopicArn = checkoutTopicArn;
        this.buyTopicArn = buyTopicArn;
        this.returnTopicArn = returnTopicArn;
        this.sellTopicArn = sellTopicArn;
        this.listTopicArn = listTopicArn;
    }

    // Helper method for building client with optional endpoint
    private DynamoDbClient createClient(String region, String endpoint) {
        DynamoDbClientBuilder builder = DynamoDbClient.builder()
                .credentialsProvider(DefaultCredentialsProvider.create());

        if (endpoint != null && !endpoint.isBlank()) {
            builder.endpointOverride(URI.create(endpoint));
        } else {
            builder.region(Region.of(region));
        }

        return builder.build();
    }

    // ---------- Queries ----------
    public List<Book> getAllBooks() {
        List<Book> list = new ArrayList<>();
        bookTable.scan().items().forEach(list::add);
        publishEvent(listTopicArn, "LIST", null, null, null);
        return list;
    }

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
    public void saveBook(Book book) {
        bookTable.putItem(book);
    }

    public void upsert(Book book) {
        saveBook(book);
    }

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

    private void publishEvent(String topicArn, String eventType, Book book, String actor, Double offeredPrice) {
        String message = String.format(
                "{\"event\":\"%s\",\"bookId\":\"%s\",\"title\":\"%s\",\"actor\":\"%s\",\"offeredPrice\":%s}",
                eventType,
                book != null ? book.getBookId() : "",
                book != null ? book.getTitle() : "",
                actor != null ? actor : "",
                offeredPrice != null ? offeredPrice.toString() : "null"
        );
        snsClient.publish(PublishRequest.builder()
                .topicArn(topicArn)
                .message(message)
                .build());
    }

    // ---------- Business Ops ----------
    public Book checkoutBook(String bookId, String owner) {
        Book book = getBookById(bookId);
        if (book.getStatus() != Book.BookStatus.AVAILABLE)
            throw new RuntimeException("Book not available for checkout");
        book.setStatus(Book.BookStatus.CHECKED_OUT);
        book.setOwner(owner);
        saveBook(book);

        publishEvent(checkoutTopicArn, "CHECKOUT", book, owner, null);
        return book;
    }

    public Book buyBook(String bookId, String buyer, Double offeredPrice) {
        Book book = getBookById(bookId);
        if (book.getStatus() != Book.BookStatus.AVAILABLE)
            throw new RuntimeException("Book not available for purchase");
        book.setStatus(Book.BookStatus.SOLD);
        book.setOwner(buyer);
        saveBook(book);

        publishEvent(buyTopicArn, "BUY", book, buyer, offeredPrice);
        return book;
    }

    public Book returnBook(String bookId, String owner) {
        Book book = getBookById(bookId);
        book.setStatus(Book.BookStatus.AVAILABLE);
        book.setOwner(null);
        saveBook(book);

        publishEvent(returnTopicArn, "RETURN", book, owner, null);
        return book;
    }

    public Book sellBook(String bookId, String seller, double askingPrice) {
        Book book = getBookById(bookId);
        book.setStatus(Book.BookStatus.AVAILABLE);
        book.setOwner(null);
        book.setAskingPrice(askingPrice);
        saveBook(book);

        publishEvent(sellTopicArn, "SELL", book, seller, askingPrice);
        return book;
    }
}
