package devmatching2021.rotateMatrix;

import java.util.stream.*;
import java.util.*;
import java.util.function.*;

class Solution {
    public int[] solution(int rows, int columns, int[][] queries) {
        
        if(rows>50) return null;
        int[] answer = {};
        
        int[][] board = new int[rows][columns];
        
        //fill board
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
        for(int q=0, leng=queries.length; q<leng; ++q) {
        	answer[q] = round3(board, queries[q]);
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
    
  //처음엔 반복을 4번 돌릴까 했는 데 그와 방식이 유사하다 할 수 있다.
    int round3(int[][] board, int [] area) {
        int[] min = {Integer.MAX_VALUE};
        //실제 배열 인덱스는 0부터지만 입력으로 들어온 영역의 좌표는 1부터 시작한다.
        
        final int r1 = area[0]-1, r2 = area[2]-1;
        final int c1 = area[1]-1, c2 = area[3]-1;
        
        System.out.printf("%d, %d / %d, %d%n", r1, c1, r2, c2);
        
        //saved value for rounding
       
                
        //code for next Point
        int dir = 0;
        
        Predicate<int[]>[] endOfLine = new Predicate[4];
        endOfLine[0] = curPoint->curPoint[1]==c2;
        endOfLine[1] = curPoint->curPoint[0]==r2;
        endOfLine[2] = curPoint->curPoint[1]==c1;
        endOfLine[3] = curPoint->curPoint[0]==r1;;
        
        Consumer<int[]>[] move = new Consumer[4];        
        move[0] = curPoint->curPoint[1]++;
        move[1] = curPoint->curPoint[0]++;
        move[2] = curPoint->curPoint[1]--;
        move[3] = curPoint->curPoint[0]--;

        int[] point = {r1, c1};//start point
        int[] saved= {board[r1][c1]};
        
        //round logic
        //이 부분은 사실 함수형 인터페이스를 쓰지 않는 편이 적절하다 생각한다.
        //이동 부분이 많이 간략화 되어 숫자만 변동하면 되기도하고, min을 포인터처럼 쓰기 위해 배열로 만들지 않아도 된다.
        Consumer<int[]> swap = (_point) -> {
            int tmp = board[_point[0]][_point[1]];
            if(min[0]>tmp) min[0]=tmp;
            board[_point[0]][_point[1]] = saved[0];
            saved[0] = tmp;
        };
                
        while (dir<4) {
            move[dir].accept(point);
            swap.accept(point);
            if(endOfLine[dir].test(point)) ++dir;
        }
        
        return min[0];
    }
    
}