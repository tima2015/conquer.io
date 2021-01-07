package io.conquer.engine;

import com.badlogic.gdx.utils.Disposable;
import io.conquer.world.World;
import io.conquer.world.object.GameObject;

public interface Engine extends Disposable {//TODO удали это к чёрту!

    void update(float delta);

    void updateWorld(float delta, World world);

    void updateWorldObject(float delta, GameObject object);
}
