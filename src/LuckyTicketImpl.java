import java.math.BigInteger;

public class LuckyTicketImpl implements LuckyTicket {
	
	public boolean isLucky(String number) {
		int //first, last, 
		res = 0, length = number.length()/2;
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
		long countLucky = 0;
		BigInteger minimum = new BigInteger(min);
		BigInteger maximum = new BigInteger(max);
		maximum = maximum.add(BigInteger.ONE);
		while( !minimum.equals(maximum) ){ // equals while(minimum <= maximum)
			if(isLucky(String.valueOf(minimum))){
				countLucky++;
			}
			minimum = minimum.add(BigInteger.ONE);
		}
		return countLucky;
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
