package group12.ca.mcgill.sel.ram.ui.views.message.handler.impl;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mt4j.input.inputProcessors.componentProcessors.tapAndHoldProcessor.TapAndHoldEvent;

public class TestMessageHandler {

   
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        
//        try {
//            Thread.sleep(3000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }  
        
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
        
        
        
        
    }
    
    @Test
    public void testLine61_isHoldComplete() {
        
       TapAndHoldEvent myTabAndHoldEvent = new TapAndHoldEvent(null, 0, null, null, false, null, 0, 0, 0);
       assertEquals(false, myTabAndHoldEvent.isHoldComplete());
        
    }
    
    @After
    public void tearDown() throws Exception {
    }

  
}
