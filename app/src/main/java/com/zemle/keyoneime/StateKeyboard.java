package com.zemle.keyoneime;

/**
 * ソフトキーボードの状態遷移
 *
 * Created by shntn on 2018/01/11.
 */

class StateKeyboard {
    enum State {
        Qwerty(R.xml.qwerty) {
            @Override
            public void pushSoftSYM(StateKeyboard context) {
                context.changeState(Symbol1);
            }

            @Override
            public void pushHardSYM(StateKeyboard context) {
                context.changeState(HideQwerty);
            }

            @Override
            public void downHardALT(StateKeyboard context) {
                context.changeState(Symbol2);
            }
        },
        Symbol1(R.xml.symbol1) {
            @Override
            public void pushSoftSYM(StateKeyboard context) {
                context.changeState(Symbol2);
            }

            @Override
            public void pushHardSYM(StateKeyboard context) {
                context.changeState(Symbol2);
            }

            @Override
            public void downHardALT(StateKeyboard context) {
                context.changeState(Symbol2);
            }

            @Override
            public State getType() {
                return State.Symbol1;
            }
        },
        Symbol2(R.xml.symbol2) {
            @Override
            public void pushSoftSYM(StateKeyboard context) {
                context.changeState(Qwerty);
            }

            @Override
            public void pushHardSYM(StateKeyboard context) {
                context.changeState(HideQwerty);
            }

            @Override
            public void upHardALT(StateKeyboard context) {
                context.changeState(HideQwerty);
            }

            @Override
            public State getType() {
                return State.Symbol2;
            }
        },
        HideQwerty(R.xml.hide) {
            @Override
            public void pushSoftSYM(StateKeyboard context) {
                context.changeState(Qwerty);
            }

            @Override
            public void pushHardSYM(StateKeyboard context) {
                context.changeState(Symbol1);
            }

            @Override
            public void downHardALT(StateKeyboard context) {
                context.changeState(HideSymbol2);
            }

            @Override
            public State getType() {
                return State.Qwerty;
            }
        },
        HideSymbol2(R.xml.hide) {
            @Override
            public void pushSoftSYM(StateKeyboard context) {
                context.changeState(Qwerty);
            }

            @Override
            public void pushHardSYM(StateKeyboard context) {
                context.changeState(HideQwerty);
            }

            @Override
            public void upHardALT(StateKeyboard context) {
                context.changeState(HideQwerty);
            }

            @Override
            public State getType() {
                return Symbol2;
            }
        };

        int xmlId;

        State(int xmlId) {
            this.xmlId = xmlId;
        }

        void leave(KoimeKeyboardView keyboardView) {
        }

        void enter(KoimeKeyboardView keyboardView) {
            keyboardView.setKeyboard(this.xmlId);
        }

        void pushSoftSYM(StateKeyboard context) {
        }

        void pushHardSYM(StateKeyboard context) {
        }

        void downHardALT(StateKeyboard context) {
        }

        void upHardALT(StateKeyboard context) {
        }

        State getType() {
            return State.Qwerty;
        }
    }

    private State mState;
    private KoimeKeyboardView mKeyboardView;

    private void changeState(State state) {
        mState.leave(mKeyboardView);
        mState = state;
        mState.enter(mKeyboardView);
    }

    StateKeyboard() {
        mState = State.Qwerty;
    }

    void setup(KoimeKeyboardView keyboardView) {
        mKeyboardView = keyboardView;
    }

    void pushSoftSYM() {
        mState.pushSoftSYM(this);
    }

    void pushHardSYM() {
        mState.pushHardSYM(this);
    }

    void downHardALT() {
        mState.downHardALT(this);
    }

    void upHardALT() {
        mState.upHardALT(this);
    }

    boolean isState(State state) {
        return state == mState;
    }

    boolean isType(State state) {
        return state == mState.getType();
    }
}

