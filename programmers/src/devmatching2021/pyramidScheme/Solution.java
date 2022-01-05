package devmatching2021.pyramidScheme;

import java.util.function.*;
import java.util.stream.*;
import java.util.*;

// 일종의 역나무 형태가 아닐까. referral을 Person으로 만들고 Person내부에서 돌게 만들 수도 있었다.
// 오늘은 쉬고 내일 만들자.
class Solution {
    public int[] solution(String[] enroll, String[] referral, String[] seller, int[] amount) {
       
        return solution2(enroll, referral, seller, amount);
    }
    
    static class Person {
        String name;
        String referral;
        int earnings;
        public Person(String name, String referral) {
            this.name = name;
            this.referral = referral;
            this.earnings = 0;
        }
        public void earn(int money){
            earnings+=money;            
        }
        public String getRef() { return referral; }
        public int getEarnings() { return earnings; }
        
        public String toString() { return name + ": " + earnings; }
    }
    
    public void calc(Map<String, Person> map, Person p, int amount) {
        // String ref = p.getRef();
        //amount*0.9로 하면 오차가 생긴다.. *100이전의 원본값을 보내는 것도 고려해봤지만 한 번 이상 거치면 소수점으로 떨어지기 쉬워 오차리스크를 감수해야할 것 같으니 이렇게 한다.
        p.earn(amount - amount/10);
        Person ref = map.get(p.getRef());       
        
        if(ref == null) return;
        calc(map, ref, amount/10);
    }
    //기본형
    public int[] solution1(String[] enroll, String[] referral, String[] seller, int[] amount) {
        int[] answer = {};
        
//         Consumer<Object[]> c = (arr) -> {
//             Arrays.stream(arr).forEach((obj) -> {
//                System.out.print(obj + " "); 
//             });
//             System.out.println("");
//         };
        
//         c.accept(enroll);
//         c.accept(referral);
//         c.accept(seller);
//         c.accept(IntStream.of(amount).boxed().toArray(Integer[]::new));
        
        Map<String, Person> map = new LinkedHashMap<>();
        
        for(int i=0, leng=enroll.length; i<leng; ++i) {
            map.put(enroll[i], new Person(enroll[i], referral[i]));
        }
        
        for(int i=0, leng=seller.length; i<leng; ++i) {
            //map, person, amount
            calc(map, map.get(seller[i]), amount[i]*100);
        }   
        
        List<Integer> list = new ArrayList<>();
        map.forEach( (name, person) -> {
            list.add(person.getEarnings());
        } );
        
        answer = list.stream().mapToInt(Integer::intValue).toArray();
        
        
        return answer;
    }
    
    //조금 수정함, 큰 차이는 없음.
    //enroll의 순서와 같은 Person의 배열도 마련해 Map을 선택할 수 있게 하고, 마무리 작업 등을 배열로 작업함
    public int[] solution2(String[] enroll, String[] referral, String[] seller, int[] amount) {
        int enleng = enroll.length; 
        int[] answer = new int[enleng]; 
        
        Map<String, Person> map = new LinkedHashMap<>();
        Person[] persons = new Person[enleng];
        
        for(int i=0; i<enleng; ++i) {
            Person p = new Person(enroll[i], referral[i]);
            map.put(enroll[i], p);
            persons[i] = p;
        }
        
        for(int i=0, leng=seller.length; i<leng; ++i) {
            //map, person, amount
            calc(map, map.get(seller[i]), amount[i]*100);
        }
        
        for(int i=0; i<enleng; ++i) {
            answer[i] = persons[i].getEarnings();
        }
        
        return answer;
    }
    
    //person class for solution 3
    static class Person2 {
        String name;
        Person2 referral;
        int earnings;
        public Person2(String name, Person2 referral) {
            this.name = name;
            this.referral = referral;
            this.earnings = 0;
        }
        public void earn(int money){
        	int tax = money/10;
        	earnings+=money-tax;
        	referral.earn(tax);        	       
        }
        public String getRef() { return referral.name; }
        public int getEarnings() { return earnings; }
        public String toString() { return name + ": " + earnings; }
    }

    //no need more method
    public int[] solution3(String[] enroll, String[] referral, String[] seller, int[] amount) {
        int enleng = enroll.length; 
        int[] answer = new int[enleng]; 
        
        Map<String, Person2> map = new HashMap<>();
        Person2[] persons = new Person2[enleng];
        
        
        //처음엔 Person2.earn()에 if문을 달려했으나 이렇게 써도 재밌을것 같다.
        //상속구조도 생각하지 않은 것은 아니나 거창한 것 같다.
        map.put("-", new Person2("mock", null) {
        	@Override
        	public void earn(int money) {
        		 
        	}
        });
        //조직참여 순이라 referral은 항상 속해있다.
        for(int i=0; i<enleng; ++i) {
            Person2 p = new Person2(enroll[i], map.get(referral[i]));
            map.put(enroll[i], p);
            persons[i] = p;
        }
        
        for(int i=0, leng=seller.length; i<leng; ++i) {
            //map, person, amount
        	map.get(seller[i]).earn(amount[i]*100);
        }
        
        for(int i=0; i<enleng; ++i) {
            answer[i] = persons[i].getEarnings();
        }
        
        return answer;
    }
}