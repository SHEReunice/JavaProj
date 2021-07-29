package CSUOJ_Algorithm.Minimum_interval_covering_problem;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            int N = scanner.nextInt(), L = scanner.nextInt(), R = scanner.nextInt();
            int[] lineL = new int[N], lineR = new int[N];
            for (int i = 0; i < N; i++) {
                lineL[i] = scanner.nextInt();
                lineR[i] = scanner.nextInt();
            }
            int ans = 0;
            while (true) {
                int nm = -1, max = 1;

                if(L==R){
                    boolean flag=false;
                    for (int i = 0; i < N; i++) {
                        if (lineL[i]<=L&&lineR[i]>=R){
                            flag=true;
                            break;
                        }
                    }
                    if(flag){
                        ans++;
                    }else {
                        ans=-1;
                    }
                    break;
                }
                for (int i = 0; i < N; i++) {
                    if (lineL[i] <= L && lineR[i] > L) {
                        if (max <= lineR[i] - L) {
                            max = lineR[i] - L;
                            nm = i;
                        }
                    }
                }
                if (nm == -1) {
                    ans = -1;
                    break;
                } else {
                    L += max;
                    ans++;
                    if (L>=R){
                        break;
                    }
                }
            }
            System.out.println(ans);
        }
    }
}
