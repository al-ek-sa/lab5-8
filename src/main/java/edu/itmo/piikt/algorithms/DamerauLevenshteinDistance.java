package edu.itmo.piikt.algorithms;

import edu.itmo.piikt.io.IOProvider;

public class DamerauLevenshteinDistance {
    private IOProvider io;
    public DamerauLevenshteinDistance(IOProvider io){
        this.io = io;
    }
    public static int distance(String s1, String s2) {

            int[][] dl = new int[s1.length() + 1][s2.length() + 1];

            for (int i = 0; i <= s1.length(); i++) {
                dl[i][0] = i;
            }

            for (int j = 0; j <= s2.length(); j++) {
                dl[0][j] = j;
            }

            for (int i = 1; i <= s1.length(); i++) {
                for (int j = 1; j <= s2.length(); j++) {
                    if (i > 1 && j > 1 && s1.charAt(i - 2) == s2.charAt(j - 1) &&
                            s1.charAt(i - 1) == s2.charAt(j - 2)) {
                        if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
                            dl[i][j] = dl[i - 1][j - 1];
                        } else {
                            dl[i][j] = Math.min(Math.min(dl[i - 1][j], dl[i][j - 1]),
                                    Math.min(dl[i - 1][j - 1], dl[i - 2][j - 2])) + 1;
                        }
                    } else {
                        if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
                            dl[i][j] = dl[i - 1][j - 1];
                        } else {
                            dl[i][j] = Math.min(Math.min(dl[i - 1][j], dl[i][j - 1]), dl[i - 1][j - 1]) + 1;
                        }

                    }
                }
            }
            return dl[s1.length()][s2.length()];

    }
}