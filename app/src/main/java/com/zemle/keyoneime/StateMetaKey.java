package com.zemle.keyoneime;

/**
 * メタキーの状態遷移
 *
 * Created by shntn on 2018/01/10.
 */

class StateMetaKey {
    private boolean CONFIG_SIMULTANEOUS = true; // 同時押しではなくても有効にする
    private boolean CONFIG_LOCKED = true;  // 2回押すとON状態でロック

    private State state = State.OFF;

    private void changeState(State next) {
        state.exit(this);
        state = next;
        state.enter(this);
    }

    State getCurrent() {
        return state;
    }

    void press() {
        state.press(this);
    }

    void release() {
        state.release(this);
    }

    void use() {
        state.use(this);
    }

    boolean isPress() {
        return (state != State.OFF);
    }

    private enum State {
        OFF {
            void press(StateMetaKey outer) {
                outer.changeState(ON);
            }
        },
        ON {
            void enter(StateMetaKey outer) {
                this.used = false;
                this.pressed = true;
            }

            void exit(StateMetaKey outer) {
                this.used = false;
                this.pressed = false;
            }

            void press(StateMetaKey outer) {
                if (outer.CONFIG_LOCKED) {
                    outer.changeState(LOCKED);
                } else {
                    outer.changeState(OFF);
                }
            }

            void use(StateMetaKey outer) {
                this.used = true;
                if (!this.pressed) {
                    outer.changeState(OFF);
                }
            }

            void release(StateMetaKey outer) {
                this.pressed = false;
                if ((this.used) || (!outer.CONFIG_SIMULTANEOUS)) {
                    outer.changeState(OFF);
                }
            }
        },
        LOCKED {
            void press(StateMetaKey outer) {
                outer.changeState(OFF);
            }
        };

        boolean pressed;
        boolean used;

        void enter(StateMetaKey outer) {}
        void exit(StateMetaKey outer) {}
        void press(StateMetaKey outer) {}
        void release(StateMetaKey outer) {}
        void use(StateMetaKey outer) {}
    }
}
