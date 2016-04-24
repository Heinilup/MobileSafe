package ikabi.com.mobilesafe.kpswitch;

import android.view.View;

/**
 * Created by Rob on 4/24/16.
 */
public interface IPanelConflictLayout {
    boolean isKeyboardShowing();

    /**
     * @return The real status of Visible or not
     */
    boolean isVisible();

    /**
     * Keyboard->Panel
     *
     * @see ikabi.com.mobilesafe.kpswitch.utils.KPSwitchConflictUtil#showPanel(View)
     */
    void handleShow();

    /**
     * Panel->Keyboard
     *
     * @see ikabi.com.mobilesafe.kpswitch.utils.KPSwitchConflictUtil#showKeyboard(View, View)
     */
    void handleHide();
}
