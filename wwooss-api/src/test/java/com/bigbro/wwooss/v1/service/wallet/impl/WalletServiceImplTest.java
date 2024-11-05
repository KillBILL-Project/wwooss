package com.bigbro.wwooss.v1.service.wallet.impl;

import static org.junit.jupiter.api.Assertions.*;

class WalletServiceImplTest {

    public int min = Integer.MAX_VALUE;
    @Test
    void test() {
        String[] board = {"...D..R", ".D.G...", "....D.D", "D....D.", "..D...."};
        int answer = 0;
        int[][] direction = {{-1, 0}, {0, -1}, {1, 0}, {0, 1}};
        char[][] map = new char[board.length][board[0].length()];
        int[] start = new int[2];

        for(int i = 0; i < board.length; i++) {
            map[i] = board[i].toCharArray();
            int startIndex = board[i].indexOf('R');
            if(startIndex > -1) {
                start[0] = i;
                start[1] = startIndex;
            }
        }
        int recursive = recursive(direction, map, start, 0);
        System.out.println(recursive);

    }
    private int recursive(int[][] direction, char[][] map, int[] current, int cnt) {
        for(int i = 0; i < 4; i++) {
            int[] next = {current[0], current[1]};

            while(true) {
                next[0] += direction[i][0];
                next[1] += direction[i][1];
                if(next[0] <= -1 || next[1] <= -1) {
                    break;
                }
                ++cnt;
                if(map[next[0]][next[1]] == 'D') {
                    --cnt;
                    next[0] -= direction[i][0];
                    next[1] -= direction[i][1];
                    break;
                };
            }
            if(next[0] <= -1 || next[1] <= -1) {
                continue;
            } else if(map[next[0]][next[1]] == 'G') {
                return Math.min(cnt - 1, min);
            } else {
                recursive(direction, map, next, cnt);
            }
        }
        return -1;
    }
}
