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
    Qwerty(R.xml.qwerty) {
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
    Symbol1(R.xml.symbol1) {
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
    Symbol2(R.xml.symbol2) {
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
    HideQwerty(R.xml.hide) {
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
    HideSymbol2(R.xml.hide) {
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

    int xmlId;

    StateKeyboard(int xmlId){
        this.xmlId = xmlId;
    }

    public void leave(KoimeService service) {}
    public void enter(KoimeService service) { service.setKeyboard(this.xmlId); }
    public void pushSoftSYM(StateKeyboardFrame context) {}
    public void pushHardSYM(StateKeyboardFrame context) {}
    public void downHardALT(StateKeyboardFrame context) {}
    public void upHardALT(StateKeyboardFrame context) {}
    public StateKeyboard getType() { return StateKeyboard.Qwerty; }
}
