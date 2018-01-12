package com.zemle.keyoneime;

import android.content.Context;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;

import java.util.List;

/**
 * キーボードビューの
 *
 * Created by shntn on 2018/01/13.
 */

class KoimeKeyboardView {
    private static final int KEYCODE_QWERTY_CTRL = -2;

    private Context mContext;
    private KoimeService mService;
    private KeyboardView mKeyboardView;

    KoimeKeyboardView(Context context, KoimeService service) {
        mContext = context;
        mService = service;
    }

    KeyboardView createView() {
        mKeyboardView = (KeyboardView) mService.getLayoutInflater().inflate(R.layout.keyboard, null);
        mKeyboardView.setPreviewEnabled(false);
        setKeyboard(R.xml.qwerty);
        mKeyboardView.setOnKeyboardActionListener(mService);

        return mKeyboardView;
    }

    void setKeyboard(int xmlLayoutResId) {
        Keyboard keyboard;

        keyboard = new Keyboard(mContext, xmlLayoutResId);
        mKeyboardView.setKeyboard(keyboard);
    }

    private void setSticky(int keyCode, boolean state) {
        int i = 0;
        List<Keyboard.Key> mKeyboardViewKeys = mKeyboardView.getKeyboard().getKeys();

        for (Keyboard.Key key : mKeyboardViewKeys) {
            if (key.codes[0] == keyCode) {
                key.on = state;
                break;
            }
            i++;
        }
        mKeyboardView.invalidateKey(i);
    }

    void setCtrl(boolean state) {
        setSticky(KEYCODE_QWERTY_CTRL, state);
    }

    void setShift(boolean state) {
        mKeyboardView.setShifted(state);
    }
}

