package group12.ca.mcgill.sel.ram.ui.views.message.handler.impl;

import org.mt4j.input.inputData.InputCursor;
import org.mt4j.util.math.Vector3D;

/**
 * @author yazami (Yahya Azami)
 *
 */
public class InputCursorStub extends InputCursor {

    
    @Override
    public Vector3D getStartPosition()
    {
        Vector3D v1 = new Vector3D(1,1); 
        //Vector3D v2 = new Vector3D();
        
        return v1;
        
    }
    
    @Override
    public float getCurrentEvtPosX()
    {
        return (float) 1.0;
    }
    
    /**
     * Gets the current events position y.
     * 
     * @return the current events position y
     */
    @Override
    public float getCurrentEvtPosY()
    {
        return (float) 43;
    }
    
    @Override
    public Vector3D getPosition()
    {
        return new Vector3D(getCurrentEvtPosX(), getCurrentEvtPosY()); 
    }

}
