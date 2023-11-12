import java.awt.*;
import javax.swing.*;

/**
 * This is the top frame for the maze application
 *  
 * @author Charles Hoot 
 * @version 4.0
 */
    
public class MazeApplication 
{    
        
    public static void main (String args[]) 
    {
        JPanel myPanel;
        Stepper myStepper;
        ActionThread myThread;
        Object dispatcher;
        
        myThread = new MazeActionThread();                 // Change this line for different
                                                                // applications
        myPanel =  myThread.getAnimationPanel();
 
        dispatcher = new Object();
        AnimatedApplicationFrame myFrame = 
            new AnimatedApplicationFrame(myThread.getApplicationTitle(),
                                        dispatcher, myPanel, 
                                        myThread);
        myStepper = myFrame.getStepper();
        
        // must set the stepper before we start the thread
        myThread.setStepper(myStepper);
        myThread.start();
    }
    
    
 
}
