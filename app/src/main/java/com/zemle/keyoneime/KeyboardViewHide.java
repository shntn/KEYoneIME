package com.zemle.keyoneime;

import android.content.Context;
import android.inputmethodservice.KeyboardView;
import android.util.AttributeSet;

/**
 * Created by nobu on 2017/12/16.
 */

public class KeyboardViewHide extends KeyboardView implements KeyboardView.OnKeyboardActionListener {

    private KEYoneKeyboard mHideKeyboard;

    public KeyboardViewHide(Context context, AttributeSet attrs) {
        super(context, attrs);
        setup(context);
    }

    private void setup(Context context) {
        // Soft KEYoneKeyboard 押下時にpreview表示を無効化
        setPreviewEnabled(false);
        mHideKeyboard = new KEYoneKeyboard(context, R.xml.hide);
        setKeyboard(mHideKeyboard);
        setOnKeyboardActionListener(this);
    }

    @Override
    public void onKey(int primaryCode, int[] keyCodes) {
    }

    public void onPress(int primaryCode) {
    }

    public void onRelease(int primaryCode) {
    }

    public void onText(CharSequence text) {
    }

    public void swipeRight() {
    }

    public void swipeLeft() {
    }

    public void swipeDown() {
    }

    public void swipeUp() {
    }
}
