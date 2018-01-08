package com.zemle.keyoneime;

import android.content.Context;
import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;

import java.util.List;
import java.util.Locale;


/**
 * InputMethod
 *
 * InputMethodのライフサイクルに関わる処理はここで行う。
 * 受け付けたキー入力イベントはKeyControllerに移譲。
 *
 * Created by shntn on 2017/12/16.
 */

public class KoimeService extends InputMethodService implements KeyboardView.OnKeyboardActionListener {
    private static final int KEYCODE_QWERTY_CTRL = -2;

    private KeyController mKeyController = null;
    private KeyboardView mKeyboardView = null;

    @Override
    public void onInitializeInterface() {
        KoimeLog.d("Method Start: KoimeService.onInitializeInterface()");
        super.onInitializeInterface();
    }

    @Override
    public void onBindInput() {
        KoimeLog.d("Method Start: KoimeService.onBindInput()");
        super.onBindInput();
    }

    @Override
    public void onStartInput(EditorInfo attr, boolean restarting) {
        KoimeLog.d(String.format(Locale.US,
                "Method Start: KoimeService.onStartInput(EditorInfo={" +
                        "actionId=%d, " +
                        "imeOptions=%08x, " +
                        "initialSelStart=%d, " +
                        "initialSelEnd=%d, " +
                        "inputType=%x}" +
                        ", restarting=%b)",
                attr.actionId,
                attr.imeOptions,
                attr.initialSelStart,
                attr.initialSelEnd,
                attr.inputType,
                restarting

        ));
        super.onStartInput(attr, restarting);
    }

    @Override
    public void onStartInputView(EditorInfo info, boolean restarting) {
        KoimeLog.d(String.format(Locale.US,
                "Method Start: KoimeService.onStartInputView(EditorInfo={" +
                        "actionId=%d, " +
                        "imeOptions=%08x, " +
                        "initialSelStart=%d, " +
                        "initialSelEnd=%d, " +
                        "inputType=%x}" +
                        ", restarting=%b)",
                info.actionId,
                info.imeOptions,
                info.initialSelStart,
                info.initialSelEnd,
                info.inputType,
                restarting

        ));
        super.onStartInputView(info, restarting);
    }

    private KeyboardView createInputView() {
        Context context = getApplicationContext();
        mKeyboardView = (KeyboardView) getLayoutInflater().inflate(R.layout.keyboard, null);
        mKeyboardView.setPreviewEnabled(false);
        setKeyboard(R.xml.qwerty);
        mKeyboardView.setOnKeyboardActionListener(this);


        mKeyController = KeyController.getInstance();
        mKeyController.setService(context, this);

        return mKeyboardView;
    }

    @Override
    public View onCreateInputView() {
        KoimeLog.d("Method Start: KoimeService.onCreateInputView()");

        return createInputView();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        KoimeLog.d(String.format(Locale.US,
                "Method Start: KoimeService.onKeyDown(keyCode=%d(%x), KeyEvent=\"%s\")",
                keyCode, keyCode, event.toString()
        ));
        boolean status;

        status = mKeyController.onKeyDown(keyCode, event);
        return status || super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        KoimeLog.d(String.format(Locale.US,
                "Method Start: KoimeService.onKeyUp(keyCode=%d(%x), KeyEvent=\"%s\")",
                keyCode, keyCode, event.toString()
        ));
        boolean status;

        status = mKeyController.onKeyUp(keyCode, event);
        return status || super.onKeyUp(keyCode, event);
    }

    @Override
    public void onFinishInput() {
        KoimeLog.d("Method Start: KoimeService.onFinishInput()");
        super.onFinishInput();
    }

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

    public void setKeyboard(int xmlLayoutResId) {
        Keyboard keyboard;

        keyboard = new Keyboard(this, xmlLayoutResId);
        mKeyboardView.setKeyboard(keyboard);
    }

    public void setInputViewQwerty() {
        setKeyboard(R.xml.qwerty);
    }

    public void setInputViewSymbol1() {
        setKeyboard(R.xml.symbol1);
    }

    public void setInputViewSymbol2() {
        setKeyboard(R.xml.symbol2);
    }

    public void setInputViewHide() {
        setKeyboard(R.xml.hide);
    }

    public void setSticky(int keyCode, boolean state) {
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

    public void setCtrl(boolean state) {
        setSticky(KEYCODE_QWERTY_CTRL, state);
    }

    public void setShift(boolean state) {
        mKeyboardView.setShifted(state);
    }
}
