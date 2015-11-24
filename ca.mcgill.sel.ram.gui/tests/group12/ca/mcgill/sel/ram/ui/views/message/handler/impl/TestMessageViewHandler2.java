package group12.ca.mcgill.sel.ram.ui.views.message.handler.impl;

import static org.junit.Assert.*;

import java.awt.event.MouseEvent;

import org.jodah.concurrentunit.Waiter;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mt4j.input.inputData.AbstractCursorInputEvt;
import org.mt4j.input.inputData.InputCursor;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.unistrokeProcessor.UnistrokeContext;
import org.mt4j.input.inputProcessors.componentProcessors.unistrokeProcessor.UnistrokeEvent;
import org.mt4j.input.inputProcessors.componentProcessors.unistrokeProcessor.UnistrokeUtils;
import org.mt4j.input.inputProcessors.componentProcessors.unistrokeProcessor.UnistrokeUtils.Recognizer;
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
import ca.mcgill.sel.ram.util.RamResourceFactoryImpl;
import processing.core.PApplet;

public class TestMessageViewHandler2 {
    
    private static Waiter waiter = new Waiter();
    
    private static Aspect aspect;
    
    private static String modelsFolder = "../ca.mcgill.sel.ram.gui/models/demos/Bank_completed/";
    
    
    //Special attributes
    /** The pa. */
    private PApplet pa;
    /** The plane normal. */
    private Vector3D planeNormal;
    /** The point in plane. */
    private Vector3D pointInPlane;
    
    
    /** The context. */
    private UnistrokeContext context;
    
    /** The recognizer. */
    private Recognizer recognizer;
    
    /** The du. */
    private UnistrokeUtils du;
    //End Special attributes
    
    
    
//    public void cursorStarted(InputCursor inputCursor,AbstractCursorInputEvt currentEvent){
//        
//          context=new UnistrokeContext(pa,planeNormal,pointInPlane,inputCursor,recognizer,du,inputCursor.getTarget());
//        
//    }


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
                waiter.resume();
            }
        });
        
        // Wait for RamApp to be initialized.
        try {
            waiter.await();
        } catch (Throwable e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
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
        aspect = (Aspect) ResourceManager.loadModel(modelsFolder + "Transfer.ram");
        
        RamApp.getApplication().addSceneChangeListener(new ISceneChangeListener() {
            
            @Override
            public void processSceneChangeEvent(SceneChangeEvent sc) {
                if (sc.getNewScene() instanceof DisplayAspectScene) {
                    RamApp.getApplication().removeSceneChangeListener(this);
                    waiter.resume();
                }
            }

            
        });
        
        RamApp.getApplication().loadAspect(aspect);
        
        // Wait for UI to be updated.
        try {
            waiter.await();
        } catch (Throwable e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void test() {
        //fail("Not yet implemented");
        
        RamApp.getApplication().loadAspect(aspect);
        RamApp app = RamApp.getApplication();
//        app.dispatchEvent(new MouseEvent(app, MouseEvent.MOUSE_PRESSED, 0, MouseEvent.BUTTON1_MASK, x, y, x, y, 1, false, MouseEvent.BUTTON1));
//        app.dispatchEvent(new MouseEvent(app, MouseEvent.MOUSE_RELEASED, 0, MouseEvent.BUTTON1_MASK, x, y, x, y, 1, false, MouseEvent.BUTTON1));
    }
    
    /* A few basic tests first, then onto branch testing.
     *
     * Specifically, the branches to be tested are the following:
     * 
     * 67 -t-> 125 --> exit
     * 67 -f-> 69 -f-> 125 --> exit
     * 67 -f-> 69 -t-> 76 -f-> 125 --> exit
     * 67 -f-> 69 -t-> 76 -t-> 79 -t-> 125 --> exit
     * 67 -f-> 69 -t-> 76 -t-> 79 -f-> 81 -f-> 125 --> exit
     * 67 -f-> 69 -t-> 76 -t-> 79 -f-> 81 -t-> 97 -f-> 125 --> exit
     * 67 -f-> 69 -t-> 76 -t-> 79 -f-> 81 -t-> 97 -f-> 125 --> exit
     * 67 -f-> 69 -t-> 76 -t-> 79 -f-> 81 -t-> 97 -t-> 125 --> exit
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
    
    
    
    
//    /**
//     * @author yazami (Yahya Azami)
//     */
//    @Test
//    public void findLifelineViewLine138()
//    {
//        RamFactoryImpl ramfactory = (RamFactoryImpl) RamFactoryImpl.init();
//        Lifeline lifeline = ramfactory.createLifeline();
//        
//    }
   
    
      /*
        InputCursor inputCursor = new InputCursor();
        
        Object context;
        UnistrokeEvent uni = new UnistrokeEvent(this, MTGestureEvent.GESTURE_STARTED,
              inputCursor.getCurrentTarget(),context.getVisualizer(),
              UnistrokeGesture.NOGESTURE,inputCursor);
              
        */
    //System.out.println(MTGestureEvent.GESTURE_STARTED);
    //System.out.println(gestureID);
    
    
    /*
    @Test
    public void testHandleCreateFragment1() {
        RamFactoryImpl ramfactory = (RamFactoryImpl) RamFactoryImpl.init();
        MessageView msgview = (MessageView) aspect.getMessageViews().get(0);
        ContainerMapImpl layout = EMFModelUtil.getEntryFromMap(aspect.getLayout().getContainers(), msgview);
        MessageViewView msgViewView = new MessageViewView(msgview, layout, 1024, 768);
        
        Lifeline lifeline = ramfactory.createLifeline();
        
//        LayoutElement layoutElement = layout.getValue().get(lifeline);
        LayoutElement layoutElement = ramfactory.createLayoutElement();
        Vector3D location = new Vector3D(layoutElement.getX(), layoutElement.getY());
        LifelineView lifeLineView = new LifelineView(msgViewView, lifeline, layoutElement);
        FragmentContainer fragContainer = ramfactory.createInteraction();
        
        msgViewView.getHandler().handleCreateFragment(msgViewView, lifeLineView, location, fragContainer);
        
    }
    */

}
