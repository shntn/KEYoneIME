package com.zemle.keyoneime;

import android.content.Context;
import android.inputmethodservice.KeyboardView;
import android.view.KeyEvent;
import android.view.inputmethod.InputConnection;
import android.inputmethodservice.Keyboard;

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
        if (primaryCode == KEYCODE_QWERTY_ALT) {
            return;
        }
        if (primaryCode == KEYCODE_QWERTY_ABC) {
            return;
        }
        if (primaryCode == KEYCODE_QWERTY_DONE) {
            return;
        }

        // SHIFT, ALT, CTRL
        keycode = convertKeyCode(primaryCode);
        event_down = createKeyEvent(keycode, KeyEvent.ACTION_DOWN);
        event_up = createKeyEvent(keycode, KeyEvent.ACTION_UP);
        mStateMetaKey.changeMetaKey(keycode, event_down);
        mStateMetaKey.changeMetaKey(keycode, event_up);

        // SoftKeyBoard 更新
        mStateKeyboard.changeSoftKey(event_down);

        // DELETE :
        if (primaryCode == Keyboard.KEYCODE_DELETE) {
            keyDownUp(KeyEvent.KEYCODE_DEL);

        // SYM :
        } else if (primaryCode == KEYCODE_QWERTY_SYM) {
            //mStateKeyboard.pushSoftSYM();
            if (mStateKeyboard.isState(StateKeyboard.State.Symbol2)) {
                keycode = convertKeyCode(KEYCODE_QWERTY_ALT);
                event_down = createKeyEvent(KEYCODE_QWERTY_ALT, KeyEvent.ACTION_DOWN);
                event_up = createKeyEvent(KEYCODE_QWERTY_ALT, KeyEvent.ACTION_UP);
                mStateMetaKey.changeMetaKey(keycode, event_down);
                mStateMetaKey.changeMetaKey(keycode, event_up);
                mStateMetaKey.changeMetaKey(keycode, event_down);
                mStateMetaKey.changeMetaKey(keycode, event_up);
            }

        // 矢印キー
        } else if (primaryCode == KEYCODE_QWERTY_UP) {
            keyDownUp(KeyEvent.KEYCODE_DPAD_UP);

        } else if (primaryCode == KEYCODE_QWERTY_DOWN) {
            keyDownUp(KeyEvent.KEYCODE_DPAD_DOWN);

        } else if (primaryCode == KEYCODE_QWERTY_LEFT) {
            keyDownUp(KeyEvent.KEYCODE_DPAD_LEFT);

        } else if (primaryCode == KEYCODE_QWERTY_RIGHT) {
            keyDownUp(KeyEvent.KEYCODE_DPAD_RIGHT);

        // 文字キー
        } else {

            // CTRL + LowerCase
            if (mStateMetaKey.isPressed(StateMetaKeyFacade.Meta.CTRL)) {
                ctrl_moji(primaryCode);

            } else {
                if (mStateMetaKey.isPressed(StateMetaKeyFacade.Meta.SHIFT)) {
                    primaryCode = Character.toUpperCase(primaryCode);
                }
                pressText(primaryCode);
            }
        }

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
        int code;
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
            keyDownUp(KeyEvent.KEYCODE_DEL);
            result = true;
        }

        else {

            code = mKeyCode.convert(keycode);

            if (code == KEYCODE_QWERTY_UP) {
                keyDownUp(KeyEvent.KEYCODE_DPAD_UP);
                result = true;
            }
            else if (code == KEYCODE_QWERTY_DOWN) {
                keyDownUp(KeyEvent.KEYCODE_DPAD_DOWN);
                result = true;
            }
            else if (code == KEYCODE_QWERTY_LEFT) {
                keyDownUp(KeyEvent.KEYCODE_DPAD_LEFT);
                result = true;
            }
            else if (code == KEYCODE_QWERTY_RIGHT) {
                keyDownUp(KeyEvent.KEYCODE_DPAD_RIGHT);
                result = true;
            }

            // CTRL + LowerCase
            else if (mStateMetaKey.isPressed(StateMetaKeyFacade.Meta.CTRL)) {
                ctrl_moji(code);
                result = true;
            }

            else if (mKeyCode.isHardKey(keycode)) {
                if (mStateMetaKey.isPressed(StateMetaKeyFacade.Meta.SHIFT)) {
                    code = Character.toUpperCase(code);
                }
                pressText(code);
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
/*
        // ソフトキーボードの表示を更新
        if (keycode != KeyEvent.KEYCODE_SYM) {
            // ALT : ソフトキーボードの表示切り替え
            if (!mStateMetaKey.isPressed(StateMetaKeyFacade.Meta.ALT)) {
                mStateKeyboard.upHardALT();
            }

        }
*/
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

    void sendKey(int action, int code) {
        InputConnection ic = mService.getCurrentInputConnection();

        if (ic == null) { return; }
        ic.sendKeyEvent(new KeyEvent(action, code));
    }

    void clearMetaKey(int states) {
        InputConnection ic = mService.getCurrentInputConnection();
        if (ic != null) {
            ic.clearMetaKeyStates(states);
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

    private int convertKeyCode(int primaryCode) {
        int code = 0;

        switch (primaryCode) {
            case 'q':
            case '#':
                code = KeyEvent.KEYCODE_Q;
                break;
            case 'w':
            case '1':
                code = KeyEvent.KEYCODE_W;
                break;
            case 'e':
            case '2':
                code = KeyEvent.KEYCODE_E;
                break;
            case 'r':
            case '3':
                code = KeyEvent.KEYCODE_R;
                break;
            case 't':
            case '(':
                code = KeyEvent.KEYCODE_T;
                break;
            case 'y':
            case ')':
                code = KeyEvent.KEYCODE_Y;
                break;
            case 'u':
            case '_':
                code = KeyEvent.KEYCODE_U;
                break;
            case 'i':
            case '-':
                code = KeyEvent.KEYCODE_I;
                break;
            case 'o':
            case '+':
                code = KeyEvent.KEYCODE_O;
                break;
            case 'p':
            case '@':
                code = KeyEvent.KEYCODE_P;
                break;

            case 'a':
            case '*':
                code = KeyEvent.KEYCODE_A;
                break;
            case 's':
            case '4':
                code = KeyEvent.KEYCODE_S;
                break;
            case 'd':
            case '5':
                code = KeyEvent.KEYCODE_D;
                break;
            case 'f':
            case '6':
                code = KeyEvent.KEYCODE_F;
                break;
            case 'g':
            case '/':
                code = KeyEvent.KEYCODE_G;
                break;
            case 'h':
            case ':':
                code = KeyEvent.KEYCODE_H;
                break;
            case 'j':
            case ';':
                code = KeyEvent.KEYCODE_J;
                break;
            case 'k':
            case '\'':
                code = KeyEvent.KEYCODE_K;
                break;
            case 'l':
            case '"':
                code = KeyEvent.KEYCODE_L;
                break;
            case -5:
                code = KeyEvent.KEYCODE_DEL;
                break;

            // ALT はない
            case 'z':
            case '7':
                code = KeyEvent.KEYCODE_Z;
                break;
            case 'x':
            case '8':
                code = KeyEvent.KEYCODE_X;
                break;
            case 'c':
            case '9':
                code = KeyEvent.KEYCODE_C;
                break;
            case 'v':
            case '?':
                code = KeyEvent.KEYCODE_V;
                break;
            case 'b':
            case '!':
                code = KeyEvent.KEYCODE_B;
                break;
            case 'n':
            case ',':
                code = KeyEvent.KEYCODE_N;
                break;
            case 'm':
            case '.':
                code = KeyEvent.KEYCODE_M;
                break;
            case '$':
                code = KeyEvent.KEYCODE_4;
                break;
            case 10:    // enter
                code = KeyEvent.KEYCODE_ENTER;
                break;

            case -2:    // Ctrl
                code = KeyEvent.KEYCODE_SHIFT_LEFT;
                break;
            case ' ':
                code = KeyEvent.KEYCODE_SPACE;
                break;
            case -1012: // SYM
                code = KeyEvent.KEYCODE_SYM;
                break;
            case -1:    // SHIFT
                code = KeyEvent.KEYCODE_SHIFT_RIGHT;
                break;
        }
        return code;
    }

    private KeyEvent createKeyEvent(int keycode, int action) {
        int metaState = 0;

        // 現状のメタキー状態を作成
        if (mStateMetaKey.isPressed(StateMetaKeyFacade.Meta.ALT)) {
            metaState |= KeyEvent.META_ALT_LEFT_ON;
            metaState |= KeyEvent.META_ALT_ON;
        }
        if (mStateMetaKey.isPressed(StateMetaKeyFacade.Meta.SHIFT)) {
            metaState |= KeyEvent.META_SHIFT_RIGHT_ON;
            metaState |= KeyEvent.META_SHIFT_ON;
        }
        if (mStateMetaKey.isPressed(StateMetaKeyFacade.Meta.CTRL)) {
            metaState |= KeyEvent.META_CTRL_LEFT_ON;
            metaState |= KeyEvent.META_CTRL_ON;
        }

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
