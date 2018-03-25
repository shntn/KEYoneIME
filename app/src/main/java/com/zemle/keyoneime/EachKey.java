package com.zemle.keyoneime;

import android.view.KeyEvent;
import android.view.inputmethod.InputConnection;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.Set;
import java.util.function.Function;

/**
 * キー入力処理
 *
 * Created by shntn on 2018/01/31.
 */

class EachKey {
    enum KeySet {
        NONE(EnumSet.noneOf(KoimeKey.Key.class)){
            boolean run(EachKey context, KoimeKey key) {
                return false;
            }
        },
        SYM(EnumSet.of(KoimeKey.Key.SYM, KoimeKey.Key.DOWN)) {
            boolean run(EachKey context, KoimeKey key) {
                return true;
            }
        },
        LOWER_Q(EnumSet.of(KoimeKey.Key.Q, KoimeKey.Key.DOWN)) {
            boolean run(EachKey context, KoimeKey key) {
                int code = 'q';
                KeyEvent event;

                // ALT, SYM, SHIFTにより入力する文字を決定
                if (key.contains(EnumSet.of(KoimeKey.Key.ALT))) {
                    code = '#';
                } else if (key.contains(EnumSet.of(KoimeKey.Key.SYM))) {
                    // ESC
                    event = new KeyEvent(
                            0,
                            0,
                            KeyEvent.ACTION_DOWN,
                            KeyEvent.KEYCODE_ESCAPE,
                            0,
                            key.makeMetaState()
                    );
                    context.sendKey(event);
                    return true;
                } else if (key.contains(EnumSet.of(KoimeKey.Key.SHIFT_RIGHT))) {
                    code = Character.toUpperCase(code);
                }
                // CTRLの同時押しの場合は入力の通知方法が変化
                if (key.contains(EnumSet.of(KoimeKey.Key.SHIFT_LEFT))) {
                    event = new KeyEvent(
                            0,
                            0,
                            KeyEvent.ACTION_DOWN,
                            KeyEvent.KEYCODE_Q,
                            0,
                            key.makeMetaState()
                    );
                    context.sendKey(event);
                } else {
                    context.pressText(code);
                }
                return true;
            }
        },
        LOWER_W(EnumSet.of(KoimeKey.Key.W, KoimeKey.Key.DOWN)) {
            boolean run(EachKey context, KoimeKey key) {
                int code = 'w';
                KeyEvent event;

                if (key.contains(EnumSet.of(KoimeKey.Key.ALT))) {
                    code = '1';
                } else if (key.contains(EnumSet.of(KoimeKey.Key.SYM))) {
                    code = '`';
                } else if (key.contains(EnumSet.of(KoimeKey.Key.SHIFT_RIGHT))) {
                    code = Character.toUpperCase(code);
                }
                if (key.contains(EnumSet.of(KoimeKey.Key.SHIFT_LEFT))) {
                    event = new KeyEvent(
                            0,
                            0,
                            KeyEvent.ACTION_DOWN,
                            KeyEvent.KEYCODE_W,
                            0,
                            key.makeMetaState()
                    );
                    context.sendKey(event);
                } else {
                    context.pressText(code);
                }
                return true;
            }
        },
        LOWER_E(EnumSet.of(KoimeKey.Key.E, KoimeKey.Key.DOWN)) {
            boolean run(EachKey context, KoimeKey key) {
                int code = 'e';
                KeyEvent event;

                if (key.contains(EnumSet.of(KoimeKey.Key.ALT))) {
                    code = '2';
                } else if (key.contains(EnumSet.of(KoimeKey.Key.SYM))) {
                    // ALLOW UP
                    event = new KeyEvent(
                            0,
                            0,
                            KeyEvent.ACTION_DOWN,
                            KeyEvent.KEYCODE_DPAD_UP,
                            0,
                            key.makeMetaState()
                    );
                    context.sendKey(event);
                    return true;
                } else if (key.contains(EnumSet.of(KoimeKey.Key.SHIFT_RIGHT))) {
                    code = Character.toUpperCase(code);
                }
                if (key.contains(EnumSet.of(KoimeKey.Key.SHIFT_LEFT))) {
                    event = new KeyEvent(
                            0,
                            0,
                            KeyEvent.ACTION_DOWN,
                            KeyEvent.KEYCODE_E,
                            0,
                            key.makeMetaState()
                    );
                    context.sendKey(event);
                } else {
                    context.pressText(code);
                }
                return true;
            }
        },
        LOWER_R(EnumSet.of(KoimeKey.Key.R, KoimeKey.Key.DOWN)) {
            boolean run(EachKey context, KoimeKey key) {
                int code = 'r';
                KeyEvent event;

                if (key.contains(EnumSet.of(KoimeKey.Key.ALT))) {
                    code = '3';
                } else if (key.contains(EnumSet.of(KoimeKey.Key.SYM))) {
                    code = '&';
                } else if (key.contains(EnumSet.of(KoimeKey.Key.SHIFT_RIGHT))) {
                    code = Character.toUpperCase(code);
                }
                if (key.contains(EnumSet.of(KoimeKey.Key.SHIFT_LEFT))) {
                    event = new KeyEvent(
                            0,
                            0,
                            KeyEvent.ACTION_DOWN,
                            KeyEvent.KEYCODE_R,
                            0,
                            key.makeMetaState()
                    );
                    context.sendKey(event);
                } else {
                    context.pressText(code);
                }
                return true;
            }
        },
        LOWER_T(EnumSet.of(KoimeKey.Key.T, KoimeKey.Key.DOWN)) {
            boolean run(EachKey context, KoimeKey key) {
                int code = 't';
                KeyEvent event;

                if (key.contains(EnumSet.of(KoimeKey.Key.ALT))) {
                    code = '(';
                } else if (key.contains(EnumSet.of(KoimeKey.Key.SYM))) {
                    code = '<';
                } else if (key.contains(EnumSet.of(KoimeKey.Key.SHIFT_RIGHT))) {
                    code = Character.toUpperCase(code);
                }
                if (key.contains(EnumSet.of(KoimeKey.Key.SHIFT_LEFT))) {
                    event = new KeyEvent(
                            0,
                            0,
                            KeyEvent.ACTION_DOWN,
                            KeyEvent.KEYCODE_T,
                            0,
                            key.makeMetaState()
                    );
                    context.sendKey(event);
                } else {
                    context.pressText(code);
                }
                return true;
            }
        },
        LOWER_Y(EnumSet.of(KoimeKey.Key.Y, KoimeKey.Key.DOWN)) {
            boolean run(EachKey context, KoimeKey key) {
                int code = 'y';
                KeyEvent event;

                if (key.contains(EnumSet.of(KoimeKey.Key.ALT))) {
                    code = ')';
                } else if (key.contains(EnumSet.of(KoimeKey.Key.SYM))) {
                    code = '>';
                } else if (key.contains(EnumSet.of(KoimeKey.Key.SHIFT_RIGHT))) {
                    code = Character.toUpperCase(code);
                }
                if (key.contains(EnumSet.of(KoimeKey.Key.SHIFT_LEFT))) {
                    event = new KeyEvent(
                            0,
                            0,
                            KeyEvent.ACTION_DOWN,
                            KeyEvent.KEYCODE_Y,
                            0,
                            key.makeMetaState()
                    );
                    context.sendKey(event);
                } else {
                    context.pressText(code);
                }
                return true;
            }
        },
        LOWER_U(EnumSet.of(KoimeKey.Key.U, KoimeKey.Key.DOWN)) {
            boolean run(EachKey context, KoimeKey key) {
                int code = 'u';
                KeyEvent event;

                if (key.contains(EnumSet.of(KoimeKey.Key.ALT))) {
                    code = '_';
                } else if (key.contains(EnumSet.of(KoimeKey.Key.SYM))) {
                    code = '[';
                } else if (key.contains(EnumSet.of(KoimeKey.Key.SHIFT_RIGHT))) {
                    code = Character.toUpperCase(code);
                }
                if (key.contains(EnumSet.of(KoimeKey.Key.SHIFT_LEFT))) {
                    event = new KeyEvent(
                            0,
                            0,
                            KeyEvent.ACTION_DOWN,
                            KeyEvent.KEYCODE_U,
                            0,
                            key.makeMetaState()
                    );
                    context.sendKey(event);
                } else {
                    context.pressText(code);
                }
                return true;
            }
        },
        LOWER_I(EnumSet.of(KoimeKey.Key.I, KoimeKey.Key.DOWN)) {
            boolean run(EachKey context, KoimeKey key) {
                int code = 'i';
                KeyEvent event;

                if (key.contains(EnumSet.of(KoimeKey.Key.ALT))) {
                    code = '-';
                } else if (key.contains(EnumSet.of(KoimeKey.Key.SYM))) {
                    code = ']';
                } else if (key.contains(EnumSet.of(KoimeKey.Key.SHIFT_RIGHT))) {
                    code = Character.toUpperCase(code);
                }
                if (key.contains(EnumSet.of(KoimeKey.Key.SHIFT_LEFT))) {
                    event = new KeyEvent(
                            0,
                            0,
                            KeyEvent.ACTION_DOWN,
                            KeyEvent.KEYCODE_I,
                            0,
                            key.makeMetaState()
                    );
                    context.sendKey(event);
                } else {
                    context.pressText(code);
                }
                return true;
            }
        },
        LOWER_O(EnumSet.of(KoimeKey.Key.O, KoimeKey.Key.DOWN)) {
            boolean run(EachKey context, KoimeKey key) {
                int code = 'o';
                KeyEvent event;

                if (key.contains(EnumSet.of(KoimeKey.Key.ALT))) {
                    code = '+';
                } else if (key.contains(EnumSet.of(KoimeKey.Key.SYM))) {
                    code = '{';
                } else if (key.contains(EnumSet.of(KoimeKey.Key.SHIFT_RIGHT))) {
                    code = Character.toUpperCase(code);
                }
                if (key.contains(EnumSet.of(KoimeKey.Key.SHIFT_LEFT))) {
                    event = new KeyEvent(
                            0,
                            0,
                            KeyEvent.ACTION_DOWN,
                            KeyEvent.KEYCODE_O,
                            0,
                            key.makeMetaState()
                    );
                    context.sendKey(event);
                } else {
                    context.pressText(code);
                }
                return true;
            }
        },
        LOWER_P(EnumSet.of(KoimeKey.Key.P, KoimeKey.Key.DOWN)) {
            boolean run(EachKey context, KoimeKey key) {
                int code = 'p';
                KeyEvent event;

                if (key.contains(EnumSet.of(KoimeKey.Key.ALT))) {
                    code = '@';
                } else if (key.contains(EnumSet.of(KoimeKey.Key.SYM))) {
                    code = '}';
                } else if (key.contains(EnumSet.of(KoimeKey.Key.SHIFT_RIGHT))) {
                    code = Character.toUpperCase(code);
                }
                if (key.contains(EnumSet.of(KoimeKey.Key.SHIFT_LEFT))) {
                    event = new KeyEvent(
                            0,
                            0,
                            KeyEvent.ACTION_DOWN,
                            KeyEvent.KEYCODE_P,
                            0,
                            key.makeMetaState()
                    );
                    context.sendKey(event);
                } else {
                    context.pressText(code);
                }
                return true;
            }
        },

        LOWER_A(EnumSet.of(KoimeKey.Key.A, KoimeKey.Key.DOWN)) {
            boolean run(EachKey context, KoimeKey key) {
                int code = 'a';
                KeyEvent event;

                if (key.contains(EnumSet.of(KoimeKey.Key.ALT))) {
                    code = '*';
                } else if (key.contains(EnumSet.of(KoimeKey.Key.SYM))) {
                    // TAB
                    event = new KeyEvent(
                            0,
                            0,
                            KeyEvent.ACTION_DOWN,
                            KeyEvent.KEYCODE_TAB,
                            0,
                            key.makeMetaState()
                    );
                    context.sendKey(event);
                    return true;
                } else if (key.contains(EnumSet.of(KoimeKey.Key.SHIFT_RIGHT))) {
                    code = Character.toUpperCase(code);
                }
                if (key.contains(EnumSet.of(KoimeKey.Key.SHIFT_LEFT))) {
                    event = new KeyEvent(
                            0,
                            0,
                            KeyEvent.ACTION_DOWN,
                            KeyEvent.KEYCODE_A,
                            0,
                            key.makeMetaState()
                    );
                    context.sendKey(event);
                } else {
                    context.pressText(code);
                }
                return true;
            }
        },
        LOWER_S(EnumSet.of(KoimeKey.Key.S, KoimeKey.Key.DOWN)) {
            boolean run(EachKey context, KoimeKey key) {
                int code = 's';
                KeyEvent event;

                if (key.contains(EnumSet.of(KoimeKey.Key.ALT))) {
                    code = '4';
                } else if (key.contains(EnumSet.of(KoimeKey.Key.SYM))) {
                    // ALLOW LEFT
                    event = new KeyEvent(
                            0,
                            0,
                            KeyEvent.ACTION_DOWN,
                            KeyEvent.KEYCODE_DPAD_LEFT,
                            0,
                            key.makeMetaState()
                    );
                    context.sendKey(event);
                    return true;
                } else if (key.contains(EnumSet.of(KoimeKey.Key.SHIFT_RIGHT))) {
                    code = Character.toUpperCase(code);
                }
                if (key.contains(EnumSet.of(KoimeKey.Key.SHIFT_LEFT))) {
                    event = new KeyEvent(
                            0,
                            0,
                            KeyEvent.ACTION_DOWN,
                            KeyEvent.KEYCODE_S,
                            0,
                            key.makeMetaState()
                    );
                    context.sendKey(event);
                } else {
                    context.pressText(code);
                }
                return true;
            }
        },
        LOWER_D(EnumSet.of(KoimeKey.Key.D, KoimeKey.Key.DOWN)) {
            boolean run(EachKey context, KoimeKey key) {
                int code = 'd';
                KeyEvent event;

                if (key.contains(EnumSet.of(KoimeKey.Key.ALT))) {
                    code = '5';
                } else if (key.contains(EnumSet.of(KoimeKey.Key.SYM))) {
                    // ALLOW DOWN
                    event = new KeyEvent(
                            0,
                            0,
                            KeyEvent.ACTION_DOWN,
                            KeyEvent.KEYCODE_DPAD_DOWN,
                            0,
                            key.makeMetaState()
                    );
                    context.sendKey(event);
                    return true;
                } else if (key.contains(EnumSet.of(KoimeKey.Key.SHIFT_RIGHT))) {
                    code = Character.toUpperCase(code);
                }
                if (key.contains(EnumSet.of(KoimeKey.Key.SHIFT_LEFT))) {
                    event = new KeyEvent(
                            0,
                            0,
                            KeyEvent.ACTION_DOWN,
                            KeyEvent.KEYCODE_D,
                            0,
                            key.makeMetaState()
                    );
                    context.sendKey(event);
                } else {
                    context.pressText(code);
                }
                return true;
            }
        },
        LOWER_F(EnumSet.of(KoimeKey.Key.F, KoimeKey.Key.DOWN)) {
            boolean run(EachKey context, KoimeKey key) {
                int code = 'f';
                KeyEvent event;

                if (key.contains(EnumSet.of(KoimeKey.Key.ALT))) {
                    code = '6';
                } else if (key.contains(EnumSet.of(KoimeKey.Key.SYM))) {
                    // ALLOW RIGHT
                    event = new KeyEvent(
                            0,
                            0,
                            KeyEvent.ACTION_DOWN,
                            KeyEvent.KEYCODE_DPAD_RIGHT,
                            0,
                            key.makeMetaState()
                    );
                    context.sendKey(event);
                    return true;
                } else if (key.contains(EnumSet.of(KoimeKey.Key.SHIFT_RIGHT))) {
                    code = Character.toUpperCase(code);
                }
                if (key.contains(EnumSet.of(KoimeKey.Key.SHIFT_LEFT))) {
                    event = new KeyEvent(
                            0,
                            0,
                            KeyEvent.ACTION_DOWN,
                            KeyEvent.KEYCODE_F,
                            0,
                            key.makeMetaState()
                    );
                    context.sendKey(event);
                } else {
                    context.pressText(code);
                }
                return true;
            }
        },
        LOWER_G(EnumSet.of(KoimeKey.Key.G, KoimeKey.Key.DOWN)) {
            boolean run(EachKey context, KoimeKey key) {
                int code = 'g';
                KeyEvent event;

                if (key.contains(EnumSet.of(KoimeKey.Key.ALT))) {
                    code = '/';
                } else if (key.contains(EnumSet.of(KoimeKey.Key.SYM))) {
                    code = '|';
                } else if (key.contains(EnumSet.of(KoimeKey.Key.SHIFT_RIGHT))) {
                    code = Character.toUpperCase(code);
                }
                if (key.contains(EnumSet.of(KoimeKey.Key.SHIFT_LEFT))) {
                    event = new KeyEvent(
                            0,
                            0,
                            KeyEvent.ACTION_DOWN,
                            KeyEvent.KEYCODE_G,
                            0,
                            key.makeMetaState()
                    );
                    context.sendKey(event);
                } else {
                    context.pressText(code);
                }
                return true;
            }
        },
        LOWER_H(EnumSet.of(KoimeKey.Key.H, KoimeKey.Key.DOWN)) {
            boolean run(EachKey context, KoimeKey key) {
                int code = 'h';
                KeyEvent event;

                if (key.contains(EnumSet.of(KoimeKey.Key.ALT))) {
                    code = ':';
                } else if (key.contains(EnumSet.of(KoimeKey.Key.SYM))) {
                    code = '\\';
                } else if (key.contains(EnumSet.of(KoimeKey.Key.SHIFT_RIGHT))) {
                    code = Character.toUpperCase(code);
                }
                if (key.contains(EnumSet.of(KoimeKey.Key.SHIFT_LEFT))) {
                    event = new KeyEvent(
                            0,
                            0,
                            KeyEvent.ACTION_DOWN,
                            KeyEvent.KEYCODE_H,
                            0,
                            key.makeMetaState()
                    );
                    context.sendKey(event);
                } else {
                    context.pressText(code);
                }
                return true;
            }
        },
        LOWER_J(EnumSet.of(KoimeKey.Key.J, KoimeKey.Key.DOWN)) {
            boolean run(EachKey context, KoimeKey key) {
                int code = 'j';
                KeyEvent event;

                if (key.contains(EnumSet.of(KoimeKey.Key.ALT))) {
                    code = ';';
                } else if (key.contains(EnumSet.of(KoimeKey.Key.SYM))) {
                    code = '=';
                } else if (key.contains(EnumSet.of(KoimeKey.Key.SHIFT_RIGHT))) {
                    code = Character.toUpperCase(code);
                }
                if (key.contains(EnumSet.of(KoimeKey.Key.SHIFT_LEFT))) {
                    event = new KeyEvent(
                            0,
                            0,
                            KeyEvent.ACTION_DOWN,
                            KeyEvent.KEYCODE_J,
                            0,
                            key.makeMetaState()
                    );
                    context.sendKey(event);
                } else {
                    context.pressText(code);
                }
                return true;
            }
        },
        LOWER_K(EnumSet.of(KoimeKey.Key.K, KoimeKey.Key.DOWN)) {
            boolean run(EachKey context, KoimeKey key) {
                int code = 'k';
                KeyEvent event;

                if (key.contains(EnumSet.of(KoimeKey.Key.ALT))) {
                    code = '\'';
                } else if (key.contains(EnumSet.of(KoimeKey.Key.SYM))) {
                    code = '^';
                } else if (key.contains(EnumSet.of(KoimeKey.Key.SHIFT_RIGHT))) {
                    code = Character.toUpperCase(code);
                }
                if (key.contains(EnumSet.of(KoimeKey.Key.SHIFT_LEFT))) {
                    event = new KeyEvent(
                            0,
                            0,
                            KeyEvent.ACTION_DOWN,
                            KeyEvent.KEYCODE_K,
                            0,
                            key.makeMetaState()
                    );
                    context.sendKey(event);
                } else {
                    context.pressText(code);
                }
                return true;
            }
        },
        LOWER_L(EnumSet.of(KoimeKey.Key.L, KoimeKey.Key.DOWN)) {
            boolean run(EachKey context, KoimeKey key) {
                int code = 'l';
                KeyEvent event;

                if (key.contains(EnumSet.of(KoimeKey.Key.ALT))) {
                    code = '"';
                } else if (key.contains(EnumSet.of(KoimeKey.Key.SYM))) {
                    code = '%';
                } else if (key.contains(EnumSet.of(KoimeKey.Key.SHIFT_RIGHT))) {
                    code = Character.toUpperCase(code);
                }
                if (key.contains(EnumSet.of(KoimeKey.Key.SHIFT_LEFT))) {
                    event = new KeyEvent(
                            0,
                            0,
                            KeyEvent.ACTION_DOWN,
                            KeyEvent.KEYCODE_L,
                            0,
                            key.makeMetaState()
                    );
                    context.sendKey(event);
                } else {
                    context.pressText(code);
                }
                return true;
            }
        },
        LOWER_Z(EnumSet.of(KoimeKey.Key.Z, KoimeKey.Key.DOWN)) {
            boolean run(EachKey context, KoimeKey key) {
                int code = 'z';
                KeyEvent event;

                if (key.contains(EnumSet.of(KoimeKey.Key.ALT))) {
                    code = '7';
                } else if (key.contains(EnumSet.of(KoimeKey.Key.SYM))) {
                    code = '~';
                } else if (key.contains(EnumSet.of(KoimeKey.Key.SHIFT_RIGHT))) {
                    code = Character.toUpperCase(code);
                }
                if (key.contains(EnumSet.of(KoimeKey.Key.SHIFT_LEFT))) {
                    event = new KeyEvent(
                            0,
                            0,
                            KeyEvent.ACTION_DOWN,
                            KeyEvent.KEYCODE_Z,
                            0,
                            key.makeMetaState()
                    );
                    context.sendKey(event);
                } else {
                    context.pressText(code);
                }
                return true;
            }
        },
        LOWER_X(EnumSet.of(KoimeKey.Key.X, KoimeKey.Key.DOWN)) {
            boolean run(EachKey context, KoimeKey key) {
                int code = 'x';
                KeyEvent event;

                if (key.contains(EnumSet.of(KoimeKey.Key.ALT))) {
                    code = '8';
                } else if (key.contains(EnumSet.of(KoimeKey.Key.SYM))) {
                    return false;
                } else if (key.contains(EnumSet.of(KoimeKey.Key.SHIFT_RIGHT))) {
                    code = Character.toUpperCase(code);
                }
                if (key.contains(EnumSet.of(KoimeKey.Key.SHIFT_LEFT))) {
                    event = new KeyEvent(
                            0,
                            0,
                            KeyEvent.ACTION_DOWN,
                            KeyEvent.KEYCODE_X,
                            0,
                            key.makeMetaState()
                    );
                    context.sendKey(event);
                } else {
                    context.pressText(code);
                }
                return true;
            }
        },
        LOWER_C(EnumSet.of(KoimeKey.Key.C, KoimeKey.Key.DOWN)) {
            boolean run(EachKey context, KoimeKey key) {
                int code = 'c';
                KeyEvent event;

                if (key.contains(EnumSet.of(KoimeKey.Key.ALT))) {
                    code = '9';
                } else if (key.contains(EnumSet.of(KoimeKey.Key.SYM))) {
                    return false;
                } else if (key.contains(EnumSet.of(KoimeKey.Key.SHIFT_RIGHT))) {
                    code = Character.toUpperCase(code);
                }
                if (key.contains(EnumSet.of(KoimeKey.Key.SHIFT_LEFT))) {
                    event = new KeyEvent(
                            0,
                            0,
                            KeyEvent.ACTION_DOWN,
                            KeyEvent.KEYCODE_C,
                            0,
                            key.makeMetaState()
                    );
                    context.sendKey(event);
                } else {
                    context.pressText(code);
                }
                return true;
            }
        },
        LOWER_V(EnumSet.of(KoimeKey.Key.V, KoimeKey.Key.DOWN)) {
            boolean run(EachKey context, KoimeKey key) {
                int code = 'v';
                KeyEvent event;

                if (key.contains(EnumSet.of(KoimeKey.Key.ALT))) {
                    code = '?';
                } else if (key.contains(EnumSet.of(KoimeKey.Key.SYM))) {
                    return false;
                } else if (key.contains(EnumSet.of(KoimeKey.Key.SHIFT_RIGHT))) {
                    code = Character.toUpperCase(code);
                }
                if (key.contains(EnumSet.of(KoimeKey.Key.SHIFT_LEFT))) {
                    event = new KeyEvent(
                            0,
                            0,
                            KeyEvent.ACTION_DOWN,
                            KeyEvent.KEYCODE_V,
                            0,
                            key.makeMetaState()
                    );
                    context.sendKey(event);
                } else {
                    context.pressText(code);
                }
                return true;
            }
        },
        LOWER_B(EnumSet.of(KoimeKey.Key.B, KoimeKey.Key.DOWN)) {
            boolean run(EachKey context, KoimeKey key) {
                int code = 'b';
                KeyEvent event;

                if (key.contains(EnumSet.of(KoimeKey.Key.ALT))) {
                    code = '!';
                } else if (key.contains(EnumSet.of(KoimeKey.Key.SYM))) {
                    return false;
                } else if (key.contains(EnumSet.of(KoimeKey.Key.SHIFT_RIGHT))) {
                    code = Character.toUpperCase(code);
                }
                if (key.contains(EnumSet.of(KoimeKey.Key.SHIFT_LEFT))) {
                    event = new KeyEvent(
                            0,
                            0,
                            KeyEvent.ACTION_DOWN,
                            KeyEvent.KEYCODE_B,
                            0,
                            key.makeMetaState()
                    );
                    context.sendKey(event);
                } else {
                    context.pressText(code);
                }
                return true;
            }
        },
        LOWER_N(EnumSet.of(KoimeKey.Key.N, KoimeKey.Key.DOWN)) {
            boolean run(EachKey context, KoimeKey key) {
                int code = 'n';
                KeyEvent event;

                if (key.contains(EnumSet.of(KoimeKey.Key.ALT))) {
                    code = ',';
                } else if (key.contains(EnumSet.of(KoimeKey.Key.SYM))) {
                    return false;
                } else if (key.contains(EnumSet.of(KoimeKey.Key.SHIFT_RIGHT))) {
                    code = Character.toUpperCase(code);
                }
                if (key.contains(EnumSet.of(KoimeKey.Key.SHIFT_LEFT))) {
                    event = new KeyEvent(
                            0,
                            0,
                            KeyEvent.ACTION_DOWN,
                            KeyEvent.KEYCODE_N,
                            0,
                            key.makeMetaState()
                    );
                    context.sendKey(event);
                } else {
                    context.pressText(code);
                }
                return true;
            }
        },
        LOWER_M(EnumSet.of(KoimeKey.Key.M, KoimeKey.Key.DOWN)) {
            boolean run(EachKey context, KoimeKey key) {
                int code = 'm';
                KeyEvent event;

                if (key.contains(EnumSet.of(KoimeKey.Key.ALT))) {
                    code = '.';
                } else if (key.contains(EnumSet.of(KoimeKey.Key.SYM))) {
                    return false;
                } else if (key.contains(EnumSet.of(KoimeKey.Key.SHIFT_RIGHT))) {
                    code = Character.toUpperCase(code);
                }
                if (key.contains(EnumSet.of(KoimeKey.Key.SHIFT_LEFT))) {
                    event = new KeyEvent(
                            0,
                            0,
                            KeyEvent.ACTION_DOWN,
                            KeyEvent.KEYCODE_M,
                            0,
                            key.makeMetaState()
                    );
                    context.sendKey(event);
                } else {
                    context.pressText(code);
                }
                return true;
            }
        },
        LOWER_4(EnumSet.of(KoimeKey.Key.NUM_4, KoimeKey.Key.DOWN)) {
            boolean run(EachKey context, KoimeKey key) {
                int code;
                KeyEvent event;

                if (key.contains(EnumSet.of(KoimeKey.Key.ALT))) {
                    code = '$';
                    context.pressText(code);
                } else if (key.contains(EnumSet.of(KoimeKey.Key.SYM))) {
                    return false;
                } else if (key.contains(EnumSet.of(KoimeKey.Key.SHIFT_RIGHT))) {
                    return false;
                }
                if (key.contains(EnumSet.of(KoimeKey.Key.SHIFT_LEFT))) {
                    event = new KeyEvent(
                            0,
                            0,
                            KeyEvent.ACTION_DOWN,
                            KeyEvent.KEYCODE_4,
                            0,
                            key.makeMetaState()
                    );
                    context.sendKey(event);
                }
                return true;
            }
        },
        LOWER_0(EnumSet.of(KoimeKey.Key.NUM_0, KoimeKey.Key.DOWN)) {
            boolean run(EachKey context, KoimeKey key) {
                int code;
                KeyEvent event;

                if (key.contains(EnumSet.of(KoimeKey.Key.ALT))) {
                    code = '0';
                    context.pressText(code);
                } else if (key.contains(EnumSet.of(KoimeKey.Key.SYM))) {
                    return false;
                } else if (key.contains(EnumSet.of(KoimeKey.Key.SHIFT_RIGHT))) {
                    return false;
                }
                if (key.contains(EnumSet.of(KoimeKey.Key.SHIFT_LEFT))) {
                    event = new KeyEvent(
                            0,
                            0,
                            KeyEvent.ACTION_DOWN,
                            KeyEvent.KEYCODE_0,
                            0,
                            key.makeMetaState()
                    );
                    context.sendKey(event);
                }
                return true;
            }
        };

        private final EnumSet<KoimeKey.Key> key;
        boolean run(EachKey context, KoimeKey key) {
            return false;
        }
        KeySet(EnumSet<KoimeKey.Key> key) {
            this.key  = key;
        }

        public EnumSet<KoimeKey.Key> getKey() {
            return key;
        }

        public static KeySet getByKey(Set<KoimeKey.Key> key) {
            return Arrays.stream(values())
                    .filter(s -> s.getKey().equals(key))
                    .findFirst()
                    .orElse(KeySet.NONE);
        }
    }

    private KoimeService mService;

    EachKey(KoimeService service) {
        mService = service;
    }

    boolean run(KoimeKey key) {
        KoimeKey    key_clone;
        boolean     result;

        key_clone = key.clone();
        if (key_clone.isPressed(KoimeKey.Key.SYM)) {
            result = true;
        } else {
            key_clone.remove(EnumSet.of(
                    KoimeKey.Key.SHIFT_LEFT,
                    KoimeKey.Key.SHIFT_RIGHT,
                    KoimeKey.Key.ALT,
                    KoimeKey.Key.SYM
            ));
            KeySet keySet = KeySet.getByKey(key_clone.getBySet());
            result = keySet.run(this, key);
        }
        return result;
    }

    private void sendKey(KeyEvent event) {
        InputConnection ic = mService.getCurrentInputConnection();

        if (ic == null) { return; }
        ic.sendKeyEvent(event);
    }

    private void pressText(int charactorCode) {
        mService.getCurrentInputConnection().commitText(
                String.valueOf((char) charactorCode),
                1
        );
    }
}
