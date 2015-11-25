package group12.ca.mcgill.sel.ram.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.eclipse.emf.common.util.EList;
import org.junit.BeforeClass;
import org.junit.Test;

import ca.mcgill.sel.commons.emf.util.AdapterFactoryRegistry;
import ca.mcgill.sel.commons.emf.util.ResourceManager;
import ca.mcgill.sel.ram.Aspect;
import ca.mcgill.sel.ram.Classifier;
import ca.mcgill.sel.ram.CombinedFragment;
import ca.mcgill.sel.ram.FragmentContainer;
import ca.mcgill.sel.ram.Interaction;
import ca.mcgill.sel.ram.LayoutElement;
import ca.mcgill.sel.ram.Lifeline;
import ca.mcgill.sel.ram.Message;
import ca.mcgill.sel.ram.MessageOccurrenceSpecification;
import ca.mcgill.sel.ram.MessageView;
import ca.mcgill.sel.ram.Operation;
import ca.mcgill.sel.ram.OperationType;
import ca.mcgill.sel.ram.RamFactory;
import ca.mcgill.sel.ram.RamPackage;
import ca.mcgill.sel.ram.Reference;
import ca.mcgill.sel.ram.TypedElement;
import ca.mcgill.sel.ram.controller.ControllerFactory;
import ca.mcgill.sel.ram.controller.MessageViewController;
import ca.mcgill.sel.ram.provider.RamItemProviderAdapterFactory;
import ca.mcgill.sel.ram.util.RAMModelUtil;
import ca.mcgill.sel.ram.util.RamResourceFactoryImpl;

/**
 * The following class contains unit tests for the class MessageViewController.
 * 
 * @author zzhang(Ze Qian Zhang),tantany(YaHan Yang)
 */
public class TestMessageViewController {
    private static MessageViewController messageViewController;
    private static String modelFolder = "../ca.mcgill.sel.ram.gui/models/MessageViewControllerTest/";

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

        // get the number of lifelines before creating new lifeline
        int count = owner.getLifelines().size();

        // set up typed element
        TypedElement represents = (TypedElement) aspect.getStructuralView()
                .getClasses().get(0)
                .getAssociationEnds().get(0);

        messageViewController.createLifeline(owner, represents, 10, 10);

        Lifeline newLifeline = owner.getLifelines().get(count);// get the lifeline that we just created
        LayoutElement lifelineLayout = aspect.getLayout().getContainers().get(messageView).get(newLifeline);

        assertEquals("Error creating lifeline", count + 1, owner.getLifelines().size());// number of lifelines should
                                                                                        // increase by one
        assertNotNull("New lifeline null", newLifeline);// new lifeline should not be null

        // test x and y coordinates of the new lifeline
        assertEquals("Incorrect x coordinate of the new lifeline", 10f, lifelineLayout.getX(), Float.MIN_VALUE);
        assertEquals("Incorrect y coordinate of the new lifeline", 10f, lifelineLayout.getY(), Float.MIN_VALUE);
    }

    /**
     * Test the simple path through the method moveLifeline.
     * Note: no if/else branch.
     */
    @Test
    public void testMoveLifeline() {
        Aspect aspect = (Aspect) ResourceManager.loadModel(modelFolder + "CreateLifelineWithMessage.ram");
        MessageView messageView = (MessageView) aspect.getMessageViews().get(0);

        // set up the owner and life line
        Interaction owner = messageView.getSpecification();
        Lifeline lifeline = owner.getLifelines().get(0);
        LayoutElement lifelineLayout = aspect.getLayout().getContainers().get(messageView).get(lifeline);

        float x = lifelineLayout.getX() - 10;// new x-coordinate of the lifeline
        float y = lifelineLayout.getY() - 10;// new y-coordinate of the lifeline

        messageViewController.moveLifeline(lifeline, x, y);

        // test new x and y coordinates of the the lifeline
        assertEquals("Incorrect x coordinate of the lifeline", x, lifelineLayout.getX(), Float.MIN_VALUE);
        assertEquals("Incorrect y coordinate of the lifeline", y, lifelineLayout.getY(), Float.MIN_VALUE);
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

        // set up container and signature
        FragmentContainer container = owner;
        Operation signature = aspect.getStructuralView()
                .getClasses().get(1)
                .getOperations().get(0);

        int addAtIndex = 1;

        // get number of messages before creating reply message
        int count = owner.getMessages().size();

        messageViewController.createReplyMessage(owner, lifelineFrom, lifelineTo, container, signature, addAtIndex);
        assertEquals("Error creating reply message", count + 1, owner.getMessages().size());// number of messages should
                                                                                            // increase by one
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

        // set up the message to be deleted
        Message message = owner.getMessages().get(1);
        MessageOccurrenceSpecification sendEvent = (MessageOccurrenceSpecification) message.getSendEvent();

        // get the number of messages before deleting
        int count = owner.getMessages().size();

        messageViewController.removeMessages(owner, container, sendEvent);
        assertEquals("Error removing message", count - 1, owner.getMessages().size());// number of messages should
                                                                                      // decrease by one

    }

    /**
     * Test the path 1:242-253-256-257-258-266 through the method createMessage.
     */
    @Test
    public void testCreateMessageTestCase1() {
        Aspect aspect = (Aspect) ResourceManager.loadModel(modelFolder + "CreateLifelineWithMessageV2.ram");
        MessageView messageView = RAMModelUtil.getMessageViewFor(aspect,
                aspect.getStructuralView().getClasses().get(0).getOperations().get(2));

        // set up the owner and life line
        Interaction owner = messageView.getSpecification();
        EList<Lifeline> lifelines = owner.getLifelines();

        // get the number of lifelines before creating new lifeline
        int count = owner.getMessages().size();

        Lifeline lifelineFrom = lifelines.get(0);
        Lifeline lifelineTo = lifelines.get(1);

        // set container != owner
        CombinedFragment combinedFragment = (CombinedFragment) owner.getFragments().get(5);
        FragmentContainer container = combinedFragment.getOperands().get(0);
        Operation signature = aspect.getStructuralView()
                .getClasses().get(1)
                .getOperations().get(0);
        int addAtIndex = 0;

        // make sure the condition !lifelineTo.getCoveredBy().contains(combinedFragment) is satisfied
        assertEquals(false, lifelineTo.getCoveredBy().contains(combinedFragment));

        messageViewController.createMessage(owner, lifelineFrom, lifelineTo, container, signature, addAtIndex);
        assertEquals(count + 1, owner.getMessages().size());// number of messages should increase by one
    }

    /**
     * Test the path 2:242-253-256-257-266 through the method createMessage.
     */
    @Test
    public void testCreateMessageTestCase2() {
        Aspect aspect = (Aspect) ResourceManager.loadModel(modelFolder + "CreateLifelineWithMessageV2.ram");
        MessageView messageView = RAMModelUtil.getMessageViewFor(aspect,
                aspect.getStructuralView().getClasses().get(0).getOperations().get(2));

        // set up the owner and life line
        Interaction owner = messageView.getSpecification();
        EList<Lifeline> lifelines = owner.getLifelines();
        Lifeline lifelineFrom = lifelines.get(0);
        Lifeline lifelineTo = lifelineFrom;

        // get the number of lifelines before creating new lifeline
        int count = owner.getMessages().size();

        // set container != owner
        CombinedFragment combinedFragment = (CombinedFragment) owner.getFragments().get(5);
        FragmentContainer container = combinedFragment.getOperands().get(0);
        Operation signature = aspect.getStructuralView()
                .getClasses().get(1)
                .getOperations().get(0);
        int addAtIndex = 0;

        // make sure the condition !lifelineTo.getCoveredBy().contains(combinedFragment) is not satisfied
        assertEquals(true, lifelineTo.getCoveredBy().contains(combinedFragment));

        messageViewController.createMessage(owner, lifelineFrom, lifelineTo, container, signature, addAtIndex);
        assertEquals(count + 1, owner.getMessages().size());// number of messages should increase by one
    }

    /**
     * Test the path 3: 242-253-266 through the method createMessage.
     */
    @Test
    public void testCreateMessageTestCase3() {
        Aspect aspect = (Aspect) ResourceManager.loadModel(modelFolder + "CreateLifelineWithMessage.ram");
        MessageView messageView = (MessageView) aspect.getMessageViews().get(0);

        // set up the owner and life line
        Interaction owner = messageView.getSpecification();
        EList<Lifeline> lifelines = owner.getLifelines();

        // get the number of lifelines before creating new lifeline
        int count = owner.getMessages().size();

        Lifeline lifelineFrom = lifelines.get(0);
        Lifeline lifelineTo = lifelines.get(1);

        // set container = owner
        FragmentContainer container = owner;
        Operation signature = aspect.getStructuralView()
                .getClasses().get(1)
                .getOperations().get(0);
        int addAtIndex = 0;

        messageViewController.createMessage(owner, lifelineFrom, lifelineTo, container, signature, addAtIndex);
        assertEquals(count + 1, owner.getMessages().size());// number of messages should increase by one
    }

    /**
     * Test the path 1: 95-101-102-112-113-121-133-134-159-162-163-164-172 through the method createMessageWithMessage.
     */
    @Test
    public void testCreateLifelineWithMessageTestCase1() {
        Aspect aspect = (Aspect) ResourceManager.loadModel(modelFolder + "CreateLifelineWithMessageV2.ram");
        Classifier class3 = aspect.getStructuralView().getClasses().get(2);

        MessageView messageView = RAMModelUtil.getMessageViewFor(aspect,
                aspect.getStructuralView().getClasses().get(0).getOperations().get(3));
        Reference class3ReferenceType = RamFactory.eINSTANCE.createReference();
        class3ReferenceType.setType(class3);

        // set up the owner and life line
        int addAtIndex = 0;
        Interaction owner = messageView.getSpecification();
        EList<Lifeline> lifelines = owner.getLifelines();
        Lifeline lifelineFrom = lifelines.get(0);

        // get number of lifelines and messages before creating
        int lifelineCount = owner.getLifelines().size();
        int messageCount = owner.getMessages().size();

        // set container != owner
        CombinedFragment combinedFragment = (CombinedFragment) owner.getFragments().get(2);
        FragmentContainer container = combinedFragment.getOperands().get(0);

        Operation signature = aspect.getStructuralView()
                .getClasses().get(1)
                .getOperations().get(0);

        // set signature.getOperationType() == OperationType.NORMAL
        signature.setOperationType(OperationType.NORMAL);

        TypedElement represents = (TypedElement) aspect.getStructuralView()
                .getClasses().get(0)
                .getAssociationEnds().get(0);

        assertEquals(OperationType.NORMAL, signature.getOperationType());// operation type should be NORMAL

        messageViewController.createLifelineWithMessage(owner, represents, 10, 10, lifelineFrom, container, signature,
                addAtIndex);

        Lifeline newLifeline = lifelines.get(lifelineCount);// get the newly created lifeline
        LayoutElement lifelineLayout = aspect.getLayout().getContainers().get(messageView).get(newLifeline);

        assertNotNull("New lifeline is null", newLifeline);// new lifeline should not be null
        assertEquals("Error creating new lifeline", lifelineCount + 1, owner.getLifelines().size());// number of
                                                                                                    // lifelines should
                                                                                                    // increase by one
        assertEquals("error creating new message", messageCount + 1, owner.getMessages().size());// number of messages
                                                                                                 // should increase by
                                                                                                 // one

        // test x and y coordinates of the the lifeline
        assertEquals("Incorrect x coordinate of new lifeline", 10f, lifelineLayout.getX(), Float.MIN_VALUE);
        assertEquals("Incorrect y coordinate of new lifeline", 10f, lifelineLayout.getY(), Float.MIN_VALUE);
    }

    /**
     * Test the path 2: 95-101-104-112-121-133-137-159-162-163-172 through the method createMessageWithMessage.
     */
    @Test
    public void testCreateLifelineWithMessageTestCase2() {
        Aspect aspect = (Aspect) ResourceManager.loadModel(modelFolder + "CreateLifelineWithMessageV2.ram");
        Classifier class3 = aspect.getStructuralView().getClasses().get(2);
        MessageView messageView = RAMModelUtil.getMessageViewFor(aspect,
                aspect.getStructuralView().getClasses().get(0).getOperations().get(3));
        Reference class3ReferenceType = RamFactory.eINSTANCE.createReference();
        class3ReferenceType.setType(class3);

        // set up the owner and life line
        int addAtIndex = 1;
        Interaction owner = messageView.getSpecification();
        EList<Lifeline> lifelines = owner.getLifelines();
        Lifeline lifelineFrom = lifelines.get(0);

        // get number of lifelines and messages before creating
        int lifelineCount = owner.getLifelines().size();
        int messageCount = owner.getMessages().size();

        // set container != owner
        CombinedFragment combinedFragment = (CombinedFragment) owner.getFragments().get(2);
        FragmentContainer container = combinedFragment.getOperands().get(0);
        Operation signature = aspect.getStructuralView()
                .getClasses().get(1)
                .getOperations().get(0);

        TypedElement represents = (TypedElement) aspect.getStructuralView()
                .getClasses().get(0)
                .getAssociationEnds().get(0);

        messageViewController.createLifelineWithMessage(owner, represents, 10, 10, lifelineFrom, container, signature,
                addAtIndex);

        Lifeline newLifeline = lifelines.get(lifelineCount);// get the newly created lifeline
        LayoutElement lifelineLayout = aspect.getLayout().getContainers().get(messageView).get(newLifeline);

        assertNotNull("New lifeline is null", newLifeline);// new lifeline should not be null
        assertEquals("Error creating new lifeline", lifelineCount + 1, owner.getLifelines().size());// number of
                                                                                                    // lifelines should
                                                                                                    // increase by one
        assertEquals("error creating new message", messageCount + 1, owner.getMessages().size());// number of messages
                                                                                                 // should increase by
                                                                                                 // one

        // test x and y coordinates of the the lifeline
        assertEquals("Incorrect x coordinate of new lifeline", 10f, lifelineLayout.getX(), Float.MIN_VALUE);
        assertEquals("Incorrect y coordinate of new lifeline", 10f, lifelineLayout.getY(), Float.MIN_VALUE);

    }

    /**
     * Test the path 3: 95-101-104-112-159-172 through the method createMessageWithMessage.
     */
    @Test
    public void testCreateLifelineWithMessageTestCase3() {
        Aspect aspect = (Aspect) ResourceManager.loadModel(modelFolder + "CreateLifelineWithMessageV2.ram");
        MessageView messageView = RAMModelUtil.getMessageViewFor(aspect,
                aspect.getStructuralView().getClasses().get(0).getOperations().get(4));

        // set up the owner and life line
        int addAtIndex = 1;
        Interaction owner = messageView.getSpecification();
        EList<Lifeline> lifelines = owner.getLifelines();
        Lifeline lifelineFrom = lifelines.get(0);

        // get number of lifelines and messages before creating
        int lifelineCount = owner.getLifelines().size();
        int messageCount = owner.getMessages().size();

        TypedElement represents = (TypedElement) aspect.getStructuralView()
                .getClasses().get(0)
                .getAssociationEnds().get(0);

        // set container = owner
        FragmentContainer container = owner;

        Operation signature = aspect.getStructuralView()
                .getClasses().get(0)
                .getOperations().get(0);

        messageViewController.createLifelineWithMessage(owner, represents, 10, 10, lifelineFrom, container, signature,
                addAtIndex);

        Lifeline newLifeline = lifelines.get(lifelineCount);// get the newly created lifeline
        LayoutElement lifelineLayout = aspect.getLayout().getContainers().get(messageView).get(newLifeline);

        assertNotNull("New lifeline is null", newLifeline);// new lifeline should not be null
        assertEquals("Error creating new lifeline", lifelineCount + 1, owner.getLifelines().size());// number of
                                                                                                    // lifelines should
                                                                                                    // increase by one
        assertEquals("error creating new message", messageCount + 1, owner.getMessages().size());// number of messages
                                                                                                 // should increase by
                                                                                                 // one

        // test x and y coordinates of the the lifeline
        assertEquals("Incorrect x coordinate of new lifeline", 10f, lifelineLayout.getX(), Float.MIN_VALUE);
        assertEquals("Incorrect y coordinate of new lifeline", 10f, lifelineLayout.getY(), Float.MIN_VALUE);
    }

}
