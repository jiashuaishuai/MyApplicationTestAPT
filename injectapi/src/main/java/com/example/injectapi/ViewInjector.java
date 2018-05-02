package com.example.injectapi;



public class ViewInjector {
    private static final String SUFFIX = "$$InjectView";

    public static void injectView(Object activity) {
        ViewInject proxyActivity = findProxyActivity(activity);
        proxyActivity.inject(activity, activity);
    }

    public static void injectView(Object object, Object view) {
        ViewInject proxyActivity = findProxyActivity(object);
        proxyActivity.inject(object, view);
    }

    /**
     * 通过反射创建要使用的类的对象
     */
    private static ViewInject findProxyActivity(Object activity) {
        try {
            Class<?> clazz = activity.getClass();
            Class<?> injectorClazz = Class.forName(clazz.getName() + SUFFIX);
            return (ViewInject) injectorClazz.newInstance();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        throw new RuntimeException(String.format("can not find %s , something when compiler.", activity.getClass().getSimpleName() + SUFFIX));
    }
}
