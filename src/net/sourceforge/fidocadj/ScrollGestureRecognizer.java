package net.sourceforge.fidocadj;

import javax.swing.*;
import javax.swing.event.*;

import java.awt.*;
import java.awt.event.*;

import net.sourceforge.fidocadj.circuit.*;
import net.sourceforge.fidocadj.circuit.controllers.ElementsEdtActions;
import net.sourceforge.fidocadj.toolbars.*;


/** Employed in FidoCadJ with the author's permission.
    <pre>
    This file is part of FidoCadJ.

    FidoCadJ is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FidoCadJ is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with FidoCadJ.  If not, see <http://www.gnu.org/licenses/>.
    </pre>
    @author Santhosh Kumar T - santhosh@in.fiorano.com
*/
public final class ScrollGestureRecognizer implements AWTEventListener,
    ChangeSelectionListener
{
    private int actionSelected;

    private static ScrollGestureRecognizer instance = new
        ScrollGestureRecognizer();

    /** Constructor.
    */
    public ScrollGestureRecognizer()
    {
        start();
    }

    /** Get the current instance.
        @return the instance.
    */
    public static ScrollGestureRecognizer getInstance()
    {
        return instance;
    }

    /** Start the scroll operation.
    */
    void start()
    {
        Toolkit.getDefaultToolkit().addAWTEventListener(this,
            AWTEvent.MOUSE_EVENT_MASK);
    }

    /** Stop the scroll operation.
    */
    void stop()
    {
        Toolkit.getDefaultToolkit().removeAWTEventListener(this);
    }

    /** Event dispatched (?)
        @param event the AWT event dispatched.
    */
    public void eventDispatched(AWTEvent event)
    {
        MouseEvent me = (MouseEvent)event;
        boolean isGesture = (SwingUtilities.isMiddleMouseButton(me) ||
            actionSelected==ElementsEdtActions.HAND) &&
            me.getID()==MouseEvent.MOUSE_PRESSED;

        Component co=me.getComponent();

        if (!(co instanceof CircuitPanel))
            return;

        if(!isGesture)
            return;

        JViewport viewPort =
            (JViewport)SwingUtilities.getAncestorOfClass(JViewport.class,
            me.getComponent());
        if(viewPort==null)
            return;
        JRootPane rootPane = SwingUtilities.getRootPane(viewPort);
        if(rootPane==null)
            return;

        Point location = SwingUtilities.convertPoint(me.getComponent(),
            me.getPoint(), rootPane.getGlassPane());
        ScrollGlassPane glassPane=new ScrollGlassPane(rootPane.getGlassPane(),
            viewPort, location);
        rootPane.setGlassPane(glassPane);
        glassPane.setVisible(true);
    }

    /** ChangeSelectionListener interface implementation .
        @param s the selection state.
        @param macro the current macro key (if applicable).
    */
    public void setSelectionState(int s, String macro)
    {
        actionSelected=s;
    }

    /** Set if the strict FidoCAD compatibility mode is active
        @param strict true if the compatibility with FidoCAD should be
        obtained.
    */
    public void setStrictCompatibility(boolean strict)
    {
        // Nothing is needed here.
    }

    /** Get the current editing action (see the constants defined in this class)
        @return the current editing action
    */
    public int getSelectionState()
    {
        return actionSelected;
    }
}
