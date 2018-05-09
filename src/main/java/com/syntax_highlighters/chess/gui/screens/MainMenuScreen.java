package com.syntax_highlighters.chess.gui.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.syntax_highlighters.chess.gui.LibgdxChessGame;
import com.syntax_highlighters.chess.gui.AbstractScreen;
import com.syntax_highlighters.chess.gui.Audio;
import com.syntax_highlighters.chess.gui.actors.Button;

/**
 * Main menu screen
 */
public class MainMenuScreen extends AbstractScreen {

    private final Image background;

    private final Button playButton;
    private final Button multiplayerButton;
    private final Button scoreButton;

    /**
     * Constructor.
     *
     * Initializes resources used to render this screen.
     * @param game LibGdx game
     */
    public MainMenuScreen(LibgdxChessGame game) {
        super(game);

        AssetManager assetManager = game.getAssetManager();

        Texture tex = assetManager.get("background2.png", Texture.class);
        tex.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        background = new Image(tex);
        background.setSize(800, 800);

        stage.addActor(background);

        playButton = new Button.Builder("Local game", assetManager)
            .size(250, 75)
            .callback(() -> game.setScreen(new SetupScreen(game)))
            .stage(stage)
            .create();
        
        multiplayerButton = new Button.Builder("Multiplayer", assetManager)
            .size(250, 75)
            .callback(() -> game.setScreen(new MultiplayerSetupScreen(game)))
            .stage(stage)
            .create();
        
        scoreButton = new Button.Builder("Leaderboards", assetManager)
            .size(250, 75)
            .callback(() -> game.setScreen(new ScoreScreen(game)))
            .stage(stage)
            .create();
        
        Gdx.input.setInputProcessor(stage);

        Audio.themeMusic(assetManager,true);
    }

    /**
     * Renders the screen
     * @param delta time passed since last frame, in seconds
     */
    @Override
    public void render(float delta) {
        stage.act(delta);
        stage.draw();
    }

    /**
     * Resize event.
     *
     * Used to correctly position the elements on screen and update the viewport size to support the new window size.
     * @param width new window width
     * @param height new window height
     */
    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);

        int min = Math.min(width, height);
        background.setSize(min,  min);
        float x = 0;

        if(width > height)
        {
            x = width / 2.f - min / 2.f;
            background.setPosition(x, 0);
        }
        else
        {
            background.setPosition(0, height / 2.f - min / 2.f);
        }

        playButton.setPosition(x + 80, height/1.75f);
        multiplayerButton.setPosition(x + 80, height/1.75f - 75);
        scoreButton.setPosition(x + 80, height/1.75f - 150);
    }
}