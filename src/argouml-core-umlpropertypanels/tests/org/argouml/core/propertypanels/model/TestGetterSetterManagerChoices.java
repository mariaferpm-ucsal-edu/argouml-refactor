package org.argouml.core.propertypanels.model;

import java.util.Collections;
import java.util.List;

import junit.framework.TestCase;

import org.argouml.core.propertypanels.ModelTestSupport;
import org.argouml.kernel.Command;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectImpl;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.Model;
import org.argouml.profile.init.InitProfileSubsystem;

public class TestGetterSetterManagerChoices extends TestCase {

    private Project project;
    private Object userModel;
    private Object umlClass;
    private GetterSetterManager manager;

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
        manager = GetterSetterManager.getGetterSetter(Object.class, project);
    }

    @Override
    protected void tearDown() throws Exception {
        project.remove();
        super.tearDown();
    }

    private List<?> choices(String propertyName, Object target) {
        Command command = manager.getAddCommand(propertyName, target);
        return ((GetterSetterManagerImpl.AddModelElementCommand) command)
                .getChoices();
    }

    public void testProjectIsNotRegisteredInProjectManager() {
        assertTrue(ProjectManager.getManager().getOpenProjects().isEmpty());
    }

    public void testReceptionChoicesListReceptionsOfTheUserModel() {
        Object reception =
            Model.getCommonBehaviorFactory().buildReception(umlClass);
        List<?> choices = choices("reception", umlClass);
        assertEquals(1, choices.size());
        assertTrue(choices.contains(reception));
    }

    public void testResidentElementChoicesListUserModelElements() {
        Object other = Model.getCoreFactory().buildInterface("Outra", userModel);
        List<?> choices = choices("residentElement", umlClass);
        assertTrue(choices.contains(umlClass));
        assertTrue(choices.contains(other));
        assertFalse(choices.contains(userModel));
    }

    public void testClassifierChoicesIncludeUserModelAndProfiles() {
        List<?> choices = choices("classifier", umlClass);
        assertTrue(choices.contains(umlClass));
        int fromProfiles = 0;
        for (Object choice : choices) {
            assertTrue(Model.getFacade().isAClassifier(choice));
            if (!Model.getFacade().getRoot(choice).equals(userModel)) {
                fromProfiles++;
            }
        }
        assertTrue(fromProfiles > 0);
    }

    public void testChoicesUseTheInjectedProjectWithoutProjectManager() {
        assertTrue(ProjectManager.getManager().getOpenProjects().isEmpty());
        Object reception =
            Model.getCommonBehaviorFactory().buildReception(umlClass);
        assertTrue(choices("reception", umlClass).contains(reception));
        assertTrue(choices("residentElement", umlClass).contains(umlClass));
        assertTrue(choices("classifier", umlClass).contains(umlClass));
        assertTrue(ProjectManager.getManager().getOpenProjects().isEmpty());
    }

    public void testChoicesFailWithoutInjectedProject() {
        GetterSetterManager withoutProject =
            GetterSetterManager.getGetterSetter(Object.class, null);
        String[] properties = {"reception", "residentElement", "classifier"};
        for (String property : properties) {
            Command command = withoutProject.getAddCommand(property, umlClass);
            try {
                ((GetterSetterManagerImpl.AddModelElementCommand) command)
                        .getChoices();
                fail("NullPointerException esperada para " + property);
            } catch (NullPointerException e) {
                assertTrue(e.getStackTrace()[0].getClassName().startsWith(
                        GetterSetterManagerImpl.class.getName()));
            }
        }
    }
}
