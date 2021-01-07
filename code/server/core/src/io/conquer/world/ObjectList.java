package io.conquer.world;

import io.conquer.world.object.GameObject;

import java.util.ArrayList;
import java.util.Collection;

public class ObjectList extends ArrayList<GameObject> {
    private OnAddListener onAddListener;

    public OnAddListener getOnAddListener() {
        return onAddListener;
    }

    public void setOnAddListener(OnAddListener onAddListener) {
        this.onAddListener = onAddListener;
    }

    public interface OnAddListener{
        void onAdd(GameObject object);
    }

    private OnRemoveListener onRemoveListener;

    public OnRemoveListener getOnRemoveListener() {
        return onRemoveListener;
    }

    public void setOnRemoveListener(OnRemoveListener onRemoveListener) {
        this.onRemoveListener = onRemoveListener;
    }

    public interface OnRemoveListener{
        void onRemove(GameObject object);
    }

    private OnAddAllListener onAddAllListener;

    public OnAddAllListener getOnAddAllListener() {
        return onAddAllListener;
    }

    public void setOnAddAllListener(OnAddAllListener onAddAllListener) {
        this.onAddAllListener = onAddAllListener;
    }

    public interface OnAddAllListener{
        void onAddAll(Collection<? extends GameObject> c);
    }

    private OnRemoveRangeListener onRemoveRangeListener;

    public OnRemoveRangeListener getOnRemoveRangeListener() {
        return onRemoveRangeListener;
    }

    public void setOnRemoveRangeListener(OnRemoveRangeListener onRemoveRangeListener) {
        this.onRemoveRangeListener = onRemoveRangeListener;
    }

    public interface OnRemoveRangeListener{
        void onRemoveRange(int from, int to, Collection<? extends  GameObject> removed);
    }

    private OnRemoveAllListener onRemoveAllListener;

    public OnRemoveAllListener getOnRemoveAllListener() {
        return onRemoveAllListener;
    }

    public void setOnRemoveAllListener(OnRemoveAllListener onRemoveAllListener) {
        this.onRemoveAllListener = onRemoveAllListener;
    }

    public interface OnRemoveAllListener {
        void onRemoveAll(Collection<?> c);
    }

    @Override
    public boolean add(GameObject gameObject) {
        boolean b = super.add(gameObject);
        if (onAddListener != null) onAddListener.onAdd(gameObject);
        return b;
    }

    @Override
    public void add(int index, GameObject element) {
        super.add(index, element);
        if (onAddListener != null) onAddListener.onAdd(element);
    }

    @Override
    public GameObject remove(int index) {
        GameObject remove = super.remove(index);
        if (onRemoveListener != null) onRemoveListener.onRemove(remove);
        return remove;
    }

    @Override
    public boolean remove(Object o) {
        boolean b = super.remove(o);
        if (onRemoveListener != null) onRemoveListener.onRemove((GameObject) o);
        return b;
    }

    @Override
    public boolean addAll(Collection<? extends GameObject> c) {
        boolean b = super.addAll(c);
        if (onAddAllListener != null) onAddAllListener.onAddAll(c);
        return b;
    }

    @Override
    public boolean addAll(int index, Collection<? extends GameObject> c) {
        boolean b = super.addAll(index, c);
        if (onAddAllListener != null) onAddAllListener.onAddAll(c);
        return b;
    }

    @Override
    protected void removeRange(int fromIndex, int toIndex) {
        ArrayList<GameObject> removed = new ArrayList<>(this.subList(fromIndex,toIndex));
        super.removeRange(fromIndex, toIndex);
        if (onRemoveRangeListener != null) onRemoveRangeListener.onRemoveRange(fromIndex, toIndex, removed);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean b = super.removeAll(c);
        if (onRemoveAllListener != null) onRemoveAllListener.onRemoveAll(c);
        return b;
    }
}
