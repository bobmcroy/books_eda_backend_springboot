package books.eda.example.controller;

import books.eda.example.dto.request.BuyRequest;
import books.eda.example.dto.request.CheckoutRequest;
import books.eda.example.dto.request.ReturnRequest;
import books.eda.example.dto.request.SellRequest;
import books.eda.example.model.Book;
import books.eda.example.service.BookService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books") // all endpoints start with /books
public class BookController {

    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    // 🔹 Home
    @GetMapping("/")
    public String home() {
        return "Welcome to the Books EDA app!";
    }

    // 🔹 Fetch all books
    @GetMapping
    public Iterable<Book> getAllBooks() {
        return bookService.getAllBooks(); // Using Iterable<Book> to match service
    }

    // 🔹 Checkout a book
    @PostMapping("/checkout")
    public Book checkoutBook(@RequestBody CheckoutRequest request) {
        System.out.println("Checkout called: " + request);
        return bookService.checkoutBook(request.getBookId(), request.getOwner());
    }

    // 🔹 Buy a book
    @PostMapping("/buy")
    public Book buyBook(@RequestBody BuyRequest request) {
        System.out.println("Buy called: " + request);
        return bookService.buyBook(request.getBookId(), request.getBuyer(), request.getOfferedPrice());
    }

    // 🔹 Return a book
    @PostMapping("/return")
    public Book returnBook(@RequestBody ReturnRequest request) {
        System.out.println("Return called: " + request);
        return bookService.returnBook(request.getBookId(), request.getOwner());
    }

    // 🔹 Sell a book
    @PostMapping("/sell")
    public Book sellBook(@RequestBody SellRequest request) {
        System.out.println("Sell called: " + request);
        return bookService.sellBook(request.getBookId(), request.getSeller(), request.getAskingPrice());
    }
}
