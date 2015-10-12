public class BestFitImpl implements BestFit{

	// Possible operations:
	// empty string means the merger of two digits in one number
	private static String operators[] = { "", "+", "-", "*", "/", "(", ")" };

	public String fitPlusMinus(String digits, long expectedResult) {
		return findSolution(digits, expectedResult, 3);
	};

	public String fit(String digits, long expectedResult) {
		return findSolution(digits, expectedResult, 5);
	};

	public String fitBraces(String digits, long expectedResult) {
		return digits;
	};

	public static void main(String[] args) {
		BestFitImpl bf = new BestFitImpl();
		System.out.println(bf.fitPlusMinus("123", 9));
		System.out.println(bf.fitPlusMinus("222", 11));
		System.out.println(bf.fit("222", 11));
		System.out.println(bf.fit("4515", 1));
	}
	
	private static String findSolution(String digits, long expectedResult, int dim){
		long i = 0;
		while(i < Math.pow(dim, digits.length()-1)){
			StringBuilder sb = new StringBuilder(),
					maskAtDimBaseWithZeros = new StringBuilder();
			maskAtDimBaseWithZeros.append(Long.toString(i, dim));
			while(maskAtDimBaseWithZeros.length() < digits.length()-1){//translate number at base "dim",
																			//such as 513 to 00000513
																			//where 0="", 1="+", 2="-", 3="*" etc.
																			//see codes at operators[]
				maskAtDimBaseWithZeros.insert(0, "0");
			}
			if(eval(constuctExpression(digits, maskAtDimBaseWithZeros.toString()))==expectedResult)
				return sb.toString();
			i++;
		};
		return "";
	}
	
	private static String constuctExpression(String digits, String mask){
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

	// extract expression from the string and evaluate it
	public static double eval(final String expr) {
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
				if (currentChar == '(') { // brackets
					eatChar();
					value = parseExpression();
					if (currentChar == ')')
						eatChar();
				} else { // numbers
					StringBuilder sb = new StringBuilder();
					while ((currentChar >= '0' && currentChar <= '9')) {
						sb.append((char) currentChar);
						eatChar();
					}
					if (sb.length() == 0)
						throw new RuntimeException("Unexpected: '" + (char) currentChar + "'");
					value = Integer.parseInt(sb.toString());
				}
				return value;
			}
		}
		return new Parser().parse();
	}

}
