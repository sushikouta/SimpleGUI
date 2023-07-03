package sushikouta;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unchecked")
public class SEvent<T extends SEvent<?>> implements Cloneable {
    public SFrame master = null;

    public void onLoad() {

    }

    public void run(T event) {
        listeners.forEach(listener -> {
            for (int n = 0;n != listener.path.size() - 1;n++) { 
                if (listener.path.get(n).getContent(getMouseX(), getMouseY()) != listener.path.get(n + 1)) {
                    return;
                }
            }
            event.mouseX = getMouseX() - listener.path.get(listener.path.size() - 1).getX();
            event.mouseY = getMouseY() - listener.path.get(listener.path.size() - 1).getY();

            T e = (T) event.clone();

            onApply(e, listener);

            if (e != null) {
                listener.apply(event);
            }
        });
    }

    public void onApply(T event, SListener<?> listener) {

    }

    public List<SListener<?>> listeners = new ArrayList<>();

    public void add(SListener<?> listener) {
        listeners.add(listener);
    }

    public Class<?> getTrueClass() {
        return getClass();
    }

    protected int getMouseX() {
        return master.mouseX;
    }
    protected int getMouseY() {
        return master.mouseY;
    }

    public int mouseX = 0;
    public int mouseY = 0;

    @Override public T clone() {
		try {
			return (T) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new InternalError(e.toString());
		}
	}
}
