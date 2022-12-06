import java.util.*;

//ConnectFour v2.9
public class ConnectFour {
    static final int[] next = new int[8];
    static Scanner input = new Scanner(System.in);

    public static void main(String[] args) {
        Chess ch = new Chess();
        ch.print();
        System.out.println("开始游戏！输入q退出游戏，输入1悔棋！");
        String temp;
        int step, st;
        while (true) {
            System.out.print("你想在哪一列放入棋子？输入z可以悔棋：");
            Arrays.fill(next, 0);
            temp = input.next();
            if (temp.equals("q")) {
                System.out.println("退出游戏！");
                break;
            }
            if (temp.equals("z")) {
                if (ch.list.isEmpty())
                    System.out.println("你现在还不能悔棋！请重新输入！");
                else {
                    ch.undo();
                    ch.undo();
                    ch.print();
                    System.out.println("以上是悔棋后的状态.");
                }
                continue;
            }
            if (temp.length() != 1 || temp.charAt(0) < '1' || temp.charAt(0) > '7') {
                System.out.println("非法输入！请重新输入！");
                continue;
            }
            step = Integer.parseInt(temp);
            if (ch.count[step] <= 0) {
                System.out.println("这个竖行已经满了！请重新输入！");
                continue;
            }
            ch.map[ch.count[step]][step] = 1;
            ch.print();
            ch.list.add(step);
            if (ch.judge(ch.count[step]--, step, 1, 4) != 0) {
                System.out.println("你赢了！");
                if (exit(ch, 1))
                    continue;
                break;
            }
            if (ch.list.size() <= 1)
                st = 4;
            else if ((step = ch.OneWinJudge(next)) != 0) {
                if (step == -1) {
                    System.out.println("PC无棋可走，你赢了！");
                    if (exit(ch, 1))
                        continue;
                    break;
                }
                st = step;
            } else if ((st = ch.TwoWinJudge(next)) == 0) {
                if (ch.count[4] >= 3)
                    st = 4;
                else if ((st = ch.PairJudge(next)) == 0) {
                    do {
                        st = (int) Math.round(Math.random() * 7 + 0.5);
                    } while (ch.count[st] == 0);
                    System.out.println("PC想不到特别好的走法。");
                }
            }
            ch.map[ch.count[st]][st] = 2;
            ch.list.add(st);
            System.out.printf("PC在第%d列放了一枚棋子！\n", st);
            ch.print();
            if (ch.judge(ch.count[st]--, st, 2, 4) != 0) {
                System.out.println("PC赢了！");
                if (exit(ch, 2))
                    continue;
                break;
            }
            if (ch.draw()) {
                System.out.println("平局！");
                if (exit(ch, 2))
                    continue;
                break;
            }
        }
    }

    public static boolean axis(int a, int b) {
        return a > 0 && a <= 6 && b > 0 && b <= 7;
    }

    public static int JudgeNext(boolean a) {
        int count = 0, pos = 0;
        for (int i = 1; i <= 7; ++i) {
            if (next[i] == 1) {
                count++;
                pos = i;
                next[i] = 0;
            }
            if (next[i] == 2) {
                System.out.printf("第%d列不能下\n", i);
            }
            if (next[i] >= 3) {
                count += 2;
                break;
            }
        }
        if (a && count > 1)
            return -1;
        return count == 0 ? 0 : pos;
    }

    public static boolean exit(Chess ch, int n) {
        System.out.print("想悔棋请输入z，重新开始请输入a，否则退出：");
        String t = input.next();
        if (t.equals("z")) {
            while (n-- != 0)
                ch.undo();
            ch.print();
            System.out.println("以上是悔棋后的状态.");
            return true;
        }
        if (t.equals("a")) {
            ch.reset();
            ch.print();
            System.out.println("开始游戏！输入q退出游戏，输入1悔棋！");
            return true;
        }
        System.out.println("退出游戏！");
        return false;
    }
}

class Chess {
    final int[][] map;
    final int[] count;
    final ArrayList<Integer> list;

    Chess() {
        map = new int[7][8];
        for (int i = 1; i < map.length; ++i)
            Arrays.fill(map[i], 0);
        count = new int[8];
        Arrays.fill(count, 6);
        list = new ArrayList<>();
    }

    public int TwoWinJudge(int[] next) {
        int[] num = new int[8];
        int pos = 0, max = 0;
        Arrays.fill(num, 0);
        for (int i = 1; i <= 7; ++i) {
            if (count[i] == 0 || next[i] == 2)
                continue;
            num[i] = judge(count[i], i, 2, 3);
            map[count[i]][i] = 2;
            num[i] = judge(count[i]--, i, 2, 3) - num[i];  //如果有多个，那么不管真假都是合理的。  
            if (num[i] == 1) {  //如果只有1个，判断这1个是不是真的  
                for (int j = 1; j <= 7; ++j) {
                    if (count[j] == 0)
                        continue;
                    map[count[j]][j] = 2;
                    if (judge(count[j], j, 2, 4) != 0) {
                        map[count[j]][j] = 0;
                        break;
                    }
                    map[count[j]][j] = 0;
                    if (j == 7)
                        num[i] = 0;
                }
            }
            if (num[i] > max) {
                max = num[i];
                pos = i;
            }
            map[++count[i]][i] = 0;
        }
        if (pos != 0) {
//            System.out.println("three");  
            return pos;//如果pos为0，那么max肯定也为0，即num数组也不会有不是0的数。  
        }
        for (int i = 1; i <= 7; ++i) {
            if (count[i] == 0 || next[i] == 2)  //如果有一定不会下的地方，那么就不要考虑。  
                continue;
            num[i] = judge(count[i], i, 1, 3);
            map[count[i]][i] = 1;
            num[i] = judge(count[i]--, i, 1, 3) - num[i];  //如果有多个，那么不管真假都是合理的。  
            if (num[i] == 1) {  //如果只有1个，判断这1个是不是真的  
                for (int j = 1; j <= 7; ++j) {
                    if (count[j] == 0)
                        continue;
                    map[count[j]][j] = 1;
                    if (judge(count[j], j, 1, 4) != 0) {
                        map[count[j]][j] = 0;
                        break;
                    }
                    map[count[j]][j] = 0;
                    if (j == 7)
                        num[i] = 0;
                }
            }
            if (num[i] > max) {
                max = num[i];
                pos = i;
            }
            map[++count[i]][i] = 0;
        }
//        if (pos != 0)  
//            System.out.println("three");  
        return pos;  //如果pos还是0，那么就返回0。  
    }

    public int PairJudge(int[] next) {  //如果不存在能3连的地方。  
        int[] num = new int[8];
        int max = 0, pos = 0, temp = 0;
        ArrayList<Integer> sum = new ArrayList<>();
        for (int i = 1; i <= 7; ++i) {
            if (count[i] == 0 || next[i] == 2)
                continue;
            num[i] = judge(count[i], i, 2, 2);
            map[count[i]][i] = 2;
            num[i] = judge(count[i], i, 2, 2) - num[i];
            if (num[i] > max) {
                max = num[i];
                sum.clear();
                sum.add(i);
            }
            if (num[i] == max && max != 0)
                sum.add(i);
            map[count[i]][i] = 0;
        }
        if (sum.size() == 1) {
//            System.out.println("pair");  
            return sum.get(0);
        }
        if (max == 0)
            return 0;
        max = 0;
        for (int k : sum) {
            for (int i = -2; i <= 2; ++i)
                for (int j = -2; j <= 2; ++j)
                    if (ConnectFour.axis(count[k] + i, count[k] + j) && map[count[k] + i][count[k] + j] == 2)
                        temp++;
            if (temp > max) {
                pos = k;
                max = temp;
            }
            temp = 0;
        }
//        System.out.println("pair");  
        return pos;
    }

    public int OneWinJudge(int[] next) {
        int temp;
        for (int i = 1; i <= 7; ++i) {  //判断一步赢  
            if (count[i] <= 0)
                continue;
            map[count[i]][i] = 2;
            if (judge(count[i], i, 2, 4) != 0)
                next[i] += 1;
            map[count[i]][i] = 0;
        }
        if ((temp = ConnectFour.JudgeNext(false)) > 0)
            return temp;
        for (int i = 1; i <= 7; ++i) {  //判断有哪里不能下  
            if (count[i] <= 1)
                continue;
            map[count[i] - 1][i] = 1;
            if (judge(count[i] - 1, i, 1, 4) != 0)
                next[i] += 2;
            map[count[i] - 1][i] = 0;
        }
        for (int i = 1; i <= 7; ++i) {  //判断有哪里必须下  
            if (count[i] <= 0)
                continue;
            map[count[i]][i] = 1;
            if (judge(count[i], i, 1, 4) != 0)
                next[i] += 1;
            map[count[i]][i] = 0;
        }
        if ((temp = ConnectFour.JudgeNext(true)) < 0)
            return -1;
        return temp;
    }

    public int judge(int a, int b, int n, int m) {
        int sum = 0, num = 0;
        for (int i = 1; i < map[a].length; ++i) {
            if (map[a][i] == n)
                sum++;
            else
                sum = 0;
            if (sum == m && m == 4)
                num++;
            else if (sum == m && ConnectFour.axis(a, i + 1))
                num++;
        }
        sum = 0;
        for (int i = 1; i < map.length; ++i) {
            if (map[i][b] == n)
                sum++;
            else
                sum = 0;
            if (sum == m && m == 4)
                num++;
            else if (sum == m && ConnectFour.axis(i + 1, b))
                num++;
        }
        sum = 0;
        for (int i = -7; i <= 7; ++i) {
            if (!ConnectFour.axis(a + i, b + i))
                continue;
            if (map[a + i][b + i] == n)
                sum++;
            else
                sum = 0;
            if (sum == m && m == 4)
                num++;
            else if (sum == m && ConnectFour.axis(a + i + 1, b + i + 1))
                num++;
        }
        sum = 0;
        for (int i = -7; i <= 7; ++i) {
            if (!ConnectFour.axis(a - i, b + i))
                continue;
            if (map[a - i][b + i] == n)
                sum++;
            else
                sum = 0;
            if (sum == m && m == 4)
                num++;
            else if (sum == m && ConnectFour.axis(a - i - 1, b + i + 1))
                num++;
        }
        return num;
    }

    public void print() {
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
        for (int i = 1; i < 8; ++i) {
            System.out.printf(" %d", i);
        }
        System.out.println();
    }

    public void undo() {
        if (list.isEmpty()) {
            System.out.println("你现在还不能悔棋！请重新输入！");
            return;        }
        int i = list.get(list.size() - 1);
        list.remove(list.size() - 1);
        map[++count[i]][i] = 0;
    }

    public void reset() {
        list.clear();
        for (int i = 1; i < map.length; ++i)
            Arrays.fill(map[i], 0);
        Arrays.fill(count, 6);
    }

    public boolean draw() {
        for (int i = 1; i < map.length; ++i)
            for (int j = 1; j < map[i].length; ++j)
                if (map[i][j] == 0)
                    return false;
        return true;
    }
}