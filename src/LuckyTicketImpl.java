import java.math.BigInteger;

public class LuckyTicketImpl implements LuckyTicket {
	
	public boolean isLucky(String number) {
		int res = 0, length = number.length()/2;
		char digits[] = number.toCharArray();
		for (int i = 0; i < length; i++) {
			res += Integer.parseInt(String.valueOf(digits[i]));
			res -= Integer.parseInt(String.valueOf(digits[i + length]));
		}
		return res == 0;
	}
	
	public long countLucky(long min, long max){
		long countLucky = 0;
		while(min <= max){//brute force
			if(isLucky(String.valueOf(min))){
				countLucky++;
			}
			min++;
		}
		return countLucky;
	}
	
	public long countLucky(String min, String max){
		long lucky = 0;
		int numberExtraProcessors = 3;// 4 cores of CPU on my computer
		final BigInteger minimum = new BigInteger(min);
		final BigInteger maximum = new BigInteger(max);
		BigInteger interval = maximum.subtract(minimum).divide(BigInteger.valueOf(numberExtraProcessors));
		//execute brute force parallel
		class Counter extends Thread{
			private long countLucky = 0;
			private int threadNumber;
			private BigInteger localMinimum, localMaximum;
			Counter(int number){
				threadNumber= number;
				localMinimum = minimum.add(interval.multiply(BigInteger.valueOf(threadNumber)));
				localMaximum = (threadNumber+1) == numberExtraProcessors ? maximum.add(BigInteger.ONE) 
							: localMinimum.add(interval.add(BigInteger.ONE));//include right border of interval
			}

			@Override
			public void run() {
				while( !localMinimum.equals(localMaximum) ){ // i.e. while(localMinimum <= localMaximum)
					if(isLucky(String.valueOf(localMinimum))){
						countLucky++;
					}
					localMinimum = localMinimum.add(BigInteger.ONE);
				}
			}
			
			public long getCountLucky(){
				return countLucky;
			}
			
		}
		Counter threads[] = new Counter[numberExtraProcessors];
		for(int i = 0; i < numberExtraProcessors; i++){
			threads[i] = new Counter(i);
			if((i + 1) < numberExtraProcessors){
				threads[i].start();
			} else {//calculate last interval in main thread
				threads[i].run();
			}
		}
		for(int i = 0; i < numberExtraProcessors; i++){
			try {
				threads[i].join();
			} catch (InterruptedException e) {
				System.err.println("Calculating was interrupted!");
				e.printStackTrace();
			}
		}
		for(int i = 0; i < numberExtraProcessors; i++){
			lucky += threads[i].getCountLucky();
		}
		return lucky;
	}
	
	public static void main(String[] args) {
		LuckyTicket ln = new LuckyTicketImpl();
		System.out.println(ln.isLucky("123600"));
		System.out.println(ln.isLucky("123456"));
		System.out.println(ln.countLucky(123456123456L, 123456123465L));
		System.out.println(ln.countLucky("123456000006", "123456123465"));
		System.out.println(ln.countLucky("123450000006", "123456123465"));
	}
	
}
