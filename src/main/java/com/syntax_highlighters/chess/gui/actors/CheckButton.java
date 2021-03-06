package com.syntax_highlighters.chess.gui.actors;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.syntax_highlighters.chess.gui.WobbleDrawable;

public class CheckButton extends AbstractSelectableButton {
    public CheckButton(AssetManager assetManager, String buttonText, Color textColor) {
        super(assetManager, buttonText, textColor);
        
        this.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                setSelected(!isSelected());
            }
        });
    }

    public void setSelectableTexture() {
        boxTexture = assetManager.get("square.png", Texture.class);
        boxTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        box = new WobbleDrawable(boxTexture, assetManager);
        checkedTexture = assetManager.get("tick.png", Texture.class);
        checkedTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        checked = new WobbleDrawable(checkedTexture, assetManager);
    }
}
