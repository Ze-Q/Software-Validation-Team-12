package group12.ca.mcgill.sel.ram.ui.views.message.handler.impl;

import static org.junit.Assert.*;

import java.awt.event.MouseEvent;

import org.jodah.concurrentunit.Waiter;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mt4j.components.MTComponent;
import org.mt4j.input.inputProcessors.componentProcessors.tapAndHoldProcessor.TapAndHoldEvent;
import org.mt4j.sceneManagement.ISceneChangeListener;
import org.mt4j.sceneManagement.SceneChangeEvent;
import org.mt4j.util.math.Vector3D;

import ca.mcgill.sel.commons.emf.util.AdapterFactoryRegistry;
import ca.mcgill.sel.commons.emf.util.ResourceManager;
import ca.mcgill.sel.ram.Aspect;
import ca.mcgill.sel.ram.Classifier;
import ca.mcgill.sel.ram.Interaction;
import ca.mcgill.sel.ram.Lifeline;
import ca.mcgill.sel.ram.Message;
import ca.mcgill.sel.ram.MessageView;
import ca.mcgill.sel.ram.Operation;
import ca.mcgill.sel.ram.RamFactory;
import ca.mcgill.sel.ram.RamPackage;
import ca.mcgill.sel.ram.provider.RamItemProviderAdapterFactory;
import ca.mcgill.sel.ram.ui.RamApp;
import ca.mcgill.sel.ram.ui.events.listeners.ITapAndHoldListener;
import ca.mcgill.sel.ram.ui.scenes.DisplayAspectScene;
import ca.mcgill.sel.ram.ui.views.message.LifelineView;
import ca.mcgill.sel.ram.ui.views.message.MessageCallView;
import ca.mcgill.sel.ram.ui.views.message.MessageViewView;
import ca.mcgill.sel.ram.ui.views.message.handler.MessageViewHandlerFactory;
import ca.mcgill.sel.ram.ui.views.message.handler.impl.MessageHandler;
import ca.mcgill.sel.ram.util.RAMModelUtil;
import ca.mcgill.sel.ram.util.RamResourceFactoryImpl;


public class TestMessageHandler extends MessageHandler {
	
	private static ITapAndHoldListener tapAndHolderListner;
    private static Waiter waiter = new Waiter();
    private static boolean shouldContinue;
    private static Aspect aspect;
    private static String aspectLocation = "../ca.mcgill.sel.ram.gui/models/MessageHandlerTest/M1.ram";

    /*
     * Resume test case
     */
    private static void resumeTest() {
    	synchronized (waiter) {
            waiter.notify();
            shouldContinue = true;
        }
    }
    
    /*
     * Pause test case
     */
    private static void pauseTest() {
    	synchronized (waiter) {
            while (!shouldContinue) {
                try {
					waiter.wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
            shouldContinue = false;
        }
    }
    
	@BeforeClass
	// sample code provided by TA to allow UI to generated before running the test cases
    public static void setUpBeforeClass() throws Exception { 
        // Initialize ResourceManager.
        ResourceManager.initialize();
        // Initialize packages.
        RamPackage.eINSTANCE.eClass();
        
        // Register resource factories.
        ResourceManager.registerExtensionFactory("ram", new RamResourceFactoryImpl());
    
        // Initialize adapter factories.
        AdapterFactoryRegistry.INSTANCE.addAdapterFactory(RamItemProviderAdapterFactory.class);
        
        RamApp.initialize(new Runnable() {
            @Override
            public void run() {
                resumeTest();
            }
        });
        pauseTest();
        tapAndHolderListner = MessageViewHandlerFactory.INSTANCE.getMessageHandler();
    }
	
	@Before
    public void setUp() throws Exception {
        // Close current aspect.
        if (aspect != null) {
            RamApp.getApplication().invokeLater(new Runnable() {
                @Override
                public void run() {
                    RamApp.getApplication().closeAspectScene(RamApp.getActiveAspectScene());                    
                }
            });
        }
        
        // Load model to use in test.
        aspect = (Aspect) ResourceManager.loadModel(aspectLocation);
        RamApp.getApplication().addSceneChangeListener(new ISceneChangeListener() {

            @Override
            public void processSceneChangeEvent(SceneChangeEvent sc) {
                if (sc.getNewScene() instanceof DisplayAspectScene) {
                    RamApp.getApplication().removeSceneChangeListener(this);
                    resumeTest();
                }
            }
        });
        RamApp.getApplication().loadAspect(aspect);
        pauseTest();
    }
	
	@Test
    /**
     * Case: 1
     * Method:processTapAndHoldEvent
     * Path: first-61-62-63-64-...-74-88-92
     * Variables:
	 * tapAndHoldEvent.isHoldComplete()=true
	 * shouldProcessTapAndHold(messageCallView.getMessage()) = true
     */
	public void testMessageHandlerPath01() {
		
		// initiate required variables to make two conditions to true
		RamApp app = RamApp.getApplication();
        Classifier someClass = aspect.getStructuralView().getClasses().get(0);
        Operation doSomething = someClass.getOperations().get(0);
        final MessageView messageView = RAMModelUtil.getMessageViewFor(aspect, doSomething);        
        Interaction interaction = messageView.getSpecification();
        final Message message = interaction.getMessages().get(2);
        
        Lifeline lifelineFrom = interaction.getLifelines().get(0);
        Lifeline lifelineTo = interaction.getLifelines().get(1);
        
        final DisplayAspectScene aspectScene = (DisplayAspectScene) app.getCurrentScene();
        
        app.invokeLater(new Runnable() {
            @Override
            public void run() {
            	// change scene
                aspectScene.showMessageView(messageView);
                resumeTest();
            }
        });
        pauseTest();
        
        MessageViewView messageViewView = (MessageViewView) aspectScene.getCurrentView();
        final LifelineView from = messageViewView.getLifelineView(lifelineFrom);
        final LifelineView to = messageViewView.getLifelineView(lifelineTo);
       
        MTComponent containerParent = aspectScene.getContainerLayer().getParent();
        int previousChildCount = containerParent.getChildCount();
        
        // tap and hold
        app.invokeLater(new Runnable() {
            @Override
            public void run() {
                MessageCallView messageCallView = new MessageCallView(message, from, to);
                TapAndHoldEvent evt = new TapAndHoldEvent(null, 0, messageCallView, null, true, new Vector3D(0, 0), 0, 0, 0);
                tapAndHolderListner.processTapAndHoldEvent(evt);
                resumeTest();
            }
        });
        pauseTest();
        
        assertTrue(containerParent.getChildCount()-previousChildCount==1 );

        // click
        int x = 80;
        int y = 60;
        int previousMessageCount = interaction.getMessages().size();
        
        app.dispatchEvent(new MouseEvent(app, MouseEvent.MOUSE_PRESSED, 0, 
                MouseEvent.BUTTON1_MASK, x, y, x, y, 1, false, MouseEvent.BUTTON1));
        app.dispatchEvent(new MouseEvent(app, MouseEvent.MOUSE_RELEASED, 0, 
                MouseEvent.BUTTON1_MASK, x, y, x, y, 1, false, MouseEvent.BUTTON1));
        
        app.invokeLater(new Runnable() {
            @Override
            public void run() {
                resumeTest();
            }
        });
        
        // waiting for the click event
        pauseTest();
        
        // The controller should have removed the message
        assertEquals(previousMessageCount - 1, interaction.getMessages().size());
	}
	
	@Test
    /**
     * Case: 2
     * Method:processTapAndHoldEvent
     * Path: first-61-62-63-92
     * Variables:
	 * tapAndHoldEvent.isHoldComplete()=true
	 * shouldProcessTapAndHold(messageCallView.getMessage()) = false
     */
	public void testMessageHandlerPath02() {
		/*
		 * variables required to create a MessageCallView that will make shouldProcessTapAndHold 
		 * return false
		 */
		final Message message = RamFactory.eINSTANCE.createMessage();
        message.setSendEvent(RamFactory.eINSTANCE.createGate());
        
        // before we call the processTapAndHoldEvent
        MTComponent containerParent = RamApp.getActiveAspectScene().getContainerLayer().getParent();
        int previousChildCount = containerParent.getChildCount();
        
        RamApp.getApplication().invokeLater(new Runnable() {
            @Override
            public void run() {
                MessageCallView messageCallView = new MessageCallView(message, null, null);
                TapAndHoldEvent evt = new TapAndHoldEvent(null, 0, messageCallView, null, true, null, 0, 0, 0);
                tapAndHolderListner.processTapAndHoldEvent(evt);
                resumeTest();
            }
            
        });
        pauseTest();
        
        // Child count should not change
        assertTrue(previousChildCount-containerParent.getChildCount()==0);
	}
	
	
	@Test
    /**
     * Case: 3
     * Method:processTapAndHoldEvent
     * Path: first-61-92
     * Variables:
	 * tapAndHoldEvent.isHoldComplete() = false
     */
	public void testMessageHandlerPath03() {
		TapAndHoldEvent evt = new TapAndHoldEvent(null, 0, null, null, false, null, 0, 0, 0);
        MTComponent containerParent = RamApp.getActiveAspectScene().getContainerLayer().getParent();
        int previousChildCount = containerParent.getChildCount();

        tapAndHolderListner.processTapAndHoldEvent(evt);

        // Child count should not change
        assertTrue(previousChildCount-containerParent.getChildCount()==0);
	}
}
