package org.argouml.uml.diagram.ui;

import org.tigris.gef.presentation.FigGroup;

/**
 * Base for groups whose subclasses calculate their own layout bounds.
 */
public abstract class FigGroupWithLayoutBounds extends FigGroup {

    /**
     * Records exact bounds after layout.
     * The caller handles notifications when needed.
     */
    protected final void updateBoundsFromLayout(
            int x, int y, int width, int height) {
        _x = x;
        _y = y;
        _w = width;
        _h = height;
    }
}
