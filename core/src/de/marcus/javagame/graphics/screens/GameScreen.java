package de.marcus.javagame.graphics.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import de.marcus.javagame.datahandling.data.datahandling.SavedataHandler;
import de.marcus.javagame.graphics.ui.UI;
import de.marcus.javagame.io.logging.LoggingSystem;
import de.marcus.javagame.managers.ContactListenerExtern;
import de.marcus.javagame.managers.EntityManager;
import de.marcus.javagame.managers.InputManager;
import de.marcus.javagame.world.GameWorld;

public class GameScreen extends AbstractScreen {


    InputManager inputManager;
    World world;


    GameWorld gameWorld;
    Label label;
    //testen
    Box2DDebugRenderer debugRenderer;
    private final BitmapFont font;
    private final SpriteBatch batch;

    public EntityManager entityManager;
    UI ui;

    public static LoggingSystem loggingSystem = new LoggingSystem();


    boolean yes = true;


    // StoryHandler sthandler;
    //LoadWorld loader;
    //Entities entities;


    public GameScreen(LoadingScreen app, int profile) {
        super(app);
        //app.dispose();

        world = new World(new Vector2(0, 0), true);

        entityManager = SavedataHandler.load(EntityManager.class);
        entityManager.getPlayer().setUI(stage);
        this.ui = entityManager.getPlayer().getUi();
//        Inventory inventory = SavedataHandler.load(Inventory.class);
        debugRenderer = new Box2DDebugRenderer();


//        System.out.println(inventory.toString());
        System.out.println(entityManager.toString());
//        entityManager.getPlayer().getEffects().add(new StatusEffect(EffectType.HEAL,1000));


        inputManager = new InputManager(entityManager.getPlayer(), ui);


        font = new BitmapFont();
        batch = new SpriteBatch();
        batch.setProjectionMatrix(entityManager.getPlayer().getCamera().combined);

        gameWorld = new GameWorld(entityManager.getPlayer().getCamera());


        //TODO: Player body
        //setzt Body in Player
        entityManager.getPlayer().setPlayerBody(world.createBody(entityManager.getPlayer().getPlayerBodyDef()));
        //setzt die fixture
        entityManager.getPlayer().setPlayerFixture(entityManager.getPlayer().getPlayerBody().createFixture(entityManager.getPlayer().getPlayerFixtureDef()));
        entityManager.getPlayer().setSwordBody((world.createBody(entityManager.getPlayer().getSwordBodyDef())));
        entityManager.getPlayer().setSwordFixture(entityManager.getPlayer().getSwordBody().createFixture(entityManager.getPlayer().getSwordFixtureDef()));
        world.setContactListener(new ContactListenerExtern(this));
        gameWorld.setMap(4);

    }

    @Override
    public void update(float delta) {
        //story spawns etc
        inputManager.handleMovement();
        ui.update(entityManager.getPlayer().getPosition().x, entityManager.getPlayer().getPosition().y);
        if (gameWorld.load2) {
            gameWorld.load2 = false;
        }
        world.step(1 / 60f, 6, 2);
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void render(float delta) {
        update(delta);
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        batch.setProjectionMatrix(entityManager.getPlayer().getCamera().combined);
        gameWorld.render(entityManager.getPlayer().getCamera());
        entityManager.render(batch);
        debugRenderer.render(world, entityManager.getPlayer().getCamera().combined);

//        font.draw(batch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 10, 20);


//        label.setText("FPS: " + Gdx.graphics.getFramesPerSecond());

//        label.setPosition(Gdx.graphics.getWidth()-(Gdx.graphics.getWidth()-5f),Gdx.graphics.getHeight()-(0.1f * Gdx.graphics.getHeight()));
//        label.pack();


        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
        world.step(1 / 60f, 6, 2);
    }

    @Override
    public void show() {
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
        Gdx.input.setInputProcessor(new InputMultiplexer(inputManager, stage));
    }

    @Override
    public void dispose() {
        super.dispose();
        SavedataHandler.save(entityManager.getPlayer().getInventory());
        SavedataHandler.save(entityManager);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    public void load(int profile) {

    }
}
