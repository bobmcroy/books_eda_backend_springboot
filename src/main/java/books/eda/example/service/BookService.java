package books.eda.example.service;

import books.eda.example.model.Book;
import books.eda.example.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class BookService {

    @Autowired
    private BookRepository repository;

    // 🔹 Fetch all books as List<Book>
    public List<Book> getAllBooks() {
        return StreamSupport.stream(repository.findAll().spliterator(), false)
                            .collect(Collectors.toList());
    }

    // 🔹 Checkout a book
    public Book checkoutBook(String bookId, String owner) {
        Book book = repository.findById(bookId).orElseThrow(() -> 
            new RuntimeException("Book not found with id: " + bookId)
        );

        if (book.getStatus() != Book.BookStatus.AVAILABLE) {
            throw new RuntimeException("Book is not available for checkout");
        }

        book.setStatus(Book.BookStatus.CHECKED_OUT);
        book.setOwner(owner);
        return repository.save(book);
    }

    // 🔹 Buy a book
    public Book buyBook(String bookId, String buyer) {
        Book book = repository.findById(bookId).orElseThrow(() -> 
            new RuntimeException("Book not found with id: " + bookId)
        );

        if (book.getStatus() != Book.BookStatus.AVAILABLE) {
            throw new RuntimeException("Book is not available for purchase");
        }

        book.setStatus(Book.BookStatus.SOLD);
        book.setOwner(buyer);
        return repository.save(book);
    }

    // 🔹 Return a book
    public Book returnBook(String bookId) {
        Book book = repository.findById(bookId).orElseThrow(() -> 
            new RuntimeException("Book not found with id: " + bookId)
        );

        book.setStatus(Book.BookStatus.AVAILABLE);
        book.setOwner(null);
        return repository.save(book);
    }

    // 🔹 Sell a book
    public Book sellBook(String bookId, String seller, double askingPrice) {
        Book book = repository.findById(bookId).orElseThrow(() -> 
            new RuntimeException("Book not found with id: " + bookId)
        );

        // Set it back to available for sale
        book.setStatus(Book.BookStatus.AVAILABLE);
        book.setOwner(null);
        book.setAskingPrice(askingPrice);
        return repository.save(book);
    }
}
