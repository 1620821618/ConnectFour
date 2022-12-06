import java.util.*;

//ConnectFour v2.1
public class ConnectFour {
    static int[][] map = new int[7][8];
    static int[] count = new int[8];
    static ArrayList<Integer> list = new ArrayList<>();

    public static int random(int a) {
        if (a == 200) {
            System.out.println("电脑没有下一步棋可以走，你赢了!");
            System.exit(0);
        }
        int i = (int) Math.round(0.5 + Math.random() * 7);
        if (axis(count[i] - 1, i)) {
            map[count[i] - 1][i] = 1;
            if (judge(count[i] - 1, i, 1)) {
                map[count[i] - 1][i] = 0;
                return random(a + 1);
            }
            map[count[i] - 1][i] = 0;
        }
        return i;
    }

    public static int WinJudge() {
        for (int i = 1; i <= 7; ++i) {
            if (count[i] <= 0)
                continue;
            map[count[i]][i] = 2;
            if (judge(count[i], i, 2))
                return i;
            map[count[i]][i] = 0;
        }
        for (int i = 1; i <= 7; ++i) {
            if (count[i] <= 0)
                continue;
            map[count[i]][i] = 1;
            if (judge(count[i], i, 1))
                return i;
            map[count[i]][i] = 0;
        }
        for (int i = 1; i <= 7; ++i) {
            if (count[i] <= 0)
                continue;
            map[count[i]][i] = 2;
            if (axis(count[i] - 1, i)) {
                map[count[i] - 1][i] = 2;
                if (judge(count[i] - 1, i, 2)) {
                    map[count[i] - 1][i] = 0;
                    return i;
                }
                map[count[i] - 1][i] = 0;
            }
            if (axis(count[i - 1], i - 1)) {
                map[count[i - 1]][i - 1] = 2;
                if (judge(count[i - 1], i - 1, 2)) {
                    map[count[i - 1]][i - 1] = 0;
                    return i;
                }
                map[count[i - 1]][i - 1] = 0;
            }
            if (i + 1 <= 7 && axis(count[i + 1], i + 1)) {
                map[count[i + 1]][i + 1] = 2;
                if (judge(count[i + 1], i + 1, 2)) {
                    map[count[i + 1]][i + 1] = 0;
                    return i;
                }
                map[count[i + 1]][i + 1] = 0;
            }
            map[count[i]][i] = 0;
        }
        return 0;
    }

    public static void main(String[] args) {
        Arrays.fill(count, 6);
        Scanner input = new Scanner(System.in);
        print();
        System.out.println("开始游戏！输入0退出游戏，输入-1悔棋！");
        int a, b = -1;
        while (true) {
            System.out.print("你想在哪一行放入棋子?输入-1可以悔棋:");
            a = input.nextInt();
            if (a == 0) {
                System.out.println("退出游戏！");
                break;
            }
            if (a > 7 || a < -1) {
                System.out.println("输入非法！请重新输入！");
                continue;
            }
            if (a == -1) {
                undo();
                continue;
            }
            if (count[a] <= 0) {
                System.out.println("这个竖行已经满了！请重新输入！");
                continue;
            }
            map[count[a]--][a] = 1;
            print();
            list.add(a);
            if (judge(count[a] + 1, a, 1)) {
                System.out.println("你赢了！");
                System.out.print("输入-2重新开始游戏，输入其他数字退出：");
                a = input.nextInt();
                if (a != -2)
                    break;
                reset();
                b = -1;
                print();
                System.out.println("开始游戏！输入0退出游戏，输入-1可以悔棋：");
                continue;
            }
            if (b == -1)
                b = (count[4] == 6 ? 4 : 3);
            else if ((b = WinJudge()) == 0) {
                do {
                    b = random(0);
                } while (count[b] == 0);
            }
            list.add(b);
            System.out.printf("PC在第%d竖行放入了一个棋子！\n", b);
            map[count[b]--][b] = 2;
            print();
            if (judge(count[b] + 1, b, 2)) {
                System.out.println("PC赢了！");
                System.out.print("想悔棋请输入-1,重新开始请输入-2,输入其他数值则退出：");
                a = input.nextInt();
                if (a == -1) {
                    undo();
                    continue;
                }
                if (a == -2) {
                    reset();
                    print();
                    System.out.println("开始游戏！输入0退出游戏，输入-1悔棋！");
                    b = -1;
                    continue;
                }
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
        return a >= 1 && a <= 6 && b >= 1 && b <= 7;
    }

    public static void undo() {
        if (list.isEmpty()) {
            System.out.println("你现在还不能悔棋！请重新输入！");
            return;
        }
        int i = list.get(list.size() - 1), j = list.get(list.size() - 2);
        list.remove(list.size() - 1);
        list.remove(list.size() - 1);
        map[++count[i]][i] = map[++count[j]][j] = 0;
        print();
        System.out.println("以上是悔棋后的状态.");
    }

    public static void reset() {
        list.clear();
        for (int i = 1; i < map.length; ++i)
            Arrays.fill(map[i], 0);
        Arrays.fill(count, 6);
    }
}