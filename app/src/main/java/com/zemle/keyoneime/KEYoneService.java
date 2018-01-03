package com.zemle.keyoneime;

import android.content.Context;
import android.inputmethodservice.InputMethodService;
import android.view.KeyEvent;
import android.view.View;


/**
 * Created by shntn on 2017/12/16.
 */

public class KEYoneService extends InputMethodService {

    private KeyboardViewQwerty mQwertyInputView = null;
    private KeyController mKeyController = null;

    private void createInputView() {
        Context context = getApplicationContext();
        mQwertyInputView = new KeyboardViewQwerty(context, null);

        mKeyController = KeyController.getInstance();
        mKeyController.setService(this, mQwertyInputView);
    }

    @Override
    public View onCreateInputView() {
        createInputView();

        return mQwertyInputView;    // StateKeyboardFrameのコンストラクタでの設定と対応させること
    }

    @Override
    public boolean onKeyDown(int keycode, KeyEvent event) {
        boolean status;

        status = mKeyController.onKeyDown(keycode, event);
        if (status) {
            return true;
        }
        return super.onKeyDown(keycode, event);
    }

    @Override
    public boolean onKeyUp(int keycode, KeyEvent event) {
        boolean status;

        status = mKeyController.onKeyUp(keycode, event);
        if (status) {
            return true;
        }
        return super.onKeyUp(keycode, event);
    }

/*
    @Override
    public void onFinishInput() {
        super.onFinishInput();
    }
*/
}
