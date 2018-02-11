package com.zemle.keyoneime;

import android.view.KeyEvent;

import java.util.EnumSet;

/**
 * ソフトキーボードの状態遷移
 *
 * Created by shntn on 2018/01/11.
 */

class StateKeyboard {
    enum State {
        Qwerty(R.xml.qwerty) {
            public void changeHard(StateKeyboard context, KoimeKey key) {
                if (key.contains(EnumSet.of(KoimeKey.Key.ALT))) {
                    context.changeState(HideSymbol2);
                } else if (key.contains(EnumSet.of(KoimeKey.Key.SYM))) {
                    context.changeState(Symbol1);
                } else {
                    // 状態保持
                }
            }

            public void changeSoft(StateKeyboard context, KoimeKey key) {
                if (key.contains(EnumSet.of(KoimeKey.Key.ALT))) {
                    context.changeState(Symbol2);
                } else if (key.contains(EnumSet.of(KoimeKey.Key.SYM))) {
                    context.changeState(Symbol1);
                } else {
                    // 状態保持
                }
            }
        },
        Symbol1(R.xml.symbol1) {
            public void changeHard(StateKeyboard context, KoimeKey key) {
                if (key.contains(EnumSet.of(KoimeKey.Key.ALT))) {
                    context.changeState(HideSymbol2);
                } else if (key.contains(EnumSet.of(KoimeKey.Key.SYM))) {
                    // 状態保持
                } else {
                    context.changeState(HideQwerty);
                }
            }

            public void changeSoft(StateKeyboard context, KoimeKey key) {
                if (key.contains(EnumSet.of(KoimeKey.Key.ALT))) {
                    context.changeState(Symbol2);
                } else if (key.contains(EnumSet.of(KoimeKey.Key.SYM))) {
                    // 状態保持
                } else {
                    context.changeState(Qwerty);
                }
            }

            public State getType() {
                return State.Symbol1;
            }
        },
        Symbol2(R.xml.symbol2) {
            public void changeHard(StateKeyboard context, KoimeKey key) {
                if (key.contains(EnumSet.of(KoimeKey.Key.ALT))) {
                    // 状態保持
                } else if (key.contains(EnumSet.of(KoimeKey.Key.SYM))) {
                    context.changeState(Symbol1);
                } else {
                    context.changeState(HideQwerty);
                }
            }

            public void changeSoft(StateKeyboard context, KoimeKey key) {
                if (key.contains(EnumSet.of(KoimeKey.Key.ALT))) {
                    // 状態保持
                } else if (key.contains(EnumSet.of(KoimeKey.Key.SYM))) {
                    context.changeState(Symbol1);
                } else {
                    context.changeState(Qwerty);
                }
            }

            public State getType() {
                return State.Symbol2;
            }
        },
        HideQwerty(R.xml.hide) {
            public void changeHard(StateKeyboard context, KoimeKey key) {
                if (key.contains(EnumSet.of(KoimeKey.Key.ALT))) {
                    context.changeState(HideSymbol2);
                } else if (key.contains(EnumSet.of(KoimeKey.Key.SYM))) {
                    context.changeState(Symbol1);
                } else {
                    // 状態保持
                }
            }

            public State getType() {
                return State.Qwerty;
            }
        },
        HideSymbol2(R.xml.hide) {
            public void changeHard(StateKeyboard context, KoimeKey key) {
                if (key.contains(EnumSet.of(KoimeKey.Key.ALT))) {
                    // 状態保持
                } else if (key.contains(EnumSet.of(KoimeKey.Key.SYM))) {
                    context.changeState(Symbol1);
                } else {
                    context.changeState(HideQwerty);
                }
            }

            public State getType() {
                return State.Symbol2;
            }
        };

        int xmlId;

        State(int xmlId) {
            this.xmlId = xmlId;
        }

        void leave(KoimeKeyboard keyboardView) {
        }

        void enter(KoimeKeyboard keyboardView) {
            //keyboardView.setKeyboard(this.xmlId);
        }

        void changeHard(StateKeyboard context, KoimeKey key) {
        }
        void changeSoft(StateKeyboard context, KoimeKey key) {
        }

        State getType() {
            return State.Qwerty;
        }

        int getId() {
            return xmlId;
        }
    }

    private State mState;
    private KoimeKeyboard mKeyboardView;

    private void changeState(State state) {
        mState.leave(mKeyboardView);
        mState = state;
        mState.enter(mKeyboardView);
    }

    StateKeyboard() {
        mState = State.Qwerty;
    }

    void changeHardKey(KoimeKey key) {
        mState.changeHard(this, key);
    }

    void changeSoftKey(KoimeKey key) {
        mState.changeSoft(this, key);
    }

    boolean isState(State state) {
        return state == mState;
    }

    boolean isType(State state) {
        return state == mState.getType();
    }

    int getId() {
        return mState.getId();
    }
}

