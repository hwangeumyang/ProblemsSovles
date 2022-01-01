package devmatching2021.rotateMatrix;

import java.util.stream.*;
import java.util.*;
import java.util.function.*;
public class Solution {
    public int[] solution(int rows, int columns, int[][] queries) {
        
        if(rows>50) return null;
        int[] answer = {};
        
        int[][] board = new int[rows][columns];
        
        for(int i=1, row=0; row<rows; row++)
            for(int col=0; col<columns; col++) {
                board[row][col] = i++;
            }
        
        //arrays check, + 함수형인터페이스 연습용
        Runnable printBoard = () ->
        Arrays.stream(board).forEach(
            (arr) -> {
                Arrays.stream(arr).forEach((d) -> {
                    System.out.printf("%3d", d);
                    
                });
                System.out.println();
            }
        );
        
        // for(int[] q : queries) {
        //     for(int d : q) System.out.print(d + " ");
        //     System.out.println();
        // }
        
        // Arrays.stream(queries).forEach((arr) -> round(board, arr));
        // round(board, t);
        
        answer = new int[queries.length];
        for(int q=0; q<queries.length; ++q) {
            answer[q] = round2(board, queries[q]);
        }
        printBoard.run();
        
        
        return answer;
    }

    //area => {r1, c1, r2, c2}
    //my first round
    //최소값을 찾는 부분을 구현하지 않았다. 다른 쪽에는 구현하기때문에 필요가 없을 뿐더러 이 쪽에서 하기엔 너무 하기싫은 작업이다.
    int round(int[][] board, int [] area) {
        int minimum = Integer.MAX_VALUE;
        //실제 배열 인덱스는 0부터지만 영역의 좌표는 1부터 시작한다.
        int r1 = area[0]-1, r2 = area[2]-1;
        int c1 = area[1]-1, c2 = area[3]-1;
        
        System.out.printf("%d, %d / %d, %d%n", r1, c1, r2, c2);
        
        //회전에 쓰이는 숫자 갯수, 큰 사각형 - 작은 사각형 = rows * cols - (rows-2) * (cols-2)
        int count = (r2-r1+1) * (c2-c1+1) - (r2-r1+1-2) * (c2-c1+1-2);
        //rounding direction
        
        System.out.println("count ; " + count);

        int prev=board[r1][c1];
        int now;
        int r=r1, c=c1+1;
        int rdir=1, cdir=1;
        //처음엔 count를 이용해서 하려 했지만 중복 코드를 한줄이라도 더 쓰기 싫어서 말았다.
        while (cdir>-2){
            for(; c1<= c&&c<=c2; c+=cdir) {
                System.out.printf("가로 %d, %d%n", r, c);
                now = board[r][c];
                board[r][c] = prev;
                prev = now;
            }
            c-=cdir; //범위 엇나간 부분 수정
            r+=rdir; //다음 칸 
            cdir-=2;
            for(; r1 <= r&&r<= r2; r+=rdir){
                System.out.printf("세로 %d, %d%n", r, c);
                now = board[r][c];
                board[r][c] = prev;
                prev = now;
            }
            r-=rdir; //범위 엇나간 부분 수정
            c+=cdir; //다음 칸 
            rdir-=2;
            
        }
        return minimum;        
    }
    
    //my sec round, convert code in loop to Functional 
    int round2(int[][] board, int [] area) {
        int[] min = {Integer.MAX_VALUE};
        //실제 배열 인덱스는 0부터지만 영역의 좌표는 1부터 시작한다.
        int r1 = area[0]-1, r2 = area[2]-1;
        int c1 = area[1]-1, c2 = area[3]-1;
        
        System.out.printf("%d, %d / %d, %d%n", r1, c1, r2, c2);
        
        //saved value for rounding
        int[] saved= {board[r1][c1]};
       
        
        //round content
        BiConsumer<Integer, Integer> round = (rr, cc) -> {
            int tmp = board[rr][cc];
            if(min[0]>tmp) min[0]=tmp;
            board[rr][cc] = saved[0];
            saved[0] = tmp;
        };
        
        int r=r1, c=c1+1;
        int rdir=1, cdir=1;
        while (cdir>-2){
            for(; c1<= c&&c<=c2; c+=cdir) {
                round.accept(r, c);
            }
            c-=cdir; //범위 엇나간 부분 수정
            r+=rdir; //다음 칸 
            cdir-=2;
            for(; r1 <= r&&r<= r2; r+=rdir){
                 round.accept(r, c);
            }
            r-=rdir; //범위 엇나간 부분 수정
            c+=cdir; //다음 칸 
            rdir-=2;
            
        }
        return min[0];
    }
    
    int round3(int[][] board, int [] area) {
        int[] min = {Integer.MAX_VALUE};
        //실제 배열 인덱스는 0부터지만 영역의 좌표는 1부터 시작한다.
        
        
        final int r1 = area[0]-1, r2 = area[2]-1;
        final int c1 = area[1]-1, c2 = area[3]-1;
        
        System.out.printf("%d, %d / %d, %d%n", r1, c1, r2, c2);
        
        //saved value for rounding
       
                
        //code for next Point
        int dir = 0;
        Predicate<Integer[]>[] endOfLine = {
            curPoint -> curPoint[0]==r2,
            curPoint -> curPoint[0]==c2,
            curPoint -> curPoint[0]==r1,
            curPoint -> curPoint[0]==c1
        };
        Consumer<Integer[]>[] move = {
            curPoint -> { curPoint[0]++; },
            curPoint -> { curPoint[1]++; },
            curPoint -> { curPoint[0]--; },
            curPoint -> { curPoint[1]--; }            
        };
        
                
        int[] point = {r1, c1};
        int[] saved= {board[r1][c1]};
        
        //round logic
        Consumer<int[]> round = (point) -> {
            int tmp = board[point[0]][point[1]];
            if(min[0]>tmp) min[0]=tmp;
            board[point[0]][point[1]] = saved[0];
            saved[0] = tmp;
        };
        

        while (dir<4) {
            if(endOfLine[dir].test(point)) ++dir;
            move[dir].accept(point);
            round(point);
        }
        
        return min[0];
    }
}