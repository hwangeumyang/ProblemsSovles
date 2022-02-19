package kakaoblind2022.getprimenumber;

import static java.lang.Math.sqrt;
import java.util.*;

class Solution {
	public int solution(int n, int k) {
        return solution2(n, k);        
    }
    public int solution1(int n, int k) {
        int answer = 0;
        
        String converted = convert(n, k);
        // System.out.println("converted: " + converted);
        String[] splited = converted.split("[0]+");
        
        
        for(String token: splited) {
            if(isPrime(Double.parseDouble(token))) answer++;
        }
        
        return answer;
    }
    //Hash 적용, 100만개 적용했을 때 1초? 정도 차이난거같다.
    public int solution2(int n, int k) {
        int answer = 0;
        
        String converted = convert(n, k);
        // System.out.println("converted: " + converted);
        String[] splited = converted.split("[0]+");
        
        HashMap<String, Boolean> primemap = new HashMap<>();
        
        
        for(String token: splited) {
            // System.out.println(token);
        	Boolean isPrime = primemap.get(token);
        	if(isPrime==null) {
        		isPrime = isPrime(Double.parseDouble(token));
        		primemap.put(token, isPrime);        		
        	}
        	if(isPrime) answer++;
        }
        
        return answer;
    }
    
    
    public String convert(int n, int k) {
        StringBuffer output = new StringBuffer();
                
        while(n>0) {
            output.append(n%k);
            n/=k;            
        }        
        return output.reverse().toString();        
    }
    boolean isPrime(double n){
        if(n<2) return false;
        boolean output = true;
        
        int lim = (int)sqrt(n);        
        for(int i=2; i<=lim; ++i)
            if(n%i==0) return false;          
        
        return output;
    }
    
    public static void main(String [] args) {
    	
    	Solution sol = new Solution();
    	
    	System.out.println(Integer.MAX_VALUE);
    	
    	for(int k=3; k<=3; k++) {
    		double t1 = System.nanoTime();
    		for(int n=1; n<=1000000; n++) {
    			System.out.println("n : " + n);
    			try {
    				sol.solution(n, k);
    			} catch(Exception e) {
    				System.err.printf("%d, %d\n", n, k);
    				e.printStackTrace();
    				System.exit(0);
    				
    			}
    		}
    		System.out.println(System.nanoTime() - t1);
    	}
    	
    			
    	
    	
    }
}