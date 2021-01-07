package io.conquer.world.object;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import java.util.Arrays;

public abstract class GameObject {


    private Vector2 speed = new Vector2(1,1);
    /**
     *  Позиция
     */
    private Vector3 pos = new Vector3(1,1,1);

    /**
     * Размер
     */
    private Vector3 size = new Vector3(1,1,1);

    /**
     * Максимальное здоровье
     */
    private int maxHeal = 0;
    /**
     * Здоровье
     */
    private int heal = 0;

    private float rotationSpeed = 0;

    /**
     * Угол поворота объекта
     */
    private float angle = 0;

    /**
     * Части объекта, так же являются объектами.
     * В основном используются для орудий
     */
    private GameObject[] parts = new GameObject[0];

    /**
     * Действие выполняемое объектом при команде игрока или автоматически
     */
    protected abstract void action(float delta);

    /**
     * Состояние автомата
     */
    public interface State{

        /**
         * @param object Объект в котором выполнялась смена состояния
         * @param end заменяемое состояние
         * @param start заменяющее состояние
         * @return заменяющее состояние
         */
        static State switchAction(GameObject object, State end, State start){
            end.onEnd(object);
            start.onStart(object);
            return start;
        }

        /**
         * Выполняется при переходе в состояние, сразу после onEnd() предыдущего состояния
         * @param object объект состояния
         */
        default void onStart(GameObject object){
            Gdx.app.debug(getClass().getSimpleName(), "unimplemented onStart() called");
        }

        /**
         * Выполняется при объновлении состояния
         * @param object объект состояния
         */
        default void update(GameObject object){
            Gdx.app.debug(getClass().getSimpleName(), "unimplemented update() called");
        }
        /**
         * Выполняется при выходе из состояния, до onStart() следующего состояния
         * @param object объект состояния
         */
        default void onEnd(GameObject object){
            Gdx.app.debug(getClass().getSimpleName(), "unimplemented onEnd() called");
        }
    }

    /**
     * Нейтральное состояние, ничего не происходит
     */
    public static final State STAY = new State() {};

    /**
     * Состояние передвижение
     */
    private State moveState = STAY;

    public void onSpeedChanged(float oldX, float oldY, Vector2 current){}
    public void onPosChanged(float oldX, float oldY, float oldZ, Vector3 current){}
    public void onSizeChanged(float oldW, float oldL, float oldH, Vector3 current){}
    public void onMaxHealChanged(int old, int current){}
    public void onHealChanged(int old, int current){}
    public void onRotationSpeedChanged(float old, float current){}
    public void onAngleChanged(float old, float current){}
    public void onPartsChanged(){}
    public void onMoveStateChanged(State old, State current){}

    public Vector2 getSpeed() {
        return speed;
    }

    public void setSpeed(Vector2 speed) {
        onSpeedChanged(this.speed.x, this.speed.y, speed.set(speed));
    }

    public void setSpeed(float x, float y){
        onSpeedChanged(speed.x, speed.y, speed.set(x,y));
    }

    public Vector3 getPos() {
        return pos;
    }

    public void setPos(Vector3 pos) {
        Vector3 old = this.pos;
        this.pos = pos;
        onPosChanged(old.x, old.y, old.z, pos);
    }

    public void setPos(float x, float y, float z){
        onPosChanged(pos.x, pos.y, pos.z, pos.set(x,y,z));
    }

    public void setPos(float x, float y){
        setPos(x,y,pos.z);
    }

    public Vector3 getSize() {
        return size;
    }

    public void setSize(Vector3 size) {
        Vector3 old = this.size;
        this.size = size;
        onSizeChanged(old.x, old.y, old.z, size);
    }

    public void setSize(float width, float lenght, float height){
        onSizeChanged(size.x, size.y, size.z, size.set(width,lenght,height));
    }

    public void setSize(float width, float lenght){
        setSize(width,lenght, size.z);
    }

    public int getMaxHeal() {
        return maxHeal;
    }

    public void setMaxHeal(int maxHeal) {
        int old = this.maxHeal;
        this.maxHeal = maxHeal;
        onMaxHealChanged(old, maxHeal);
    }

    public int getHeal() {
        return heal;
    }

    public void setHeal(int heal) {
        int old = this.heal;
        this.heal = heal;
        onHealChanged(old, heal);
    }

    public float getRotationSpeed() {
        return rotationSpeed;
    }

    public void setRotationSpeed(float rotationSpeed) {
        float old = this.rotationSpeed;
        this.rotationSpeed = rotationSpeed;
        onRotationSpeedChanged(old, rotationSpeed);
    }

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        angle %= 360;

        float old = this.angle;
        this.angle = angle;
        onAngleChanged(old, angle);
    }

    public GameObject[] getParts() {
        return parts;
    }

    public void setParts(GameObject[] parts) {
        this.parts = parts;
        onPartsChanged();
    }

    public State getMoveState() {
        return moveState;
    }

    public void setMoveState(State moveState) {
        State old = this.moveState;
        this.moveState = moveState;
        onMoveStateChanged(old, moveState);
    }

    public abstract void update(float delta);

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
                "\n   pos=" + pos +
                ",\n   size=" + size +
                ",\n   maxHeal=" + maxHeal +
                ",\n   heal=" + heal +
                ",\n   angle=" + angle +
                ",\n   parts=" + Arrays.toString(parts) +
                ",\n   moveState=" + moveState +
                "\n}\n";
    }

    /**
     * @return Фигуру хитбокса, было бы разумно использовать одну статическую, вместо выделения памяти на каждую отдельную
     */
    public abstract Shape2D getHitBox();
}
