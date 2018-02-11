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
 *
 * Created by shntn on 2017/12/16.
 */

public class KoimeService extends InputMethodService implements KeyboardView.OnKeyboardActionListener {

    private KoimeKeyboard mKeyboard = null;
    private KoimeKeyboardView mKeyboardView = null;
    private StateKeyboard mStateKeyboard = null;
    private EachKey mEachKey = null;

    @Override
    public void onInitializeInterface() {
        KoimeLog.d("Method Start: KoimeService.onInitializeInterface()");

        Context context = getApplicationContext();
        mKeyboard = new KoimeKeyboard(context, this);
        mEachKey  = new EachKey(this);
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
        //updateInputViewShown();
    }

    @Override
    public boolean onEvaluateInputViewShown() {
        KoimeLog.d("Method Start: KoimeService.onEvaluateInputViewShown()");

        super.onEvaluateInputViewShown();
        return true;
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
        mKeyboardView = new KoimeKeyboardView(context, this);
        mStateKeyboard = new StateKeyboard();

        return mKeyboardView.createView();
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
        KoimeKey key;

        key = mKeyboard.press(event);
        if (mStateKeyboard != null) {
            mStateKeyboard.changeHardKey(key);
        }
        if (mKeyboardView != null) {
            mKeyboardView.setKeyboard(mStateKeyboard.getId());
            mKeyboardView.updateStickeies(key);
        }

        return mEachKey.run(key) || super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        KoimeLog.d(String.format(Locale.US,
                "Method Start: KoimeService.onKeyUp(keyCode=%d(%x), KeyEvent=\"%s\")",
                keyCode, keyCode, event.toString()
        ));
        KoimeKey key;

        key = mKeyboard.release(event);
        if (mStateKeyboard != null) {
            mStateKeyboard.changeHardKey(key);
        }
        if (mKeyboardView != null) {
            mKeyboardView.setKeyboard(mStateKeyboard.getId());
            mKeyboardView.updateStickeies(key);
        }

        return super.onKeyUp(keyCode, event);
    }

    @Override
    public void onFinishInput() {
        KoimeLog.d("Method Start: KoimeService.onFinishInput()");
        super.onFinishInput();
    }

    public void onKey(int primaryCode, int[] keyCodes) {
        KoimeLog.d(String.format(Locale.US,
                "Method Start: KoimeService.onKey(primaryCode=%d(%x))",
                primaryCode, primaryCode
        ));
        KeyEvent event;
        KoimeKey key;
        int metaState;

        metaState = mKeyboard.createMetaState();
        event = new KeyEvent(
               0,
               0,
                KeyEvent.ACTION_DOWN,
                primaryCode,
               0,
                metaState | KeyEvent.META_FUNCTION_ON);     // FUNCTIONをソフトキー入力の通知として使用
        key = mKeyboard.press(event);
        mStateKeyboard.changeSoftKey(key);
        mKeyboardView.setKeyboard(mStateKeyboard.getId());
        mKeyboardView.updateStickeies(key);
        mEachKey.run(key);

        event = event.changeAction(event, KeyEvent.ACTION_UP);
        key = mKeyboard.release(event);
        mStateKeyboard.changeSoftKey(key);
        mKeyboardView.setKeyboard(mStateKeyboard.getId());
        mKeyboardView.updateStickeies(key);
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
