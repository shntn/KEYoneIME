package com.zemle.keyoneime;

import android.view.KeyEvent;

/**
 * ソフトキーボードの表示状態と遷移を管理。
 * ソフトキーボードが表示されている状態でハードキーが押された場合、
 * 対応する文字コードを返す。
 *
 * Created by shntn on 2018/01/02.
 */

enum StateKeyboard {
    Qwerty {
        @Override
        public void enter(KeyboardViewQwerty keyboardview) {
            keyboardview.setInputViewQwerty();
        }

        @Override
        public void pushSoftSYM(StateKeyboardFrame context) {
            context.changeState(Symbol1);
        }

        @Override
        public void pushHardSYM(StateKeyboardFrame context) {
            context.changeState(HideQwerty);
        }

        @Override
        public void downHardALT(StateKeyboardFrame context) {
            context.changeState(Symbol2);
        }

        @Override
        public int converKeycode(int keycode) {
            int c = keycode;

            switch (keycode) {
                case KeyEvent.KEYCODE_Q: c =113; break;     // q
                case KeyEvent.KEYCODE_W: c =119; break;     // w
                case KeyEvent.KEYCODE_E: c =101; break;     // e
                case KeyEvent.KEYCODE_R: c =114; break;     // r
                case KeyEvent.KEYCODE_T: c =116; break;     // t
                case KeyEvent.KEYCODE_Y: c =121; break;     // y
                case KeyEvent.KEYCODE_U: c =117; break;     // u
                case KeyEvent.KEYCODE_I: c =105; break;     // i
                case KeyEvent.KEYCODE_O: c =111; break;     // o
                case KeyEvent.KEYCODE_P: c =112; break;     // p

                case KeyEvent.KEYCODE_A: c = 97; break;     // a
                case KeyEvent.KEYCODE_S: c =115; break;     // s
                case KeyEvent.KEYCODE_D: c =100; break;     // d
                case KeyEvent.KEYCODE_F: c =102; break;     // f
                case KeyEvent.KEYCODE_G: c =103; break;     // g
                case KeyEvent.KEYCODE_H: c =104; break;     // h
                case KeyEvent.KEYCODE_J: c =106; break;     // j
                case KeyEvent.KEYCODE_K: c =107; break;     // k
                case KeyEvent.KEYCODE_L: c =108; break;     // l
                case KeyEvent.KEYCODE_DEL: c = 8; break;   // bs

                case KeyEvent.KEYCODE_Z: c =122; break;     // z
                case KeyEvent.KEYCODE_X: c =120; break;     // x
                case KeyEvent.KEYCODE_C: c = 99; break;     // c
                case KeyEvent.KEYCODE_V: c =118; break;     // v
                case KeyEvent.KEYCODE_B: c = 98; break;     // b
                case KeyEvent.KEYCODE_N: c =110; break;     // n
                case KeyEvent.KEYCODE_M: c =109; break;     // m
                case 11: c = 36; break;                     // $
                case KeyEvent.KEYCODE_ENTER: c = 10; break; // ENTER

                case KeyEvent.KEYCODE_0: c = 48; break;     // 0
                case KeyEvent.KEYCODE_SPACE: c = 32; break; // SPACE
                default: c = keycode;
            }
            return c;
        }
    },
    Symbol1 {
        public void enter(KeyboardViewQwerty keyboardview) {
            keyboardview.setInputViewSymbol1();
        }

        @Override
        public void pushSoftSYM(StateKeyboardFrame context) {
            context.changeState(Symbol2);
        }

        @Override
        public void pushHardSYM(StateKeyboardFrame context) {
            context.changeState(Symbol2);
        }

        @Override
        public void downHardALT(StateKeyboardFrame context) {
            context.changeState(Symbol2);
        }

        @Override
        public StateKeyboard getType() {
            return StateKeyboard.Symbol1;
        }

        @Override
        public int converKeycode(int keycode) {
            int c = keycode;

            switch (keycode) {
                case KeyEvent.KEYCODE_Q: c = 27; break;     // esc
                case KeyEvent.KEYCODE_W: c =  0; break;     //
                case KeyEvent.KEYCODE_E: c =  0; break;     //
                case KeyEvent.KEYCODE_R: c =  0; break;     //
                case KeyEvent.KEYCODE_T: c =  0; break;     //
                case KeyEvent.KEYCODE_Y: c = 96; break;     // `
                case KeyEvent.KEYCODE_U: c = 94; break;     // ^
                case KeyEvent.KEYCODE_I: c = 60; break;     // <
                case KeyEvent.KEYCODE_O: c = 62; break;     // >
                case KeyEvent.KEYCODE_P: c = 37; break;     // %

                case KeyEvent.KEYCODE_A: c =  9; break;     // tab
                case KeyEvent.KEYCODE_S: c =  0; break;     //
                case KeyEvent.KEYCODE_D: c =-19; break;     // ↑
                case KeyEvent.KEYCODE_F: c =  0; break;     //
                case KeyEvent.KEYCODE_G: c = 92; break;     // \
                case KeyEvent.KEYCODE_H: c =124; break;     // |
                case KeyEvent.KEYCODE_J: c = 38; break;     // &
                case KeyEvent.KEYCODE_K: c = 91; break;     // [
                case KeyEvent.KEYCODE_L: c = 93; break;     // ]
                case KeyEvent.KEYCODE_DEL: c = 8; break;   // bs

                case KeyEvent.KEYCODE_Z: c =-21; break;     // ←
                case KeyEvent.KEYCODE_X: c =-20; break;     // ↓
                case KeyEvent.KEYCODE_C: c =-22; break;     // →
                case KeyEvent.KEYCODE_V: c =  0; break;     //
                case KeyEvent.KEYCODE_B: c =126; break;     // ~
                case KeyEvent.KEYCODE_N: c = 61; break;     // =
                case KeyEvent.KEYCODE_M: c =123; break;     // {
                case 11: c =125; break;                     // }
                case KeyEvent.KEYCODE_ENTER: c = 10; break; // ENTER

                case KeyEvent.KEYCODE_0: c = 48; break;     // 0
                case KeyEvent.KEYCODE_SPACE: c = 32; break; // SPACE
                default: c = keycode;
            }
            return c;
        }
    },
    Symbol2 {
        public void enter(KeyboardViewQwerty keyboardview) {
            keyboardview.setInputViewSymbol2();
        }

        @Override
        public void pushSoftSYM(StateKeyboardFrame context) {
            context.changeState(Qwerty);
        }

        @Override
        public void pushHardSYM(StateKeyboardFrame context) {
            context.changeState(HideQwerty);
        }

        @Override
        public void upHardALT(StateKeyboardFrame context) {
            context.changeState(HideQwerty);
        }

        @Override
        public StateKeyboard getType() {
            return StateKeyboard.Symbol2;
        }

        @Override
        public int converKeycode(int keycode) {
            int c = keycode;

            switch (keycode) {
                case KeyEvent.KEYCODE_Q: c = 35; break;     // #
                case KeyEvent.KEYCODE_W: c = 49; break;     // 1
                case KeyEvent.KEYCODE_E: c = 50; break;     // 2
                case KeyEvent.KEYCODE_R: c = 51; break;     // 3
                case KeyEvent.KEYCODE_T: c = 40; break;     // (
                case KeyEvent.KEYCODE_Y: c = 41; break;     // )
                case KeyEvent.KEYCODE_U: c = 95; break;     // _
                case KeyEvent.KEYCODE_I: c = 45; break;     // -
                case KeyEvent.KEYCODE_O: c = 43; break;     // +
                case KeyEvent.KEYCODE_P: c = 64; break;     // @

                case KeyEvent.KEYCODE_A: c = 42; break;     // *
                case KeyEvent.KEYCODE_S: c = 52; break;     // 4
                case KeyEvent.KEYCODE_D: c = 53; break;     // 5
                case KeyEvent.KEYCODE_F: c = 54; break;     // 6
                case KeyEvent.KEYCODE_G: c = 47; break;     // /
                case KeyEvent.KEYCODE_H: c = 58; break;     // :
                case KeyEvent.KEYCODE_J: c = 59; break;     // ;
                case KeyEvent.KEYCODE_K: c = 39; break;     // '
                case KeyEvent.KEYCODE_L: c = 34; break;     // "
                case KeyEvent.KEYCODE_DEL: c = 8; break;   // bs

                case KeyEvent.KEYCODE_Z: c = 55; break;     // 7
                case KeyEvent.KEYCODE_X: c = 56; break;     // 8
                case KeyEvent.KEYCODE_C: c = 57; break;     // 9
                case KeyEvent.KEYCODE_V: c = 63; break;     // ?
                case KeyEvent.KEYCODE_B: c = 33; break;     // !
                case KeyEvent.KEYCODE_N: c = 44; break;     // ,
                case KeyEvent.KEYCODE_M: c = 46; break;     // .
                case 11: c = 36; break;                     // $
                case KeyEvent.KEYCODE_ENTER: c = 10; break; // ENTER

                case KeyEvent.KEYCODE_0: c = 48; break;     // 0
                case KeyEvent.KEYCODE_SPACE: c = 32; break; // SPACE
                default: c = keycode;
            }
            return c;
        }
    },
    HideQwerty {
        public void enter(KeyboardViewQwerty keyboardview) {
            keyboardview.setInputViewHide();
        }

        @Override
        public void pushSoftSYM(StateKeyboardFrame context) {
            context.changeState(Qwerty);
        }

        @Override
        public void pushHardSYM(StateKeyboardFrame context) {
            context.changeState(Symbol1);
        }

        @Override
        public void downHardALT(StateKeyboardFrame context) {
            context.changeState(HideSymbol2);
        }

        @Override
        public StateKeyboard getType() {
            return StateKeyboard.Qwerty;
        }

        @Override
        public int converKeycode(int keycode) {
            int c = keycode;

            switch (keycode) {
                case KeyEvent.KEYCODE_Q: c =113; break;     // q
                case KeyEvent.KEYCODE_W: c =119; break;     // w
                case KeyEvent.KEYCODE_E: c =101; break;     // e
                case KeyEvent.KEYCODE_R: c =114; break;     // r
                case KeyEvent.KEYCODE_T: c =116; break;     // t
                case KeyEvent.KEYCODE_Y: c =121; break;     // y
                case KeyEvent.KEYCODE_U: c =117; break;     // u
                case KeyEvent.KEYCODE_I: c =105; break;     // i
                case KeyEvent.KEYCODE_O: c =111; break;     // o
                case KeyEvent.KEYCODE_P: c =112; break;     // p

                case KeyEvent.KEYCODE_A: c = 97; break;     // a
                case KeyEvent.KEYCODE_S: c =115; break;     // s
                case KeyEvent.KEYCODE_D: c =100; break;     // d
                case KeyEvent.KEYCODE_F: c =102; break;     // f
                case KeyEvent.KEYCODE_G: c =103; break;     // g
                case KeyEvent.KEYCODE_H: c =104; break;     // h
                case KeyEvent.KEYCODE_J: c =106; break;     // j
                case KeyEvent.KEYCODE_K: c =107; break;     // k
                case KeyEvent.KEYCODE_L: c =108; break;     // l
                case KeyEvent.KEYCODE_DEL: c = 8; break;   // bs

                case KeyEvent.KEYCODE_Z: c =122; break;     // z
                case KeyEvent.KEYCODE_X: c =120; break;     // x
                case KeyEvent.KEYCODE_C: c = 99; break;     // c
                case KeyEvent.KEYCODE_V: c =118; break;     // v
                case KeyEvent.KEYCODE_B: c = 98; break;     // b
                case KeyEvent.KEYCODE_N: c =110; break;     // n
                case KeyEvent.KEYCODE_M: c =109; break;     // m
                case 11: c = 36; break;                     // $
                case KeyEvent.KEYCODE_ENTER: c = 10; break; // ENTER

                case KeyEvent.KEYCODE_0: c = 48; break;     // 0
                case KeyEvent.KEYCODE_SPACE: c = 32; break; // SPACE
                default: c = keycode;
            }
            return c;
        }
    },
    HideSymbol2 {
        public void enter(KeyboardViewQwerty keyboardview) {
            keyboardview.setInputViewHide();
        }

        @Override
        public void pushSoftSYM(StateKeyboardFrame context) {
            context.changeState(Qwerty);
        }

        @Override
        public void pushHardSYM(StateKeyboardFrame context) {
            context.changeState(HideQwerty);
        }

        @Override
        public void upHardALT(StateKeyboardFrame context) {
            context.changeState(HideQwerty);
        }

        @Override
        public StateKeyboard getType() {
            return StateKeyboard.Symbol2;
        }

        @Override
        public int converKeycode(int keycode) {
            int c = keycode;

            switch (keycode) {
                case KeyEvent.KEYCODE_Q: c = 35; break;     // #
                case KeyEvent.KEYCODE_W: c = 49; break;     // 1
                case KeyEvent.KEYCODE_E: c = 50; break;     // 2
                case KeyEvent.KEYCODE_R: c = 51; break;     // 3
                case KeyEvent.KEYCODE_T: c = 40; break;     // (
                case KeyEvent.KEYCODE_Y: c = 41; break;     // )
                case KeyEvent.KEYCODE_U: c = 95; break;     // _
                case KeyEvent.KEYCODE_I: c = 45; break;     // -
                case KeyEvent.KEYCODE_O: c = 43; break;     // +
                case KeyEvent.KEYCODE_P: c = 64; break;     // @

                case KeyEvent.KEYCODE_A: c = 42; break;     // *
                case KeyEvent.KEYCODE_S: c = 52; break;     // 4
                case KeyEvent.KEYCODE_D: c = 53; break;     // 5
                case KeyEvent.KEYCODE_F: c = 54; break;     // 6
                case KeyEvent.KEYCODE_G: c = 47; break;     // /
                case KeyEvent.KEYCODE_H: c = 58; break;     // :
                case KeyEvent.KEYCODE_J: c = 59; break;     // ;
                case KeyEvent.KEYCODE_K: c = 39; break;     // '
                case KeyEvent.KEYCODE_L: c = 34; break;     // "
                case KeyEvent.KEYCODE_DEL: c = 8; break;   // bs

                case KeyEvent.KEYCODE_Z: c = 55; break;     // 7
                case KeyEvent.KEYCODE_X: c = 56; break;     // 8
                case KeyEvent.KEYCODE_C: c = 57; break;     // 9
                case KeyEvent.KEYCODE_V: c = 63; break;     // ?
                case KeyEvent.KEYCODE_B: c = 33; break;     // !
                case KeyEvent.KEYCODE_N: c = 44; break;     // ,
                case KeyEvent.KEYCODE_M: c = 46; break;     // .
                case 11: c = 36; break;                     // $
                case KeyEvent.KEYCODE_ENTER: c = 10; break; // ENTER

                case KeyEvent.KEYCODE_0: c = 48; break;     // 0
                case KeyEvent.KEYCODE_SPACE: c = 32; break; // SPACE
                default: c = keycode;
            }
            return c;
        }
    },
    ;

    public void leave(KeyboardViewQwerty keyboardview) {}
    public void enter(KeyboardViewQwerty keyboardview) {}
    public void pushSoftSYM(StateKeyboardFrame context) {}
    public void pushHardSYM(StateKeyboardFrame context) {}
    public void downHardALT(StateKeyboardFrame context) {}
    public void upHardALT(StateKeyboardFrame context) {}
    public StateKeyboard getType() { return StateKeyboard.Qwerty; }
    public int converKeycode(int keycode) {return keycode; }
}
