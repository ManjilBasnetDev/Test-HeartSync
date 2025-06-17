package heartsync.navigation;

import javax.swing.JFrame;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

/**
 * Central registry for top-level windows – ensures only one instance of every
 * JFrame subclass exists at a time and offers handy show/hide helpers.
 */
public final class WindowManager {
    private static final Map<Class<? extends JFrame>, JFrame> WINDOWS = new ConcurrentHashMap<>();

    private WindowManager() { }

    /** Obtain existing window or create via factory. */
    public static <T extends JFrame> T getWindow(Class<T> clazz, Supplier<T> factory) {
        JFrame window = WINDOWS.get(clazz);
        if (window == null) {
            window = factory.get();
            WINDOWS.put(clazz, window);
        }
        return clazz.cast(window);
    }

    /** Hide current (may be null) & show target (centred, front). */
    public static <T extends JFrame> void show(Class<T> clazz, Supplier<T> factory, JFrame currentToHide) {
        if (currentToHide != null) currentToHide.setVisible(false);
        T win = getWindow(clazz, factory);
        win.setLocationRelativeTo(null);
        win.setVisible(true);
        win.setExtendedState(JFrame.NORMAL);
        win.toFront();
    }

    /** Dispose and unregister a specific window class. */
    public static void dispose(Class<? extends JFrame> clazz) {
        JFrame w = WINDOWS.remove(clazz);
        if (w != null) w.dispose();
    }

    /** Close every tracked window – call on full application exit. */
    public static void closeAll() {
        WINDOWS.values().forEach(JFrame::dispose);
        WINDOWS.clear();
    }
}
