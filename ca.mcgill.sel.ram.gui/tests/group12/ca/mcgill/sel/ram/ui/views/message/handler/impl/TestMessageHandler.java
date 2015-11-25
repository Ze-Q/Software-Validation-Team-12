package group12.ca.mcgill.sel.ram.ui.views.message.handler.impl;

import org.jodah.concurrentunit.Waiter;
import org.junit.After;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mt4j.input.inputProcessors.componentProcessors.tapAndHoldProcessor.TapAndHoldEvent;

import ca.mcgill.sel.commons.emf.util.AdapterFactoryRegistry;
import ca.mcgill.sel.commons.emf.util.ResourceManager;
import ca.mcgill.sel.ram.RamPackage;
import ca.mcgill.sel.ram.provider.RamItemProviderAdapterFactory;
import ca.mcgill.sel.ram.ui.RamApp;
import ca.mcgill.sel.ram.ui.events.listeners.ITapAndHoldListener;
import ca.mcgill.sel.ram.ui.views.message.handler.MessageViewHandlerFactory;
import ca.mcgill.sel.ram.ui.views.message.handler.impl.MessageHandler;
import ca.mcgill.sel.ram.ui.views.message.handler.impl.MessageViewHandler;
import ca.mcgill.sel.ram.util.RamResourceFactoryImpl;

public class TestMessageHandler extends MessageHandler {
	
	private static ITapAndHoldListener tapAndHolderListner;
    private static Waiter waiter = new Waiter();
    
    private static boolean shouldContinue;

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
        
        // Wait for RamApp to be initialized.
        pauseTest();
        tapAndHolderListner = MessageViewHandlerFactory.INSTANCE.getMessageHandler();
    }

    @Test
    /**
     * Test tapAndHoldEvent.isHoldComplete is true
     */
    public void tapAndHoldEventHoldNotComplete() {
        TapAndHoldEvent tapHoldEvt = new TapAndHoldEvent(null, 0, null, null, false,
                null, 0, 0.0f, 0.0f);
        Assert.assertFalse(tapHoldEvt.isHoldComplete());
    }

    @Test
    /*
     * Test tapAndHoldEvent.isHoldComplete is false
     */
    public void tapAndHoldEventHoldComplete() {
        TapAndHoldEvent tapHoldEvt = new TapAndHoldEvent(null, 0, null, null, true,
                null, 0, 0.0f, 0.0f);
        Assert.assertTrue(tapHoldEvt.isHoldComplete());
    }

    @Test
    /*
     * Test processTapAndHoldEventIsHoldCompleteFalse
     */
    public void processTapAndHoldEventIsHoldCompleteFalse() {
        MessageHandler msgHandler = new MessageHandler();
        TapAndHoldEvent tapAndHoldEvt = new TapAndHoldEvent(null, 0, null, null, false,
                null, 0, 0.0f, 0.0f);
        /* isHoldComplete = false, returns true */
        Assert.assertTrue(msgHandler.processTapAndHoldEvent(tapAndHoldEvt));
    }

    @Test
    /*
     * tapAndHoldEvent.isHoldComplete() = true
     * shouldProcessTapAndHold(messageCallView.getMessage()) = false
     */
    public void tapAndHoldEventCompleteShouldProcessTapAndHoldFalse() {
        MessageHandler msgHandler = new MessageHandler();
        TapAndHoldEvent tapAndHoldEvt = new TapAndHoldEvent(null, 0, null, null, false,
                null, 3, 3.0f, 3.0f);
        // Target is null, therefore messageCallView will be null and finally message will be null
        Assert.assertTrue(msgHandler.processTapAndHoldEvent(tapAndHoldEvt));
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
