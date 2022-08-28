package de.marcus.javagame.screens;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import de.marcus.javagame.managers.GameScreenManager;
import de.marcus.javagame.managers.TextureManager;

public class StartMenuScreen extends AbstractScreen{
    Image backgroundImage; //background
    ImageButton.ImageButtonStyle startStyle;
    ImageButton.ImageButtonStyle menuStyle;
    ImageButton.ImageButtonStyle quitStyle;
    ImageButton.ImageButtonStyle archievementStyle;
    ImageButton archievment;
    ImageButton start;
    ImageButton settings; //setting Screen
    ImageButton quit;
    VerticalGroup verticalGroup;
    Table table;
    public StartMenuScreen(LoadingScreen app) {

        super(app);
        verticalGroup = new VerticalGroup();
        app.dispose();

        //style für Startbutton
         startStyle = new ImageButton.ImageButtonStyle();
        startStyle.imageUp      = new TextureRegionDrawable(TextureManager.getTexture("play"));
        startStyle.imageDown    = new TextureRegionDrawable(TextureManager.getTexture("play_ausgewaehlt"));
        startStyle.imageOver = new TextureRegionDrawable(TextureManager.getTexture("play_ausgewaehlt"));
        //startStyle.imageChecked = new TextureRegionDrawable(TextureManager.getTexture("start_menu_startButton_normal"));
        //style für Menübutton
        menuStyle = new ImageButton.ImageButtonStyle();
        startStyle.imageUp      = new TextureRegionDrawable(TextureManager.getTexture("settings"));
        startStyle.imageDown    = new TextureRegionDrawable(TextureManager.getTexture("settings_ausgewaehlt"));
        startStyle.imageOver   = new TextureRegionDrawable(TextureManager.getTexture("settings_ausgewaehlt"));

       //style für quitButton
         quitStyle = new ImageButton.ImageButtonStyle();

        startStyle.imageUp      = new TextureRegionDrawable(TextureManager.getTexture("quit"));
        startStyle.imageDown    = new TextureRegionDrawable(TextureManager.getTexture("quit_ausgewaehlt"));
        startStyle.imageOver    = new TextureRegionDrawable(TextureManager.getTexture("quit_ausgewaehlt"));
       //style für archievments
        archievementStyle = new ImageButton.ImageButtonStyle();
        archievementStyle.imageUp = new TextureRegionDrawable(TextureManager.getTexture("achievement"));
        archievementStyle.imageDown = new TextureRegionDrawable(TextureManager.getTexture("achievments_ausgewaehlt"));
        archievementStyle.imageOver = new TextureRegionDrawable(TextureManager.getTexture("achievments_ausgewaehlt"));
        //erstellen der Buttons
        start = new ImageButton(startStyle);
        settings = new ImageButton(menuStyle);
        quit = new ImageButton(quitStyle);
        archievment = new ImageButton(archievementStyle);
        //listener für startButton
        start.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.gl.glClearColor( 0, 0, 0, 1 );
                Gdx.gl.glClear( GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT );
                dispose();
               app.g.setScreen(GameScreenManager.SCREENS.SELECT_PROFILE);
            };
        });
        //listner für quit Button
        quit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dispose();
                app.dispose();
            }
        });
        //listner für settings Button
        settings.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.gl.glClearColor( 0, 0, 0, 1 );
                Gdx.gl.glClear( GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT );
                dispose();
                app.g.setScreen(GameScreenManager.SCREENS.SETTINGS);
            };
        });
        //listner für archievements
        archievment.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.gl.glClearColor( 0, 0, 0, 1 );
                Gdx.gl.glClear( GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT );
                dispose();
                app.g.setScreen(GameScreenManager.SCREENS.SETTINGS);                                       //TODO: Archievment screen
            };
        });
       //Bild für linken Rand
        backgroundImage = new Image(TextureManager.getTexture("background"));
        //vertical Group wo die Buttons reinkommen
       // verticalGroup.setWidth(550f);
        verticalGroup.setDebug(true);
        verticalGroup.addActor(start);
        verticalGroup.addActor(archievment);
        verticalGroup.addActor(settings);
        verticalGroup.addActor(quit);
        verticalGroup.pad(120.0f);


        Gdx.input.setInputProcessor(stage);
        //table in der linkes Bild und verticalGroup sind
        table = new Table();
        stage.addActor(table);
        table.setFillParent(true);
        table.setDebug(true);
        table.add(backgroundImage);
        table.add(verticalGroup);
        table.setFillParent(true);

    }

    @Override
    public void update(float delta) {
      //code der geupdatet wird
    }

    @Override
    public void show() {
       //statisch

        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
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
}
