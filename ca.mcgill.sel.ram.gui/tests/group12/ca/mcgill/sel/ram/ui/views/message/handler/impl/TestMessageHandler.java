package group12.ca.mcgill.sel.ram.ui.views.message.handler.impl;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.mt4j.input.inputProcessors.componentProcessors.tapAndHoldProcessor.TapAndHoldEvent;

import ca.mcgill.sel.ram.ui.views.message.handler.impl.MessageHandler;

public class TestMessageHandler extends MessageHandler {

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
}
