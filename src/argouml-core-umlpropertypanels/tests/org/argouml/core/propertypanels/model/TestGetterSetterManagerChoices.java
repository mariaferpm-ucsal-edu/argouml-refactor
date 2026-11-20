package org.argouml.core.propertypanels.model;

import java.util.List;

import junit.framework.TestCase;

import org.argouml.core.propertypanels.ModelTestSupport;
import org.argouml.kernel.Command;
import org.argouml.kernel.Project;
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
        project = ProjectManager.getManager().makeEmptyProject();
        userModel = project.getUserDefinedModelList().get(0);
        umlClass = Model.getCoreFactory().buildClass("Alvo", userModel);
        manager = GetterSetterManager.getGetterSetter(Object.class);
    }

    @Override
    protected void tearDown() throws Exception {
        if (project != null) {
            ProjectManager.getManager().removeProject(project);
        }
        super.tearDown();
    }

    private List<?> choices(String propertyName, Object target) {
        Command command = manager.getAddCommand(propertyName, target);
        return ((GetterSetterManagerImpl.AddModelElementCommand) command)
                .getChoices();
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

    public void testChoicesFailWithoutCurrentProject() {
        ProjectManager.getManager().removeProject(project);
        project = null;
        String[] properties = {"reception", "residentElement", "classifier"};
        for (String property : properties) {
            try {
                choices(property, umlClass);
                fail("NullPointerException esperada para " + property);
            } catch (NullPointerException e) {
                assertTrue(e.getStackTrace()[0].getClassName().startsWith(
                        GetterSetterManagerImpl.class.getName()));
            }
        }
    }
}
