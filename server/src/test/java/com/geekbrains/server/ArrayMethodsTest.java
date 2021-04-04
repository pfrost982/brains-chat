package com.geekbrains.server;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

class ArrayMethodsTest {
    ArrayMethods arrayMethods = new ArrayMethods();

    @Test
    void cutOff4_1() throws Exception {
        Assertions.assertArrayEquals(new int[]{6, 0}, arrayMethods.cutOff4(new int[]{1, 3, 4, 6, 0}));
    }

    @Test
    void cutOff4_2() throws Exception {
        Assertions.assertArrayEquals(new int[]{}, arrayMethods.cutOff4(new int[]{1, 3, 6, 4}));
    }

    @Test
    void cutOff4_3() throws Exception {
        Assertions.assertArrayEquals(new int[]{}, arrayMethods.cutOff4(new int[]{4}));
    }

    @Test
    void cutOff4_exception_1() {
        Assertions.assertThrows(Exception.class, () -> {
            arrayMethods.cutOff4(new int[]{});
        });
    }

    @Test
    void cutOff4_exception_2() {
        Assertions.assertThrows(Exception.class, () -> {
            arrayMethods.cutOff4(new int[]{1, 3, 6, 0});
        });
    }

    @Test
    void only1and4_1() {
        Assertions.assertEquals(true, arrayMethods.only1and4(new int[]{1, 1, 1, 4, 4, 1, 4, 4}));
    }

    @Test
    void only1and4_2() {
        Assertions.assertEquals(false, arrayMethods.only1and4(new int[]{1, 1, 1, 1, 1, 1}));
    }

    @Test
    void only1and4_3() {
        Assertions.assertEquals(false, arrayMethods.only1and4(new int[]{4, 4, 4, 4, 4}));
    }

    @Test
    void only1and4_4() {
        Assertions.assertEquals(false, arrayMethods.only1and4(new int[]{1, 1, 1, 4, 3, 1, 4, 4}));
    }
}