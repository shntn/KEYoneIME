package com.zemle.keyoneime;

import android.view.KeyEvent;

import java.util.EnumSet;
import java.util.Locale;

/**
 * 全ての修飾キーの状態管理
 *
 * Created by shntn on 2018/01/13.
 */

class StateModifierKeyFacade {
    enum Meta { SYM, ALT, SHIFT, CTRL }

    private StateModifierKey mStateSymKey;
    private StateModifierKey mStateAltKey;
    private StateModifierKey mStateCtrlKey;
    private StateModifierKey mStateShiftKey;

    StateModifierKeyFacade(){
        mStateSymKey = new StateModifierKey();
        mStateAltKey = new StateModifierKey();
        mStateCtrlKey = new StateModifierKey();
        mStateShiftKey = new StateModifierKey();
    }

    void change(KeyEvent event){
        int action = event.getAction();
        int keycode = event.getKeyCode();

        if (event.getRepeatCount() > 0) {
            return;
        }

        switch (action) {
            case KeyEvent.ACTION_DOWN:
                if (isModifierKey(event)) {
                    pressMetaKey(event);
                } else {
                    pressPrintingKey(keycode);
                }
                break;
            case KeyEvent.ACTION_UP:
                if (isModifierKey(event)) {
                    releaseMetaKey(keycode);
                } else {
                    releasePrintingKey(keycode);
                }
                break;
        }
    }

    private boolean isPressed(Meta meta) {
        boolean pressed = false;

        switch (meta) {
            case SYM:
                pressed = mStateSymKey.isPress();
                break;
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

    private boolean isModifierKey(KeyEvent event) {
        int keycode = event.getKeyCode();
        boolean result = false;

        switch (keycode) {
            case KeyEvent.KEYCODE_SYM:
            case KeyEvent.KEYCODE_ALT_LEFT:
            case KeyEvent.KEYCODE_ALT_RIGHT:
            case KeyEvent.KEYCODE_SHIFT_LEFT:
            case KeyEvent.KEYCODE_SHIFT_RIGHT:
            case KeyEvent.KEYCODE_CTRL_LEFT:
            case KeyEvent.KEYCODE_CTRL_RIGHT:
            case KeyEvent.KEYCODE_EISU:
                result = true;
        }
        return result;
    }

    private void pressMetaKey(KeyEvent event) {
        switch (event.getKeyCode()) {
            case KeyEvent.KEYCODE_SYM:
                mStateSymKey.press();
                mStateAltKey.clear();
                if (event.hasModifiers(KeyEvent.META_FUNCTION_ON)) {
                    if (mStateSymKey.isOn()) {
                        mStateSymKey.press();
                    }
                }
                log();
                break;
            case KeyEvent.KEYCODE_ALT_LEFT:
            case KeyEvent.KEYCODE_ALT_RIGHT:
                mStateAltKey.press();
                mStateSymKey.clear();
                if (event.hasModifiers(KeyEvent.META_FUNCTION_ON)) {
                    if (mStateAltKey.isOn()) {
                        mStateAltKey.press();
                    }
                }
                log();
                break;
            case KeyEvent.KEYCODE_SHIFT_LEFT:
                mStateCtrlKey.press();
                log();
                break;
            case KeyEvent.KEYCODE_SHIFT_RIGHT:
                mStateShiftKey.press();
                log();
            case KeyEvent.KEYCODE_EISU:
                mStateAltKey.clear();
                mStateSymKey.clear();
                log();
                break;
        }
    }

    private void pressPrintingKey(int keycode) {
    }

    private void releaseMetaKey(int keycode) {
        switch (keycode) {
            case KeyEvent.KEYCODE_SYM:
                mStateSymKey.release();
                log();
                break;
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
        if ((keycode >= KeyEvent.KEYCODE_A)
                && (keycode <= KeyEvent.KEYCODE_Z)) {
            mStateSymKey.use();
            mStateAltKey.use();
            mStateShiftKey.use();
            mStateCtrlKey.use();
            log();
        } else if ((keycode == KeyEvent.KEYCODE_4)
                || (keycode == KeyEvent.KEYCODE_0)) {
            mStateSymKey.use();
            mStateAltKey.use();
            mStateCtrlKey.use();
            log();
        }
    }

    KoimeKey getState() {
        boolean state;
        KoimeKey key = new KoimeKey();

        state = isPressed(Meta.SYM);
        key.update(EnumSet.of(KoimeKey.Key.SYM), state);
        state = isPressed(Meta.ALT);
        key.update(EnumSet.of(KoimeKey.Key.ALT), state);
        state = isPressed(Meta.CTRL);
        key.update(EnumSet.of(KoimeKey.Key.SHIFT_LEFT), state);
        state = isPressed(Meta.SHIFT);
        key.update(EnumSet.of(KoimeKey.Key.SHIFT_RIGHT), state);
        return key;
    }

    private void log() {
        KoimeLog.d(String.format(
                Locale.US,
                "MetaFacade: sym=%b, alt=%b, shift=%b, ctrl=%b",
                isPressed(Meta.SYM),
                isPressed(Meta.ALT),
                isPressed(Meta.SHIFT),
                isPressed(Meta.CTRL)
        ));
    }
}
