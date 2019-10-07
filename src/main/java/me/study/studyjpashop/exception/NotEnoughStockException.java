package me.study.studyjpashop.exception;

/**
 * Created by dongchul on 2019-10-07.
 */
public class NotEnoughStockException extends RuntimeException {
    public NotEnoughStockException() {
        super();
    }

    public NotEnoughStockException(String message) {
        super(message);
    }

    public NotEnoughStockException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotEnoughStockException(Throwable cause) {
        super(cause);
    }

}
