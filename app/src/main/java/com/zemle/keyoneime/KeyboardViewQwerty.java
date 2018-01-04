package com.zemle.keyoneime;

import android.content.Context;
import android.inputmethodservice.KeyboardView;
import android.util.AttributeSet;

/**
 * ソフトキーボードの表示、及びソフトキー押下時のイベント受付処理。
 * ソフトキーボードはQWERTY, SYMBOL1, SYMBOL2 の3タイプを持ち、このクラスで表示切り替えを行う。
 * ソフトキーが押された場合は、KeyController で処理する。
 * ソフトキーボードの状態については StateKeyboard で管理。
 * ソフトキーボード非表示は、キー定義をしてない hide.xml を表示することで対応。
 *
 * Created by shntn on 2017/12/16.
 */

public class KeyboardViewQwerty extends KeyboardView implements KeyboardView.OnKeyboardActionListener {

    private KoimeKeyboard mQwertyKeyboard;
    private KoimeKeyboard mHideKeyboard;
    private KoimeKeyboard mSymbol1Keyboard;
    private KoimeKeyboard mSymbol2Keyboard;
    private KeyController mKeyController;

    public KeyboardViewQwerty(Context context, AttributeSet attrs) {
        super(context, attrs);
        setup(context);
    }

    private void setup(Context context) {
        // Soft KoimeKeyboard 押下時にpreview表示を無効化
        setPreviewEnabled(false);
        mQwertyKeyboard = new KoimeKeyboard(context, R.xml.qwerty);
        mHideKeyboard = new KoimeKeyboard(context, R.xml.hide);
        mSymbol1Keyboard = new KoimeKeyboard(context, R.xml.symbol1);
        mSymbol2Keyboard = new KoimeKeyboard(context, R.xml.symbol2);
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
