package com.zemle.keyoneime;

import android.content.Context;
import android.inputmethodservice.KeyboardView;
import android.util.AttributeSet;

/**
 * Created by nobu on 2017/12/16.
 */

public class KeyboardViewQwerty extends KeyboardView implements KeyboardView.OnKeyboardActionListener {
    private static final int KEYCODE_QWERTY_SYM   = -1012;
    private static final int KEYCODE_QWERTY_CTRL  = -2;

    private KEYoneKeyboard mQwertyKeyboard;
    private KEYoneKeyboard mHideKeyboard;
    private KEYoneKeyboard mSymbol1Keyboard;
    private KEYoneKeyboard mSymbol2Keyboard;
    private KeyController mKeyController;

    public KeyboardViewQwerty(Context context, AttributeSet attrs) {
        super(context, attrs);
        setup(context);
    }

    private void setup(Context context) {
        // Soft KEYoneKeyboard 押下時にpreview表示を無効化
        setPreviewEnabled(false);
        mQwertyKeyboard = new KEYoneKeyboard(context, R.xml.qwerty);
        mHideKeyboard = new KEYoneKeyboard(context, R.xml.hide);
        mSymbol1Keyboard = new KEYoneKeyboard(context, R.xml.symbol1);
        mSymbol2Keyboard = new KEYoneKeyboard(context, R.xml.symbol2);
        setKeyboard(mQwertyKeyboard);
        setOnKeyboardActionListener(this);
        mKeyController = KeyController.getInstance();
    }

    @Override
    public void onKey(int primaryCode, int[] keyCodes) {
        mKeyController.onKey(primaryCode, keyCodes);
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

    public void setInputViewQwerty() {
        setKeyboard(mQwertyKeyboard);
    }

    public void setInputViewSymbol1() {
        setKeyboard(mSymbol1Keyboard);
    }

    public void setInputViewSymbol2() {
        setKeyboard(mSymbol2Keyboard);
    }

    public void setInputViewHide() {
        setKeyboard(mHideKeyboard);
    }
}
