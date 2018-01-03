package com.zemle.keyoneime;

/**
 * StateKeyboard と共にソフトキーボードの状態を管理
 *
 * Created by shntn on 2018/01/02.
 */

class StateKeyboardFrame {
    private StateKeyboard mState;
    private KeyboardViewQwerty mKeyboardView;

    StateKeyboardFrame() {
        mState = StateKeyboard.Qwerty;
    }

    void setKeyboardView(KeyboardViewQwerty keyboardview) {
        mKeyboardView = keyboardview;
    }

    void changeState(StateKeyboard state) {
        mState.leave(mKeyboardView);
        mState = state;
        mState.enter(mKeyboardView);
    }

    void pushSoftSYM() {
        mState.pushSoftSYM(this);
    }

    void pushHardSYM() {
        mState.pushHardSYM(this);
    }

    void downHardALT() {
        mState.downHardALT(this);
    }

    void upHardALT() {
        mState.upHardALT(this);
    }

    StateKeyboard getType() {
        return mState.getType();
    }

    StateKeyboard getState() {
        return mState;
    }

    int convertKeycode(int keycode) {
        return mState.converKeycode(keycode);
    }
}
