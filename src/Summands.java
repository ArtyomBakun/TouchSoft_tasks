public class Summands {
	
	long[] maxProduct(long n){
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
		long rest = n;
		for(int j = 0; j < i; j++){
			rest -= numbers[j];
		}
		for(int j = 0; j < rest; j++){
			numbers[i - j%i - 1]++;
		}
		return numbers;
	}
	
	long[] maxPairProduct(long n){
		int next = (int) ((Math.sqrt(8*n + 1)-1)/2);
		long numbers[] = new long[next]; 
		for (int i = 0; i < next; i++) {
			numbers[i] = i + 1;
		}
		n -= next + next*(next - 1)/2;
		for(int i = 0; i < n; i++){
			numbers[next - i%next - 1]++;
		}
		return numbers;
		
	}
	
	public static void main(String[] args) {
		Summands sm = new Summands();
		long res[] = sm.maxPairProduct(49);//maxProduct(1_00_000_000_000_000L);
		for (int i = 0; i < res.length; i++) {
			System.out.printf("%3s",res[i]);
			if (i%15 == 14) System.out.println("");
		}
	}

}
