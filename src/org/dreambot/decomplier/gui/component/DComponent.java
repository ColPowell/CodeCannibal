package org.dreambot.decomplier.gui.component;

import java.awt.*;

/**
 * @author Notorious BPP
 * @since 7/16/2016.
 */
public interface DComponent<C extends Component> {

    C getOwner();

}
