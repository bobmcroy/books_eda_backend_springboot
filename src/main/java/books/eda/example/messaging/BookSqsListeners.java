package books.eda.example.messaging;

import books.eda.example.dto.BookEvent;
import books.eda.example.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "app.sqs.enabled", havingValue = "true")
public class BookSqsListeners {

  private final BookService bookService;
  private final ObjectMapper mapper = new ObjectMapper();

  public BookSqsListeners(BookService bookService) {
    this.bookService = bookService;
  }

  @SqsListener("${app.sqs.checkoutQueueUrl}")
  public void handleCheckout(String message) throws Exception {
    if (bookService.isDirectDynamoUpdates()) return;  // extra safety
    BookEvent e = mapper.readValue(message, BookEvent.class);
    bookService.applyCheckoutEvent(e);
  }

  @SqsListener("${app.sqs.buyQueueUrl}")
  public void handleBuy(String message) throws Exception {
    if (bookService.isDirectDynamoUpdates()) return;
    BookEvent e = mapper.readValue(message, BookEvent.class);
    bookService.applyBuyEvent(e);
  }

  @SqsListener("${app.sqs.returnQueueUrl}")
  public void handleReturn(String message) throws Exception {
    if (bookService.isDirectDynamoUpdates()) return;
    BookEvent e = mapper.readValue(message, BookEvent.class);
    bookService.applyReturnEvent(e);
  }

  @SqsListener("${app.sqs.sellQueueUrl}")
  public void handleSell(String message) throws Exception {
    if (bookService.isDirectDynamoUpdates()) return;
    BookEvent e = mapper.readValue(message, BookEvent.class);
    bookService.applySellEvent(e);
  }
}
