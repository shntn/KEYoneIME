package com.zemle.keyoneime;

import android.content.Context;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.view.KeyEvent;

import java.util.List;
import java.util.Locale;

/**
 * キーボードの管理
 *
 * Created by shntn on 2018/01/13.
 */

class KoimeKeyboard {
    private Context mContext;
    private KoimeService mService;
    private StateModifierKeyFacade  mModifierKey;
    private StateKeyboard mStateKeyboard;

    KoimeKeyboard(Context context, KoimeService service) {
        mContext = context;
        mService = service;
        mModifierKey = new StateModifierKeyFacade();
        mStateKeyboard = null;
    }

    KoimeKey press(KeyEvent event) {
        KoimeKey key;

        mModifierKey.change(event);
        key = mModifierKey.getState();
        key.updateFromKeyEvent(event);

        KoimeLog.d(String.format(
                Locale.US,
                "KoimeKey: %s",
                key.toString()
        ));
        return key;
    }

    KoimeKey release(KeyEvent event) {
        KoimeKey key;

        mModifierKey.change(event);
        key = mModifierKey.getState();
        key.updateFromKeyEvent(event);

        KoimeLog.d(String.format(
                Locale.US,
                "KoimeKey: %s",
                key.toString()
        ));
        return key;
    }

    int createMetaState() {
        KoimeKey key;

        key = mModifierKey.getState();
        return key.makeMetaState();
    }
}

