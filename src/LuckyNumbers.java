import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;

public class LuckyNumbers {
	
	boolean isLucky(String number) {
		int //first, last, 
		res = 0, length = number.length()/2;
		char digits[] = number.toCharArray();
		for (int i = 0; i < length; i++) {
			res += Integer.parseInt(String.valueOf(digits[i]));
			res -= Integer.parseInt(String.valueOf(digits[i + length]));
		}
		//first = sumDigits(number.substring(0, number.length()/2));
		//last = sumDigits(number.substring(number.length()/2));
		return res == 0;
	}
	
	long countLucky(long min, long max){
		long countLucky = 0;
		while(min <= max){//brute force
			if(isLucky(String.valueOf(min))){
				countLucky++;
			}
			min++;
		}
		return countLucky;
	}
	
	long countLucky(String min, String max){//too slow :(
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
		LuckyNumbers ln = new LuckyNumbers();
		System.out.println(ln.isLucky("123600"));
		System.out.println(ln.isLucky("123456"));
		//System.out.println(ln.countLucky(123456123456L, 123456123465L));
		//System.out.println(ln.countLucky("123456000006", "123456123465"));
		//System.out.println(ln.countLucky("123450000006", "123456123465"));
	}
	
	private static int sumDigits(String number){
		int res = 0;
		for(char c : number.toCharArray()){
			res += Integer.parseInt(String.valueOf(c));
		}
		return res;
	}
	
	ArrayList<ArrayList<String>> initArray(int dim){
		ArrayList<File> files = new ArrayList<File>();
		ArrayList<BufferedWriter> writers = new ArrayList<BufferedWriter>();
		ArrayList<BufferedReader> readers = new ArrayList<BufferedReader>();
		ArrayList<ArrayList<String>> possibleSums = new ArrayList<ArrayList<String>>();
		for (int i = 0; i < 9*dim+1; i++) {
			File file = new File(i + ".tmp");
			files.add(file);
			try {
				writers.add(new BufferedWriter(new FileWriter(file)));
			} catch (IOException e) {
				System.err.println("Can't open file " + file.getName());
				e.printStackTrace();
			}
		}
		for(long i = 0; i < Math.pow(10, dim); i++){
			StringBuilder sb = new StringBuilder(String.valueOf(i));
			while(sb.length() < dim){
				sb.insert(0, "0");
			}
			possibleSums.get(sumDigits(sb.toString())).add(sb.toString());
		}
		return possibleSums;
	}

}
