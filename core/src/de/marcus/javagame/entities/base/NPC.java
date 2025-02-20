package de.marcus.javagame.entities.base;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.*;
import de.marcus.javagame.ui.ui.UI;
import de.marcus.javagame.framework.dialog.DialogHandler;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class NPC extends Creature{
    public Fixture npcFixture;
    public BodyDef npcBodyDef;
    public FixtureDef npcFixtureDef;
    public Body body;
    DialogHandler.Dialogs dialog;
    List<Body> bodies;
    float x;
    float y;
    private UI ui;
    World world;
    public NPC(float x, float y,DialogHandler.Dialogs dialog, Texture t, UI ui, World world) {
        super(x,  y, t,  1,  1,  0,  0, 0f , null);
        this.x = x;
        this.y = y;
        this.ui = ui;
        this.dialog = dialog;
        this.bodies = new ArrayList<>();

        this.world = world;
        createCollisionNpc();
    }

    public void callDialog() {
        System.out.println("calling dialog");
        ui.getDialogWindow().getDialogHandler().setCurrentDialog(dialog);
    }
    public void createCollisionNpc() {
        npcBodyDef = new BodyDef();

        npcFixtureDef = new FixtureDef();
        npcBodyDef.type = BodyDef.BodyType.StaticBody;
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(0.5f, 1);



        //TODO: position anpassen
        npcBodyDef.position.set(x + 1.3f, y + 1f);

        npcFixtureDef.shape = shape;
        npcFixtureDef.density = 0f;
        npcFixtureDef.friction = 0.0f;
        body = world.createBody(npcBodyDef);
        body.createFixture(shape,100f);

    }
}
