import java.util.*;

//ConnectFour v3.0
public class ConnectFour {

    public static void main(String[] args) {
        final Chess ch = new Chess();
        final Control control = new Control(ch);
        boolean hand = true, begin = true, back;
        int step1, step2, end;
        while (true) {
            Arrays.fill(control.st.next, 0);
            if (begin) {
                hand = random();
                System.out.println("开始游戏！输入q退出游戏。");
                if (ch.print(hand))
                    System.out.println("你被分配到了先手，你的棋子为O");
                else
                    System.out.println("你被分配到了后手，你的棋子为X");
                begin = false;
            }
            if (hand) {
                if ((step1 = control.player(true)) == 0)
                    continue;
                else if (step1 == -1)
                    System.exit(0);
                if ((ch.judge(ch.count[step1]--, step1, 1, 4)) != 0) {
                    System.out.println("你赢了！本轮游戏结束。");
                    end = control.exit(1, true);
                    if (end != 0) {
                        if (end < 0)
                            begin = true;
                        continue;
                    }
                    System.exit(0);
                }
                if ((step2 = control.PC_Step(true)) == -1) {
                    System.out.println("PC无棋可走，你赢了！");
                    end = control.exit(1, true);
                    if (end != 0) {
                        if (end < 0)
                            begin = true;
                        continue;
                    }
                    System.exit(0);
                } else if (ch.judge(ch.count[step2]--, step2, 2, 4) != 0) {
                    System.out.println("PC赢了！本轮游戏结束。");
                    end = control.exit(2, true);
                    if (end != 0) {
                        if (end < 0)
                            begin = true;
                        continue;
                    }
                    System.exit(0);
                }
            } else {
                back = true;
                if ((step1 = control.PC_Step(false)) == -1) {
                    System.out.println("PC无棋可走，你赢了！");
                    end = control.exit(1, false);
                    if (end < 0) {
                        begin = true;
                        continue;
                    }
                    if (end == 0)
                        System.exit(0);
                } else if (ch.judge(ch.count[step1]--, step1, 2, 4) != 0) {
                    System.out.println("PC赢了！本轮游戏结束。");
                    end = control.exit(2, false);
                    if (end < 0) {
                        begin = true;
                        continue;
                    }
                    if (end == 0)
                        System.exit(0);
                }
                while (back && !begin) {
                    if ((step2 = control.player(false)) == 0)
                        continue;
                    else if (step2 == -1)
                        System.exit(0);
                    else
                        back = false;
                    if ((ch.judge(ch.count[step2]--, step2, 1, 4)) != 0) {
                        System.out.println("你赢了！本轮游戏结束。");
                        end = control.exit(1, false);
                        if (end != 0) {
                            if (end < 0)
                                begin = true;
                            else
                                back = true;
                            continue;
                        }
                        System.exit(0);
                    }
                }
            }
            if (ch.draw()) {
                System.out.println("平局！游戏结束。");
                end = control.exit(hand ? 2 : 1, hand);
                if (end != 0) {
                    if (end < 0)
                        begin = true;
                    continue;
                }
                System.exit(0);
            }
        }
    }

    public static boolean random() {
        return ((int) (Math.random() * 2)) != 0;
    }
}

final class Chess {
    final int[][] map;
    final int[] count;
    final ArrayList<Integer> list;

    public Chess() {
        map = new int[7][8];
        for (int i = 1; i < map.length; ++i)
            Arrays.fill(map[i], 0);
        count = new int[8];
        Arrays.fill(count, 6);
        list = new ArrayList<>();
    }

    final class Strategy {
        final int[] next;

        Strategy() {
            next = new int[8];
            Arrays.fill(next, 0);
        }

        public int TwoWinJudge() {
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
//                System.out.println("three");//
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
//            if (pos != 0)//
//                System.out.println("three");//
            return pos;  //如果pos还是0，那么就返回0。
        }

        public int PairJudge() {  //如果不存在能3连的地方。
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
//                System.out.println("pair");//
                return sum.get(0);
            }
            if (max == 0)
                return 0;
            max = 0;
            for (int k : sum) {
                for (int i = -2; i <= 2; ++i)
                    for (int j = -2; j <= 2; ++j)
                        if (Control.axis(count[k] + i, count[k] + j) && map[count[k] + i][count[k] + j] == 2)
                            temp++;
                if (temp > max) {
                    pos = k;
                    max = temp;
                }
                temp = 0;
            }
//            System.out.println("pair");//
            return pos;
        }

        public int OneWinJudge() {
            int temp;
            for (int i = 1; i <= 7; ++i) {  //判断一步赢
                if (count[i] <= 0)
                    continue;
                map[count[i]][i] = 2;
                if (judge(count[i], i, 2, 4) != 0)
                    next[i] += 1;
                map[count[i]][i] = 0;
            }
            if ((temp = JudgeNext(false)) > 0)
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
            if ((temp = JudgeNext(true)) < 0)
                return -1;
            return temp;
        }

        public int JudgeNext(boolean a) {
            int num = 0, pos = 0;
            boolean none = true;
            for (int i = 1; i <= 7; ++i) {
                if (next[i] == 1) {
                    num++;
                    pos = i;
                    next[i] = 0;
                    none = false;
                }
//                else if (next[i] == 2)//
//                    System.out.printf("第%d列不能下\n", i);//
                else if (next[i] >= 3) {
                    num += 2;
//                    System.out.printf("PC对第%d列无能为力\n", i);//
                    break;
                } else if (count[i] != 0)
                    none = false;
            }
            if ((a && num > 1) || none)
                return -1;
            return num == 0 ? 0 : pos;
        }

        public int OnlyOne() {
            int pos = 0;
            for (int i = 1; i <= 7; ++i) {
                if (count[i] != 0 && pos != 0)
                    return 0;
                else if (count[i] != 0)
                    pos = i;
            }
            return pos;
        }
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
            else if (sum == m && Control.axis(a, i + 1))
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
            else if (sum == m && Control.axis(i + 1, b))
                num++;
        }
        sum = 0;
        for (int i = -7; i <= 7; ++i) {
            if (!Control.axis(a + i, b + i))
                continue;
            if (map[a + i][b + i] == n)
                sum++;
            else
                sum = 0;
            if (sum == m && m == 4)
                num++;
            else if (sum == m && Control.axis(a + i + 1, b + i + 1))
                num++;
        }
        sum = 0;
        for (int i = -7; i <= 7; ++i) {
            if (!Control.axis(a - i, b + i))
                continue;
            if (map[a - i][b + i] == n)
                sum++;
            else
                sum = 0;
            if (sum == m && m == 4)
                num++;
            else if (sum == m && Control.axis(a - i - 1, b + i + 1))
                num++;
        }
        return num;
    }

    public boolean print(boolean x) {
        for (int i = 1; i < map.length; ++i) {
            for (int j = 1; j < map[i].length; ++j) {
                switch (map[i][j]) {
                    case 0:
                        System.out.print("| ");
                        break;
                    case 1:
                        System.out.print(x ? "|O" : "|X");
                        break;
                    case 2:
                        System.out.print(x ? "|X" : "|O");
                }
            }
            System.out.println("|");
        }
        for (int i = 1; i < 8; ++i) {
            System.out.printf(" %d", i);
        }
        System.out.println();
        return x;
    }

    public void undo() {
        if (list.isEmpty()) {
            System.out.println("你现在还不能悔棋！请重新输入！");
            return;
        }
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

class Control {
    final Scanner input = new Scanner(System.in);
    final Chess ch;
    final Chess.Strategy st;

    Control(Chess chess) {
        ch = chess;
        st = ch.new Strategy();
    }

    public int PC_Step(boolean x) {
        int step;
        if (ch.list.size() <= 1)
            step = 4;
        else if ((step = st.OnlyOne()) != 0) ;
        else if ((step = st.OneWinJudge()) != 0) {
            if (step == -1)
                return -1;
        } else if ((step = st.TwoWinJudge()) == 0) {
            if (ch.count[4] >= 3)
                step = 4;
            else if ((step = st.PairJudge()) == 0) {
                do {
                    step = (int) Math.round(Math.random() + 0.5);
                } while (ch.count[step] == 0 && st.next[step] != 2);
                System.out.println("PC没有想到特别好的走法");
            }
        }
        ch.map[ch.count[step]][step] = 2;
        ch.list.add(step);
        System.out.printf("PC在第%d列放入一枚棋子！\n", step);
        ch.print(x);
        return step;
    }

    public static boolean axis(int a, int b) {
        return a > 0 && a <= 6 && b > 0 && b <= 7;
    }

    public int exit(int n, boolean x) {
        System.out.print("想悔棋请输入z，重新开始请输入a，否则退出：");
        String temp = input.next();
        if (temp.equals("z")) {
            while (n-- != 0)
                ch.undo();
            ch.print(x);
            System.out.println("以上是悔棋后的状态.");
            return 1;
        }
        if (temp.equals("a")) {
            ch.reset();
            return -1;
        }
        System.out.println("退出游戏！");
        return 0;
    }

    public int player(boolean x) {
        System.out.print("你想在哪一列放入棋子？输入z可以悔棋：");
        String temp = input.next();
        int step;
        if (temp.equals("q")) {
            System.out.println("退出游戏！");
            return -1;
        }
        if (temp.equals("z")) {
            if (ch.list.size() <= 1)
                System.out.println("你现在还不能悔棋！请重新输入！");
            else {
                ch.undo();
                ch.undo();
                ch.print(x);
                System.out.println("以上是悔棋后的状态.");
            }
            return 0;
        }
        if (temp.length() != 1 || temp.charAt(0) < '1' || temp.charAt(0) > '7') {
            System.out.println("非法输入！请重新输入！");
            return 0;
        }
        step = Integer.parseInt(temp);
        if (ch.count[step] <= 0) {
            System.out.println("这个竖行已经满了！请重新输入！");
            return 0;
        }
        ch.map[ch.count[step]][step] = 1;
        ch.print(x);
        ch.list.add(step);
        return step;
    }
}