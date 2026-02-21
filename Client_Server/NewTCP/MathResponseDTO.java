import java.io.Serializable;

/**
 * DTO that carries the server's response (result or error) back to the client.
 * Implements Serializable so it can be sent over an ObjectOutputStream.
 */
public class MathResponseDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String message;   // human-readable result or error text
    private final boolean success;  // true = valid result, false = error

    // --- Factory helpers keep construction intention clear ---

    public static MathResponseDTO right(double result) {
        return new MathResponseDTO(String.valueOf(result), true);
    }

    //Use this for handle error in MathHandler
    public static MathResponseDTO error(String errorMessage) {
        return new MathResponseDTO(errorMessage, false);
    }

    private MathResponseDTO(String message, boolean success) {
        this.message = message;
        this.success = success;
    }

    // --- Getters ---

    public String  getMessage() { return message; }
    public boolean isSuccess()  { return success; }

    @Override
    public String toString() {
        return (success ? "RESULT: " : "ERROR:  ") + message;
    }
}