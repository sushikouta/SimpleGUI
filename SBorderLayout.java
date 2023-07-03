package sushikouta;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.HashMap;

public class SBorderLayout extends SContent {
    public static final String Center = "CENTER";
    public static final String Top    = "TOP";
    public static final String Bottom = "BOTTOM";
    public static final String Left   = "LEFT";
    public static final String Right  = "RIGHT";

    public static final String[] POSITIONS = {
        Center,
        Top,
        Bottom,
        Left,
        Right
    };

    private class ContentModel {
        public ContentModel(int width, SContent content) {
            this.width = width;
            this.content = content;
        }
        public int width = 0;
        public SContent content = null;
    }

    public HashMap<String, ContentModel> contents = new HashMap<>() {{
        put(Center, new ContentModel(0, null));
        put(Top, new ContentModel(0, null));
        put(Bottom, new ContentModel(0, null));
        put(Left, new ContentModel(0, null));
        put(Right, new ContentModel(0, null));
    }};

    @Override public void add(SContent content) {
        add(content, 0);
    }
    public void add(SContent content, int width) {
        for (String position : POSITIONS) {
            if (contents.get(position).content == null) {
                add(position, new ContentModel(width, content));
                return;
            }
        }
    }
    public void add(String key, SContent content) {
        add(key, new ContentModel(0, content));
    }
    public void add(String key, SContent content, int width) {
        add(key, new ContentModel(width, content));
    }
    private void add(String key, ContentModel content) {
        content.content.master = this;
        if (Arrays.asList(POSITIONS).contains(key)) {
            contents.put(key, content);
        }
        content.content.onAdded();
    }

    public String toPosition(SComponent target) {
        for (int n = 0;n != contents.size();n++) {
            if (contents.get(POSITIONS[n]).content == target) {
                return POSITIONS[n];
            }
        }
        return null;
    }

    @Override public int getWidth(SComponent target) {
        return getWidth(toPosition(target));
    }
    @Override public int getHeight(SComponent target) {
        return getHeight(toPosition(target));
    }
    public int getWidth(String position) {
        switch (position) {
            case Center:
                return Math.max(getWidth() - contents.get(Left).width - contents.get(Right).width, 0);
            case Left:
                return contents.get(Left).width;
            case Right:
                return contents.get(Right).width;
            default:
                return getWidth();
        }
    }
    public int getHeight(String position) {
        switch (position) {
            case Top:
                return contents.get(Top).width;
            case Bottom:
                return contents.get(Bottom).width;
            default:
                return Math.max(getHeight() - contents.get(Top).width - contents.get(Bottom).width, 0);
        }
    }
    @Override public int getX(SComponent target) {
        return getX() + getX(toPosition(target));
    }
    @Override public int getY(SComponent target) {
        return getY() + getY(toPosition(target));
    }
    public int getX(String position) {
        switch (position) {
            case Center:
                return getWidth(Left);
            case Right:
                return getWidth() - getWidth(Right);
            default:
                return 0;
        }
    }
    public int getY(String position) {
        switch (position) {
            case Top:
                return 0;
            case Bottom:
                return getHeight() - getHeight(Bottom);
            default:
                return getHeight(Top);
        }
    }

    @Override public BufferedImage getGraphics(int width, int height) {
        BufferedImage window = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D g = window.createGraphics();
        for (String position : POSITIONS) {
            if (contents.get(position).content == null) continue;

            BufferedImage w = contents.get(position).content.getGraphics(width, height);
            if (window != null) {
                g.drawImage(w, getX(position), getY(position), null);
            }
        }
        return window;
    }

    @Override public SContent getContent(int x, int y) {
        for (String position : POSITIONS) {
            SContent target = contents.get(position).content;
            if (getX(target) <= x && x < getX(target) + getWidth(position) && getY(target) <= y && y < getY(target) + getHeight(position)) {
                return target;
            }
        }
        return null;
    }
}
