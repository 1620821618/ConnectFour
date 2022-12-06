import java.util.*;

//ConnectFour v1.0
public class ConnectFour {
    static int[][] map = new int[7][8];
    static int[] count = new int[8];

    public static int random() {
        return (int) Math.round(0.5 + Math.random() * 7);
    }

    public static void main(String[] args) {
        Arrays.fill(count, 6);
        Scanner input = new Scanner(System.in);
        print();
        System.out.println("开始游戏！输入0退出游戏！");
        while (true) {
            System.out.print("你想在哪一行放入棋子？");
            int a = input.nextInt(), b;
            if (a == 0) {
                System.out.println("退出游戏！");
                break;
            }
            if (a > 7 || a < 0) {
                System.out.println("输入非法！请重新输入！");
                continue;
            }
            if (count[a] == 0) {
                System.out.println("这个竖行已经满了！请重新输入！");
                continue;
            }
            map[count[a]--][a] = 1;
            print();
            if (judge(count[a] + 1, a, 1)) {
                System.out.println("你赢了！");
                break;
            }
            do {
                b = random();
            } while (count[b] == 0);
            System.out.printf("PC在第%d竖行放入了一个棋子！\n", b);
            map[count[b]--][b] = 2;
            print();
            if (judge(count[b] + 1, b, 2)) {
                System.out.println("电脑赢了！");
                break;
            }
            for (b = 1; b < count.length; ++b) {
                if (count[b] != 0)
                    break;
            }
            if (b == count.length) {
                System.out.println("游戏平局！");
                break;
            }
        }
    }

    public static void print() {
        for (int i = 1; i < map.length; ++i) {
            for (int j = 1; j < map[i].length; ++j) {
                switch (map[i][j]) {
                    case 0:
                        System.out.print("| ");
                        break;
                    case 1:
                        System.out.print("|O");
                        break;
                    case 2:
                        System.out.print("|X");
                }
            }
            System.out.println("|");
        }
    }

    public static boolean judge(int a, int b, int c) {
        int sum = 0;
        for (int i = 1; i < map[a].length; ++i) {
            if (map[a][i] == c)
                sum++;
            else
                sum = 0;
            if (sum == 4)
                return true;
        }
        sum = 0;
        for (int i = 1; i < map.length; ++i) {
            if (map[i][b] == c)
                sum++;
            else
                sum = 0;
            if (sum == 4)
                return true;
        }
        sum = 0;
        for (int i = -7; i <= 7; ++i) {
            if (axis(a + i, b + i)) {
                if (map[a + i][b + i] == c)
                    sum++;
                else
                    sum = 0;
                if (sum == 4)
                    return true;
            }
        }
        sum = 0;
        for (int i = -7; i <= 7; ++i) {
            if (axis(a - i, b + i)) {
                if (map[a - i][b + i] == c)
                    sum++;
                else
                    sum = 0;
                if (sum == 4)
                    return true;
            }
        }
        return false;
    }

    public static boolean axis(int a, int b) {
        return a >= 1 && a <= 6 && b >= 1 && b <= 6;
    }
}