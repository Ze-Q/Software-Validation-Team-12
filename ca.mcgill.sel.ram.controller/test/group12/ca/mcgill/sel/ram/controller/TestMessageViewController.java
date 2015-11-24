package group12.ca.mcgill.sel.ram.controller;

import org.junit.Test;
import static org.junit.Assert.*;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.ecore.EObject;
import org.junit.BeforeClass;

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
import ca.mcgill.sel.ram.RamPackage;
import ca.mcgill.sel.ram.TypedElement;
import ca.mcgill.sel.ram.controller.ControllerFactory;
import ca.mcgill.sel.ram.controller.MessageViewController;
import ca.mcgill.sel.ram.impl.MessageOccurrenceSpecificationImpl;
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
        int count = owner.getLifelines().size();

        TypedElement represents = (TypedElement) aspect.getStructuralView()
                .getClasses().get(0)
                .getAssociationEnds().get(0);
        
        messageViewController.createLifeline(owner, represents, 10, 10);
        assertEquals("Error creating lifeline",count+1,owner.getLifelines().size());//number of lifelines should increase by one
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
        
        int count = owner.getMessages().size();
        messageViewController.createReplyMessage(owner, lifelineFrom, lifelineTo, container, signature, addAtIndex);
        assertEquals("Error creating reply message", count+1,owner.getMessages().size());//number of messages should increase by one
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

        Message message = owner.getMessages().get(1);
        MessageOccurrenceSpecification sendEvent = (MessageOccurrenceSpecification) message.getSendEvent();
        
        int count = owner.getMessages().size();
        messageViewController.removeMessages(owner, container, sendEvent);
        assertEquals("Error removing message", count-1,owner.getMessages().size());//number of messages should decrease by one
        
    }
   
    /**
     * Test the path 1:241-253-256-257-258-266 through the method createMessage.
     */
    @Test
    public void testCreateMessageTestCase1() {
    	Aspect aspect = (Aspect) ResourceManager.loadModel(modelFolder + "CreateLifelineWithMessageV2.ram");
    	Classifier class1 = aspect.getStructuralView().getClasses().get(0);
        Operation foo2 = class1.getOperations().get(2);
 		MessageView messageView = RAMModelUtil.getMessageViewFor(aspect,foo2);

        //MessageView messageView = (MessageView) aspect.getMessageViews().get(1);
        
        // set up the owner and life line
        Interaction owner = messageView.getSpecification();
        EList<Lifeline> lifelines = owner.getLifelines();
        Lifeline lifelineFrom = lifelines.get(0);
        Lifeline lifelineTo = lifelines.get(1);
        CombinedFragment combinedFragment = (CombinedFragment) owner.getFragments().get(5);
        FragmentContainer container = combinedFragment.getOperands().get(0);
        Operation signature = aspect.getStructuralView()
                .getClasses().get(1)
                .getOperations().get(0);
        int addAtIndex = 0;
        
        int count = owner.getMessages().size();
        messageViewController.createMessage(owner, lifelineFrom, lifelineTo, container, signature, addAtIndex);
        assertEquals(false,lifelineTo.getCoveredBy().contains(combinedFragment));//number of messages should decrease by one
        assertEquals(count+1,owner.getMessages().size());//number of messages should increase by one
    }
    
    /**
     * Test the path 2:241-253-256-257-266 through the method createMessage.
     */
    @Test
    public void testCreateMessageTestCase2() {
    	Aspect aspect = (Aspect) ResourceManager.loadModel(modelFolder + "CreateLifelineWithMessageV2.ram");
        Classifier class1 = aspect.getStructuralView().getClasses().get(0);
        Operation foo2 = class1.getOperations().get(2);
		MessageView messageView = RAMModelUtil.getMessageViewFor(aspect,foo2);

        //MessageView messageView = (MessageView) aspect.getMessageViews().get(1);
        
        // set up the owner and life line
        Interaction owner = messageView.getSpecification();
        EList<Lifeline> lifelines = owner.getLifelines();
        Lifeline lifelineFrom = lifelines.get(0);
        Lifeline lifelineTo = lifelines.get(1);
        CombinedFragment combinedFragment = (CombinedFragment) owner.getFragments().get(5);
        FragmentContainer container = combinedFragment.getOperands().get(0);
        Operation signature = aspect.getStructuralView()
                .getClasses().get(1)
                .getOperations().get(0);
        int addAtIndex = 0;
        
        int count = owner.getMessages().size();
        messageViewController.createMessage(owner, lifelineFrom, lifelineTo, container, signature, addAtIndex);
        assertEquals(true,lifelineTo.getCoveredBy().contains(combinedFragment));//number of messages should decrease by one
        assertEquals(count+1,owner.getMessages().size());//number of messages should increase by one
    }
    
    /**
     * Test the path 3: 241-253-266 through the method createMessage.
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
        
        int count = owner.getMessages().size();
        messageViewController.createMessage(owner, lifelineFrom, lifelineTo, container, signature, addAtIndex);
        assertEquals(count+1,owner.getMessages().size());//number of messages should increase by one
    }
    
    /**
     * Test the path 1: 95-101-103-112-121-133-134-159-162-163-164-172 through the method createMessageWithMessage.
     */
    @Test
    public void testCreateLifelineWithMessageTestCase1() {
    	Aspect aspect = (Aspect) ResourceManager.loadModel(modelFolder + "CreateLifelineWithMessageV2.ram");
        Classifier class1 = aspect.getStructuralView().getClasses().get(0);
        Operation foo2 = class1.getOperations().get(2);
		MessageView messageView = RAMModelUtil.getMessageViewFor(aspect,foo2);
        
        // set up the owner and life line
        int addAtIndex = 0;
        Interaction owner = messageView.getSpecification();
        EList<Lifeline> lifelines = owner.getLifelines();
        Lifeline lifelineFrom = lifelines.get(0);
        Lifeline lifelineTo = lifelines.get(1);
        CombinedFragment combinedFragment = (CombinedFragment) owner.getFragments().get(5);
        FragmentContainer container = combinedFragment.getOperands().get(0);
        Operation signature = aspect.getStructuralView()
                .getClasses().get(1)
                .getOperations().get(0);  
        signature.setOperationType(OperationType.NORMAL);
        TypedElement represents = (TypedElement) aspect.getStructuralView()
                .getClasses().get(0)
                .getAssociationEnds().get(0);
              
        int count = owner.getLifelines().size();
        
        messageViewController.createLifelineWithMessage(owner,represents,10,10,lifelineFrom,container,signature,addAtIndex);
        
        Lifeline newLifeline = lifelines.get(1);
        String className = newLifeline.getRepresents().getName();

        //test lifeline class name
        assertNotNull("Error creating lifeline", newLifeline);
        assertEquals("Error creating lifeline",count+1,owner.getLifelines().size());//number of lifelines should increase by one
        assertEquals(OperationType.NORMAL,signature.getOperationType());//number of messages should decrease by one
        assertEquals(false,lifelineTo.getCoveredBy().contains(combinedFragment));//number of messages should decrease by one
        assertNull("Error creating lifeline", represents.eContainer());
        assertNull(RAMModelUtil.findInitialMessage(container.getFragments().get(addAtIndex)));
        
    }
    
    /**
     * Test the path 3: 95-101-103-112-159-172 through the method createMessageWithMessage.
     */
    @Test
    public void testCreateLifelineWithMessageTestCase3() {
        Aspect aspect = (Aspect) ResourceManager.loadModel(modelFolder + "CreateLifelineWithMessageV4.ram");
        MessageView messageView = (MessageView) aspect.getMessageViews().get(0);
        
        // set up the owner and life line
        int addAtIndex = 1;
        Interaction owner = messageView.getSpecification();
        EList<Lifeline> lifelines = owner.getLifelines();
        Lifeline lifelineFrom = lifelines.get(0);
        TypedElement represents = (TypedElement) aspect.getStructuralView()
                .getClasses().get(0)
                .getAssociationEnds().get(0);
        
        FragmentContainer container = owner;
        
        Operation signature = aspect.getStructuralView()
                .getClasses().get(0)
                .getOperations().get(0);
        //signature.setOperationType(OperationType.CONSTRUCTOR);
        int count = owner.getLifelines().size();
        
        messageViewController.createLifelineWithMessage(owner,represents,10,10,lifelineFrom,container,signature,addAtIndex);
        
        Lifeline newLifeline = lifelines.get(1);
        String className = newLifeline.getRepresents().getName();

        //test lifeline class name
        assertNotNull("Error creating lifeline", newLifeline);
        assertEquals("Error Lifeline class name", "myClass2", className);
        assertEquals("Error creating lifeline",count+1,owner.getLifelines().size());//number of lifelines should increase by one
        assertEquals("Error creating lifeline",0,container.getFragments().size());
        
    }
    
    
}
