package com.zemle.keyoneime;

import android.content.Context;
import android.view.KeyEvent;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * キーコードからASCIIコードに変換
 *
 * ASCIIコードが 33〜126の場合はMapに直値で記載。
 * その他のキーは、リソースに定義した値を参照。
 * （記載箇所を一元化するため）
 *
 * Created by shntn on 2018/01/06.
 */

class KeyCode {
    private StateKeyboard mKeyboard;
    private Map<Integer, Integer> mQwertyMap;
    private Map<Integer, Integer> mSymbol1Map;
    private Map<Integer, Integer> mSymbol2Map;
    private int mKeyNone;

    KeyCode(Context context, StateKeyboard keyboard){
        mKeyboard = keyboard;
        mQwertyMap = createQwertyMap(context);
        mSymbol1Map = createSymbol1Map(context);
        mSymbol2Map = createSymbol2Map(context);
        mKeyNone = context.getResources().getInteger(R.integer.key_none);
    }

    private static Map<Integer, Integer> createQwertyMap(Context context) {
        Map<Integer, Integer> map = new LinkedHashMap<>();
        map.put(KeyEvent.KEYCODE_Q, 113);
        map.put(KeyEvent.KEYCODE_W, 119);
        map.put(KeyEvent.KEYCODE_E, 101);
        map.put(KeyEvent.KEYCODE_R, 114);
        map.put(KeyEvent.KEYCODE_T, 116);
        map.put(KeyEvent.KEYCODE_Y, 121);
        map.put(KeyEvent.KEYCODE_U, 117);
        map.put(KeyEvent.KEYCODE_I, 105);
        map.put(KeyEvent.KEYCODE_O, 111);
        map.put(KeyEvent.KEYCODE_P, 112);

        map.put(KeyEvent.KEYCODE_A, 97);
        map.put(KeyEvent.KEYCODE_S, 115);
        map.put(KeyEvent.KEYCODE_D, 100);
        map.put(KeyEvent.KEYCODE_F, 102);
        map.put(KeyEvent.KEYCODE_G, 103);
        map.put(KeyEvent.KEYCODE_H, 104);
        map.put(KeyEvent.KEYCODE_J, 106);
        map.put(KeyEvent.KEYCODE_K, 107);
        map.put(KeyEvent.KEYCODE_L, 108);
        map.put(KeyEvent.KEYCODE_DEL, context.getResources().getInteger(R.integer.key_delete));

        map.put(KeyEvent.KEYCODE_ALT_LEFT, context.getResources().getInteger(R.integer.key_alt_left));
        map.put(KeyEvent.KEYCODE_Z, 122);
        map.put(KeyEvent.KEYCODE_X, 120);
        map.put(KeyEvent.KEYCODE_C, 99);
        map.put(KeyEvent.KEYCODE_V, 118);
        map.put(KeyEvent.KEYCODE_B, 98);
        map.put(KeyEvent.KEYCODE_N, 110);
        map.put(KeyEvent.KEYCODE_M, 109);
        map.put(KeyEvent.KEYCODE_4, 36);
        map.put(KeyEvent.KEYCODE_ENTER, context.getResources().getInteger(R.integer.key_enter));

        map.put(KeyEvent.KEYCODE_SHIFT_LEFT, context.getResources().getInteger(R.integer.key_ctrl_left));
        map.put(KeyEvent.KEYCODE_0, 48);
        map.put(KeyEvent.KEYCODE_SPACE, context.getResources().getInteger(R.integer.key_space));
        map.put(KeyEvent.KEYCODE_SYM, context.getResources().getInteger(R.integer.key_sym));
        map.put(KeyEvent.KEYCODE_SHIFT_RIGHT, context.getResources().getInteger(R.integer.key_shift));
        return map;
    }

    private static Map<Integer, Integer> createSymbol1Map(Context context) {
        Map<Integer, Integer> map = new LinkedHashMap<>();
        map.put(KeyEvent.KEYCODE_Q, context.getResources().getInteger(R.integer.key_esc));
        map.put(KeyEvent.KEYCODE_W, context.getResources().getInteger(R.integer.key_none));
        map.put(KeyEvent.KEYCODE_E, context.getResources().getInteger(R.integer.key_none));
        map.put(KeyEvent.KEYCODE_R, context.getResources().getInteger(R.integer.key_none));
        map.put(KeyEvent.KEYCODE_T, context.getResources().getInteger(R.integer.key_none));
        map.put(KeyEvent.KEYCODE_Y, 96);
        map.put(KeyEvent.KEYCODE_U, 94);
        map.put(KeyEvent.KEYCODE_I, 60);
        map.put(KeyEvent.KEYCODE_O, 62);
        map.put(KeyEvent.KEYCODE_P, 37);

        map.put(KeyEvent.KEYCODE_A, context.getResources().getInteger(R.integer.key_tab));
        map.put(KeyEvent.KEYCODE_S, context.getResources().getInteger(R.integer.key_none));
        map.put(KeyEvent.KEYCODE_D, context.getResources().getInteger(R.integer.key_dpad_up));
        map.put(KeyEvent.KEYCODE_F, context.getResources().getInteger(R.integer.key_none));
        map.put(KeyEvent.KEYCODE_G, 92);
        map.put(KeyEvent.KEYCODE_H, 124);
        map.put(KeyEvent.KEYCODE_J, 38);
        map.put(KeyEvent.KEYCODE_K, 91);
        map.put(KeyEvent.KEYCODE_L, 93);
        map.put(KeyEvent.KEYCODE_DEL, context.getResources().getInteger(R.integer.key_delete));

        map.put(KeyEvent.KEYCODE_ALT_LEFT, context.getResources().getInteger(R.integer.key_alt_left));
        map.put(KeyEvent.KEYCODE_Z, context.getResources().getInteger(R.integer.key_dpad_left));
        map.put(KeyEvent.KEYCODE_X, context.getResources().getInteger(R.integer.key_dpad_down));
        map.put(KeyEvent.KEYCODE_C, context.getResources().getInteger(R.integer.key_dpad_right));
        map.put(KeyEvent.KEYCODE_V, context.getResources().getInteger(R.integer.key_none));
        map.put(KeyEvent.KEYCODE_B, 126);
        map.put(KeyEvent.KEYCODE_N, 61);
        map.put(KeyEvent.KEYCODE_M, 123);
        map.put(KeyEvent.KEYCODE_4, 125);
        map.put(KeyEvent.KEYCODE_ENTER, context.getResources().getInteger(R.integer.key_enter));

        map.put(KeyEvent.KEYCODE_SHIFT_LEFT, context.getResources().getInteger(R.integer.key_ctrl_left));
        map.put(KeyEvent.KEYCODE_0, 48);
        map.put(KeyEvent.KEYCODE_SPACE, context.getResources().getInteger(R.integer.key_space));
        map.put(KeyEvent.KEYCODE_SYM, context.getResources().getInteger(R.integer.key_sym));
        map.put(KeyEvent.KEYCODE_SHIFT_RIGHT, context.getResources().getInteger(R.integer.key_shift));
        return map;
    }

    private static Map<Integer, Integer> createSymbol2Map(Context context) {
        Map<Integer, Integer> map = new LinkedHashMap<>();
        map.put(KeyEvent.KEYCODE_Q, 35);
        map.put(KeyEvent.KEYCODE_W, 49);
        map.put(KeyEvent.KEYCODE_E, 50);
        map.put(KeyEvent.KEYCODE_R, 51);
        map.put(KeyEvent.KEYCODE_T, 40);
        map.put(KeyEvent.KEYCODE_Y, 41);
        map.put(KeyEvent.KEYCODE_U, 95);
        map.put(KeyEvent.KEYCODE_I, 45);
        map.put(KeyEvent.KEYCODE_O, 43);
        map.put(KeyEvent.KEYCODE_P, 64);

        map.put(KeyEvent.KEYCODE_A, 42);
        map.put(KeyEvent.KEYCODE_S, 52);
        map.put(KeyEvent.KEYCODE_D, 53);
        map.put(KeyEvent.KEYCODE_F, 54);
        map.put(KeyEvent.KEYCODE_G, 47);
        map.put(KeyEvent.KEYCODE_H, 58);
        map.put(KeyEvent.KEYCODE_J, 59);
        map.put(KeyEvent.KEYCODE_K, 39);
        map.put(KeyEvent.KEYCODE_L, 34);
        map.put(KeyEvent.KEYCODE_DEL, context.getResources().getInteger(R.integer.key_delete));

        map.put(KeyEvent.KEYCODE_ALT_LEFT, context.getResources().getInteger(R.integer.key_alt_left));
        map.put(KeyEvent.KEYCODE_Z, 55);
        map.put(KeyEvent.KEYCODE_X, 56);
        map.put(KeyEvent.KEYCODE_C, 57);
        map.put(KeyEvent.KEYCODE_V, 63);
        map.put(KeyEvent.KEYCODE_B, 33);
        map.put(KeyEvent.KEYCODE_N, 44);
        map.put(KeyEvent.KEYCODE_M, 46);
        map.put(KeyEvent.KEYCODE_4, 36);
        map.put(KeyEvent.KEYCODE_ENTER, context.getResources().getInteger(R.integer.key_enter));

        map.put(KeyEvent.KEYCODE_SHIFT_LEFT, context.getResources().getInteger(R.integer.key_ctrl_left));
        map.put(KeyEvent.KEYCODE_0, 48);
        map.put(KeyEvent.KEYCODE_SPACE, context.getResources().getInteger(R.integer.key_space));
        map.put(KeyEvent.KEYCODE_SYM, context.getResources().getInteger(R.integer.key_sym));
        map.put(KeyEvent.KEYCODE_SHIFT_RIGHT, context.getResources().getInteger(R.integer.key_shift));
        return map;
    }

    int convert(int keyCode) {
        int result;

        if ((mKeyboard.isType(StateKeyboard.State.Qwerty))
                && (mQwertyMap.containsKey(keyCode))) {
            result = mQwertyMap.get(keyCode);
        } else if ((mKeyboard.isType(StateKeyboard.State.Symbol1))
                && (mSymbol1Map.containsKey(keyCode))) {
            result = mSymbol1Map.get(keyCode);
        } else if ((mKeyboard.isType(StateKeyboard.State.Symbol2))
                && (mSymbol2Map.containsKey(keyCode))) {
            result = mSymbol2Map.get(keyCode);
        } else {
            result = mKeyNone;
        }

        return result;
    }

    KeyEvent convertToKeyEvent(int keycode, int action, int metaState) {
        KeyEvent event = new KeyEvent(0, 0, action, keycode, 0, metaState);

        if (mKeyboard.isType(StateKeyboard.State.Qwerty)) {
            switch (keycode) {
                case KeyEvent.KEYCODE_DEL:
                case KeyEvent.KEYCODE_ENTER:
                case KeyEvent.KEYCODE_SPACE:
                case KeyEvent.KEYCODE_SYM:
                case KeyEvent.KEYCODE_ALT_LEFT:
                case KeyEvent.KEYCODE_SHIFT_RIGHT:
                    event = new KeyEvent(0, 0, action, keycode, 0, metaState);
                    break;
                case KeyEvent.KEYCODE_SHIFT_LEFT:
                    event = new KeyEvent(0, 0, action, KeyEvent.KEYCODE_CTRL_LEFT, 0, metaState);
                    break;
            }
        } else if (mKeyboard.isType(StateKeyboard.State.Symbol1)) {
            switch (keycode) {
                case KeyEvent.KEYCODE_Q:
                    event = new KeyEvent(0, 0, action, KeyEvent.KEYCODE_ESCAPE, 0, metaState);
                    break;
                case KeyEvent.KEYCODE_A:
                    event = new KeyEvent(0, 0, action, KeyEvent.KEYCODE_TAB, 0, metaState);
                    break;
                case KeyEvent.KEYCODE_D:
                    event = new KeyEvent(0, 0, action, KeyEvent.KEYCODE_DPAD_UP, 0, metaState);
                    break;
                case KeyEvent.KEYCODE_Z:
                    event = new KeyEvent(0, 0, action, KeyEvent.KEYCODE_DPAD_LEFT, 0, metaState);
                    break;
                case KeyEvent.KEYCODE_X:
                    event = new KeyEvent(0, 0, action, KeyEvent.KEYCODE_DPAD_DOWN, 0, metaState);
                    break;
                case KeyEvent.KEYCODE_C:
                    event = new KeyEvent(0, 0, action, KeyEvent.KEYCODE_DPAD_RIGHT, 0, metaState);
                    break;
                case KeyEvent.KEYCODE_DEL:
                case KeyEvent.KEYCODE_ALT_LEFT:
                case KeyEvent.KEYCODE_ENTER:
                case KeyEvent.KEYCODE_SPACE:
                case KeyEvent.KEYCODE_SYM:
                case KeyEvent.KEYCODE_SHIFT_RIGHT:
                    event = new KeyEvent(0, 0, action, keycode, 0, metaState);
                    break;
                case KeyEvent.KEYCODE_SHIFT_LEFT:
                    event = new KeyEvent(0, 0, action, KeyEvent.KEYCODE_CTRL_LEFT, 0, metaState);
                    break;
            }
        } else if (mKeyboard.isType(StateKeyboard.State.Symbol2)) {
            switch (keycode) {
                case KeyEvent.KEYCODE_DEL:
                case KeyEvent.KEYCODE_ALT_LEFT:
                case KeyEvent.KEYCODE_ENTER:
                case KeyEvent.KEYCODE_SYM:
                case KeyEvent.KEYCODE_SHIFT_RIGHT:
                    event = new KeyEvent(0, 0, action, keycode, 0, metaState);
                    break;
                case KeyEvent.KEYCODE_SHIFT_LEFT:
                    event = new KeyEvent(0, 0, action, KeyEvent.KEYCODE_CTRL_LEFT, 0, metaState);
                    break;
            }
        }

        return event;
    }

    int convertKeyCode(int primaryCode) {
        int code = 0;

        switch (primaryCode) {
            case 'q':
            case '#':
                code = KeyEvent.KEYCODE_Q;
                break;
            case 'w':
            case '1':
                code = KeyEvent.KEYCODE_W;
                break;
            case 'e':
            case '2':
                code = KeyEvent.KEYCODE_E;
                break;
            case 'r':
            case '3':
                code = KeyEvent.KEYCODE_R;
                break;
            case 't':
            case '(':
                code = KeyEvent.KEYCODE_T;
                break;
            case 'y':
            case ')':
                code = KeyEvent.KEYCODE_Y;
                break;
            case 'u':
            case '_':
                code = KeyEvent.KEYCODE_U;
                break;
            case 'i':
            case '-':
                code = KeyEvent.KEYCODE_I;
                break;
            case 'o':
            case '+':
                code = KeyEvent.KEYCODE_O;
                break;
            case 'p':
            case '@':
                code = KeyEvent.KEYCODE_P;
                break;

            case 'a':
            case '*':
                code = KeyEvent.KEYCODE_A;
                break;
            case 's':
            case '4':
                code = KeyEvent.KEYCODE_S;
                break;
            case 'd':
            case '5':
                code = KeyEvent.KEYCODE_D;
                break;
            case 'f':
            case '6':
                code = KeyEvent.KEYCODE_F;
                break;
            case 'g':
            case '/':
                code = KeyEvent.KEYCODE_G;
                break;
            case 'h':
            case ':':
                code = KeyEvent.KEYCODE_H;
                break;
            case 'j':
            case ';':
                code = KeyEvent.KEYCODE_J;
                break;
            case 'k':
            case '\'':
                code = KeyEvent.KEYCODE_K;
                break;
            case 'l':
            case '"':
                code = KeyEvent.KEYCODE_L;
                break;
            case -5:
                code = KeyEvent.KEYCODE_DEL;
                break;

            case -6:
                code = KeyEvent.KEYCODE_ALT_LEFT;
                break;
            case 'z':
            case '7':
                code = KeyEvent.KEYCODE_Z;
                break;
            case 'x':
            case '8':
                code = KeyEvent.KEYCODE_X;
                break;
            case 'c':
            case '9':
                code = KeyEvent.KEYCODE_C;
                break;
            case 'v':
            case '?':
                code = KeyEvent.KEYCODE_V;
                break;
            case 'b':
            case '!':
                code = KeyEvent.KEYCODE_B;
                break;
            case 'n':
            case ',':
                code = KeyEvent.KEYCODE_N;
                break;
            case 'm':
            case '.':
                code = KeyEvent.KEYCODE_M;
                break;
            case '$':
                code = KeyEvent.KEYCODE_4;
                break;
            case 10:    // enter
                code = KeyEvent.KEYCODE_ENTER;
                break;

            case -2:    // Ctrl
                code = KeyEvent.KEYCODE_SHIFT_LEFT;
                break;
            case ' ':
                code = KeyEvent.KEYCODE_SPACE;
                break;
            case -1012: // SYM
                code = KeyEvent.KEYCODE_SYM;
                break;
            case -1:    // SHIFT
                code = KeyEvent.KEYCODE_SHIFT_RIGHT;
                break;
            case -19:    // UP
                code = KeyEvent.KEYCODE_DPAD_UP;
                break;
            case -20:    // DOWN
                code = KeyEvent.KEYCODE_DPAD_DOWN;
                break;
            case -21:    // LEFT
                code = KeyEvent.KEYCODE_DPAD_LEFT;
                break;
            case -22:    // RIGHT
                code = KeyEvent.KEYCODE_DPAD_RIGHT;
                break;
        }
        return code;
    }

}
