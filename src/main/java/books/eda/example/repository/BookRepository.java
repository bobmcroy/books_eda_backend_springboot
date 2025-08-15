package books.eda.example.repository;

import books.eda.example.model.Book;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import org.springframework.stereotype.Repository;

@Repository
public class BookRepository {

    private final DynamoDbTable<Book> bookTable;

    public BookRepository(DynamoDbEnhancedClient enhancedClient) {
        this.bookTable = enhancedClient.table("book_list", TableSchema.fromBean(Book.class));
    }

    public Book save(Book book) {
        bookTable.putItem(book);
        return book;
    }

    public Book findById(String id) {
        return bookTable.getItem(r -> r.key(k -> k.partitionValue(id)));
    }

    public Iterable<Book> findAll() {
        return bookTable.scan().items();
    }
}