package org.argouml.uml.diagram.state.ui;

import java.awt.Rectangle;

import junit.framework.TestCase;

import org.argouml.model.Model;
import org.argouml.model.mdr.MDRModelImplementation;
import org.argouml.notation.InitNotation;
import org.argouml.notation.providers.uml.InitNotationUml;
import org.argouml.uml.diagram.DiagramSettings;
import org.tigris.gef.presentation.FigGroup;
import org.tigris.gef.presentation.FigRect;

public class TestFigVertexBounds extends TestCase {

    public void testRegionLayoutKeepsVertexBounds() throws Exception {
        Model.setImplementation(new MDRModelImplementation());
        new InitNotation().init();
        new InitNotationUml().init();

        Object state =
                Model.getStateMachinesFactory().createCompositeState();
        Model.getCoreHelper().setName(state, "Estado");

        FigVertex vertex = new FigVertex(
                state, new Rectangle(50, 60, 100, 80),
                new DiagramSettings());

        // initialize() adds the region compartment last.
        int last = vertex.getFigs().size() - 1;
        FigGroup compartment =
                (FigGroup) vertex.getFigs().get(last);

        Rectangle vertexBounds = vertex.getBounds();
        Rectangle expected = new Rectangle(200, 210, 180, 120);
        compartment.setBounds(expected);

        assertEquals(expected, compartment.getBounds());
        assertEquals(vertexBounds, vertex.getBounds());

        compartment.addFig(new FigRect(0, 0, 30, 20));
        compartment.addFig(new FigRect(0, 20, 50, 40));

        vertexBounds = vertex.getBounds();
        expected = new Rectangle(300, 310, 220, 140);
        compartment.setBounds(expected);

        assertEquals(expected, compartment.getBounds());
        assertEquals(vertexBounds, vertex.getBounds());
        assertEquals(2, compartment.getFigs().size());
        assertEquals(220, compartment.getFigs().get(0).getWidth());
        assertEquals(220, compartment.getFigs().get(1).getWidth());
        assertEquals(140,
                compartment.getFigs().get(0).getHeight()
                        + compartment.getFigs().get(1).getHeight());
        assertEquals(
                compartment.getFigs().get(0).getY()
                        + compartment.getFigs().get(0).getHeight(),
                compartment.getFigs().get(1).getY());
    }
}