package io.conquer.world;

import com.badlogic.gdx.utils.Array;

import io.conquer.engine.Engine;
import io.conquer.world.object.GameObject;

public class World {

    private final ObjectList objects = new ObjectList();

    public ObjectList getObjects() {
        return objects;
    }

    public void update(float delta){
        for (GameObject object : objects) {
            object.update(delta);
        }
    }
}
