package com.zemle.keyoneime;

import android.content.Context;
import android.inputmethodservice.Keyboard;

/**
 * 必要そうだから作った。よく理解できてない。
 *
 * Created by shntn on 2017/12/16.
 */

public class KoimeKeyboard extends Keyboard {

    public KoimeKeyboard(Context context, int xmlLayoutResId) {
        super(context, xmlLayoutResId);
    }
}