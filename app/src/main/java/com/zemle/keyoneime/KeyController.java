package com.zemle.keyoneime;

import android.content.Context;
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
    private StateKeyboard mFrame = new StateKeyboard();
    private KeyCode mKeyCode;

    private StateMetaKey mStateCtrlKey;
    private StateMetaKey mStateShiftKey;
    private StateMetaKey mStateAltKey;

    // 状態変数
    private static boolean shift = false;
    private static boolean ctrl  = false;

    private KeyController() { }

    static KeyController getInstance() {
        return singleton;
    }

    void setService(Context context, KoimeService service) {
        mService = service;
        mFrame.setService(service);
        mStateCtrlKey = new StateMetaKey();
        mStateShiftKey = new StateMetaKey();
        mStateAltKey = new StateMetaKey();
        mKeyCode = new KeyCode(context, mFrame);
    }

    void onKey(int primaryCode, int[] keyCodes) {

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

        // SHIFT :
        if (primaryCode == Keyboard.KEYCODE_SHIFT) {
            mStateShiftKey.press();
            mStateShiftKey.release();
            mService.setShift(mStateShiftKey.isPress());

        // CTRL :
        } else if (primaryCode == KEYCODE_QWERTY_CTRL) {
            mStateCtrlKey.press();
            mStateCtrlKey.release();
            mService.setCtrl(mStateCtrlKey.isPress());

        // DELETE :
        } else if (primaryCode == Keyboard.KEYCODE_DELETE) {
            keyDownUp(KeyEvent.KEYCODE_DEL);

        // SYM :
        } else if (primaryCode == KEYCODE_QWERTY_SYM) {
            mFrame.pushSoftSYM();
            if (mFrame.isState(StateKeyboard.State.Symbol2)) {
                mStateAltKey.press();
                mStateAltKey.press();
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
            if (mStateCtrlKey.isPress()) {
                ctrl_moji(primaryCode);
                mStateCtrlKey.use();

            } else {
                if (mStateShiftKey.isPress()) {
                    primaryCode = Character.toUpperCase(primaryCode);
                    mStateShiftKey.use();
                }
                pressText(primaryCode);
            }

            // ソフトキーボードのインジケータを更新
            mService.setCtrl(mStateCtrlKey.isPress());
            mService.setCtrl(mStateShiftKey.isPress());
        }

        KoimeLog.d(String.format(
                Locale.US,
                "SoftKey: MetaKey: SHIFT=%b, CTRL=%b, ALT=%b",
                mStateShiftKey.isPress(), mStateCtrlKey.isPress(), mStateAltKey.isPress()

        ));
    }

    boolean onKeyDown(int keycode, KeyEvent event) {
        int code;

        // SYM : キーボードの切り替え
        if (keycode == KeyEvent.KEYCODE_SYM) {
            mFrame.pushHardSYM();
            return true;
        }

        // ALT : キーボード切り替え
        if (keycode == KeyEvent.KEYCODE_ALT_LEFT) {
            if (event.getRepeatCount() == 0) {
                mStateAltKey.press();
                mFrame.downHardALT();

                KoimeLog.d(String.format(
                        Locale.US,
                        "HardKeyDown: MetaKey: SHIFT=%b, CTRL=%b, ALT=%b",
                        mStateShiftKey.isPress(), mStateCtrlKey.isPress(), mStateAltKey.isPress()

                ));

                return false;
            } else {
                return true;
            }
        }

        // SHIFT_LEFT : Ctrl
        if (keycode == KeyEvent.KEYCODE_SHIFT_LEFT) {
            if (event.getRepeatCount() == 0) {
                mStateCtrlKey.press();
                mService.setCtrl(true);

                KoimeLog.d(String.format(
                        Locale.US,
                        "HardKeyDown: MetaKey: SHIFT=%b, CTRL=%b, ALT=%b",
                        mStateShiftKey.isPress(), mStateCtrlKey.isPress(), mStateAltKey.isPress()

                ));

                return true;
            } else {
                return true;
            }
        }

        // SHIFT_RIGHT : SHIFT
        if (keycode == KeyEvent.KEYCODE_SHIFT_RIGHT) {
            if (event.getRepeatCount() == 0) {
                mStateShiftKey.press();
                mService.setShift(true);

                KoimeLog.d(String.format(
                        Locale.US,
                        "HardKeyDown: MetaKey: SHIFT=%b, CTRL=%b, ALT=%b",
                        mStateShiftKey.isPress(), mStateCtrlKey.isPress(), mStateAltKey.isPress()

                ));

                return false;
            } else {
                return true;
            }
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
                && (!mFrame.isType(StateKeyboard.State.Symbol2))) {
            mFrame.downHardALT();
        }

        code = mKeyCode.convert(keycode);

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
        if (mStateCtrlKey.isPress()) {
            ctrl_moji(code);
            mStateCtrlKey.use();

            KoimeLog.d(String.format(
                    Locale.US,
                    "HardKey: MetaKey: SHIFT=%b, CTRL=%b, ALT=%b",
                    mStateShiftKey.isPress(), mStateCtrlKey.isPress(), mStateAltKey.isPress()

            ));

            return true;
        }

        if (mKeyCode.isHardKey(keycode)) {
            if (mStateShiftKey.isPress()) {
                code = Character.toUpperCase(code);
                mStateShiftKey.use();

                KoimeLog.d(String.format(
                        Locale.US,
                        "HardKey: MetaKey: SHIFT=%b, CTRL=%b, ALT=%b",
                        mStateShiftKey.isPress(), mStateCtrlKey.isPress(), mStateAltKey.isPress()

                ));
            }
            pressText(code);
            return true;
        }

        return false;
    }

    boolean onKeyUp(int keycode, KeyEvent event) {
        boolean result = false;

        // ALTキーを押下中でもonKeyUpのイベントが発生する対策
        // ALTキー押下中にonKeyUpが発生したら、無視
        if ((keycode == KeyEvent.KEYCODE_ALT_LEFT)
                && event.isAltPressed()) {
            result = true;

        // ALT : キーボード切り替え
        } else if ((keycode == KeyEvent.KEYCODE_ALT_LEFT)
                || (keycode == KeyEvent.KEYCODE_ALT_RIGHT)) {
            mStateAltKey.release();
            result = true;

            KoimeLog.d(String.format(
                    Locale.US,
                    "HardKeyUp: MetaKey: SHIFT=%b, CTRL=%b, ALT=%b",
                    mStateShiftKey.isPress(), mStateCtrlKey.isPress(), mStateAltKey.isPress()
            ));

        // SHIFT_LEFT : CTRL
        } else if (keycode == KeyEvent.KEYCODE_SHIFT_LEFT) {
            mStateCtrlKey.release();
            result = true;

            KoimeLog.d(String.format(
                    Locale.US,
                    "HardKeyUp: MetaKey: SHIFT=%b, CTRL=%b, ALT=%b",
                    mStateShiftKey.isPress(), mStateCtrlKey.isPress(), mStateAltKey.isPress()
            ));

        // SHIFT_RIGHT : SHIFT
        } else if (keycode == KeyEvent.KEYCODE_SHIFT_RIGHT) {
            mStateShiftKey.release();
            result = false;

            KoimeLog.d(String.format(
                    Locale.US,
                    "HardKeyUp: MetaKey: SHIFT=%b, CTRL=%b, ALT=%b",
                    mStateShiftKey.isPress(), mStateCtrlKey.isPress(), mStateAltKey.isPress()
            ));
        }

        // ソフトキーボードの表示を更新
        if (keycode != KeyEvent.KEYCODE_SYM) {
            // ALT : ソフトキーボードの表示切り替え
            if (mStateAltKey.isPress()) {
                mFrame.upHardALT();
            }

            // CTRL : CTRLキーのインジケータを更新
            mService.setCtrl(mStateCtrlKey.isPress());

            // SHIFT : SHIFTキーのインジケータを更新
            mService.setShift(mStateShiftKey.isPress());
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
}
