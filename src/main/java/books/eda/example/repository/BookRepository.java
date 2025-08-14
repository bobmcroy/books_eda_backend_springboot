package books.eda.example.repository;

import org.springframework.data.repository.CrudRepository;
import books.eda.example.model.Book;
import java.util.List;

public interface BookRepository extends CrudRepository<Book, String> {
    // Custom query by book title (optional)
    List<Book> findByTitle(String title);
}
