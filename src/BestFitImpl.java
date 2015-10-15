import java.util.ArrayList;

public class BestFitImpl implements BestFit{

	// Possible operations:
	// empty string means the merge two digits in one number (4""2 == 42)
	private static String operators[] = { "", "+", "-", "*", "/", "(", ")" };

	public String fitPlusMinus(String digits, long expectedResult) {
		return findSolution(digits, expectedResult, 3);
	};

	public String fit(String digits, long expectedResult) {
		return findSolution(digits, expectedResult, 5);
	};

	public String fitBraces(String digits, long expectedResult) {
		long i = 0;
		int dim = 5;//dimension of task: use all 5 operators for construction of expressions
		String resultExpression;
		while(i < Math.pow(dim, digits.length()-1)){
			StringBuilder maskAtDimBaseWithZeros = new StringBuilder();
			maskAtDimBaseWithZeros.append(Long.toString(i, dim));
			while(maskAtDimBaseWithZeros.length() < digits.length()-1){//translate number at base "dim",
																			//such as 513 to 00000513
																			//where 0="", 1="+", 2="-", 3="*" etc.
																			//see codes at operators[]
				maskAtDimBaseWithZeros.insert(0, "0");
			}
			resultExpression = placeBrackets(constructExpression(digits, maskAtDimBaseWithZeros.toString()), expectedResult);
			if(!"".equals(resultExpression)){
				return resultExpression;
			}
			i++;
		};
		return "";
	};

	public static void main(String[] args) {
		BestFitImpl bf = new BestFitImpl();
		System.out.println(bf.fitPlusMinus("123", 9));
		System.out.println(bf.fitPlusMinus("222", 11));
		System.out.println(bf.fit("222", 11));
		System.out.println(bf.fit("4515", 1));
		System.out.println(bf.fitBraces("452223", 90));
	}
	
	
	//dim is dimension of task: use 3 or 5 operators for construction of expressions
	private String findSolution(String digits, long expectedResult, int dim){
		long i = 0;
		String resultExpression;
		while(i < Math.pow(dim, digits.length()-1)){
			StringBuilder maskAtDimBaseWithZeros = new StringBuilder();
			maskAtDimBaseWithZeros.append(Long.toString(i, dim));
			while(maskAtDimBaseWithZeros.length() < digits.length()-1){//translate number at base "dim",
																			//such as 513 to 00000513
																			//where 0="", 1="+", 2="-", 3="*" etc.
																			//see codes at operators[]
				maskAtDimBaseWithZeros.insert(0, "0");
			}
			resultExpression = constructExpression(digits, maskAtDimBaseWithZeros.toString());
			if(eval(resultExpression)==expectedResult)
				return resultExpression;
			i++;
		};
		return "";
	}
	
	//parse expression to array of numbers and to array of operators
	private void splitNumbersAndOperations( String expression, 
			ArrayList<String> numbers, ArrayList<String> operations){
		char chars[] = expression.toCharArray();
		for (int i = 0; i < chars.length; ) {
			StringBuilder sb = new StringBuilder();
			if(chars[i] == '('){
				while(chars[i] != ')'){//extract numbers like (-6.0) from expression like 2+(-6.0)*4-4/2.0
					sb.append(chars[i++]);
				}
				sb.append(chars[i++]);
				numbers.add(sb.toString());
			} else if(chars[i] >= '0' && chars[i] <= '9' || chars[i] == '.'){//extract numbers
				while(i < chars.length && (chars[i] >= '0' && chars[i] <= '9'  || chars[i] == '.')){
					sb.append(chars[i++]);
				}
				numbers.add(sb.toString());
			} else {//extract operators
				sb.append(chars[i++]);
				operations.add(sb.toString());
			}
		}
	}
	
	private String placeBrackets(String expression, long expectedResult){
		if(eval(expression) == expectedResult){
			return expression;
		}
		ArrayList<String> numbers = new ArrayList<String>(), 
				operations = new ArrayList<String>();
		splitNumbersAndOperations(expression, numbers, operations);
		int size = operations.size();
		for(int i = 0; i < size; i++){
			StringBuilder expressionWithBrackets = new StringBuilder(), 
					beforeBrackets = new StringBuilder(), 
					insideBrackets = new StringBuilder(), 
					afterBrackets = new StringBuilder();
			for(int j = 0, k = 0; k < size;){//place brackets around operations[i]
				if(k < i){
					beforeBrackets.append(numbers.get(j++) + operations.get(k++));
				} else if(k == i){
					insideBrackets.append("(" + numbers.get(j++) + operations.get(k++) + numbers.get(j++) + ")");
				} else{
					afterBrackets.append(operations.get(k++) + numbers.get(j++));
				}
			}
			expressionWithBrackets.append(beforeBrackets);
			expressionWithBrackets.append(insideBrackets);
			expressionWithBrackets.append(afterBrackets);
			if(eval(expressionWithBrackets.toString()) == expectedResult){
				return expressionWithBrackets.toString();
			} else if(size > 1){// i.e. expression was number+operator+number
				double valueInsideBrackets = eval(insideBrackets.toString());//replace expression in brackets to number
				String nextBrackets = placeBrackets(
						(beforeBrackets.toString() + "(" + valueInsideBrackets + ")" + afterBrackets) , expectedResult);
				if(!nextBrackets.isEmpty()){
					ArrayList<String> finishNumbers = new ArrayList<String>(), 
							finishOperations = new ArrayList<String>();
					splitNumbersAndOperations(nextBrackets, finishNumbers, finishOperations);
					finishNumbers.set(i, insideBrackets.toString());//reverse replace number to expression in brackets
					StringBuilder finishExpression = new StringBuilder(finishNumbers.get(0));
					for(int j = 0; j < finishOperations.size(); j++){
						finishExpression.append(finishOperations.get(j) + finishNumbers.get(j + 1));
					}
					return finishExpression.toString();
				}
			} else{
				return "";
			}
		}
		return "";
	} 
		
	private String constructExpression(String digits, String mask){
		int j = 0;
		StringBuilder sb = new StringBuilder();
		for(char c : mask.toCharArray()){//arrange operators: "123456" with 04231 mask 
																		//form a "12/3-4*5+6" string
			sb.append(digits.charAt(j) + operators[Integer.parseInt(String.valueOf(c))]);
			j++;
		}
		sb.append(digits.substring(j));
		return sb.toString();
	}
	
	//evaluate expression

	// extract expression from the string and evaluate it
	private double eval(String expr) {
		class Parser {
			int pos = -1, currentChar;

			// move to next character
			void eatChar() {
				currentChar = (++pos < expr.length()) ? expr.charAt(pos) : -1;
			}

			double parse() {
				eatChar();
				double value = parseExpression();
				if (currentChar != -1)
					throw new RuntimeException("Unexpected: '" + (char) currentChar + "'");
				return value;
			}

			//parse and calculating of expression
			double parseExpression() {
				double value = parseTerm();// move to the calculation of
											// multiplication/division,
											// as they have a higher priority
				for (;;) {
					if (currentChar == '+') { // addition
						eatChar();
						value += parseTerm();
					} else if (currentChar == '-') { // subtraction
						eatChar();
						value -= parseTerm();
					} else {
						return value;
					}
				}
			}

			double parseTerm() {
				double value = parseFactor();// move to the calculation of
												// expression in brackets,
												// as they have a highest
												// priority
				for (;;) {
					if (currentChar == '/') { // division
						eatChar();
						value /= parseFactor();
					} else if (currentChar == '*' || currentChar == '(') { // multiplication
						if (currentChar == '*')
							eatChar();
						value *= parseFactor();
					} else {
						return value;
					}
				}
			}

			double parseFactor() {
				double value;
				 boolean negate = false;
		            if (currentChar == '+' || currentChar == '-') { // unary plus & minus
		                negate = (currentChar == '-');
		                eatChar();
		            }
				if (currentChar == '(') { // brackets
					eatChar();
					value = parseExpression();
					if (currentChar == ')')
						eatChar();
				} else { // numbers
					StringBuilder sb = new StringBuilder();
					while ((currentChar >= '0' && currentChar <= '9' || currentChar == '.')) {
						sb.append((char) currentChar);
						eatChar();
					}
					if (sb.length() == 0)
						throw new RuntimeException("Unexpected: '" + (char) currentChar + "' at " + expr);
					value = Double.parseDouble(sb.toString());
				}
				if (negate){
					value = -value;
				}
				return value;
			}
		}
		return new Parser().parse();
	}

}
