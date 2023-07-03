package sushikouta.events;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.function.Supplier;

import sushikouta.SEvent;

public class SClickEvent extends SEvent<SClickEvent> {
    @Override public void onLoad() {
        master.frame.getContentPane().addMouseListener(new MouseListener() {
            @Override public void mouseClicked(MouseEvent e) { }
            @Override public void mousePressed(MouseEvent e) {
                run(new SClickEvent() {{
                    isPress = true;
                    switch (e.getButton()) {
                        case MouseEvent.BUTTON1:
                            isLeft = true;
                            break;
                        case MouseEvent.BUTTON2:
                            isMiddle = true;
                            break;
                        case MouseEvent.BUTTON3:
                            isRight = true;
                            break;
                    }
                }});
            }
            @Override public void mouseReleased(MouseEvent e) {
                run(new SClickEvent() {{
                    isRelease = true;
                    switch (e.getButton()) {
                        case MouseEvent.BUTTON1:
                            isLeft = true;
                            break;
                        case MouseEvent.BUTTON2:
                            isMiddle = true;
                            break;
                        case MouseEvent.BUTTON3:
                            isRight = true;
                            break;
                    }
                }});
            }
            @Override public void mouseEntered(MouseEvent e) { }
            @Override public void mouseExited(MouseEvent e) { }
        });
    }
    
    public boolean isPress = false;
    public boolean isRelease = false;

    public boolean isLeft = false;
    public boolean isMiddle = false;
    public boolean isRight = false;

    @Override public String toString() {
        return "[STATUS: " + (isPress ? "PRESS" : "RELEASE") + ", KEY: " + new Supplier<String>() {
            @Override public String get() {
                if (isLeft) {
                    return "LEFT";
                }
                if (isMiddle) {
                    return "MIDDLE";
                }
                return "RIGHT";
            }
        }.get() + "]";
    }
}
