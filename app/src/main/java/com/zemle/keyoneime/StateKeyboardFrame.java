package com.zemle.keyoneime;

/**
 * Created by nobu on 2018/01/02.
 */

public class StateKeyboardFrame {
    private StateKeyboard mState;
    private KeyboardViewQwerty mKeyboardView;

    public StateKeyboardFrame() {
        mState = StateKeyboard.Qwerty;
    }

    public void setKeyboardView(KeyboardViewQwerty keyboardview) {
        mKeyboardView = keyboardview;
    }

    public void changeState(StateKeyboard state) {
        mState.leave(mKeyboardView);
        mState = state;
        mState.enter(mKeyboardView);
    }

    public void pushSoftSYM() {
        mState.pushSoftSYM(this);
    }

    public void pushHardSYM() {
        mState.pushHardSYM(this);
    }

    public void downHardALT() {
        mState.downHardALT(this);
    }

    public void upHardALT() {
        mState.upHardALT(this);
    }

    public StateKeyboard getType() {
        return mState.getType();
    }

    public StateKeyboard getState() {
        return mState;
    }

    public int convertKeycode(int keycode) {
        return mState.converKeycode(keycode);
    }
}
