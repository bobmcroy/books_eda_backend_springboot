package books.eda.example.config;

import books.eda.example.model.Book;
import books.eda.example.service.BookService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

//@Component
public class DataSeedInitializer implements CommandLineRunner {

    private final BookService bookService;

    public DataSeedInitializer(BookService bookService) {
        this.bookService = bookService;
    }

    @Override
    public void run(String... args) throws Exception {
        // Only seed if no books exist
        if (bookService.getAllBooks().isEmpty()) {
            // Create books with full details
            Book book1 = new Book();
            book1.setBookId("1");
            book1.setTitle("Effective Java Third Edition");
            book1.setAuthor("Joshua Bloch");
            book1.setPublisher("Addison-Wesley Professional");
            book1.setPublicationDate("December 27, 2017");
            book1.setPageCount(416);
            book1.setWeight("1.5");
            book1.setWeightUom(Book.BookWeightUOM.POUNDS);
            book1.setSizeUom(Book.BookSizeUOM.INCHES);
            book1.setDimensions("7.4 x 0.9 x 9");
            book1.setIsbn("0134685997");
            book1.setLanguage("English");
            book1.setPrice(37.67);
            book1.setOwner(null);
            book1.setType(Book.BookMediaType.HARDCOVER);
            book1.setStatus(Book.BookStatus.AVAILABLE);
            book1.setCondition(Book.BookCondition.NEW);
            book1.setAskingPrice(0.0);

            Book book2 = new Book();
            book2.setBookId("2");
            book2.setTitle("Learn React Hooks");
            book2.setAuthor("Daniel Bugl");
            book2.setPublisher("Packt Publishing");
            book2.setPublicationDate("May 23, 2025");
            book2.setPageCount(372);
            book2.setWeight("1.41");
            book2.setWeightUom(Book.BookWeightUOM.POUNDS);
            book2.setSizeUom(Book.BookSizeUOM.INCHES);
            book2.setDimensions("7.5 x 0.84 x 9.25");
            book2.setIsbn("1836209177");
            book2.setLanguage("English");
            book2.setPrice(35.99);
            book2.setOwner(null);
            book2.setType(Book.BookMediaType.PAPERBACK);
            book2.setStatus(Book.BookStatus.AVAILABLE);
            book2.setCondition(Book.BookCondition.LIKE_NEW);
            book2.setAskingPrice(0.0);

            Book book3 = new Book();
            book3.setBookId("3");
            book3.setTitle("JavaScript: The Definitive Guide 7th Edition");
            book3.setAuthor("David Flanagan");
            book3.setPublisher("O'Reilly Media");
            book3.setPublicationDate("May 14, 2020");
            book3.setPageCount(708);
            book3.setWeight("1.21");
            book3.setWeightUom(Book.BookWeightUOM.POUNDS);
            book3.setSizeUom(Book.BookSizeUOM.INCHES);
            book3.setDimensions("6.5 x 0.64 x 8.33");
            book3.setIsbn("1491951996");
            book3.setLanguage("English");
            book3.setPrice(32.58);
            book3.setOwner(null);
            book3.setType(Book.BookMediaType.HARDCOVER);
            book3.setStatus(Book.BookStatus.AVAILABLE);
            book3.setCondition(Book.BookCondition.NEW);
            book3.setAskingPrice(0.0);

            // Save all books to local DynamoDB
            bookService.sellBook(book1.getBookId(), null, book1.getAskingPrice());
            bookService.sellBook(book2.getBookId(), null, book2.getAskingPrice());
            bookService.sellBook(book3.getBookId(), null, book3.getAskingPrice());

            System.out.println("✅ Seeded local DynamoDB with initial books.");
        } else {
            System.out.println("ℹ️ Books already exist. Skipping seeding.");
        }
    }
}
