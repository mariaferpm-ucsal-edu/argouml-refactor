package org.argouml.uml.diagram.ui;

import junit.framework.TestCase;

import org.argouml.uml.diagram.DiagramSettings;
import org.tigris.gef.presentation.FigText;

public class TestFigTextGroup extends TestCase {

    public void testCalcBoundsKeepsSpaceForHiddenText() {
        FigTextGroup group =
                new FigTextGroup(null, new DiagramSettings());

        FigText first = new FigText(0, 0, 80, 20);
        first.setText("empresta");

        FigText second = new FigText(0, 0, 140, 20);
        second.setText("outro texto");

        group.addFig(first);
        group.addFig(second);
        group.calcBounds();

        int expectedWidth =
                Math.max(first.getWidth(), second.getWidth());
        int expectedHeight =
                first.getHeight() + second.getHeight();

        assertEquals(expectedWidth, group.getWidth());
        assertEquals(expectedHeight, group.getHeight());

        second.setVisible(false);
        group.calcBounds();

        assertEquals(expectedWidth, group.getWidth());
        assertEquals(expectedHeight, group.getHeight());
    }
}
