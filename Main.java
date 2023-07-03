import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.imageio.ImageIO;

import sushikouta.events.SResizeEvent;

public class Main {
    public static void main(String[] args) {
        System.out.println(System.getProperty("java.version"));
        new TypeReference<String>();
        // new Main();
    }
    public Main() {
        new SFrame(new SBorderLayout() {
            @Override public void onAdded() {
                add(Top, new SPane() {
                    @Override public void draw(BufferedImage window, Graphics2D g) {
                        g.setColor(new Color(0x2C2C2C));
                        g.fillRect(0, 0, getWidth(), getHeight());
                    }
                }, 60);
                add(Left, new SPane() {
                    @Override public void draw(BufferedImage window, Graphics2D g) {
                        g.setColor(Color.WHITE);
                        g.fillRect(0, 0, getWidth(), getHeight());
                    }
                }, 180);
                add(Center, new SScrollPane(new SPane() {
                    @Override public void draw(BufferedImage window, Graphics2D g) {
                        g.setColor(new Color(0x1E1E1E));
                        g.fillRect(0, 0, getWidth(), getHeight());
                        g.setColor(Color.WHITE);
                        for (int n = 0;n != getWidth() / 100;n++) {
                            g.drawLine(n * 100, 0, n * 100, getHeight());
                        }
                        for (int n = 0;n != getHeight() / 100;n++) {
                            g.drawLine(0, n * 100,getWidth(), n * 100);
                        }
                    }
                }, 2000, 2000));
                add(Right, new SPane() {
                    @Override public void draw(BufferedImage window, Graphics2D g) {
                        g.setColor(Color.WHITE);
                        g.fillRect(0, 0, getWidth(), getHeight());
                    }
                }, 180);
            }
        }) {
            @Override public void ready() {
                setTitle("Vector Editor");
                setSize(1000, 800);
                show();

                setInterval(0, this::every);
            }
            public void every() {
                repaint();
            }
            @Override public void onClose() {
                System.exit(0);
            }
        };
    }
}
