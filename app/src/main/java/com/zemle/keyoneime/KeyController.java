package com.zemle.keyoneime;

import android.content.Context;
import android.inputmethodservice.KeyboardView;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.ExtractedTextRequest;
import android.view.inputmethod.InputConnection;

import java.util.Locale;

/**
 * 入力されたキーに関する処理を行う。
 * ソフトキーが押された場合は onKey で処理する。
 * ハードキーが押された場合は onKeyDown, onKeyUp で処理する。
 *
 * Created by shntn on 2018/01/02.
 */

class KeyController {
    private static final int KEYCODE_QWERTY_SYM   = -1012;
    private static final int KEYCODE_QWERTY_CTRL  = -2;
    private static final int KEYCODE_QWERTY_UP    = -19;
    private static final int KEYCODE_QWERTY_DOWN  = -20;
    private static final int KEYCODE_QWERTY_LEFT  = -21;
    private static final int KEYCODE_QWERTY_RIGHT = -22;
    // 未使用
    private static final int KEYCODE_QWERTY_ALT   = -6;
    private static final int KEYCODE_QWERTY_ABC   = -101;
    private static final int KEYCODE_QWERTY_DONE  = -4;


    private static KeyController singleton = new KeyController();
    private KoimeService mService;
    private KoimeKeyboardView mKeyboardView;
    private StateKeyboard mStateKeyboard = new StateKeyboard();
    private KeyCode mKeyCode;

    private StateMetaKeyFacade mStateMetaKey;

    // 状態変数
    private static boolean shift = false;
    private static boolean ctrl  = false;

    private KeyController() { }

    static KeyController getInstance() {
        return singleton;
    }

    void setup(Context context, KoimeService service) {
        mService = service;
        mKeyboardView = new KoimeKeyboardView(context, service);
        mStateKeyboard.setup(mKeyboardView);
        mStateMetaKey = new StateMetaKeyFacade();
        mKeyCode = new KeyCode(context, mStateKeyboard);
    }

    KeyboardView createKeyboardView() {
        return mKeyboardView.createView();
    }

    void onKey(int primaryCode, int[] keyCodes) {
        int keycode;
        KeyEvent event_down;
        KeyEvent event_up;

        // 画面に定義しているが未実装のキー
        if (primaryCode == KEYCODE_QWERTY_ABC) {
            return;
        }
        if (primaryCode == KEYCODE_QWERTY_DONE) {
            return;
        }

        // SHIFT, ALT, CTRL
        keycode = mKeyCode.convertKeyCode(primaryCode);
        event_down = createKeyEventForSoftKeyboard(keycode, KeyEvent.ACTION_DOWN);
        event_up = createKeyEventForSoftKeyboard(keycode, KeyEvent.ACTION_UP);
        mStateMetaKey.changeMetaKey(keycode, event_down);
        mStateMetaKey.changeMetaKey(keycode, event_up);

        // SoftKeyBoard 更新
        mStateKeyboard.changeSoftKey(event_down);

        // 表示可能な文字を出力
        if ((primaryCode > 0)
                && (!mStateMetaKey.isPressed(StateMetaKeyFacade.Meta.CTRL))) {

            if (mStateMetaKey.isPressed(StateMetaKeyFacade.Meta.SHIFT)) {
                primaryCode = Character.toUpperCase(primaryCode);
            }
            pressText(primaryCode);

        // 表示不可な文字を出力
        } else if ((primaryCode < 0)
                || (mStateMetaKey.isPressed(StateMetaKeyFacade.Meta.CTRL))) {

            int metaState = mStateMetaKey.createMetaState();
            event_down = mKeyCode.convertToKeyEvent(keycode, KeyEvent.ACTION_DOWN, metaState);
            sendKey(event_down);
        }

        // 同時押しでなくても有効なメタキーのOFF判定
        mStateMetaKey.changePrintingKey(keycode, event_down);

        // ソフトキーボードのインジケータを更新
        mKeyboardView.setCtrl(mStateMetaKey.isPressed(StateMetaKeyFacade.Meta.CTRL));
        mKeyboardView.setShift(mStateMetaKey.isPressed(StateMetaKeyFacade.Meta.SHIFT));

        KoimeLog.d(String.format(
                Locale.US,
                "SoftKey: MetaKey: SHIFT=%b, CTRL=%b, ALT=%b",
                mStateMetaKey.isPressed(StateMetaKeyFacade.Meta.SHIFT),
                mStateMetaKey.isPressed(StateMetaKeyFacade.Meta.CTRL),
                mStateMetaKey.isPressed(StateMetaKeyFacade.Meta.ALT)
        ));
    }

    boolean onKeyDown(int keycode, KeyEvent event) {
        boolean result = false;

        // ALTキーのキーリピートは無視
        if ((keycode == KeyEvent.KEYCODE_ALT_LEFT)
            && (event.getRepeatCount() > 0)) {
            return true;
        }

        // CTRLキーのキーリピートは無視
        if ((keycode == KeyEvent.KEYCODE_SHIFT_LEFT)
                && (event.getRepeatCount() > 0)) {
            return true;
        }

        // SHIFTキーのキーリピートは無視
        if ((keycode == KeyEvent.KEYCODE_SHIFT_RIGHT)
                && (event.getRepeatCount() > 0)) {
            return true;
        }

        // CTRL. SHIFT, ALTの状態、ソフトキーボードを更新
        mStateMetaKey.changeMetaKey(keycode, event);
        mStateKeyboard.changeHardKey(event);

        // SYM : キーボードの切り替え
        if (keycode == KeyEvent.KEYCODE_SYM) {
            result = true;
        }

        // ALT : キーボード切り替え
        else if (keycode == KeyEvent.KEYCODE_ALT_LEFT) {
            result = false;
        }

        // SHIFT_LEFT : Ctrl
        else if (keycode == KeyEvent.KEYCODE_SHIFT_LEFT) {
            result = true;
        }

        // SHIFT_RIGHT : SHIFT
        else if (keycode == KeyEvent.KEYCODE_SHIFT_RIGHT) {
            result = false;
        }

        else if (keycode == KeyEvent.KEYCODE_ENTER) {
            result = false;
        }
        else if (keycode == KeyEvent.KEYCODE_DEL) {
            handlerDelete();
            result = true;
        }
        else {

            int code = mKeyCode.convert(keycode);

            // 表示可能な文字を出力
            if ((code > 0)
                    && (!mStateMetaKey.isPressed(StateMetaKeyFacade.Meta.CTRL))) {
                if (mStateMetaKey.isPressed(StateMetaKeyFacade.Meta.SHIFT)) {
                    code = Character.toUpperCase(code);
                }
                pressText(code);
                result = true;

            // 表示不可な文字を出力
            } else if ((code < 0)
                    || (mStateMetaKey.isPressed(StateMetaKeyFacade.Meta.CTRL))) {
                int metaState = mStateMetaKey.createMetaState();

                KeyEvent event_down = mKeyCode.convertToKeyEvent(keycode, KeyEvent.ACTION_DOWN, metaState);
                sendKey(event_down);
                result = true;
            }
        }

        mStateMetaKey.changePrintingKey(keycode, event);

        if ((!mStateMetaKey.isPressed(StateMetaKeyFacade.Meta.ALT))
            && (mStateKeyboard.isType(StateKeyboard.State.Symbol2))) {
            KeyEvent event_down = createKeyEvent(KeyEvent.KEYCODE_ALT_LEFT, KeyEvent.ACTION_UP);
            mStateKeyboard.changeHardKey(event_down);
        }

        mKeyboardView.setCtrl(mStateMetaKey.isPressed(StateMetaKeyFacade.Meta.CTRL));
        mKeyboardView.setShift(mStateMetaKey.isPressed(StateMetaKeyFacade.Meta.SHIFT));

        KoimeLog.d(String.format(
                Locale.US,
                "HardKey: MetaKey: SHIFT=%b, CTRL=%b, ALT=%b",
                mStateMetaKey.isPressed(StateMetaKeyFacade.Meta.SHIFT),
                mStateMetaKey.isPressed(StateMetaKeyFacade.Meta.CTRL),
                mStateMetaKey.isPressed(StateMetaKeyFacade.Meta.ALT)
        ));

        return result;
    }

    boolean onKeyUp(int keycode, KeyEvent event) {
        boolean result = false;

        // ALTキーを押下中でもonKeyUpのイベントが発生する対策
        // ALTキー押下中にonKeyUpが発生したら、無視
        if ((keycode == KeyEvent.KEYCODE_ALT_LEFT)
                && event.isAltPressed()) {
            return true;
        }

        mStateMetaKey.changeMetaKey(keycode, event);

        // SHIFT, ALT, CTRLキーをロックする場合は、KeyUpのイベントを無視
        if (!isValidateMetaKey(event)) {
            return true;
        }

        mStateKeyboard.changeHardKey(event);

        // ALT : キーボード切り替え
        if (keycode == KeyEvent.KEYCODE_ALT_LEFT) {
            result = true;

        // SHIFT_LEFT : CTRL
        } else if (keycode == KeyEvent.KEYCODE_SHIFT_LEFT) {
            result = true;

        // SHIFT_RIGHT : SHIFT
        } else if (keycode == KeyEvent.KEYCODE_SHIFT_RIGHT) {
            result = false;

        }

        // CTRL : CTRLキーのインジケータを更新
        mKeyboardView.setCtrl(mStateMetaKey.isPressed(StateMetaKeyFacade.Meta.CTRL));

        // SHIFT : SHIFTキーのインジケータを更新
        mKeyboardView.setShift(mStateMetaKey.isPressed(StateMetaKeyFacade.Meta.SHIFT));

        KoimeLog.d(String.format(
                Locale.US,
                "HardKeyUp: MetaKey: SHIFT=%b, CTRL=%b, ALT=%b",
                mStateMetaKey.isPressed(StateMetaKeyFacade.Meta.SHIFT),
                mStateMetaKey.isPressed(StateMetaKeyFacade.Meta.CTRL),
                mStateMetaKey.isPressed(StateMetaKeyFacade.Meta.ALT)
        ));

        return result;
    }

    void sendKey(KeyEvent event) {
        InputConnection ic = mService.getCurrentInputConnection();

        if (ic == null) { return; }
        ic.sendKeyEvent(event);
    }

    void handlerDelete() {
        InputConnection ic = mService.getCurrentInputConnection();
        EditorInfo info = mService.getCurrentInputEditorInfo();
        if (ic != null) {
            CharSequence currentText = ic.getExtractedText(new ExtractedTextRequest(), 0).text;
            int length = ic.getTextBeforeCursor(currentText.length(), 0).length();
            if (length > 0) {
                ic.deleteSurroundingText(1, 0);
            }
        }
    }

    private void keyDownUp(int keyEventCode) {
        InputConnection ic = mService.getCurrentInputConnection();
        if (ic == null) { return; }
        ic.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, keyEventCode));
        ic.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_UP,   keyEventCode));
    }
    private void pressText(int primaryCode) {
        mService.getCurrentInputConnection().commitText(
                String.valueOf((char) primaryCode),
                1
        );
    }

    private KeyEvent createKeyEvent(int keycode, int action) {
        int metaState = mStateMetaKey.createMetaState();
        return new KeyEvent(0, 0, action, keycode, metaState);
    }

    private KeyEvent createKeyEventForSoftKeyboard(int keycode, int action) {
        int metaState = mStateMetaKey.createMetaState();

        // 入力されたメタキーを反映
        if (action == KeyEvent.ACTION_DOWN) {
            if (keycode == KeyEvent.KEYCODE_ALT_LEFT) {
                metaState |= KeyEvent.META_ALT_LEFT_ON;
                metaState |= KeyEvent.META_ALT_ON;
            }
            if (keycode == KeyEvent.KEYCODE_SHIFT_RIGHT) {
                metaState |= KeyEvent.META_SHIFT_RIGHT_ON;
                metaState |= KeyEvent.META_SHIFT_ON;
            }
            if (keycode == KeyEvent.KEYCODE_SHIFT_LEFT) {
                metaState |= KeyEvent.META_CTRL_LEFT_ON;
                metaState |= KeyEvent.META_CTRL_ON;
            }
        }
        if (action == KeyEvent.ACTION_UP) {
            if (keycode == KeyEvent.KEYCODE_ALT_LEFT) {
                metaState &= KeyEvent.META_ALT_LEFT_ON;
                metaState &= KeyEvent.META_ALT_ON;
            }
            if (keycode == KeyEvent.KEYCODE_SHIFT_RIGHT) {
                metaState &= KeyEvent.META_SHIFT_RIGHT_ON;
                metaState &= KeyEvent.META_SHIFT_ON;
            }
            if (keycode == KeyEvent.KEYCODE_SHIFT_LEFT) {
                metaState &= KeyEvent.META_CTRL_LEFT_ON;
                metaState &= KeyEvent.META_CTRL_ON;
            }
        }

        return new KeyEvent(0, 0, action, keycode, metaState);
    }

    private boolean isValidateMetaKey(KeyEvent event) {
        int keycode = event.getKeyCode();
        int action = event.getAction();
        boolean result = true;

        if ((mStateMetaKey.isPressed(StateMetaKeyFacade.Meta.CTRL))
            && (keycode == KeyEvent.KEYCODE_SHIFT_LEFT)
            && (action == KeyEvent.ACTION_UP)) {
            result = false;
        }
        if ((mStateMetaKey.isPressed(StateMetaKeyFacade.Meta.SHIFT))
                && (keycode == KeyEvent.KEYCODE_SHIFT_RIGHT)
                && (action == KeyEvent.ACTION_UP)) {
            result = false;
        }
        if ((mStateMetaKey.isPressed(StateMetaKeyFacade.Meta.ALT))
                && (keycode == KeyEvent.KEYCODE_ALT_LEFT)
                && (action == KeyEvent.ACTION_UP)) {
            result = false;
        }
        return result;
    }
}
