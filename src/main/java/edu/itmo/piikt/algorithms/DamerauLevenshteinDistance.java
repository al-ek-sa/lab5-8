package edu.itmo.piikt.algorithms;

/**
 * The class implements the Damerau-Levenshtein algorithm.
 *
 * @author Lishyk Aliaksandra
 * @version 1.1
 */
public class DamerauLevenshteinDistance {
    public DamerauLevenshteinDistance() {
    }

    public static int distance(String s1, String s2) {
        int length1 = s1.length();
        int length2 = s2.length();
        int[][] dl = new int[length1 + 1][length2 + 1];

        for (int i = 0; i <= length1; i++) {
            dl[i][0] = i;
        }

        for (int j = 0; j <= length2; j++) {
            dl[0][j] = j;
        }

        for (int i = 1; i <= length1; i++) {
            for (int j = 1; j <= length2; j++) {
                if (i > 1 && j > 1 && s1.charAt(i - 2) == s2.charAt(j - 1) && s1.charAt(i - 1) == s2.charAt(j - 2)) {
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
        return dl[length1][length2];
    }
}
