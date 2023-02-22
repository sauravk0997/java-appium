package com.disney.qa.common.utils;

import io.appium.java_client.android.nativekey.AndroidKey;

import java.util.HashMap;
import java.util.Map;

public class AndroidKeys {

    public static Map<Character, AndroidKey> getKeyCodes(){
        HashMap<Character,AndroidKey> keyMap = new HashMap<>();

        keyMap.put('a', AndroidKey.A);
        keyMap.put('b', AndroidKey.B);
        keyMap.put('c', AndroidKey.C);
        keyMap.put('d', AndroidKey.D);
        keyMap.put('e', AndroidKey.E);
        keyMap.put('f', AndroidKey.F);
        keyMap.put('g', AndroidKey.G);
        keyMap.put('h', AndroidKey.H);
        keyMap.put('i', AndroidKey.I);
        keyMap.put('j', AndroidKey.J);
        keyMap.put('k', AndroidKey.K);
        keyMap.put('l', AndroidKey.L);
        keyMap.put('m', AndroidKey.M);
        keyMap.put('n', AndroidKey.N);
        keyMap.put('o', AndroidKey.O);
        keyMap.put('p', AndroidKey.P);
        keyMap.put('q', AndroidKey.Q);
        keyMap.put('r', AndroidKey.R);
        keyMap.put('s', AndroidKey.S);
        keyMap.put('t', AndroidKey.T);
        keyMap.put('u', AndroidKey.U);
        keyMap.put('v', AndroidKey.V);
        keyMap.put('w', AndroidKey.W);
        keyMap.put('x', AndroidKey.X);
        keyMap.put('y', AndroidKey.Y);
        keyMap.put('0', AndroidKey.DIGIT_0);
        keyMap.put('1', AndroidKey.DIGIT_1);
        keyMap.put('2', AndroidKey.DIGIT_2);
        keyMap.put('3', AndroidKey.DIGIT_3);
        keyMap.put('4', AndroidKey.DIGIT_4);
        keyMap.put('5', AndroidKey.DIGIT_5);
        keyMap.put('6', AndroidKey.DIGIT_6);
        keyMap.put('7', AndroidKey.DIGIT_7);
        keyMap.put('8', AndroidKey.DIGIT_8);
        keyMap.put('9', AndroidKey.DIGIT_9);
        keyMap.put('.', AndroidKey.PERIOD);
        keyMap.put('@', AndroidKey.AT);
        keyMap.put('+', AndroidKey.PLUS);
        keyMap.put('#', AndroidKey.POUND);

        return keyMap;
    }

    private AndroidKeys(){

    }
}
