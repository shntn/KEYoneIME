package com.zemle.keyoneime;

/**
 * Created by nobu on 2018/01/02.
 */

public class StateKeyboardFrame {
    private StateKeyboard mState;
    private KEYoneService mService;

    public StateKeyboardFrame() {
        mState = StateKeyboard.Qwerty;
    }

    public void setService(KEYoneService listener) {
        mService = listener;
    }

    public void changeState(StateKeyboard state) {
        mState.leave(mService);
        mState = state;
        mState.enter(mService);
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
