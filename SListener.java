package sushikouta;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class SListener<T> {
    public Consumer<T> action = null;
    public Class<T> type = null;
    
    public List<SComponent> path = new ArrayList<>();

    @SuppressWarnings("unchecked")
    public SListener(Consumer<T> action, T... c) {
        this.action = action;
        type = (Class<T>) (Class<?>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    public void apply(SEvent<?> event) {
        action.accept(type.cast(event));
    }

    public SComponent getTarget() {
        return path.get(path.size() - 1);
    }
}