package com.slickqa.junit;

/**
 * This is a factory for SlickJunitController.  If you want to subclass and extend the default functionality to
 * override defaults you need to replace the Contro
 */
public class SlickJunitControllerFactory {

    public static Class<? extends SlickJunitController> ControllerClass = SlickJunitController.class;

    public static SlickJunitController INSTANCE = null;

    public static synchronized SlickJunitController getControllerInstance() {
        if(SlickJunitControllerFactory.INSTANCE == null) {
            try {
                INSTANCE = ControllerClass.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return SlickJunitControllerFactory.INSTANCE;
    }
}
