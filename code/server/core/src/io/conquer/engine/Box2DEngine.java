package io.conquer.engine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import io.conquer.world.ObjectList;
import io.conquer.world.World;
import io.conquer.world.object.GameObject;
import io.conquer.world.object.unit.Vehicles;

import java.util.*;

public class Box2DEngine implements Engine{

    private com.badlogic.gdx.physics.box2d.World b2dWorld = new com.badlogic.gdx.physics.box2d.World(new Vector2(0,0), false);
    private Array<Box2DGameObject> gameObjects = new Array<>();
    private float timer = 0;
    public static final float UPDATE_TIME = 5;

    public void initalize(World world){
        checkListeners(world);
        checkObjects(world);
        timer = 0;
    }

    @Override
    public void update(float delta) {
        b2dWorld.step(1/delta, 1,1);
        for (Box2DGameObject object : gameObjects) {
            object.update(delta);
        }
    }

    /**
     * Проверяет, все ли слушатели движка установлены в лист объектов мира
     * @param world Мир с которым происходит взаимодействие
     */
    private void checkListeners(World world){
        ObjectList list = world.getObjects();
        if (list.getOnAddListener() == null)
            list.setOnAddListener(new OnAddListener());
        if (list.getOnRemoveListener() == null)
            list.setOnRemoveListener(new OnRemoveListener());
        if (list.getOnAddAllListener() == null)
            list.setOnAddAllListener(new OnAddAllListener());
        if (list.getOnRemoveRangeListener() == null)
            list.setOnRemoveRangeListener(new OnRemoveRangeListener());
        if (list.getOnRemoveAllListener() == null)
            list.setOnRemoveAllListener(new OnRemoveAllListener());
    }

    private void checkObjects(World world){
        for (int i = 0; i < world.getObjects().size(); i++) {
            boolean inEngine = false;
            for (int j = 0; j < gameObjects.size; j++) {
                if (gameObjects.get(j).equals(world.getObjects().get(i))){
                    inEngine = true;
                    break;
                }
            }
            if (!inEngine)
                gameObjects.add(new Box2DGameObject(world.getObjects().get(i)));
        }
    }

    @Override
    public void updateWorld(float delta, World world) {
        world.update(delta);
        timer += delta;
        if (timer >= UPDATE_TIME) initalize(world);
    }


    @Override
    public void updateWorldObject(float delta, GameObject object) {
        object.update(delta);
    }

    public com.badlogic.gdx.physics.box2d.World getB2dWorld() {
        return b2dWorld;
    }

    @Override
    public String toString() {
        return "Box2DEngine{" +
                "\n   gameObjects=" + gameObjects +
                ",\n   timer=" + timer +
                "\n}\n";
    }

    @Override
    public void dispose() {
        b2dWorld.dispose();
    }

    private class Box2DGameObject{
        private GameObject object;
        private Body body;
        private Box2DGameObject[] parts;

        public Box2DGameObject(GameObject object) {
            this.object = object;
            GameObject[] parts = object.getParts();
            this.parts = new Box2DGameObject[parts.length];
            for (int i = 0; i < parts.length; i++) {
                this.parts[i] = new Box2DGameObject(parts[i]);
            }

            BodyDef bodyDef = new BodyDef();
            bodyDef.angle = object.getAngle();
            bodyDef.position.set(object.getPos().x, object.getPos().y);
            bodyDef.type = BodyDef.BodyType.DynamicBody;
            body = b2dWorld.createBody(bodyDef);

            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.shape = buildShape(object);
            body.createFixture(fixtureDef);
            fixtureDef.shape.dispose();
            for (int i = 0; i < this.parts.length; i++) {
                JointDef jointDef = new JointDef();
                jointDef.bodyA = body;
                jointDef.bodyB = this.parts[i].body;
                jointDef.type = JointDef.JointType.WeldJoint;
            }
        }

        private Shape buildShape(GameObject object){
            if (object.getHitBox() instanceof Circle){
                CircleShape circleShape = new CircleShape();
                circleShape.setRadius(((Circle) object.getHitBox()).radius);
                return circleShape;
            } else if (object.getHitBox() instanceof Polygon){
                PolygonShape polygonShape = new PolygonShape();
                polygonShape.setAsBox(object.getSize().x, object.getSize().y);
                return polygonShape;
            }
            return null;
        }

        public void update(float delta){
            GameObject.State state = object.getMoveState();
            if (state.equals(Vehicles.MoveState.TURN_RIGHT)){
                body.setAngularVelocity(-object.getRotationSpeed()*MathUtils.degreesToRadians);
                object.setAngle(body.getAngle()*MathUtils.radiansToDegrees);
            } else if (state.equals(Vehicles.MoveState.TURN_LEFT)){
                body.setAngularVelocity(object.getRotationSpeed()*MathUtils.degreesToRadians);
                object.setAngle(body.getAngle()*MathUtils.radiansToDegrees);
            }
        }

        public boolean equals(GameObject object){
            return object.equals(this.object);
        }

        public void destroy(){
            b2dWorld.destroyBody(body);
            for (int i = 0; i < parts.length; i++) {
                parts[i].destroy();
            }
        }

        @Override
        public String toString() {
            return "Box2DGameObject{" +
                    "\n   object=" + object +
                    ",\n   body=" + body +
                    ",\n   parts=" + Arrays.toString(parts) +
                    "\n}\n";
        }
    }

    private class OnAddListener implements ObjectList.OnAddListener{
        @Override
        public void onAdd(GameObject object) {
            gameObjects.add(new Box2DGameObject(object));
        }
    }

    private class OnRemoveListener implements ObjectList.OnRemoveListener{
        @Override
        public void onRemove(GameObject object) {
            Array.ArrayIterator<Box2DGameObject> iterator = gameObjects.iterator();
            while (iterator.hasNext()){
                Box2DGameObject next = iterator.next();
                if (next.equals(object)){
                    iterator.remove();
                    next.destroy();
                    return;
                }
            }
        }
    }

    private class OnAddAllListener implements ObjectList.OnAddAllListener{
        @Override
        public void onAddAll(Collection<? extends GameObject> c) {
            for (GameObject gameObject : c) {
                gameObjects.add(new Box2DGameObject(gameObject));
            }
        }
    }

    private class OnRemoveRangeListener implements ObjectList.OnRemoveRangeListener{
        @Override
        public void onRemoveRange(int from, int to, Collection<? extends GameObject> removed) {
            Array.ArrayIterator<Box2DGameObject> iterator = gameObjects.iterator();
            while (iterator.hasNext()){
                Box2DGameObject next = iterator.next();
                Iterator<? extends GameObject> ri = removed.iterator();
                while (ri.hasNext()){
                    GameObject object = ri.next();
                    if (next.equals(object)){
                        ri.remove();
                        next.destroy();
                        iterator.remove();
                        break;
                    }
                }
            }
        }
    }

    private class OnRemoveAllListener implements ObjectList.OnRemoveAllListener{
        @Override
        public void onRemoveAll(Collection<?> c) {
            for (int i = 0; i < gameObjects.size; i++) {
                gameObjects.get(i).destroy();
            }
            gameObjects.clear();
        }
    }
}
