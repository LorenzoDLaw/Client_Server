import java.io.Serializable;

/**
 * DTO that carries a math operation request from the client to the server.
 * Implements Serializable so it can be sent over an ObjectOutputStream.
 */
public class MathRequestDTO implements Serializable {

    // Required for safe serialization across JVM versions
    private static final long serialVersionUID = 1L;

    private final double value1;
    private final double value2;
    private final char operator;

    public MathRequestDTO(double value1, char operator, double value2) {
        this.value1   = value1;
        this.operator = operator;
        this.value2   = value2;
    }

    // --- Getters ---

    public double getValue1()   { return value1;   }
    public double getValue2()   { return value2;   }
    public char   getOperator() { return operator; }

    @Override
    public String toString() {
        return value1 + " " + operator + " " + value2;
    }
}