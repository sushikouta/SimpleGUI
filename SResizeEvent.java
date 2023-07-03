package sushikouta.events;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import sushikouta.SEvent;
import sushikouta.SListener;

public class SResizeEvent extends SEvent<SResizeEvent> {
    @Override public void onLoad() {
        master.frame.getContentPane().addComponentListener(new ComponentListener() {
            @Override public void componentResized(ComponentEvent e) {
                run(new SResizeEvent() {{
                    width = e.getComponent().getWidth();
                    height = e.getComponent().getHeight();
                }});
            }
            @Override public void componentMoved(ComponentEvent e) { }
            @Override public void componentShown(ComponentEvent e) { }
            @Override public void componentHidden(ComponentEvent e) { }
        });
    }

    @Override public void onApply(SResizeEvent event, SListener<?> listener) {
        event.width = listener.getTarget().getWidth();
        event.height = listener.getTarget().getHeight();
    }

    @Override public void add(SListener<?> listener) {
        super.add(listener);
        int width_ = master.frame.getContentPane().getWidth();
        int height_ = master.frame.getContentPane().getHeight();
        listener.apply(new SResizeEvent() {{
            width = width_;
            height = height_;
        }});
    }

    public int width = 0;
    public int height = 0;
}