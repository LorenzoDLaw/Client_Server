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

        public MathResponseDTO calc(){
            //Use a switch case for handle the operator
            char operator = request.getOperator();
            double value1 = request.getValue1();
            double value2 = request.getValue2();
            
            switch(operator){

                case '+': return MathResponseDTO.right(value1 + value2);
                case '-': return MathResponseDTO.right(value1 - value2);
                case '*': return MathResponseDTO.right(value1 * value2);
                case '/':
                    if (value2 != 0) return MathResponseDTO.right(value1 / value2);
                    else             return MathResponseDTO.error("Cannot divide by zero");
                default:
                    return MathResponseDTO.error("Invalid operator: '" + operator + "'");
                }

        }
        
}