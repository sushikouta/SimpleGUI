package sushikouta;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import sushikouta.SFrame.SMotionEvent;
import sushikouta.events.SClickEvent;
import sushikouta.events.SResizeEvent;

public class SScrollPane extends SContent {
    public SContent content = null;

    public SScrollPane() { }
    public SScrollPane(SContent content) {
        add(content);
    }
    public SScrollPane(SContent content, int contentWidth, int contentHeight) {
        this(content);
        setSize(contentWidth, contentHeight);
    }
    public SScrollPane(SContent content, int contentWidth, int contentHeight, int type) {
        this(content, contentWidth, contentHeight);
        scrollbarType = type;
    }

    @Override public void add(SContent content) {
        content.master = this;
        this.content = content;
        content.onAdded();
    }

    public int contentWidth = 0;
    public int contentHeight = 0;

    public void setSize(int width, int height) {
        contentWidth = width;
        contentHeight = height;
    }

    @Override public int getWidth(SComponent target) {
        if (master != null) {
            return Math.max(getWidth(), (scrollbarType == Horizontal || scrollbarType == Both ? contentWidth : 0));
        }
        return contentWidth;
    }
    @Override public int getHeight(SComponent target) {
        if (master != null) {
            return Math.max(getHeight(), (scrollbarType == Vertical || scrollbarType == Both ? contentHeight : 0));
        }
        return contentHeight;
    }

    public static final int None = 0;
    public static final int Vertical = 1; 
    public static final int Horizontal = 2;
    public static final int Both = 3;

    public int scrollbarType = Both;

    public int scrollbarWidth = 20;

    public int hasScrollbar = 0;

    public double VScrollbarTrack = 0;
    public double HScrollbarTrack = 0;

    public int mouseX = 0;
    public int mouseY = 0;

    public int beforeX = 0;
    public int beforeY = 0;

    public int VScrollbarPosition = 0;
    public int HScrollbarPosition = 0;

    public int beforeWidth = 1;
    public int beforeHeight = 1;

    @Override public void onAdded() {
        on(new SListener<SMotionEvent>(this::onMotion));
        on(new SListener<SResizeEvent>(this::onResize));
        on(new SListener<SClickEvent>(this::onClick));
    }

    private void onMotion(SMotionEvent event) {
        mouseX = event.mouseX;
        mouseY = event.mouseY;

        if (hasScrollbar == Vertical) {
            VScrollbarPosition = Math.min(Math.max(VScrollbarPosition + mouseY - beforeY, 0),  (int) Math.round(VScrollbarTrack - VScrollbarTrack * VScrollbarTrack / contentHeight));
            beforeY = mouseY;
            return;
        }
        if (hasScrollbar == Horizontal) {
            HScrollbarPosition = Math.min(Math.max(HScrollbarPosition + mouseX - beforeX, 0), (int) Math.round(HScrollbarTrack - HScrollbarTrack * HScrollbarTrack / contentWidth));
            beforeX = mouseX;
        }
    }
    private void onClick(SClickEvent event) {
        if (event.isRelease) {
            hasScrollbar = None;
            return;
        }
        if ((scrollbarType == Vertical || scrollbarType == Both) && mouseX >= getWidth() - scrollbarWidth && mouseY >= VScrollbarPosition && mouseY < Math.min(VScrollbarPosition + (int) Math.round(VScrollbarTrack * VScrollbarTrack / contentHeight), VScrollbarTrack)) {
            hasScrollbar = Vertical;
            beforeY = mouseY;
            return;
        }
        if ((scrollbarType == Horizontal || scrollbarType == Both) && mouseY >= getHeight() - scrollbarWidth && mouseX >= HScrollbarPosition && mouseX < Math.min(HScrollbarPosition + (int) (HScrollbarTrack * HScrollbarTrack / contentWidth), HScrollbarTrack)) {
            hasScrollbar = Horizontal;
            beforeX = mouseX;
        }
    }
    private void onResize(SResizeEvent event) {
        VScrollbarTrack = event.height - (scrollbarType == Both ? scrollbarWidth : 0);
        HScrollbarTrack = event.width - (scrollbarType == Both ? scrollbarWidth : 0);

        if (contentWidth > HScrollbarTrack) {
            HScrollbarPosition = (int) ((double) HScrollbarPosition * event.width / beforeWidth);
        } else {
            HScrollbarPosition = 0;
        }
        if (contentHeight > VScrollbarTrack) {
            VScrollbarPosition = (int) ((double) VScrollbarPosition * event.height / beforeHeight);
        } else {
            VScrollbarPosition = 0;
        }

        beforeWidth = event.width;
        beforeHeight = event.height;
    }

    @Override public BufferedImage getGraphics(int width, int height) {
        if (width == 0 || height == 0) {
            return null;
        }
        BufferedImage window = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D g = window.createGraphics();

        if (content != null) {
            g.drawImage(content.getGraphics(getWidth(content), getHeight(content)), (int) Math.round(-HScrollbarPosition / HScrollbarTrack * contentWidth), (int) Math.round(-VScrollbarPosition / VScrollbarTrack * contentHeight), null);
        }

        if (scrollbarType == Vertical || scrollbarType == Both) {
            g.setColor(new Color(0xf0f0f0));
            g.fillRect(getWidth() - scrollbarWidth, 0, scrollbarWidth, getHeight());
            g.setColor(new Color(0x616161));
            g.fillRect(getWidth() - scrollbarWidth, VScrollbarPosition, scrollbarWidth, (int) Math.round(VScrollbarTrack * VScrollbarTrack / contentHeight));
            g.setColor(Color.RED);
            g.drawRect(getWidth() - scrollbarWidth, 0, scrollbarWidth, (int) Math.round(VScrollbarTrack * VScrollbarTrack / contentHeight));
        }
        if (scrollbarType == Horizontal || scrollbarType == Both) {
            g.setColor(new Color(0xf0f0f0));
            g.fillRect(0, getHeight() - scrollbarWidth, getWidth(), scrollbarWidth);  
            g.setColor(new Color(0x616161)); 
            g.fillRect(HScrollbarPosition, getHeight() - scrollbarWidth, (int) Math.round(HScrollbarTrack * HScrollbarTrack / contentWidth), scrollbarWidth);
        }
        if (scrollbarType == Both) {
            g.setColor(Color.GRAY);
            g.fillRect(getWidth() - scrollbarWidth, getHeight() - scrollbarWidth, scrollbarWidth, scrollbarWidth);
        }

        return window;
    }
}
