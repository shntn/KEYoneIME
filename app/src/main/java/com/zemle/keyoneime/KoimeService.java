package com.zemle.keyoneime;

import android.content.Context;
import android.inputmethodservice.InputMethodService;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;

import java.util.Locale;


/**
 * InputMethod
 *
 * InputMethodのライフサイクルに関わる処理はここで行う。
 * 受け付けたキー入力イベントはKeyControllerに移譲。
 *
 * Created by shntn on 2017/12/16.
 */

public class KoimeService extends InputMethodService {

    private KeyboardViewQwerty mQwertyInputView = null;
    private KeyController mKeyController = null;

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

    private void createInputView() {
        Context context = getApplicationContext();
        mQwertyInputView = new KeyboardViewQwerty(context, null);

        mKeyController = KeyController.getInstance();
        mKeyController.setService(this, mQwertyInputView);
    }

    @Override
    public View onCreateInputView() {
        KoimeLog.d("Method Start: KoimeService.onCreateInputView()");

        createInputView();

        return mQwertyInputView;    // StateKeyboardFrameのコンストラクタでの設定と対応させること
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
}
