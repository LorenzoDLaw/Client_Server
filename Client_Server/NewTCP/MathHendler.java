//In this class the gooal is to hanlde basic math operation
//recived from a client through a TCP comunication with a server class
public class MathHendler{
        
        private char operator;
        private double value1;
        private double value2;

        private final MathRequestDTO request;

        public MathHendler(MathRequestDTO request){
            this.request = request;
        }

        public MathHendler(char operator, double value1, double value2) {
            this.operator = operator;
            this.value1 = value1;
            this.value2 = value2;
            this.request = null;
        }

        public String calc(/*char operator, double value1, double value2*/){
                double result = 0;
                String sResult = null;
                //Use a switch case for handle the operator
                switch(operator){
                        case '+' :
                            result = value1 + value2;
                            sResult = ""+result;
                            break;
                        case '-' :
                            result = value1 - value2;
                            sResult = ""+result;
                            break;
                        case '*' :
                            result = value1 * value2;
                            sResult = ""+result;
                            break;
                        case '/' :
                            if(value2 != 0){
                                result = value1 / value2;
                                sResult = ""+result;
                                break;
                            } else {
                                return ("ERROR: CANNOT DIVIDE ZERO");
                            }
                        default: 
                            sResult = "ERROR -> INVALID MATH OPERATOR";
                }
                return (sResult);
        }

    //Getter and setter
    public char getOperator() {
        return operator;
    }

    public void setOperator(char operator) {
        this.operator = operator;
    }

    public double getValue1() {
        return value1;
    }

    public void setValue1(double value1) {
        this.value1 = value1;
    }

    public double getValue2() {
        return value2;
    }

    public void setValue2(double value2) {
        this.value2 = value2;
    }

        
}