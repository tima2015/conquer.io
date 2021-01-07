package io.conquer.world.object.unit;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.math.Vector3;

public class TestUnit extends Vehicles{

    private static final String TAG = "TestUnit";

    private final static Circle HIT_BOX = new Circle(0,0,50);

    public TestUnit(){
        setHeal(50);
        setMaxHeal(100);
        setPos(20,60,1);
        setRotationSpeed(0.001f);
        setSize(100,100,1);
        setMoveState(MoveState.TURN_RIGHT);
    }

    @Override
    protected void action(float delta) {

    }

    @Override
    public void update(float delta) {

    }

    @Override
    public Shape2D getHitBox() {
        return HIT_BOX;
    }

    @Override
    public void onAngleChanged(float old, float current) {
        Gdx.app.debug(TAG, "onAngleChanged() called with: old = [" + old + "], current = [" + current + "]");
    }
}
