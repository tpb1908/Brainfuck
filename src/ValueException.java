/**
 * Created by theo on 21/08/16.
 */
public class ValueException extends Exception {
    final int value;
    final Exception exception;

    public ValueException(int value, Exception exception) {
        this.value = value;
        this.exception = exception;
    }


}
