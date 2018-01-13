package com.zemle.keyoneime;

import android.view.KeyEvent;

import java.util.Locale;

/**
 * Created by shntn on 2018/01/13.
 */

class StateMetaKeyFacade {
    enum Meta { ALT, SHIFT, CTRL }

    private StateMetaKey mStateAltKey;
    private StateMetaKey mStateCtrlKey;
    private StateMetaKey mStateShiftKey;

    StateMetaKeyFacade(){
        mStateAltKey = new StateMetaKey();
        mStateCtrlKey = new StateMetaKey();
        mStateShiftKey = new StateMetaKey();
    }

    void changeMetaKey(int keycode, KeyEvent event){
        int action = event.getAction();

        switch (action) {
            case KeyEvent.ACTION_DOWN:
                pressMetaKey(keycode);
                break;
            case KeyEvent.ACTION_UP:
                releaseMetaKey(keycode);
                break;
        }
    }

    void changePrintingKey(int keycode, KeyEvent event) {
        int action = event.getAction();

        switch (action) {
            case KeyEvent.ACTION_DOWN:
                pressPrintingKey(keycode);
                break;
            case KeyEvent.ACTION_UP:
                releasePrintingKey(keycode);
                break;
        }
    }

    boolean isPressed(Meta meta) {
        boolean pressed = false;

        switch (meta) {
            case ALT:
                pressed = mStateAltKey.isPress();
                break;
            case SHIFT:
                pressed = mStateShiftKey.isPress();
                break;
            case CTRL:
                pressed = mStateCtrlKey.isPress();
                break;
        }
        return pressed;
    }

    private void pressMetaKey(int keycode) {
        switch (keycode) {
            case KeyEvent.KEYCODE_ALT_LEFT:
            case KeyEvent.KEYCODE_ALT_RIGHT:
                mStateAltKey.press();
                log();
                break;
            case KeyEvent.KEYCODE_SHIFT_LEFT:
                mStateCtrlKey.press();
                log();
                break;
            case KeyEvent.KEYCODE_SHIFT_RIGHT:
                mStateShiftKey.press();
                log();
                break;
        }
    }

    private void pressPrintingKey(int keycode) {
        if ((keycode >= KeyEvent.KEYCODE_A)
            && (keycode <= KeyEvent.KEYCODE_Z)) {
            mStateAltKey.use();
            mStateShiftKey.use();
            mStateCtrlKey.use();
            log();
        } else if (keycode == KeyEvent.KEYCODE_4) {
            mStateAltKey.use();
            mStateCtrlKey.use();
            log();
        }
    }

    private void releaseMetaKey(int keycode) {
        switch (keycode) {
            case KeyEvent.KEYCODE_ALT_LEFT:
            case KeyEvent.KEYCODE_ALT_RIGHT:
                mStateAltKey.release();
                log();
                break;
            case KeyEvent.KEYCODE_SHIFT_LEFT:
                mStateCtrlKey.release();
                log();
                break;
            case KeyEvent.KEYCODE_SHIFT_RIGHT:
                mStateShiftKey.release();
                log();
                break;
        }
    }

    private void releasePrintingKey(int keycode) {

    }

    private void log() {
        KoimeLog.d(String.format(
                Locale.US,
                "MetaFacade: shift=%b, alt=%b, ctrl=%b",
                isPressed(Meta.SHIFT),
                isPressed(Meta.ALT),
                isPressed(Meta.CTRL)
        ));
    }
}
