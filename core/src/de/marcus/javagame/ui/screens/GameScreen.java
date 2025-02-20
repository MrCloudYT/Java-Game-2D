package de.marcus.javagame.ui.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Array;
import de.marcus.javagame.framework.collisions.BodyData;
import de.marcus.javagame.framework.data.SavedataHandler;
import de.marcus.javagame.entities.base.Item;
import de.marcus.javagame.entities.base.NPC;
import de.marcus.javagame.ui.ui.UI;
import de.marcus.javagame.framework.logging.LoggingSystem;
import de.marcus.javagame.framework.collisions.ContactListenerExtern;
import de.marcus.javagame.entities.EntityManager;
import de.marcus.javagame.player.InputManager;
import de.marcus.javagame.world.GameWorld;
import lombok.Getter;

import java.util.*;

@Getter
public class GameScreen extends AbstractScreen {


    InputManager inputManager;


   public GameWorld gameWorld;
    Label label;
    //testen
    Box2DDebugRenderer debugRenderer;
    private final BitmapFont font;
    private final SpriteBatch batch;
    boolean timer = false;
    public EntityManager entityManager;
    UI ui;

    public static LoggingSystem loggingSystem = new LoggingSystem();
    LinkedHashMap<Body, NPC> npcs;
    boolean yes = true;
    LinkedHashMap<Body, Item> items;
    ArrayList<Vector2> itemKoord = new ArrayList<Vector2>(); //TODO: koord setzen
    ArrayList<String> itemNames = new ArrayList<>(); //TODO: namen setzen
    ArrayList<Item> itemsList = new ArrayList<>();
    ArrayList<String> currentItemName = new ArrayList<>();

    ArrayList<Item> currentItem = new ArrayList<>();
    float timeSeconds = 0f;
    float period = 120f;
    // StoryHandler sthandler;
    //LoadWorld loader;
    //Entities entities;


    public GameScreen(LoadingScreen app, int profile) {
        super(app);
        //app.dispose();
        currentItemName.add(0, "keins");
        currentItem.add(0,new Item(new Vector2(-100,-100)));
        items = new LinkedHashMap<>();
         //TODO: items adden
        itemKoord.add(new Vector2(120f, 90f));
        itemNames.add("test");
        npcs = new LinkedHashMap<>();
        new Thread(() -> {
            Scanner scanner = new Scanner(System.in);
            try {
                while (true) {
                    System.out.println("Please input a line");
                    String line = scanner.nextLine();
                    String[] split = line.split(",");
                    float x = Float.parseFloat(split[0]);
                    float y = Float.parseFloat(split[1]);

                    System.out.println("input is " + x + " " + y);
                    entityManager.getPlayer().tp(x,y);
                }
            } catch(IllegalStateException | NoSuchElementException e) {
                System.out.println("System.in was closed; exiting");
            }

        }).start();


        entityManager = SavedataHandler.load(EntityManager.class);
        entityManager.getPlayer().setUI(stage);
        this.ui = entityManager.getPlayer().getUi();
//        Inventory inventory = SavedataHandler.load(Inventory.class);
        debugRenderer = new Box2DDebugRenderer();


//        System.out.println(inventory.toString());
        System.out.println(entityManager.toString());
//        entityManager.getPlayer().getEffects().add(new StatusEffect(EffectType.HEAL,1000));


        inputManager = new InputManager(entityManager.getPlayer(), ui, this);


        font = new BitmapFont();
        batch = new SpriteBatch();
        batch.setProjectionMatrix(entityManager.getPlayer().getCamera().combined);

        gameWorld = new GameWorld(entityManager.getPlayer().getCamera());


        //TODO: Player body
        entityManager.getPlayer().createCollisionPlayer();
        //setzt Body in Player
        entityManager.getPlayer().setPlayerBody(gameWorld.getWorld().createBody(entityManager.getPlayer().getPlayerBodyDef()));
        //setzt die fixture
        entityManager.getPlayer().setPlayerFixture(entityManager.getPlayer().getPlayerBody().createFixture(entityManager.getPlayer().getPlayerFixtureDef()));
        Body body = gameWorld.getWorld().createBody(entityManager.getPlayer().getSwordBodyDef());
        body.setUserData(new BodyData(false,false, BodyData.CollisionType.PLAYER));
        entityManager.getPlayer().setSwordBody(body);
        entityManager.getPlayer().setSwordFixture(entityManager.getPlayer().getSwordBody().createFixture(entityManager.getPlayer().getSwordFixtureDef()));
        gameWorld.getWorld().setContactListener(new ContactListenerExtern(this));
       // gameWorld.setMap(1,entityManager.getPlayer());

        entityManager.getPlayer().tp(123f,90.5f);
        entityManager.generateNPCs(Arrays.asList(new Vector2(211.59f,225.96f),new Vector2(186.43f,121.92f),
                new Vector2(216.94f,123.55f)
                ,new Vector2(123.48f,92.75f),new Vector2(68.38f,155.39f),new Vector2(140f,142f)),ui,gameWorld.getWorld());


       createItems();
    }

    public void createItems(){
        for(Vector2 v : itemKoord){
            Item item = new Item(v);
            item.createCollisionItem();
            item.body = gameWorld.getWorld().createBody(item.itemBodyDef);

            //setzt die fixture
            item.itemFixture = item.body.createFixture(item.itemFixtureDef);
            itemsList.add(item);
            items.put(item.body, item);
        }
    }
    //TODO in dialog callen
    public void startSearch(){
        if(itemsList.size() > 0){
            currentItem.set(0,itemsList.get(itemsList.size()-1));
            currentItemName.set(0,itemNames.get(itemsList.size()-1));
            //TODO: Benachrichtigung mit current Item name
            itemsList.remove(itemsList.size()-1);
            itemNames.remove(itemsList.size()-1);

            startTimer();
        }else if( itemsList.size() ==0){
                currentItem.set(0,itemsList.get(0));
                currentItemName.set(0,itemNames.get(0));
            //TODO: Benachrichtigung mit current Item name
                itemsList.remove(0);
                itemNames.remove(0);
            startTimer();

        }else{
            //TODO: Fenster öffnen alle gefunden
        }
        //setze mission in current
    }
    public void startTimer(){
        timer = true;
        //Dialogfenster mit timer wenn 0 tod
    }
    public void stopTimer(){
        timer = false;
        period = 120f;
    }
public void itemFound(){
        //TODO: lana
    if(currentItem.size() > 0) {

        currentItem.set(0, new Item(new Vector2(-100,-100)));
        currentItemName.set(0,"keins");
        stopTimer();
        startSearch();
    }
}

    @Override
    public void update(float delta) {
        //story spawns etc
        inputManager.handleMovement();
        ui.update(entityManager.getPlayer().getPosition().x, entityManager.getPlayer().getPosition().y);

        gameWorld.getWorld().step(1 / 60f, 6, 2);
       // cleanupBodys();
    }
    public void kill(){
        //TODO: kill player
    }

    private void cleanupBodys() {
        Array<Body> bodies = new Array<>();
        gameWorld.getWorld().getBodies(bodies);
        for (Body body : bodies) {
            BodyData userData = (BodyData) body.getUserData();
            System.out.println(userData.isCanBeDestroyed());
        }

    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void render(float delta) {
        update(delta);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

       if(timer){
           timeSeconds +=Gdx.graphics.getRawDeltaTime();
           if(timeSeconds > period){
               timeSeconds-=period;
               kill();
           }
       }

        batch.setProjectionMatrix(entityManager.getPlayer().getCamera().combined);
        gameWorld.render(entityManager.getPlayer().getCamera());
        entityManager.render(batch);
        gameWorld.getWorld().setContactListener(new ContactListenerExtern(this));

//        font.draw(batch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 10, 20);


//        label.setText("FPS: " + Gdx.graphics.getFramesPerSecond());

//        label.setPosition(Gdx.graphics.getWidth()-(Gdx.graphics.getWidth()-5f),Gdx.graphics.getHeight()-(0.1f * Gdx.graphics.getHeight()));
//        label.pack();


        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();

    }

    @Override
    public void show() {
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
        Gdx.input.setCursorCatched(true);
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