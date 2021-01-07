package io.conquer.world.object.unit;

import io.conquer.world.object.GameObject;

public abstract class Vehicles extends Unit{
    public enum MoveState implements State{
        TURN_LEFT,
        TURN_RIGHT,
        FORWARD,
        BACK,
        FORWARD_LEFT,
        FORWARD_RIGHT,
        BACK_LEFT,
        BACK_RIGHT
    }
}
