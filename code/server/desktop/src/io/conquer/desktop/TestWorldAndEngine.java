package io.conquer.desktop;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.conquer.Core;
import io.conquer.engine.Box2DEngine;
import io.conquer.world.World;
import io.conquer.world.object.unit.TestUnit;

import java.util.ArrayList;

public class TestWorldAndEngine extends ApplicationAdapter {
    public static void main(String[] args) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = config.height = 768;
        new LwjglApplication(new TestWorldAndEngine(), config);
    }

    private Box2DEngine engine;
    private World world;
    private Box2DDebugRenderer renderer;
    private Viewport viewport = new FitViewport(768,768);
    private BitmapFont font;
    private SpriteBatch batch;

    @Override
    public void create() {
        engine = new Box2DEngine();
        world = new World();
        engine.initalize(world);
        renderer = new Box2DDebugRenderer();
        font = new BitmapFont();
        font.setColor(Color.LIME);
        batch = new SpriteBatch();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, false);
    }

    @Override
    public void render() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE))
            Gdx.app.exit();
        if (Gdx.input.isKeyJustPressed(Input.Keys.L))
            world.getObjects().add(new TestUnit());
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        engine.update(Gdx.graphics.getDeltaTime());
        engine.updateWorld(Gdx.graphics.getDeltaTime(), world);
        renderer.render(engine.getB2dWorld(), viewport.getCamera().combined);
        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();
        font.draw(batch, engine.toString() + "\nUpdate time" + Box2DEngine.UPDATE_TIME, 8 + viewport.getCamera().position.x - viewport.getWorldWidth()/2f,viewport.getCamera().position.y + viewport.getWorldHeight()/2f);
        batch.end();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        font.dispose();
        batch.dispose();
        engine.dispose();
    }
}
