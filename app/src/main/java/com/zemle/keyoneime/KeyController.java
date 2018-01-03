package com.zemle.keyoneime;

import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.InputConnection;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;

/**
 * Created by nobu on 2018/01/02.
 */

public class KeyController {
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
    private KEYoneService mService;
    private KeyboardView mKeyboardView;
    private StateKeyboardFrame mFrame = new StateKeyboardFrame();

    // 状態変数
    private static boolean shift = false;
    private static boolean ctrl  = false;

    private KeyController() { }

    public static KeyController getInstance() {
        return singleton;
    }

    public void setService(KEYoneService listener, KeyboardView keyboardview) {
        mService = listener;
        mKeyboardView = keyboardview;
        mFrame.setKeyboardView((KeyboardViewQwerty)keyboardview);
    }

    public void onKey(int primaryCode, int[] keyCodes) {

        // 画面に定義しているが未実装のキー
        if (primaryCode == KEYCODE_QWERTY_ALT) {
            return;
        }
        if (primaryCode == KEYCODE_QWERTY_ABC) {
            return;
        }
        if (primaryCode == KEYCODE_QWERTY_DONE) {
            return;
        }

        if (primaryCode == Keyboard.KEYCODE_DELETE) {
            keyDownUp(KeyEvent.KEYCODE_DEL);
            return;
        }

        if (primaryCode == Keyboard.KEYCODE_SHIFT) {
            shift = !shift;
            mKeyboardView.setShifted(shift);
            return;
        }

        if (primaryCode == KEYCODE_QWERTY_SYM) {
            mFrame.pushSoftSYM();
            return;
        }

        if (primaryCode == KEYCODE_QWERTY_CTRL) {
            ctrl = !ctrl;
            if (ctrl) {
                sendKey(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_CTRL_LEFT, KeyEvent.META_CTRL_ON);
            } else {
                sendKey(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_CTRL_LEFT, KeyEvent.META_CTRL_ON);
            }
            return;
        }

        if (primaryCode == KEYCODE_QWERTY_UP) {
            keyDownUp(KeyEvent.KEYCODE_DPAD_UP);
            return;
        }

        if (primaryCode == KEYCODE_QWERTY_DOWN) {
            keyDownUp(KeyEvent.KEYCODE_DPAD_DOWN);
            return;
        }

        if (primaryCode == KEYCODE_QWERTY_LEFT) {
            keyDownUp(KeyEvent.KEYCODE_DPAD_LEFT);
            return;
        }

        if (primaryCode == KEYCODE_QWERTY_RIGHT) {
            keyDownUp(KeyEvent.KEYCODE_DPAD_RIGHT);
            return;
        }

        if (ctrl) {
            ctrl_moji(primaryCode);
            ctrl = false;
            return;
        }

        letter(primaryCode);
    }

    public boolean onKeyDown(int keycode, KeyEvent event) {
        int code;

        // SYM : キーボードの切り替え
        if (keycode == KeyEvent.KEYCODE_SYM) {
            mFrame.pushHardSYM();
            return false;
        }

        // ALT : キーボード切り替え
        if (keycode == KeyEvent.KEYCODE_ALT_LEFT) {
            mFrame.downHardALT();
            return false;
        }

        // SHIFT_LEFT : Ctrl
        if (keycode == KeyEvent.KEYCODE_SHIFT_LEFT) {
            ctrl = true;
            sendKey(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_CTRL_LEFT, KeyEvent.META_CTRL_ON);
            return true;
        }

        // SHIFT_RIGHT : SHIFT
        if (keycode == KeyEvent.KEYCODE_SHIFT_RIGHT) {
            shift = true;
            mKeyboardView.setShifted(shift);
            return false;
        }

        if (keycode == KeyEvent.KEYCODE_ENTER) {
            return false;
        }

        if (keycode == KeyEvent.KEYCODE_DEL) {
            keyDownUp(KeyEvent.KEYCODE_DEL);
            return true;
        }

        // ALTキーを押下中でもonKeyUpのイベントが発生する対策
        if ((event.isAltPressed())
                && (mFrame.getType() != StateKeyboard.Symbol2)) {
            mFrame.downHardALT();
        }

        code = mFrame.convertKeycode(keycode);

        if (code == KEYCODE_QWERTY_UP) {
            keyDownUp(KeyEvent.KEYCODE_DPAD_UP);
            return true;
        }
        if (code == KEYCODE_QWERTY_DOWN) {
            keyDownUp(KeyEvent.KEYCODE_DPAD_DOWN);
            return true;
        }
        if (code == KEYCODE_QWERTY_LEFT) {
            keyDownUp(KeyEvent.KEYCODE_DPAD_LEFT);
            return true;
        }
        if (code == KEYCODE_QWERTY_RIGHT) {
            keyDownUp(KeyEvent.KEYCODE_DPAD_RIGHT);
            return true;
        }

        // CTRL + LowerCase
        if (ctrl) {
            ctrl_moji(code);
            ctrl = false;
            return true;
        }

        if (isHardKey(keycode)) {
            letter(code);
            return true;
        }

        return false;
    }

    public boolean onKeyUp(int keycode, KeyEvent event) {
        InputConnection ic = mService.getCurrentInputConnection();
        int code;

        // ALT : キーボード切り替え
        if (keycode == KeyEvent.KEYCODE_ALT_LEFT) {
            mFrame.upHardALT();
            return false;
        }

        // SHIFT_LEFT : CTRL
        if (keycode == KeyEvent.KEYCODE_SHIFT_LEFT) {
            ctrl = false;
            sendKey(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_CTRL_LEFT, KeyEvent.META_CTRL_ON);
            return true;
        }

        // SHIFT_RIGHT : SHIFT
        if (keycode == KeyEvent.KEYCODE_SHIFT_RIGHT) {
            shift = false;
            mKeyboardView.setShifted(shift);
            return false;
        }

        return false;
    }

    private boolean isHardKey(int primaryCode) {
        boolean result = false;

        switch(primaryCode) {
            case KeyEvent.KEYCODE_Q: result = true; break;
            case KeyEvent.KEYCODE_W: result = true; break;
            case KeyEvent.KEYCODE_E: result = true; break;
            case KeyEvent.KEYCODE_R: result = true; break;
            case KeyEvent.KEYCODE_T: result = true; break;
            case KeyEvent.KEYCODE_Y: result = true; break;
            case KeyEvent.KEYCODE_U: result = true; break;
            case KeyEvent.KEYCODE_I: result = true; break;
            case KeyEvent.KEYCODE_O: result = true; break;
            case KeyEvent.KEYCODE_P: result = true; break;

            case KeyEvent.KEYCODE_A: result = true; break;
            case KeyEvent.KEYCODE_S: result = true; break;
            case KeyEvent.KEYCODE_D: result = true; break;
            case KeyEvent.KEYCODE_F: result = true; break;
            case KeyEvent.KEYCODE_G: result = true; break;
            case KeyEvent.KEYCODE_H: result = true; break;
            case KeyEvent.KEYCODE_J: result = true; break;
            case KeyEvent.KEYCODE_K: result = true; break;
            case KeyEvent.KEYCODE_L: result = true; break;
            case KeyEvent.KEYCODE_DEL: result = true; break;

            case KeyEvent.KEYCODE_ALT_LEFT: result = true; break;
            case KeyEvent.KEYCODE_Z: result = true; break;
            case KeyEvent.KEYCODE_X: result = true; break;
            case KeyEvent.KEYCODE_C: result = true; break;
            case KeyEvent.KEYCODE_V: result = true; break;
            case KeyEvent.KEYCODE_B: result = true; break;
            case KeyEvent.KEYCODE_N: result = true; break;
            case KeyEvent.KEYCODE_M: result = true; break;
            case KeyEvent.KEYCODE_4: result = true; break;
            case KeyEvent.KEYCODE_ENTER: result = true; break;

            case KeyEvent.KEYCODE_SHIFT_LEFT: result = true; break;
            case KeyEvent.KEYCODE_0: result = true; break;
            case KeyEvent.KEYCODE_SPACE: result = true; break;
            case KeyEvent.KEYCODE_SYM: result = true; break;
            case KeyEvent.KEYCODE_SHIFT_RIGHT: result = true; break;
        }
        return result;
    }

    private void ctrl_moji(int primaryCode) {
        switch ((char)primaryCode) {
            case 'a':
                sendKey(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_A, KeyEvent.META_CTRL_ON);
                break;
            case 'b':
                sendKey(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_B, KeyEvent.META_CTRL_ON);
                break;
            case 'c':
                sendKey(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_C, KeyEvent.META_CTRL_ON);
                break;
            case 'd':
                sendKey(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_D, KeyEvent.META_CTRL_ON);
                break;
            case 'e':
                sendKey(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_E, KeyEvent.META_CTRL_ON);
                break;
            case 'f':
                sendKey(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_F, KeyEvent.META_CTRL_ON);
                break;
            case 'g':
                sendKey(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_G, KeyEvent.META_CTRL_ON);
                break;
            case 'h':
                sendKey(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_H, KeyEvent.META_CTRL_ON);
                break;
            case 'i':
                sendKey(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_I, KeyEvent.META_CTRL_ON);
                break;
            case 'j':
                sendKey(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_J, KeyEvent.META_CTRL_ON);
                break;
            case 'k':
                sendKey(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_K, KeyEvent.META_CTRL_ON);
                break;
            case 'l':
                sendKey(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_L, KeyEvent.META_CTRL_ON);
                break;
            case 'm':
                sendKey(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_M, KeyEvent.META_CTRL_ON);
                break;
            case 'n':
                sendKey(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_N, KeyEvent.META_CTRL_ON);
                break;
            case 'o':
                sendKey(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_O, KeyEvent.META_CTRL_ON);
                break;
            case 'p':
                sendKey(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_P, KeyEvent.META_CTRL_ON);
                break;
            case 'q':
                sendKey(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_Q, KeyEvent.META_CTRL_ON);
                break;
            case 'r':
                sendKey(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_R, KeyEvent.META_CTRL_ON);
                break;
            case 's':
                sendKey(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_S, KeyEvent.META_CTRL_ON);
                break;
            case 't':
                sendKey(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_T, KeyEvent.META_CTRL_ON);
                break;
            case 'u':
                sendKey(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_U, KeyEvent.META_CTRL_ON);
                break;
            case 'v':
                sendKey(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_V, KeyEvent.META_CTRL_ON);
                break;
            case 'w':
                sendKey(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_W, KeyEvent.META_CTRL_ON);
                break;
            case 'x':
                sendKey(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_X, KeyEvent.META_CTRL_ON);
                break;
            case 'y':
                sendKey(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_Y, KeyEvent.META_CTRL_ON);
                break;
            case 'z':
                sendKey(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_Z, KeyEvent.META_CTRL_ON);
                break;
            default:
                letter(primaryCode);
                break;
        }
    }

    private void keyDownUp(int keyEventCode) {
        InputConnection ic = mService.getCurrentInputConnection();
        if (ic == null) { return; }
        ic.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, keyEventCode));
        ic.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_UP,   keyEventCode));
    }

    private void letter(int primaryCode) {
        if (shift) {
            primaryCode = Character.toUpperCase(primaryCode);
        }
        pressText(primaryCode);
    }

    private void pressText(int primaryCode) {
        mService.getCurrentInputConnection().commitText(
            String.valueOf((char) primaryCode),
            1
        );
    }

    private void sendKey(int action, int code, int meta) {
        long now = System.currentTimeMillis();
        InputConnection ic = mService.getCurrentInputConnection();

        if (ic == null) { return; }
        ic.sendKeyEvent(new KeyEvent(
                now, now, action, code,0, meta
        ));
    }
}
