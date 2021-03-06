package com.syntax_highlighters.chess.gui.screens;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.syntax_highlighters.chess.account.Account;
import com.syntax_highlighters.chess.gui.AssetLoader;
import com.syntax_highlighters.chess.gui.LibgdxChessGame;
import com.syntax_highlighters.chess.gui.WobbleDrawable;
import com.syntax_highlighters.chess.gui.actors.Button;
import com.syntax_highlighters.chess.gui.actors.Text;

/**
 * Screen displaying the scores of the top ten players accounts in the accounts
 * data.
 */
class ScoreScreen extends AbstractScreen {
    private final Text title;

    private final Table scoreList;
    private final Button mainMenuButton;

    public ScoreScreen(LibgdxChessGame game) {
        super(game);

        BitmapFont font = AssetLoader.GetDefaultFont(game.getAssetManager());

        title = new Text(AssetLoader.GetDefaultFont(game.getAssetManager(), true));
        title.setText("Leaderboard:");
        title.setCenter(WORLDWIDTH / 2.f, WORLDHEIGHT / 2.f + 400.f - 50.f);
        title.setColor(0,0,0,1);

        stage.addActor(title);

        Text rank = new Text(font);
        rank.setText("Rank");
        rank.setColor(0,0,0,1);
        Text name = new Text(font);
        name.setText("Name");
        name.setColor(0,0,0,1);
        Text score = new Text(font);
        score.setText("Score");
        score.setColor(0,0,0,1);
        Text wins = new Text(font);
        wins.setText("Wins");
        wins.setColor(0,0,0,1);
        Text losses = new Text(font);
        losses.setText("Losses");
        losses.setColor(0,0,0,1);

        scoreList = new Table();
        scoreList.pad(30).padTop(40);
        scoreList.setBackground(new WobbleDrawable(game.getAssetManager().get("leaderboardsBackground.png"), game.getAssetManager()));
        scoreList.top();
        scoreList.row().height(40.f).padBottom(20);
        scoreList.add(rank).width(100).center();
        scoreList.add(name).expandX().left();
        scoreList.add(score).width(100).center();
        scoreList.add(wins).width(100).center();
        scoreList.add(losses).width(100).center();

        int i = 0;
        List<Account> all = game.getAccountManager().getTop(10);
        for(Account acc : all)
        {
            ++i;
            Text accRank = new Text(font);
            accRank.setText("" + i);
            accRank.setColor(0,0,0,1);
            Text accName = new Text(font);
            accName.setText(acc.getName());
            accName.setColor(0,0,0,1);
            Text accScore = new Text(font);
            accScore.setText("" + acc.getRating());
            accScore.setColor(0,0,0,1);
            Text accWins = new Text(font);
            accWins.setText("" + acc.getWinCount());
            accWins.setColor(0,0,0,1);
            Text accLosses = new Text(font);
            accLosses.setText("" + acc.getLossCount());
            accLosses.setColor(0,0,0,1);

            scoreList.row().height(30.f);
            scoreList.add(accRank).width(100).center();
            scoreList.add(accName).expandX().left();
            scoreList.add(accScore).width(100).center();
            scoreList.add(accWins).width(100).center();
            scoreList.add(accLosses).width(100).center();
        }
        float size = Math.min(WORLDWIDTH, WORLDHEIGHT) - 200.f;
        scoreList.setSize(size, size);
        scoreList.setPosition(WORLDWIDTH / 2.f - scoreList.getWidth() / 2.f, WORLDHEIGHT / 2.f - scoreList.getHeight() / 2.f);
        stage.addActor(scoreList);

        mainMenuButton = new Button("Main menu", game.getAssetManager());
        mainMenuButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                game.setScreen(new MainMenuScreen(game));
            }
        });
        mainMenuButton.setSize(200, 75);
        mainMenuButton.setPosition(WORLDWIDTH / 2.f - mainMenuButton.getWidth() / 2.f, WORLDHEIGHT / 2.f - 400f + 25.f);
        stage.addActor(mainMenuButton);

        Gdx.input.setInputProcessor(stage);
    }


    /***
     * Resize event.
     *
     * Used to correctly position the elements on screen and update the viewport size to support the new window size.
     * @param width new window width
     * @param height new window height
     */
    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }
}
