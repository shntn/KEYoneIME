package com.zemle.keyoneime;

import android.view.KeyEvent;

import java.util.EnumMap;

class StateMetaKey {
    enum MetaKey {SHIFT_KEY, ALT_KEY, CTRL_KEY}
    private enum MetaKeyState {STATE_NONE, STATE_ON, STATE_LOCKED}

    private static final EnumMap<MetaKey, Integer> KEYCODES = new EnumMap<>(MetaKey.class);
    private static final EnumMap<MetaKey, Integer> MASK_STATES = new EnumMap<>(MetaKey.class);

    private static final int MASK_SHIFT_STATES =
            KeyEvent.META_SHIFT_ON | KeyEvent.META_SHIFT_LEFT_ON | KeyEvent.META_SHIFT_RIGHT_ON;
    private static final int MASK_ALT_STATES =
            KeyEvent.META_ALT_ON | KeyEvent.META_ALT_LEFT_ON | KeyEvent.META_ALT_RIGHT_ON;
    private static final int MASK_CTRL_STATES =
            KeyEvent.META_CTRL_ON | KeyEvent.META_CTRL_LEFT_ON | KeyEvent.META_CTRL_RIGHT_ON;
    private static final int MASK_ALL_STATES =
            MASK_SHIFT_STATES | MASK_ALT_STATES | MASK_CTRL_STATES;

    static {
        KEYCODES.put(MetaKey.SHIFT_KEY, KeyEvent.KEYCODE_SHIFT_LEFT);
        KEYCODES.put(MetaKey.ALT_KEY, KeyEvent.KEYCODE_ALT_LEFT);
        KEYCODES.put(MetaKey.CTRL_KEY, KeyEvent.KEYCODE_SHIFT_LEFT);
        MASK_STATES.put(MetaKey.SHIFT_KEY, MASK_SHIFT_STATES);
        MASK_STATES.put(MetaKey.ALT_KEY, MASK_ALT_STATES);
        MASK_STATES.put(MetaKey.CTRL_KEY, MASK_CTRL_STATES);
    }


    private EnumMap<MetaKey, MetaKeyState> mState = new EnumMap<>(MetaKey.class);
    private EnumMap<MetaKey, Boolean> isPressed = new EnumMap<>(MetaKey.class);
    private EnumMap<MetaKey, Boolean> isUsed = new EnumMap<>(MetaKey.class);

    private KeyController mKeyCtrl;

    StateMetaKey(KeyController kc) {
        mKeyCtrl = kc;
        clearMetaKeyState();
    }

    void clearMetaKeyState() {
        mKeyCtrl.clearMetaKey(MASK_ALL_STATES);

        for (MetaKey m : MetaKey.values()) {
            mState.put(m, MetaKeyState.STATE_NONE);
            isPressed.put(m, false);
            isUsed.put(m, false);
        }
    }

    void pressMetaKey(MetaKey key) {
        mKeyCtrl.sendKey(KeyEvent.ACTION_DOWN, KEYCODES.get(key));

        isPressed.put(key, true);
        isUsed.put(key, false);
        switch (mState.get(key)) {
            case STATE_NONE:
                mState.put(key, MetaKeyState.STATE_ON);
                // off -> on
                break;
            case STATE_ON:
                mState.put(key, MetaKeyState.STATE_LOCKED);
                // on -> locked
                break;
            case STATE_LOCKED:
                mState.put(key, MetaKeyState.STATE_NONE);
                // locked -> off
                break;
        }
    }

    void releaseMetaKey(MetaKey key) {
        mKeyCtrl.sendKey(KeyEvent.ACTION_UP, KEYCODES.get(key));

        isPressed.put(key, false);
        if (mState.get(key) == MetaKeyState.STATE_ON && isUsed.get(key)) {
            mState.put(key, MetaKeyState.STATE_NONE);
            mKeyCtrl.clearMetaKey(MASK_STATES.get(key));
            // on -> off
        }
    }

    int useMetaState() {
        int meta = 0;

        if (useMetaKey(MetaKey.SHIFT_KEY)) {
            meta |= KeyEvent.META_SHIFT_ON;
        }
        if (useMetaKey(MetaKey.ALT_KEY)) {
            meta |= KeyEvent.META_ALT_ON;
        }
        if (useMetaKey(MetaKey.CTRL_KEY)) {
            meta |= KeyEvent.META_CTRL_ON;
        }

        return meta;
    }

    boolean isPress(MetaKey key) {
        boolean status = false;

        switch (mState.get(key)) {
            case STATE_NONE:
                status = false;
                break;
            case STATE_ON:
                status = true;
                break;
            case STATE_LOCKED:
                status = true;
                break;
        }
        return status;
    }

    private boolean useMetaKey(MetaKey key) {
        boolean status = false;

        switch (mState.get(key)) {
            case STATE_NONE:
                status = false;
                break;
            case STATE_ON:
                if (isPressed.get(key)) {
                    // stay on
                    isUsed.put(key, true);
                } else {
                    mState.put(key, MetaKeyState.STATE_NONE);
                    mKeyCtrl.clearMetaKey(MASK_STATES.get(key));
                    // on -> off
                }
                status = true;
                break;
            case STATE_LOCKED:
                status = true;
                break;
        }

        return status;
    }
}