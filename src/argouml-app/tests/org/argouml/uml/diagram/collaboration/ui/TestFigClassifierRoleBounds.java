package org.argouml.uml.diagram.collaboration.ui;

import java.awt.Rectangle;

import junit.framework.TestCase;

import org.argouml.model.Model;
import org.argouml.model.mdr.MDRModelImplementation;
import org.argouml.notation.InitNotation;
import org.argouml.notation.providers.uml.InitNotationUml;
import org.argouml.uml.diagram.DiagramSettings;

public class TestFigClassifierRoleBounds extends TestCase {

    public void testMovingWithEmptyStereotypeKeepsExactBounds()
            throws Exception {
        Model.setImplementation(new MDRModelImplementation());
        new InitNotation().init();
        new InitNotationUml().init();

        Object role =
                Model.getCollaborationsFactory().createClassifierRole();
        Model.getCoreHelper().setName(role, "Aluno");

        TestableClassifierRole fig = new TestableClassifierRole(role);
        fig.showEmptyStereotypeAtOriginalPosition();

        Rectangle expected = new Rectangle(300, 200, 180, 100);
        fig.setBounds(expected);

        assertEquals(expected, fig.getBounds());
    }

    private static class TestableClassifierRole extends FigClassifierRole {

        TestableClassifierRole(Object owner) {
            super(owner, new Rectangle(20, 30, 100, 80),
                    new DiagramSettings());
        }

        void showEmptyStereotypeAtOriginalPosition() {
            getStereotypeFig().setBounds(20, 30, 90, 18);
            getStereotypeFig().setVisible(true);
        }
    }
}