package com.zemle.keyoneime;

import android.view.KeyEvent;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * 入力キー管理
 *
 * Created by shntn on 2018/01/28.
 */

class KoimeKey implements Cloneable {
    enum Key {
        // Printing Key
        Q, W, E, R, T, Y, U, I, O, P,
        A, S, D, F, G, H, J, K, L,
        Z, X, C, V, B, N, M, NUM_4,
        NUM_0, SPACE,

        // SPECIAL KEY
        DEL,
        ENTER,
        ALT,
        SYM,
        SHIFT_LEFT,
        SHIFT_RIGHT,

        // UP, DOWN, REPEAT
        UP, DOWN, REPEAT,
    }

    private static final List<Key> KEYSTATE = Collections.unmodifiableList(
        new ArrayList<Key>() {{
            add(Key.DOWN);
            add(Key.UP);
            add(Key.REPEAT);
        }}
    );

    private static final Map<Integer, Key> KEYCODE2KEY = Collections.unmodifiableMap(
        new HashMap<Integer, Key>() {{
            put(KeyEvent.KEYCODE_Q, Key.Q);
            put(KeyEvent.KEYCODE_W, Key.W);
            put(KeyEvent.KEYCODE_E, Key.E);
            put(KeyEvent.KEYCODE_R, Key.R);
            put(KeyEvent.KEYCODE_T, Key.T);
            put(KeyEvent.KEYCODE_Y, Key.Y);
            put(KeyEvent.KEYCODE_U, Key.U);
            put(KeyEvent.KEYCODE_I, Key.I);
            put(KeyEvent.KEYCODE_O, Key.O);
            put(KeyEvent.KEYCODE_P, Key.P);

            put(KeyEvent.KEYCODE_A, Key.A);
            put(KeyEvent.KEYCODE_S, Key.S);
            put(KeyEvent.KEYCODE_D, Key.D);
            put(KeyEvent.KEYCODE_F, Key.F);
            put(KeyEvent.KEYCODE_G, Key.G);
            put(KeyEvent.KEYCODE_H, Key.H);
            put(KeyEvent.KEYCODE_J, Key.J);
            put(KeyEvent.KEYCODE_K, Key.K);
            put(KeyEvent.KEYCODE_L, Key.L);

            put(KeyEvent.KEYCODE_Z, Key.Z);
            put(KeyEvent.KEYCODE_X, Key.X);
            put(KeyEvent.KEYCODE_C, Key.C);
            put(KeyEvent.KEYCODE_V, Key.V);
            put(KeyEvent.KEYCODE_B, Key.B);
            put(KeyEvent.KEYCODE_N, Key.N);
            put(KeyEvent.KEYCODE_M, Key.M);
            put(KeyEvent.KEYCODE_4, Key.NUM_4);

            put(KeyEvent.KEYCODE_0, Key.NUM_0);
            put(KeyEvent.KEYCODE_SPACE, Key.SPACE);

            put(KeyEvent.KEYCODE_DEL, Key.DEL);
            put(KeyEvent.KEYCODE_ENTER, Key.ENTER);

            put(KeyEvent.KEYCODE_SHIFT_LEFT, Key.SHIFT_LEFT);
            put(KeyEvent.KEYCODE_SHIFT_RIGHT, Key.SHIFT_RIGHT);
            put(KeyEvent.KEYCODE_ALT_LEFT, Key.ALT);
            put(KeyEvent.KEYCODE_SYM, Key.SYM);
        }}
    );

    private EnumSet<Key>    mKeySet;
    private EnumSet<Key>    mPressed;

    KoimeKey() {
        mKeySet = EnumSet.noneOf(Key.class);
        mPressed = EnumSet.noneOf(Key.class);
    }

    Set<Key> getBySet() {
        return mKeySet;
    }

    void add(Set<Key> set) {
        for (Key key : set ) {
            mKeySet.add(key);
        }
    }

    void remove(Set<Key> set) {
        for (Key key : set) {
            mKeySet.remove(key);
        }
    }

    void update(Set<Key> key, boolean state) {
        if (state) {
            add(key);
        } else {
            remove(key);
        }
    }

    boolean isPressed(Key key) {
        boolean result = false;

        if (mPressed.containsAll(EnumSet.of(key, Key.DOWN))) {
            result = true;
        }
        if (mPressed.containsAll(EnumSet.of(key, Key.REPEAT))) {
            result = true;
        }
        return result;
    }

    boolean contains(Set<Key> key) {
        return mKeySet.containsAll(key);
    }

    boolean equals(Set<Key> key) {
        return mKeySet.equals(key);
    }

    // 修飾キー以外のキー情報を反映
    void updateFromKeyEvent(KeyEvent event) {
        Key key;
        Key state = Key.UP;

        key = KEYCODE2KEY.get(event.getKeyCode());
        if (key == null) {
            return;
        }
        if (!isModifier(key)) {
            add(EnumSet.of(key));

            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                state = Key.DOWN;
                if (event.getRepeatCount() > 0) {
                    state = Key.REPEAT;
                }
            }
            mKeySet.removeAll(KEYSTATE);
            mKeySet.add(state);
        }
        {
            mPressed = EnumSet.noneOf(Key.class);
            mPressed.add(key);
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                state = Key.DOWN;
                if (event.getRepeatCount() > 0) {
                    state = Key.REPEAT;
                }
            }
            if (event.getAction() == KeyEvent.ACTION_UP) {
                state = Key.UP;
            }
            mPressed.add(state);
        }
    }

    int makeMetaState() {
        int metaState = 0;

        if (mKeySet.contains(Key.ALT)) {
            metaState |= KeyEvent.META_ALT_LEFT_ON;
            metaState |= KeyEvent.META_ALT_ON;
        }
        if (mKeySet.contains(Key.SHIFT_RIGHT)) {
            metaState |= KeyEvent.META_SHIFT_RIGHT_ON;
            metaState |= KeyEvent.META_SHIFT_ON;
        }
        if (mKeySet.contains(Key.SHIFT_LEFT)) {
            metaState |= KeyEvent.META_CTRL_LEFT_ON;
            metaState |= KeyEvent.META_CTRL_ON;
        }

        return metaState;
    }

    public String toString() {
       return String.format(
               Locale.US,
               "KoimeKey {%s}",
               mKeySet.toString()
        );
    }

    @Override
    public KoimeKey clone() {
        KoimeKey result = null;

        try {
            result = (KoimeKey)super.clone();
            result.mKeySet = this.mKeySet.clone();
            result.mPressed = this.mPressed;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return result;
    }

    private boolean isModifier(Key key) {
        boolean result = false;

        switch (key) {
            case SYM:
            case ALT:
            case SHIFT_LEFT:
            case SHIFT_RIGHT:
                result = true;
                break;
        }

        return result;
    }
}

