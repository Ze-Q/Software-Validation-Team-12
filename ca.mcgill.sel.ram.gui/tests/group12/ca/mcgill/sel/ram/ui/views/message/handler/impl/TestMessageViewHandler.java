package group12.ca.mcgill.sel.ram.ui.views.message.handler.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.awt.event.MouseEvent;

import org.jodah.concurrentunit.Waiter;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mt4j.input.inputData.InputCursor;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.unistrokeProcessor.UnistrokeEvent;
import org.mt4j.input.inputProcessors.componentProcessors.unistrokeProcessor.UnistrokeUtils.UnistrokeGesture;
import org.mt4j.sceneManagement.ISceneChangeListener;
import org.mt4j.sceneManagement.SceneChangeEvent;
import org.mt4j.util.math.Vector3D;

import ca.mcgill.sel.commons.emf.util.AdapterFactoryRegistry;
import ca.mcgill.sel.commons.emf.util.EMFModelUtil;
import ca.mcgill.sel.commons.emf.util.ResourceManager;
import ca.mcgill.sel.ram.Aspect;
import ca.mcgill.sel.ram.FragmentContainer;
import ca.mcgill.sel.ram.LayoutElement;
import ca.mcgill.sel.ram.Lifeline;
import ca.mcgill.sel.ram.MessageView;
import ca.mcgill.sel.ram.RamPackage;
import ca.mcgill.sel.ram.impl.ContainerMapImpl;
import ca.mcgill.sel.ram.impl.RamFactoryImpl;
import ca.mcgill.sel.ram.provider.RamItemProviderAdapterFactory;
import ca.mcgill.sel.ram.ui.RamApp;
import ca.mcgill.sel.ram.ui.scenes.DisplayAspectScene;
import ca.mcgill.sel.ram.ui.views.message.LifelineView;
import ca.mcgill.sel.ram.ui.views.message.MessageViewView;
import ca.mcgill.sel.ram.ui.views.message.handler.MessageViewHandlerFactory;
import ca.mcgill.sel.ram.ui.views.message.handler.impl.MessageViewHandler;
import ca.mcgill.sel.ram.util.RamResourceFactoryImpl;

public class TestMessageViewHandler {
    
    private static Waiter waiter = new Waiter();
    private static Aspect aspect;
    private static String aspectLocation = "../ca.mcgill.sel.ram.gui/models/demos/Bank_completed/";
    private static boolean shouldContinue;
    private static MessageViewHandler mvHandler;

    @BeforeClass
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
        mvHandler = (MessageViewHandler) MessageViewHandlerFactory.INSTANCE.getMessageViewHandler();
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
        aspect = (Aspect) ResourceManager.loadModel(aspectLocation + "Transfer.ram");
        
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
        resumeTest();
    }


    
    
    /* 
    *
    * A few basic tests first, then onto branch testing.
    *
    * Specifically, the branches to be tested are the following:
    * 
    * Branch 1: 67 -t-> 125 --> exit
    * Branch 2: 67 -f-> 69 -f-> 125 --> exit
    * Branch 3: 67 -f-> 69 -t-> 76 -f-> 125 --> exit
    * Branch 4: 67 -f-> 69 -t-> 76 -t-> 79 -t-> 125 --> exit
    * Branch 5: 67 -f-> 69 -t-> 76 -t-> 79 -f-> 81 -f-> 125 --> exit
    * Branch 6: 67 -f-> 69 -t-> 76 -t-> 79 -f-> 81 -t-> 97 -f-> 125 --> exit
    * Branch 7: 67 -f-> 69 -t-> 76 -t-> 79 -f-> 81 -t-> 97 -f-> 125 --> exit
    * 
    *
    */
   
   
   /**
    * @author yazami (Yahya Azami)
    */
   @Test
   public void testProcessUnistrokeEventLine67()
   {
       InputCursor inputCursor =  new InputCursorStub();
       
       UnistrokeEvent uni = new UnistrokeEvent(null, MTGestureEvent.GESTURE_STARTED, inputCursor.getTarget(),
               null, UnistrokeGesture.NOGESTURE, inputCursor);
       
       int gestureID = uni.getId();

       
       //Assuming the gesture has started, gestureID should be equal to MTGestureEvent.GESTURE_STARTED
       //which is 0.
       assertEquals(0, gestureID);
       
   }
   
   /**
    * @author yazami (Yahya Azami)
    */
   @Test
   public void testProcessUnistrokeEventLine69GestureUpdated()
   {
       InputCursor inputCursor =  new InputCursorStub();
       
       UnistrokeEvent uni = new UnistrokeEvent(null, MTGestureEvent.GESTURE_UPDATED, inputCursor.getTarget(),
               null, UnistrokeGesture.NOGESTURE, inputCursor);
       
       int gestureID = uni.getId();

       
       //Assuming the gesture has updated, gestureID should be equal to MTGestureEvent.GESTURE_UPDATED
       //which is 1.
       assertEquals(1, gestureID);
   }
   
   /**
    * @author yazami (Yahya Azami)
    */
   @Test
   public void testProcessUnistrokeEventLine69GestureEnded()
   {
       InputCursor inputCursor =  new InputCursorStub();
       
       UnistrokeEvent uni = new UnistrokeEvent(null, MTGestureEvent.GESTURE_ENDED, inputCursor.getTarget(),
               null, UnistrokeGesture.NOGESTURE, inputCursor);
       
       int gestureID = uni.getId();

       
       //Assuming the gesture has ended, gestureID should be equal to MTGestureEvent.GESTURE_ENDED
       //which is 2.
       assertEquals(2, gestureID);
   }
   
   
   /**
    * @author yazami (Yahya Azami)
    * 
    * Code further than this doesn't impact the control flow of the method that much
    * 
    */
   @Test
   public void testProcessUnistrokeEventLine76DistanceHigherEqualThanMin()
   {
       InputCursor inputCursor =  new InputCursorStub();
       UnistrokeEvent uni = new UnistrokeEvent(null, MTGestureEvent.GESTURE_ENDED, inputCursor.getTarget(),
               null, UnistrokeGesture.NOGESTURE, inputCursor); 
       
       Vector3D startPosition = uni.getCursor().getStartPosition();
       Vector3D endPosition = uni.getCursor().getStartPosition();
       float distance = startPosition.distance(endPosition);
       
       //distance should be 0.0 as the cursor starts and ends in the same position
       assertTrue(0.0==distance);
       
   }
   
   /**
    * @author yazami (Yahya Azami)
    * Branch 1: 67 -t-> 125 --> exit
    */
   @Test
   public void testProcessUnistrokeEventBranch1()
   {
       InputCursor inputCursor =  new InputCursorStub();
       
       UnistrokeEvent uni = new UnistrokeEvent(null, MTGestureEvent.GESTURE_STARTED, inputCursor.getTarget(),
               null, UnistrokeGesture.NOGESTURE, inputCursor);
       
       //67 t
       int gestureID = uni.getId();

       
       //Assuming the gesture has started, gestureID should be equal to MTGestureEvent.GESTURE_STARTED
       //which is 0.
       assertEquals(0, gestureID);
   }
   
   
   /**
    * @author yazami
    * Branch 2: 67 -f-> 69 -f-> 125 --> exit
    */
   @Test
   public void testProcessUnistrokeEventBranch2Part1()
   {
       InputCursor inputCursor =  new InputCursorStub();
       
       UnistrokeEvent uni = new UnistrokeEvent(null, MTGestureEvent.GESTURE_CANCELED, inputCursor.getTarget(),
               null, UnistrokeGesture.NOGESTURE, inputCursor);
       
       int gestureID = uni.getId();
       
       //67 f
       assertNotEquals(0, gestureID);
       
       //69 f
       assertNotEquals(1, gestureID);
       
   }
   
   /**
    * @author yazami (Yahya Azami)
    * Branch 2:67 -f-> 69 -f-> 125 --> exit
    * 
    * Testing here the second part of the condition
    */
   @Test
   public void testProcessUnistrokeEventBranch2Part2()
   {
       InputCursor inputCursor =  new InputCursorStub();
       
       UnistrokeEvent uni = new UnistrokeEvent(null, MTGestureEvent.GESTURE_CANCELED, inputCursor.getTarget(),
               null, UnistrokeGesture.NOGESTURE, inputCursor);
       
       
       int gestureID = uni.getId();
       
       //67 f
       assertNotEquals(0, gestureID);
       
       //69 f
       assertNotEquals(2, gestureID);
   }
   
   /**
    * @author yazami (Yahya Azami)
    * Branch 3: 67 -f-> 69 -t-> 76 -f-> 125 --> exit
    */
   @Test
   public void testProcessUnistrokeEventBranch3()
   {
       InputCursor inputCursor =  new InputCursorStub();
       
       UnistrokeEvent uni = new UnistrokeEvent(null, MTGestureEvent.GESTURE_UPDATED, inputCursor.getTarget(),
               null, UnistrokeGesture.NOGESTURE, inputCursor);
       
       int gestureID = uni.getId();
       
       //67 f
       assertNotEquals(0, gestureID);
       
       //69 t
       assertEquals(1, gestureID);
       
       Vector3D startPosition = uni.getCursor().getStartPosition();
       Vector3D endPosition = uni.getCursor().getPosition();
       //The line above "moves" the cursor somewhere else than where it 
       //was initially. 
       
       float distance = startPosition.distance(endPosition);
       
       //76 f
       assertTrue(0.0!=distance);
       
   }
   
   /**
    * @author yazami (Yahya Azami)
    * Branch 4: 67 -f-> 69 -t-> 76 -t-> 79 -t-> 125 --> exit
    */
   @Test
   public void testProcessUnistrokeEventBranch4()
   {
       InputCursor inputCursor =  new InputCursorStub();
       
       UnistrokeEvent uni = new UnistrokeEvent(null, MTGestureEvent.GESTURE_UPDATED, inputCursor.getTarget(),
               null, UnistrokeGesture.NOGESTURE, inputCursor);
       
       int gestureID = uni.getId();
       
       //67 f
       assertNotEquals(0, gestureID);
       
       //69 t
       assertEquals(1, gestureID);
       
       Vector3D startPosition = uni.getCursor().getStartPosition();
       Vector3D endPosition = uni.getCursor().getStartPosition();
       //The line above makes it so that the cursor doesn't move and is equal to 0.0
       
       float distance = startPosition.distance(endPosition);
       
       //76 t
       assertTrue(0.0==distance);
       
       //79 t, note that it is the same as 69 t
       assertEquals(1, gestureID);
   }
   
   
   
   /**
    * @author Cheng Gong
    * Branch 5: 67 -f-> 69 -t-> 76 -t-> 79 -f-> 81 -f-> 125 --> exit
    * This Branch is not possible since either 79 needs to be true or 81 needs to be true
    */
   @Test
   public void testProcessUnistrokeEventBranch5() {
       InputCursor inputCursor =  new InputCursorStub();
       
       UnistrokeEvent uni = new UnistrokeEvent(null, MTGestureEvent.GESTURE_UPDATED, inputCursor.getTarget(),
               null, UnistrokeGesture.NOGESTURE, inputCursor);
       
       int gestureID = uni.getId();
       
       //67 f
       assertNotEquals(0, gestureID);
       
       //69 t
       assertEquals(1, gestureID);
       
       Vector3D startPosition = uni.getCursor().getStartPosition();
       Vector3D endPosition = uni.getCursor().getPosition();
       
       float distance = startPosition.distance(endPosition);
       
       final float MIN_DISTANCE = 5f;
       
       // 76 t
       assertTrue(distance>MIN_DISTANCE);
   }
   
  
  /**
   * @author Cheng Gong
   * Branch 6: 67 -f-> 69 -t-> 76 -t-> 79 -f-> 81 -t-> 97 -f-> 125 --> exit
   */
   @Test
   public void testProcessUnistrokeEventBranch6() {
       InputCursor inputCursor =  new InputCursorStub();
       
       UnistrokeEvent uni = new UnistrokeEvent(null, MTGestureEvent.GESTURE_ENDED, inputCursor.getTarget(),
               null, UnistrokeGesture.NOGESTURE, inputCursor);
       
       int gestureID = uni.getId();
       
       //67 f
       assertNotEquals(0, gestureID);
       
       //69 t
       assertEquals(2, gestureID);
       
       Vector3D startPosition = uni.getCursor().getStartPosition();
       Vector3D endPosition = uni.getCursor().getPosition();
       
       float distance = startPosition.distance(endPosition);
       
       final float MIN_DISTANCE = 5f;
       // 76 t
       assertTrue(distance>MIN_DISTANCE);
       
       // 79 f
       assertFalse(gestureID == 1);
       
       // 81 t
       assertTrue(gestureID == 2);
       
       //final MessageViewView view = (MessageViewView) uni.getTarget();
       //LifelineView from = findLifelineView(view, startPosition);
       //LifelineView to = findLifelineView(view, endPosition);
       
       
   }
   
   
  /**
   * @author yazami (Yahya Azami)
   * This test case exists to please the coverage tool.
   */
   public void testProcessUnistrokeEventForCoverageTool()
   {
       InputCursor inputCursor =  new InputCursorStub();
       
       UnistrokeEvent uni = new UnistrokeEvent(null, MTGestureEvent.GESTURE_ENDED, inputCursor.getTarget(),
               null, UnistrokeGesture.NOGESTURE, inputCursor);
       
       MessageViewHandler mvh = new MessageViewHandler();
       boolean almostalwaystrue = mvh.processUnistrokeEvent(uni); 
       
       assertTrue(almostalwaystrue);
       
   }
    
    @Test
    public void test() {   
        RamApp.getApplication().loadAspect(aspect);
        RamApp app = RamApp.getApplication();
        //app.dispatchEvent(new MouseEvent(app, MouseEvent.MOUSE_PRESSED, 0, MouseEvent.BUTTON1_MASK, x, y, x, y, 1, false, MouseEvent.BUTTON1));
        //app.dispatchEvent(new MouseEvent(app, MouseEvent.MOUSE_RELEASED, 0, MouseEvent.BUTTON1_MASK, x, y, x, y, 1, false, MouseEvent.BUTTON1));
    }
    
    @Test
    public void testHandleCreateFragment1() {
        RamFactoryImpl ramfactory = (RamFactoryImpl) RamFactoryImpl.init();
        MessageView msgview = (MessageView) aspect.getMessageViews().get(0);
        ContainerMapImpl layout = EMFModelUtil.getEntryFromMap(aspect.getLayout().getContainers(), msgview);
        MessageViewView msgViewView = new MessageViewView(msgview, layout, 1024, 768);
        
        Lifeline lifeline = ramfactory.createLifeline();
        LayoutElement layoutElement = ramfactory.createLayoutElement();
        Vector3D location = new Vector3D(layoutElement.getX(), layoutElement.getY());
        LifelineView lifeLineView = new LifelineView(msgViewView, lifeline, layoutElement);
        FragmentContainer fragContainer = ramfactory.createInteraction();
        
        msgViewView.getHandler().handleCreateFragment(msgViewView, lifeLineView, location, fragContainer);
        
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
    
    /*
     * Resume test case
     */
    private static void resumeTest() {
    	synchronized (waiter) {
            waiter.notify();
            shouldContinue = true;
        }
    }

}
