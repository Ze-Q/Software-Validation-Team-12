package group12.ca.mcgill.sel.ram.ui.views.message.handler.impl;

import static org.junit.Assert.fail;

import java.awt.event.MouseEvent;

import org.jodah.concurrentunit.Waiter;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
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

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void test() {
        fail("Not yet implemented");
        
        RamApp.getApplication().loadAspect(aspect);
        RamApp app = RamApp.getApplication();
//        app.dispatchEvent(new MouseEvent(app, MouseEvent.MOUSE_PRESSED, 0, MouseEvent.BUTTON1_MASK, x, y, x, y, 1, false, MouseEvent.BUTTON1));
//        app.dispatchEvent(new MouseEvent(app, MouseEvent.MOUSE_RELEASED, 0, MouseEvent.BUTTON1_MASK, x, y, x, y, 1, false, MouseEvent.BUTTON1));
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
