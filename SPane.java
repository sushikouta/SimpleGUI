package sushikouta;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class SPane extends SContent {
    public SContent content = null;

    @Override public void add(SContent content) {
        content.master = this;
        this.content = content;
        content.onAdded();
    }

    @Override public BufferedImage getGraphics(int width, int height) {
        if (width == 0 || height == 0) {
            return null;
        }
        BufferedImage window = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
        draw(window, window.createGraphics());
        return window;
    }
    public void draw(BufferedImage window, Graphics2D g) { }

    @Override public SContent getContent(int x, int y) {
        return content;
    }
}
