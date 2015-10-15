public class SummandsImpl implements Summands {
	
	public long[] maxProduct(long n){
		long numbers[];
		double relation = 1;
		int i = 2;
		while(true){//the numbers should be as close as possible, 
					//so you have to share the initial number of equal parts
			double central = n/(double)i;
			relation = Math.pow(i/(i-1), (i-1))*i/n;//relation product all central of previous step
														//to product all centrals of current step
			/*#i
			 *  x*x*x*x*x       where x=n/(i-1)   -central of previous iteration (¹i-1)
			 *  ___________  == relation == (x/y)^(i-1) / y == (i/(i-1))^(i-1) / (n/i)
			 *  y*y*y*y*y*y     where y=n/i   -central of current iteration (¹i)
			 * */
			if(relation < 1.0 && central >= i){
				i++;
			} else {
				i--;
				break;
			}
		}
		numbers = new long[i];
		long center = n/i;
		long min = center - (i+1)/2;
		for(int j = 0; j < i; j++){
			numbers[j] = min + j;
		}
		long rest = n - (min*i + i*(i - 1)/2);
		for(int j = 0; j < rest; j++){
			numbers[i - j%i - 1]++;
		}
		return numbers;
	}

	@Override
	public long[][] allMaxProduct(long n) {
//		One consequence of Quadratic sieve algorithm is reduced to that a constant amount of a limited set of numbers 
//		will give the maximum product when these numbers are equal. Accordingly, each of these numbers is sum / n. 
//		Prohibition of identical values imposes a restriction n <(sum / n) / 2 (otherwise it will be impossible to spread 
//		the n integers in the interval (0, ..., n)). Thus, the function s (n) = (c / n) ^ n (where c - sum of all numbers, 
//		and n - number of them) has a single maximum. At the same time n = floor (s ^ -1 (s_max)).
//		Algorithm of the method maxProduct() constructing an array with a maximum product of all possible array of specified length.
//		Thus, for any number exists two arrays of any length in which the product of all the elements is maximum 
//		and equal to each other. So:
		long res[][] = {maxProduct(n)};
		return res;
	}
	
	public long[] maxPairProduct(long n){
		int count = (int) ((Math.sqrt(8*n + 1)-1)/2);//count of elements (== count) of 
														//arithmetic progression depend of it sum (== n)
		long numbers[] = new long[count]; 
		for (int i = 0; i < count; i++) {
			numbers[i] = i + 1;
		}
		long rest = n - (count + count*(count - 1)/2);
		for(int i = 0; i < rest; i++){
			numbers[count - i - 1]++;
		}
		return numbers;
		
	}
	
	public static void main(String[] args) {
		Summands sm = new SummandsImpl();
		long res[] = //sm.maxPairProduct(49);
		sm.maxProduct(19);
		for (int i = 0; i < res.length; i++) {
			System.out.printf("%3s",res[i]);
			if (i%15 == 14) System.out.println("");
		}
	}

}
