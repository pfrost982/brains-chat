package com.geekbrains.server;

import java.util.Arrays;

public class ArrayMethods {
    public int[] cutOff4(int[] array) throws Exception {
        for (int i = array.length - 1; i >= 0; i--) {
            if (array[i] == 4) {
                int resultLength = array.length - i - 1;
                int[] resultArray = new int[resultLength];
                System.arraycopy(array, i + 1, resultArray, 0, resultLength);
                return resultArray;
            }
        }
        throw new RuntimeException("В массиве отсутствует 4:\n" + Arrays.toString(array));
    }

    public boolean only1and4(int[] array) {
        boolean present1 = false;
        boolean present4 = false;
        boolean noOthers = true;
        for (int i = 0; i < array.length; i++) {
            switch (array[i]) {
                case (1):
                    present1 = true;
                    break;
                case (4):
                    present4 = true;
                    break;
                default:
                    noOthers = false;
                    break;
            }
        }
        return present1 & present4 & noOthers;
    }
}
