package com.zemle.keyoneime;

import android.content.Context;
import android.inputmethodservice.InputMethodService;
import android.view.KeyEvent;
import android.view.View;


/**
 * Created by nobu on 2017/12/16.
 */

public class KEYoneService extends InputMethodService {

    private KeyboardViewQwerty mQwertyInputView = null;
    private KeyController mKeyController = null;

    @Override
    public boolean onEvaluateInputViewShown() {
        return true;
    }

    private void createInputView() {
        Context context = getApplicationContext();
        mQwertyInputView = new KeyboardViewQwerty(context, null);

        mKeyController = KeyController.getInstance();
        mKeyController.setService(this, mQwertyInputView);
    }

    @Override
    public View onCreateInputView() {
        createInputView();

        return mQwertyInputView;
    }

    @Override
    public boolean onKeyDown(int keycode, KeyEvent event) {
        return mKeyController.onKeyDown(keycode, event);
    }

    @Override
    public boolean onKeyUp(int keycode, KeyEvent event) {
        return mKeyController.onKeyUp(keycode, event);
    }

/*
    @Override
    public void onFinishInput() {
        super.onFinishInput();
    }
*/
}
