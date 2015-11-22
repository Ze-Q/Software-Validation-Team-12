package group12.ca.mcgill.sel.ram.controller;

import org.junit.Test;
import static org.junit.Assert.*;

import org.eclipse.emf.common.util.EList;
import org.junit.BeforeClass;

import ca.mcgill.sel.commons.emf.util.AdapterFactoryRegistry;
import ca.mcgill.sel.commons.emf.util.ResourceManager;
import ca.mcgill.sel.ram.Aspect;
import ca.mcgill.sel.ram.FragmentContainer;
import ca.mcgill.sel.ram.Interaction;
import ca.mcgill.sel.ram.Lifeline;
import ca.mcgill.sel.ram.Message;
import ca.mcgill.sel.ram.MessageOccurrenceSpecification;
import ca.mcgill.sel.ram.MessageView;
import ca.mcgill.sel.ram.Operation;
import ca.mcgill.sel.ram.RamPackage;
import ca.mcgill.sel.ram.TypedElement;
import ca.mcgill.sel.ram.controller.ControllerFactory;
import ca.mcgill.sel.ram.controller.MessageViewController;
import ca.mcgill.sel.ram.impl.MessageOccurrenceSpecificationImpl;
import ca.mcgill.sel.ram.provider.RamItemProviderAdapterFactory;
import ca.mcgill.sel.ram.util.RamResourceFactoryImpl;

/**
 * The following class contains unit tests for the class MessageViewController.
 * 
 * @author zzhang
 */
public class TestMessageViewController {
    private static MessageViewController messageViewController;
    private static String modelFolder = "../ca.mcgill.sel.ram.gui/models/MessageViewControllerTest/";
    // private static String modelBaseFolder = "../ca.mcgill.sel.ram.gui/models/demos/Bank/";

    /**
     * Before starting the tests, we must load all the required classes.
     * We gain access to the class under test by using ControllerFactory.
     */
    @BeforeClass
    public static void setUpSuite() {
        // Initialize ResourceManager.
        ResourceManager.initialize();
        // Initialize packages.
        RamPackage.eINSTANCE.eClass();
        // Register resource factories.
        ResourceManager.registerExtensionFactory("ram", new RamResourceFactoryImpl());
        // Initialize adapter factories.
        AdapterFactoryRegistry.INSTANCE.addAdapterFactory(RamItemProviderAdapterFactory.class);

        messageViewController = ControllerFactory.INSTANCE.getMessageViewController();
    }

    /**
     * Test the simple path through the method createLifeline.
     * Note: no if/else branch.
     */
    @Test
    public void testCreateLifeline() {
        Aspect aspect = (Aspect) ResourceManager.loadModel(modelFolder + "CreateLifelineWithMessage.ram");
        MessageView messageView = (MessageView) aspect.getMessageViews().get(0);

        // set up the owner and life line
        Interaction owner = messageView.getSpecification();
        Lifeline startingLifeline = owner.getLifelines().get(0);

        TypedElement represents = (TypedElement) aspect.getStructuralView()
                .getClasses().get(0)
                .getAssociationEnds().get(0);

        messageViewController.createLifeline(owner, represents, 10, 10);
    }

    /**
     * Test the simple path through the method moveLifeline.
     * Note: no if/else branch.
     */
    @Test
    public void testMoveLifelineCommand() {
        Aspect aspect = (Aspect) ResourceManager.loadModel(modelFolder + "CreateLifelineWithMessage.ram");
        MessageView messageView = (MessageView) aspect.getMessageViews().get(0);

        // set up the owner and life line
        Interaction owner = messageView.getSpecification();
        Lifeline lifeline = owner.getLifelines().get(0);

        messageViewController.moveLifeline(lifeline, 10, 10);
    }
    
    /**
     * Test the simple path through the method createReplyMessage.
     * Note: no if/else branch.
     */
    @Test
    public void testCreateReplyMessage() {
        Aspect aspect = (Aspect) ResourceManager.loadModel(modelFolder + "CreateLifelineWithMessage.ram");
        MessageView messageView = (MessageView) aspect.getMessageViews().get(0);

        // set up the owner and life line
        Interaction owner = messageView.getSpecification();
        EList<Lifeline> lifelines = owner.getLifelines();
        Lifeline lifelineFrom = lifelines.get(0);
        Lifeline lifelineTo = lifelines.get(1);
        
        FragmentContainer container = owner;
        Operation signature = aspect.getStructuralView()
                .getClasses().get(1)
                .getOperations().get(0);
        int addAtIndex = 1;

        messageViewController.createReplyMessage(owner, lifelineFrom, lifelineTo, container, signature, addAtIndex);
    }
    
    /**
     * Test the simple path through the method removeMessages.
     * Note: no if/else branch.
     */
    @Test
    public void testRemoveMessages() {
        Aspect aspect = (Aspect) ResourceManager.loadModel(modelFolder + "CreateLifelineWithMessage.ram");
        MessageView messageView = (MessageView) aspect.getMessageViews().get(0);

        // set up the owner and life line
        Interaction owner = messageView.getSpecification();
        FragmentContainer container = owner;

        Message message = owner.getMessages().get(2);
        MessageOccurrenceSpecification sendEvent = (MessageOccurrenceSpecification) message.getSendEvent();

        messageViewController.removeMessages(owner, container, sendEvent);
    }
   
    
    /**
     * Test the path 3 (refer to report for details) through the method createMessage.
     */
    @Test
    public void testCreateMessageTestCase3() {
        Aspect aspect = (Aspect) ResourceManager.loadModel(modelFolder + "CreateLifelineWithMessage.ram");
        MessageView messageView = (MessageView) aspect.getMessageViews().get(0);

        // set up the owner and life line
        Interaction owner = messageView.getSpecification();
        EList<Lifeline> lifelines = owner.getLifelines();
        Lifeline lifelineFrom = lifelines.get(0);
        Lifeline lifelineTo = lifelines.get(1);
        FragmentContainer container = owner;
        Operation signature = aspect.getStructuralView()
                .getClasses().get(1)
                .getOperations().get(0);
        int addAtIndex = 0;

        messageViewController.createMessage(owner, lifelineFrom, lifelineTo, container, signature, addAtIndex);
    }
}
