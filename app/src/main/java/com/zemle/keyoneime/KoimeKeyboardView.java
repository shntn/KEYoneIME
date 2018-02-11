package com.zemle.keyoneime;

import android.content.Context;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.view.KeyEvent;

import java.util.EnumSet;
import java.util.List;
import java.util.Locale;

/**
 * キーボードビューの管理
 *
 * Created by shntn on 2018/01/13.
 */

class KoimeKeyboardView {
    private Context mContext;
    private KoimeService mService;
    private KeyboardView mKeyboardView;

    KoimeKeyboardView(Context context, KoimeService service) {
        mContext = context;
        mService = service;
        mKeyboardView = null;
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

    void updateStickeies(KoimeKey key) {
        setCtrl(key);
        setShift(key);
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

    private void setCtrl(KoimeKey key) {
        boolean state;

        state = key.contains(EnumSet.of(KoimeKey.Key.SHIFT_LEFT));
        setSticky(KeyEvent.KEYCODE_SHIFT_LEFT, state);
    }

    private void setShift(KoimeKey key) {
        boolean state;

        state = key.contains(EnumSet.of(KoimeKey.Key.SHIFT_RIGHT));
        setSticky(KeyEvent.KEYCODE_SHIFT_RIGHT, state);
        mKeyboardView.setShifted(key.contains(EnumSet.of(KoimeKey.Key.SHIFT_RIGHT)));
    }
}
