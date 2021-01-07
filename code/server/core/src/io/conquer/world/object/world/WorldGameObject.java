package io.conquer.world.object.world;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import io.conquer.world.object.GameObject;

public abstract class WorldGameObject extends GameObject {

    public void setPos(Vector2 pos){
        super.setPos(new Vector3(pos, 1));
    }
}
