package org.argouml.core.propertypanels.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import junit.framework.TestCase;

import org.argouml.core.propertypanels.ModelTestSupport;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectImpl;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.Model;
import org.argouml.profile.init.InitProfileSubsystem;

public class TestUserModelComboBoxModels extends TestCase {

    private Project project;
    private Object userModel;
    private Object umlClass;
    private Object dataType;
    private Object operation;
    private Object pack;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        ModelTestSupport.initializeModel();
        new InitProfileSubsystem().init();
        project = new ProjectImpl();
        userModel = Model.getModelManagementFactory().createModel();
        project.setRoots(Collections.singletonList(userModel));
        project.addMember(userModel);
        umlClass = Model.getCoreFactory().buildClass("Alvo", userModel);
        dataType = Model.getCoreFactory().buildDataType("int", userModel);
        operation =
            Model.getCoreFactory().buildOperation2(umlClass, dataType, "op");
        pack = Model.getModelManagementFactory().buildPackage("pacote");
        Model.getCoreHelper().addOwnedElement(userModel, pack);
    }

    @Override
    protected void tearDown() throws Exception {
        project.remove();
        super.tearDown();
    }

    private List<Object> contents(UMLComboBoxModel comboModel) {
        List<Object> result = new ArrayList<Object>();
        for (int i = 0; i < comboModel.getSize(); i++) {
            result.add(comboModel.getElementAt(i));
        }
        return result;
    }

    public void testActivityGraphContextListsClassifiersFeaturesAndPackages() {
        Object graph =
            Model.getActivityGraphsFactory().buildActivityGraph(umlClass);
        UMLActivityGraphContextComboBoxModel comboModel =
            new UMLActivityGraphContextComboBoxModel("context", graph, project);
        List<Object> contents = contents(comboModel);
        assertTrue(contents.contains(umlClass));
        assertTrue(contents.contains(dataType));
        assertTrue(contents.contains(operation));
        assertTrue(contents.contains(pack));
        assertFalse(contents.contains(null));
        assertSame(umlClass, comboModel.getSelectedItem());
        assertTrue(comboModel.isValidElement(pack));
        assertFalse(comboModel.isValidElement(graph));
    }

    public void testStateMachineContextListsClassifiersAndFeatures() {
        Object stateMachine =
            Model.getStateMachinesFactory().buildStateMachine(umlClass);
        UMLStateMachineContextComboBoxModel comboModel =
            new UMLStateMachineContextComboBoxModel("context", stateMachine, project);
        List<Object> contents = contents(comboModel);
        assertTrue(contents.contains(umlClass));
        assertTrue(contents.contains(operation));
        assertFalse(contents.contains(pack));
        assertFalse(contents.contains(null));
        assertSame(umlClass, comboModel.getSelectedItem());
        assertTrue(comboModel.isValidElement(operation));
        assertFalse(comboModel.isValidElement(pack));
    }

    public void testRepresentedClassifierExcludesElementsInsideCollaboration() {
        Object collaboration =
            Model.getCollaborationsFactory().buildCollaboration(userModel);
        Object role =
            Model.getCollaborationsFactory().buildClassifierRole(collaboration);
        UMLCollaborationRepresentedClassifierComboBoxModel comboModel =
            new UMLCollaborationRepresentedClassifierComboBoxModel(
                    "representedClassifier", collaboration, project);
        List<Object> contents = contents(comboModel);
        assertTrue(contents.contains(umlClass));
        assertTrue(contents.contains(dataType));
        assertFalse(contents.contains(role));
        assertTrue(contents.contains(null));
        assertNull(comboModel.getSelectedItem());
        assertFalse(comboModel.isValidElement(umlClass));
    }

    public void testRepresentedOperationListsOperationsOutsideCollaboration() {
        Object collaboration =
            Model.getCollaborationsFactory().buildCollaboration(userModel);
        UMLCollaborationRepresentedOperationComboBoxModel comboModel =
            new UMLCollaborationRepresentedOperationComboBoxModel(
                    "representedOperation", collaboration, project);
        List<Object> contents = contents(comboModel);
        assertTrue(contents.contains(operation));
        assertFalse(contents.contains(umlClass));
        assertTrue(contents.contains(null));
        assertNull(comboModel.getSelectedItem());
        assertFalse(comboModel.isValidElement(operation));
    }

    public void testPowertypeIsLazyAndListsUserAndProfileClassifiers() {
        Object other = Model.getCoreFactory().buildClass("Outra", userModel);
        Object generalization =
            Model.getCoreFactory().buildGeneralization(umlClass, other);
        UMLGeneralizationPowertypeComboBoxModel comboModel =
            new UMLGeneralizationPowertypeComboBoxModel(
                    "powertype", generalization, project);
        assertTrue(comboModel.isLazy());
        List<Object> minimal = contents(comboModel);
        assertFalse(minimal.contains(umlClass));

        comboModel.buildModelList();
        List<Object> full = contents(comboModel);
        assertTrue(full.contains(umlClass));
        assertTrue(full.contains(other));
        assertTrue(full.contains(dataType));
        int fromProfiles = 0;
        for (Object element : full) {
            assertTrue(Model.getFacade().isAClassifier(element));
            if (!Model.getFacade().getRoot(element).equals(userModel)) {
                fromProfiles++;
            }
        }
        assertTrue(fromProfiles > 0);
    }

    public void testModelsUseTheInjectedProjectWithoutProjectManager() {
        assertTrue(ProjectManager.getManager().getOpenProjects().isEmpty());
        Object stateMachine =
            Model.getStateMachinesFactory().buildStateMachine(umlClass);
        UMLStateMachineContextComboBoxModel comboModel =
            new UMLStateMachineContextComboBoxModel(
                    "context", stateMachine, project);
        assertSame(project, comboModel.getProject());
        assertTrue(contents(comboModel).contains(umlClass));
        assertTrue(ProjectManager.getManager().getOpenProjects().isEmpty());
    }

    public void testModelsFailWithoutInjectedProject() {
        Object other = Model.getCoreFactory().buildClass("Outra", userModel);
        Object stateMachine =
            Model.getStateMachinesFactory().buildStateMachine(umlClass);
        Object generalization =
            Model.getCoreFactory().buildGeneralization(umlClass, other);
        try {
            new UMLStateMachineContextComboBoxModel(
                    "context", stateMachine, null);
            fail("NullPointerException esperada");
        } catch (NullPointerException e) {
            assertTrue(e.getStackTrace()[0].getClassName().startsWith(
                    UMLStateMachineContextComboBoxModel.class.getName()));
        }
        UMLGeneralizationPowertypeComboBoxModel lazy =
            new UMLGeneralizationPowertypeComboBoxModel(
                    "powertype", generalization, null);
        try {
            lazy.buildModelList();
            fail("NullPointerException esperada");
        } catch (NullPointerException e) {
            assertTrue(e.getStackTrace()[0].getClassName().startsWith(
                    UMLGeneralizationPowertypeComboBoxModel.class.getName()));
        }
    }
}
