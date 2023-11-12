
/**
 * Create a file so we can find the default directory that java is using
 * 
 * @author Charles Hoot
 * @version 3.0
 */

import java.io.*;
    
public class FindDefaultDirectory
{

    public static void main(String args[])
    {


        System.out.println("Attempting to create the file DefDirHere.txt");
        System.out.println("Use it to find where to put input files.");        
        PrintWriter output;
        
        try
        {
            output = new PrintWriter(
                        new FileWriter(
                            "DefDirHere.txt" ) );
            
            output.println("abcdefghij");
            output.println("0123456789");
            output.close();
            System.out.println("Succeeded");
            
        }
        catch(IOException e)
        {
            System.out.println("There was an error with DefDirHere.txt");
            System.out.println(e.getMessage());            
        }
                                   
    }
}
