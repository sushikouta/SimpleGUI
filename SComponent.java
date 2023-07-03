package sushikouta;

import java.awt.image.BufferedImage;

public interface SComponent {
    public void add(SContent content);

    public int getWidth();
    public int getWidth(SComponent target);
    public int getHeight();
    public int getHeight(SComponent target);
    public int getX();
    public int getX(SComponent target);
    public int getY();
    public int getY(SComponent target);

    public BufferedImage getGraphics(int width, int height);

    public SContent getContent(int x, int y);

    public void on(SListener<?> listener);
}