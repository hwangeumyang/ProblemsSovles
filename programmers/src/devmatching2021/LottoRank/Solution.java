package devmatching2021.LottoRank;

/**
 * 
 * @author GoldenSheep
 * https://programmers.co.kr/learn/courses/30/lessons/77484
 * (불확정수를 포함한 로또 번호 배열, 당첨 로또 번호 배열) -> solution { } -> { 최고가능순위, 최소순위 }
 */

import java.util.function.*;
import java.util.stream.*;
import java.util.*;



class Solution {
    public int[] solution(int[] lottos, int[] win_nums) {
        return solution3(lottos, win_nums);
    }
    
    //최소순위 7 - 확정개수, 최고 순위 7 - ( 7 - 확정개수) - 0개수 = 확정개수 - 0 개수
    //6위를 제외하면 7 - 맞춘 개수에 기인한 것.
    //결국 7위가 지저분해서 쓸 순 없어졌다.
    public int[] solution1(int[] lottos, int[] win_nums) {
        // int[] answer = {7, 7}; //maximum rank, minimum rank
        int[] answer = {0, 7}; //maximum rank, minimum rank
        
        for(int l : lottos) {
            if(l==0) {
                answer[0]--;
                continue;
            }
            else if(search(l, win_nums)) {
                answer[1]--;
                continue;
            }
        }
        
        // answer[0] -= 7 - answer[1];
        answer[0] += answer[1];
        
        for(int i=0; i<2; ++i) if(answer[i]>6) answer[i] = 6;
        
        
        return answer;
    }
    //simple, 개수, 등급 - 매핑
    public int[] solution2(int[] lottos, int[] win_nums) {
        int[] answer = {0, 0}; //maximum rank, minimum rank
        int[] rank = {6, 6, 5, 4, 3, 2, 1};
        
        //count balls, {unchecked numbers, hits!}
        for(int l : lottos) {
            if(l==0) {
                answer[0]++;
                continue;
            }
            else if(search(l, win_nums)) {
                answer[1]++;
                // continue;
            }
        }
        
        answer[0] += answer[1];
        
        //finding rank
        for(int i=0; i<2; ++i) answer[i] = rank[answer[i]];
        
        return answer;
    }
    
    //functional interface 연습
    public int[] solution3(int[] lottos, int[] win_nums) {
        int[] answer = {0, 0}; //maximum rank, minimum rank
        int[] rank = {6, 6, 5, 4, 3, 2, 1};
        
        
        // IntStream is = Arrays.stream(lottos);
        // 한번에 써서 할 수 없다. 소모돼서.
        // answer[0] = (int)Arrays.stream(lottos).filter(i -> i==0).count();
        // answer[1] = (int)Arrays.stream(lottos).filter(i -> search(i, win_nums)).count();
        // 중복이 심해서 폐기
        
        //count balls, {unchecked numbers, hits!}
        Arrays.stream(lottos).forEach( (i) -> {
            if(i==0) answer[0]++;
            else if(search(i, win_nums)) answer[1]++;
        });
        
        answer[0] += answer[1];
        
        //finding rank
        for(int i=0; i<2; ++i) answer[i] = rank[answer[i]];
        
        return answer;
    }
    
    public boolean search(int num, int[] arr) {
        for(int i : arr) if(i==num) return true;
        return false;
    }
}