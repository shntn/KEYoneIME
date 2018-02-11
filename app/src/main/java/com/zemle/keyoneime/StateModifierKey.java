package com.zemle.keyoneime;

/**
 * メタキーの状態遷移
 *
 * Created by shntn on 2018/01/10.
 */

class StateModifierKey {
    private boolean CONFIG_SIMULTANEOUS = true; // 同時押しではなくても有効にする
    private boolean CONFIG_LOCKED = true;  // 2回押すとON状態でロック

    private State state = State.OFF;

    private void changeState(State next) {
        state.exit(this);
        state = next;
        state.enter(this);
    }

    boolean press() {
        state.press(this);
        return state != State.OFF;
    }

    boolean release() {
        state.release(this);
        return state != State.OFF;
    }

    void clear() {
        state = State.OFF;
    }

    void use() {
        state.use(this);
    }

    boolean isPress() {
        return (state != State.OFF);
    }
    boolean isOn() {
        return state == State.ON;
    }
    boolean isLocked() {
        return state == State.LOCKED;
    }


    private enum State {
        OFF {
            void press(StateModifierKey outer) {
                outer.changeState(ON);
            }
        },
        ON {
            void enter(StateModifierKey outer) {
                this.used = false;
                this.pressed = true;
            }

            void exit(StateModifierKey outer) {
                this.used = false;
                this.pressed = false;
            }

            void press(StateModifierKey outer) {
                State next = OFF;

                if (outer.CONFIG_LOCKED) {
                    next = LOCKED;
                }
                outer.changeState(next);
            }

            void use(StateModifierKey outer) {
                this.used = true;
                if (!this.pressed) {
                    outer.changeState(OFF);
                }
            }

            void release(StateModifierKey outer) {
                this.pressed = false;
                if ((this.used) || (!outer.CONFIG_SIMULTANEOUS)) {
                    outer.changeState(OFF);
                }
            }
        },
        LOCKED {
            void press(StateModifierKey outer) {
                outer.changeState(OFF);
            }
        };

        boolean pressed;
        boolean used;

        void enter(StateModifierKey outer) {}
        void exit(StateModifierKey outer) {}
        void press(StateModifierKey outer) {}
        void release(StateModifierKey outer) {}
        void use(StateModifierKey outer) {}
    }
}
