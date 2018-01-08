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
        public void enter(KoimeService service) {
            service.setInputViewQwerty();
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
    },
    Symbol1 {
        public void enter(KoimeService service) {
            service.setInputViewSymbol1();
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
    },
    Symbol2 {
        public void enter(KoimeService service) {
            service.setInputViewSymbol2();
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
    },
    HideQwerty {
        public void enter(KoimeService service) {
            service.setInputViewHide();
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
    },
    HideSymbol2 {
        public void enter(KoimeService service) {
            service.setInputViewHide();
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
    };

    public void leave(KoimeService service) {}
    public void enter(KoimeService service) {}
    public void pushSoftSYM(StateKeyboardFrame context) {}
    public void pushHardSYM(StateKeyboardFrame context) {}
    public void downHardALT(StateKeyboardFrame context) {}
    public void upHardALT(StateKeyboardFrame context) {}
    public StateKeyboard getType() { return StateKeyboard.Qwerty; }
}
