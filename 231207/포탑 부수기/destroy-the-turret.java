import java.util.*;
import java.io.*;

public class Main {
    
    static class Turret implements Comparable<Turret>{
        int x, y, power, recent;

        Turret(int x, int y){
            this.x = x;
            this.y = y;
        }

        Turret(int x, int y, int power, int recent){
            this.x = x;
            this.y = y;
            this.power = power;
            this.recent = recent;
        }

        public int compareTo(Turret t){
            if(this.power == t.power){
                if(this.recent == t.recent){
                    if((this.x+this.y) == (t.x+t.y)) return t.x-this.x;
                    return (t.x+t.y)-(this.x+this.y);
                }
                return t.recent-this.recent;
            }
            return this.power-t.power;
        }
    }

    static int n, m, k;
    static List<Turret> list;
    static int[][] map, attack;
    static int[] dr = {0, 1, 0, -1, -1, 1, 1, -1};
    static int[] dc = {1, 0, -1, 0, 1, 1, -1, -1};
    public static void main(String[] args) throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());
        k = Integer.parseInt(st.nextToken());
        
        map = new int[n][m];
        for(int i=0; i<n; i++){
            st = new StringTokenizer(br.readLine());
            for(int j=0; j<m; j++){
                map[i][j] = Integer.parseInt(st.nextToken());
            }
        }

        attack = new int[n][m];
        for(int t=0; t<k; t++){
            list = new ArrayList<>();
            for(int i=0; i<n; i++){
                for(int j=0; j<m; j++){
                    if(map[i][j] != 0) list.add(new Turret(i, j, map[i][j], attack[i][j]));
                }
            }

            // 공격자 포탑, 공격할 포탑 선정
            int size = list.size();
            Collections.sort(list);
            Turret weak = list.get(0);
            Turret strong = list.get(size-1);
            attack[weak.x][weak.y] = t+1;

            // 공격자로 선정된 포탑 핸디캡 적용
            map[weak.x][weak.y] = weak.power = weak.power+n+m;
            
            // 레이저 공격
            if(!laserAttack(weak, strong)){
                // 포탄 공격(레이저 공격을 할 수 없는 경우)
                turretAttack(weak, strong);
            }

            // 포탑 정비
            for(Turret turret : list){
                if(map[turret.x][turret.y] == turret.power) map[turret.x][turret.y]++;
            }
        }

        // 종료 후 남은 포탑 중 가장 강한 포탑의 공격력 출력
        int max = 0;
        for(int i=0; i<n; i++){
            for(int j=0; j<m; j++){
                if(map[i][j]==0) continue;
                max = Math.max(max, map[i][j]);
            }
        }
        System.out.println(max);
    }

    static private boolean laserAttack(Turret weak, Turret strong){
        boolean[][] visited = new boolean[n][m];
        Turret[][] root = new Turret[n][m];
        Queue<Turret> queue = new LinkedList<>();
        queue.offer(weak);
        visited[weak.x][weak.y] = true;

        while(!queue.isEmpty()){
            Turret t = queue.poll();
            
            if(t.x==strong.x && t.y==strong.y){
                // 공격 대상 포탑 공격(공격력)
                map[t.x][t.y] -= map[weak.x][weak.y];
                if(map[t.x][t.y] < 0) map[t.x][t.y] = 0;

                // 지나온 경로에 있는 포탑 공격(공격력 절반)
                int half = map[weak.x][weak.y]/2;
                int x = t.x;
                int y = t.y;
                while(true){
                    Turret pre = root[x][y];
                    if(pre.x==weak.x && pre.y==weak.y) break;
                    map[pre.x][pre.y] -= half;
                    if(map[pre.x][pre.y] < 0) map[pre.x][pre.y] = 0;
                    x = pre.x;
                    y = pre.y;
                }

                return true;
            }

            for(int i=0; i<4; i++){
                int dx = t.x+dr[i];
                int dy = t.y+dc[i];

                if(dx<0 || dx>=n || dy<0 || dy>=m || visited[dx][dy]) continue;
                queue.offer(new Turret(dx, dy));
                visited[dx][dy] = true;
                root[dx][dy] = t;
            }
        }

        return false;
    }

    static private void turretAttack(Turret weak, Turret strong){
        map[strong.x][strong.y] -= map[weak.x][weak.y];
        if(map[strong.x][strong.y] < 0) map[strong.x][strong.y] = 0;

        int half = map[weak.x][weak.y]/2;
        for(int i=0; i<8; i++){
            int dx = (strong.x+dr[i])%n;
            int dy = (strong.y+dc[i])%m;

            map[dx][dy] -= half;
            if(map[dx][dy] < 0) map[dx][dy] = 0;
        }
    }

}