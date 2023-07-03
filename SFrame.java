package sushikouta;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

import sushikouta.events.SClickEvent;
import sushikouta.events.SResizeEvent;

public class SFrame implements SComponent {
    public JFrame frame = null;

    public BufferedImage window = null;
    
    public int mouseX = 0;
    public int mouseY = 0;

    private class FrameModel extends JFrame {
        public FrameModel() {
            setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
            add(new JPanel() {
                @Override public void paintComponent(Graphics g) {
                    if (window != null) {
                        g.drawImage(window, 0, 0, null);
                    }
                }
            });
            addWindowListener(new WindowListener() {
                @Override public void windowOpened(WindowEvent e) { }
                @Override public void windowClosing(WindowEvent e) { onClose(); }
                @Override public void windowClosed(WindowEvent e) { }
                @Override public void windowIconified(WindowEvent e) { }
                @Override public void windowDeiconified(WindowEvent e) { }
                @Override public void windowActivated(WindowEvent e) { }
                @Override public void windowDeactivated(WindowEvent e) { }
            });
        }
    }

    public SFrame() {
        setup();
        frame = new FrameModel();
        events.forEach(e -> {
            e.master = this;
            e.onLoad();
        });
        ready();
    }
    public SFrame(SContent content) {
        this();
        add(content);
        repaint();
    }

    public void setup() { }
    public void ready() { }

    public class Interval {
        public boolean isRunning = false;
        public Thread thread = null;
        public Runnable action = null;
        public int interval = 0;
        public Interval(int interval, Runnable action) {
            isRunning = true;
            this.interval = interval;
            this.action = action;
            thread = new Thread(this::run);
            thread.start();
        }

        public synchronized void stop() {
            isRunning = false;
        }

        public void run() {
            while (isRunning) {
                action.run();
                try {
                    Thread.sleep(interval);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public Interval setInterval(int interval, Runnable action) {
        return new Interval(interval, action);
    }

    public void setTitle(String title) {
        frame.setTitle(title);
    }
    public void setSize(int width, int height) {
        frame.getContentPane().setPreferredSize(new Dimension(width, height));
        frame.pack();
    }
    public void show() {
        frame.setVisible(true);
    }
    public void hide() {
        frame.setVisible(false);
    }
    public void repaint() {
        if (content != null) {
            window = getGraphics(getWidth(), getHeight());
            frame.repaint();
        }
    }
    
    public void onClose() { }

    public SContent content = null;
    
    @Override public void add(SContent content) {
        content.master = this;
        this.content = content;
        content.onAdded();
    }

    @Override public int getWidth() {
        return frame.getContentPane().getWidth();
    }
    @Override public int getWidth(SComponent target) {
        return frame.getContentPane().getWidth();
    }
    @Override public int getHeight() {
        return frame.getContentPane().getHeight();
    }
    @Override public int getHeight(SComponent target) {
        return frame.getContentPane().getHeight();
    }
    @Override public int getX() {
        return frame.getX();
    }
    @Override public int getX(SComponent target) {
        return 0;
    }
    @Override public int getY() {
        return frame.getY();
    }
    @Override public int getY(SComponent target) {
        return 0;
    }

    @Override public BufferedImage getGraphics(int width, int height) {
        BufferedImage window = content.getGraphics(width, height);
        if (window != null) {
            return window;
        }
        return new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
    }

    public class SMotionEvent extends SEvent<SMotionEvent> {
        @Override public void onLoad() {
            master.frame.getContentPane().addMouseMotionListener(new MouseMotionListener() {
                @Override public void mouseDragged(MouseEvent e) {
                    SFrame.this.mouseX = e.getX();
                    SFrame.this.mouseY = e.getY();
                    run(new SMotionEvent() {{
                        isDragge = true;
                    }});
                }
                @Override public void mouseMoved(MouseEvent e) {
                    SFrame.this.mouseX = e.getX();
                    SFrame.this.mouseY = e.getY();
                    run(new SMotionEvent());
                }
            });
        }

        @Override public void add(SListener<?> listener) {
            super.add(listener);
            listener.apply(new SMotionEvent());
        }

        public boolean isDragge = false;

        @Override public String toString() {
            return "[mouseX: " + mouseX + ", mouseY: " + mouseY + ", isDragge: " + isDragge + "]";
        }
    }

    public List<? extends SEvent<?>> events = Arrays.asList(new SMotionEvent(), new SClickEvent(), new SResizeEvent());

    @Override public void on(SListener<?> listener) {
        listener.path.add(0, this);
        for (int n = 0;n != events.size();n++) {
            if (events.get(n).getClass() == listener.type) {
                events.get(n).add(listener);
                return;
            }
        }
    }
    @Override public SContent getContent(int x, int y) {
        return content;
    }
}
