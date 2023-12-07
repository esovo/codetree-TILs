import java.util.*;
import java.io.*;

public class Main {
    
    static class Rabbit implements Comparable<Rabbit>{
        int x, y, pid, d, jump;
        int score;

        Rabbit(int x, int y, int pid, int d, int jump, int score){
            this.x = x;
            this.y = y;
            this.pid = pid;
            this.d = d;
            this.jump = jump;
            this.score = score;
        }

        /*
            우선 순위 결정 방법
            1. 총 점프 횟수 적은 순
            2. 현재 행+열 작은 순
            3. 행 작은 순
            4. 열 작은 순
            5. 고유번호 작은 순
        */
        public int compareTo(Rabbit r){
            if(this.jump == r.jump){
                if((this.x+this.y) == (r.x+r.y)){
                    if(this.x == r.x){
                        if(this.y == r.y) return this.pid-r.pid;
                        return this.y-r.y;
                    }
                    return this.x-r.x;
                }
                return (this.x+this.y)-(r.x+r.y);
            }
            return this.jump-r.jump;
        }
    }

    static class Loc implements Comparable<Loc>{
        int x, y;

        Loc(int x, int y){
            this.x = x;
            this.y = y;
        }

        public int compareTo(Loc l){
            if((this.x+this.y) == (l.x+l.y)){
                if(this.x == l.x) return l.y-this.y;
                return l.x-this.x;
            }
            return (l.x+l.y)-(this.x+this.y);
        }
    }

    // static PriorityQueue<Rabbit> pq;
    public static void main(String[] args) throws IOException{        
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int q = Integer.parseInt(br.readLine());
        PriorityQueue<Rabbit> pq = new PriorityQueue<>();
        int[] dr = {-1, 1, 0, 0};
        int[] dc = {0, 0, -1, 1};
        int n = 0, m = 0, p = 0;

        for(int i=0; i<q; i++){
            StringTokenizer st = new StringTokenizer(br.readLine());
            int command = Integer.parseInt(st.nextToken());

            switch(command){
                case 100: // 경주 시작 준비
                    n = Integer.parseInt(st.nextToken());
                    m = Integer.parseInt(st.nextToken());
                    p = Integer.parseInt(st.nextToken());
                    for(int j=0; j<p; j++){
                        int pid = Integer.parseInt(st.nextToken());
                        int d = Integer.parseInt(st.nextToken());
                        pq.offer(new Rabbit(1, 1, pid, d, 0, 0));
                    }
                    break;
                case 200: // 경주 진행 
                    int k = Integer.parseInt(st.nextToken());
                    int s = Integer.parseInt(st.nextToken());

                    for(int j=0; j<k; j++){
                        PriorityQueue<Loc> queue = new PriorityQueue<>();
                        Rabbit rabbit = pq.poll();

                        // 해당 토끼 상하좌우 d만큼 이동 시 위치 구하기
                        for(int z=0; z<4; z++){
                            int dx = rabbit.x + dr[z]*rabbit.d;
                            dx %= (2*(n-1));
                            if (dx > n-1) {
                                dx = (n-1)-(dx-(n-1));
                            } else if (dx < 0) {
                                dx = Math.abs(dx);
                            }

                            int dy = rabbit.y + dc[z]*rabbit.d;
                            dy %= (2*(m-1));
                            if (dy > m-1) {
                                dy = (m-1)-(dy-(m-1));
                            } else if (dy < 0) {
                                dy = Math.abs(dy);
                            }

                            queue.offer(new Loc(dx, dy)); 
                        }

                        // 4개의 위치 중 우선순위 높은 칸으로 이동
                        Loc loc = queue.poll();
                        rabbit.x = loc.x;
                        rabbit.y = loc.y;
                        rabbit.jump++;

                        // 해당 토끼 제외한 나머지 토끼 점수 획득   
                        for(Rabbit rab : pq) rab.score += loc.x+loc.y;
                        pq.offer(rabbit);
                    }
                    // K번 반복 후 우선순위 가장 높은 토끼 점수 + S
                    Rabbit rabbit = pq.poll();
                    rabbit.score += s;
                    pq.offer(rabbit);
                    break;
                case 300: // 이동거리 변경
                    int pid_t = Integer.parseInt(st.nextToken());
                    int l = Integer.parseInt(st.nextToken());
                    // 고유번호 pid인 토끼 이동거리 L배
                    for (Rabbit rab : pq) {
                        if (rab.pid == pid_t) {
                            rab.d *= l;
                            break;
                        }
                    }
                    break;
                case 400: // 최고의 토끼 선정
                    int maxScore = Integer.MIN_VALUE;
                    for (Rabbit rab : pq) {
                        maxScore = Math.max(maxScore, rab.score);
                    }
                    System.out.println(maxScore);
                    break;
            }
            
        }

    }

}