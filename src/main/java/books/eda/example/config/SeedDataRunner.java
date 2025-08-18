package books.eda.example.config;

import books.eda.example.model.Book;
import books.eda.example.service.BookService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Component
@Profile({"dev","test"}) // never runs in prod
public class SeedDataRunner implements ApplicationRunner {

    private final BookService service;
    private final boolean seedEnabled;

    public SeedDataRunner(BookService service,
                          @Value("${app.seed:false}") boolean seedEnabled) {
        this.service = service;
        this.seedEnabled = seedEnabled;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (!seedEnabled) return;
        if (!service.tableIsEmpty()) return;

        List<Book> seeds = List.of(
                make("The Pragmatic Programmer", "Andrew Hunt, David Thomas"),
                make("Clean Code", "Robert C. Martin"),
                make("Designing Data-Intensive Applications", "Martin Kleppmann")
        );

        seeds.forEach(b -> service.upsert(b));
        System.out.println("[seed] Inserted " + seeds.size() + " books.");
    }

    private static Book make(String title, String author) {
        Book b = new Book();
        //b.setBookId(UUID.randomUUID().toString());
        b.setBookId("1");
        b.setTitle("Effective Java Third Edition");
        b.setAuthor("Joshua Bloch");
        b.setPublisher("Addison-Wesley Professional");
        b.setPublicationDate("December 27, 2017");
        b.setPageCount(416);
        b.setWeight("1.5");
        b.setWeightUom(Book.BookWeightUOM.POUNDS);
        b.setSizeUom(Book.BookSizeUOM.INCHES);
        b.setDimensions("7.4 x 0.9 x 9");
        b.setIsbn("0134685997");
        b.setLanguage("English");
        b.setPrice(37.67);
        b.setOwner(null);
        b.setType(Book.BookMediaType.HARDCOVER);
        b.setStatus(Book.BookStatus.AVAILABLE);
        b.setCondition(Book.BookCondition.NEW);
        b.setAskingPrice(0.0);
        return b;
    }
}
