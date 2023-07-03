package sushikouta;

import java.awt.image.BufferedImage;

public class SContent implements SComponent {
    public SComponent master = null;

    @Override public void add(SContent content) { }

    @Override public int getWidth() {
        if (master != null) {
            return master.getWidth(this);
        }
        return 0;
    }
    @Override public int getWidth(SComponent target) {
        if (master != null) {
            return master.getWidth(target);
        }
        return 0;
    }
    @Override public int getHeight() {
        if (master != null) {
            return master.getHeight(this);
        }
        return 0;
    }
    @Override public int getHeight(SComponent target) {
        if (master != null) {
            return master.getWidth(target);
        }
        return 0;
    }
    @Override public int getX() {
        if (master != null) {
            return master.getX(this);
        }
        return 0;
    }
    @Override public int getX(SComponent target) {
        if (master != null) {
            return master.getX(target);
        }
        return 0;
    }
    @Override public int getY() {
        if (master != null) {
            return master.getY(this);
        }
        return 0;
    }
    @Override public int getY(SComponent target) {
        if (master != null) {
            return master.getY(target);
        }
        return 0;
    }

    @Override public BufferedImage getGraphics(int width, int height) {
        return new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
    }

    public void onAdded() { }

    @Override public SContent getContent(int x, int y) {
        return null;
    }

    @Override public void on(SListener<?> listener) {
        listener.path.add(0, this);
        if (master != null) {
            master.on(listener);
        }
    }
}
