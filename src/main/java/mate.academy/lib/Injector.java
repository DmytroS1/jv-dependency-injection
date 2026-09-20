package mate.academy.lib;

import java.lang.reflect.Field;

public class Injector {
    private static final Injector injector = new Injector();

    public static Injector getInjector() {

        return injector;
    }

    public Object getInstance(Class<?> interfaceClazz) throws NoSuchMethodException {
        if (!interfaceClazz.isAnnotationPresent(Component.class)) {
            throw new RuntimeException("Class " + interfaceClazz.getName()
                    + " doesn't have @Component annotation");
        }
        try {
            Object instance = interfaceClazz.getDeclaredConstructor().newInstance();
            for (Field field : interfaceClazz.getDeclaredFields()) {
                if (field.isAnnotationPresent(Inject.class)) {
                    Class<?> dependencyType = field.getType();
                    Object dependency = getInstance(dependencyType);
                    field.setAccessible(true);
                    field.set(instance, dependency);
                }

            }
            return instance;
        } catch (Exception e) {
            throw new RuntimeException("Failed to instantiate "
                    + interfaceClazz.getName(), e);
        }
    }
}
