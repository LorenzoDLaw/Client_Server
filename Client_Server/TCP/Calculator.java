
public class Calculator{
        private char operator;
        private double val1;
        private double val2;

        public void setOperator(char operator){
            this.operator = operator;
        }

        public void setVal1(double val1){
            this.val1= val1;
        }

        public void setVal2(double val2){
            this.val2 = val2;
        }

        public char getOperator(){
                return operator;
        }

        public double calc(char operator, double val1, double val2){
                this.operator = operator;
                this.val1 = val1;       
                this.val2 = val2;
                double result = 0;
                
                switch(operator){
                        case '+':
                                System.out.println(val1 + val2);
                                result = val1 + val2;
                                break;
                        case '-':
                                System.out.println(val1 - val2);
                                result = val1 - val2;
                                break;
                        case '*':
                                System.out.println(val1 * val2);
                                result = val1 * val2;
                                break;
                        case '/':
                                if(val2 != 0){
                                        System.out.println(val1 / val2);
                                        result = val1 / val2;
                                        
                                } else {
                                        System.out.println("Errore: Division per zero");
                                }
                                break;
                        default:
                                System.out.println("Errore operatore matematico non valido");
                                break;
                }

                return result;
        }

}