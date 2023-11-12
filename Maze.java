import java.util.*;
import java.awt.*;

/**
 * A class that will be used to keep track of the maze. 
 * 
 * Since it may be accessed by user interface threads
 * (paint) and the application thread, make all methods synchronized
 * 
 * @author Charles Hoot
 * @version 4.0
 */
        
public class Maze
    {
    
        private static final int EMPTY = 0;
        private static final int WALL = 1;
        private static final int VISITED = 2;
        private static final int SCHEDULED = 3;
        
        private int rows;
        private int columns;
        private int squares[][];
        
        private int startRow=-1, startCol=-1;
        private int goalRow=-1, goalCol=-1;
        private int lastRowVisit=-1, lastColVisit=-1;
        

        
        public Maze(int [][] maze)
        {
            squares = maze;
            rows = squares.length;
            columns = squares[0].length;
            
            // Convert any non zero values to walls, zeros will be empty
            for(int r = 0; r< rows; r++)
                for(int c = 0; c<columns; c++)
                    if(squares[r][c] != 0)
                        squares[r][c] = WALL;
                    else
                        squares[r][c] = EMPTY; 

        }
        
        
        /**
         * Determine if a square is a legal square.
         *
         * @param  r    The row coordinate of the square.
         * @param  c    The column coordinate of the square.
         * @return True if the square coordinates are within the array for the maze.
         */
        synchronized 
        public boolean isLegal(int r, int c)
        {
            return (r>=0 && c>=0 && r<rows && c<columns);
        }

        /**
         * Set the goal square.
         *
         * @param  r    The row coordinate of the square.
         * @param  c    The column coordinate of the square.
         */
        synchronized 
        public void setGoal(int r, int c)
        {
            if(isLegal(r,c) && isFree(r,c))
            {
                goalRow = r;
                goalCol = c;
            }
        }        


        /**
         * Set the start square.
         *
         * @param  r    The row coordinate of the square.
         * @param  c    The column coordinate of the square.
         */
        synchronized 
        public void setStart(int r, int c)
        {
            if(isLegal(r,c) && isFree(r,c))
            {
                startRow = r;
                startCol = c;
            }
        }                           
        
        /**
         * Visit a square.
         *
         * @param  r    The row coordinate of the square.
         * @param  c    The column coordinate of the square.
         */
        synchronized 
        public void visitSquare(int r, int c)
        {
            if(isLegal(r,c))
            {
                squares[r][c] = VISITED;
                lastRowVisit = r;
                lastColVisit = c;
            }
        }
        
 
         /**
         * Indicate that a square has been scheduled to be visited.
         *
         * @param  r    The row coordinate of the square.
         * @param  c    The column coordinate of the square.
         */
        synchronized 
        public void scheduleSquare(int r, int c)
        {
            if(isLegal(r,c))
                squares[r][c] = SCHEDULED;
        }

         /**
         * Determine if the square is the goal.
         *
         * @param  r    The row coordinate of the square.
         * @param  c    The column coordinate of the square.
         * @return True if the square is the goal.
         */
        synchronized 
        public boolean isGoal(int r, int c)
        {
            boolean result = false;
            if(isLegal(r,c))
                result = ( r == goalRow && c == goalCol);
            return result;
           
        }

         /**
         * Determine if the square is free to be visited.
         *
         * @param  r    The row coordinate of the square.
         * @param  c    The column coordinate of the square.
         * @return True if the square is empty.
         */
        synchronized 
        public boolean isFree(int r, int c)
        {
            boolean result = false;
            if(isLegal(r,c))
                result = ( squares[r][c] == EMPTY);
            return result;
        }
        
 
    // These are the half-widths for the various parts of the maze 
    public static final int SQUARE_SIZE = 8;
    public static final int PATH_MARK_SIZE = 4;
    public static final int GOAL_SIZE = 7;
    public static final int START_SIZE = 7;
    
    public static final int MAZE_OFFSET = 30;
        
        
    /**
     * Draw a representation of the maze at the given location of the graphics panel.
     * 
     * @param   g  The graphics context to draw on.   
     * @param   leftX  The x position of the left of the drawing.
     * @param   topY  The y position of the top.
     * 
     */
    synchronized 
    public void drawOn(Graphics g, int leftX, int topY)
    {
        int centerX=0;
        int centerY=0;
       
        // Draw the maze squares.
        if(squares != null)
        {
            g.setColor(Color.white);
            g.fillRect(leftX+ MAZE_OFFSET, topY+ MAZE_OFFSET, 2*SQUARE_SIZE*columns, 2*SQUARE_SIZE*rows);
            for(int r = 0; r< rows; r++)
                for(int c = 0; c<columns; c++)
                {
                    centerX = leftX + MAZE_OFFSET + (2*c+1)*SQUARE_SIZE;
                    centerY = topY + MAZE_OFFSET + (2*r+1)*SQUARE_SIZE;
                    switch(squares[r][c])
                    {
                        case EMPTY:
                            g.setColor(Color.gray);
                            g.drawRect(centerX-SQUARE_SIZE, centerY-SQUARE_SIZE, 2*SQUARE_SIZE, 2*SQUARE_SIZE);
                            break;
                        case WALL:
                            g.setColor(Color.black);
                            g.fillRect(centerX-SQUARE_SIZE, centerY-SQUARE_SIZE, 2*SQUARE_SIZE, 2*SQUARE_SIZE);
                            break;
                        case VISITED:
                            g.setColor(Color.gray);
                            g.drawRect(centerX-SQUARE_SIZE, centerY-SQUARE_SIZE, 2*SQUARE_SIZE, 2*SQUARE_SIZE);
                            g.setColor(Color.blue);
                            g.fillOval(centerX-PATH_MARK_SIZE, centerY-PATH_MARK_SIZE, 2*PATH_MARK_SIZE, 2*PATH_MARK_SIZE);
                            break;
                        case SCHEDULED:
                            g.setColor(Color.gray);
                            g.drawRect(centerX-SQUARE_SIZE, centerY-SQUARE_SIZE, 2*SQUARE_SIZE, 2*SQUARE_SIZE);
                            g.setColor(Color.cyan);
                            g.drawOval(centerX-PATH_MARK_SIZE, centerY-PATH_MARK_SIZE, 2*PATH_MARK_SIZE, 2*PATH_MARK_SIZE);
                            break;
                    }
                }
        } // end if
        
        
        // Draw the start
        if( startRow != -1)
        {
            centerX = leftX + MAZE_OFFSET + (2*startCol+1)*SQUARE_SIZE;
            centerY = topY + MAZE_OFFSET + (2*startRow+1)*SQUARE_SIZE;
            g.setColor(Color.black);
            g.drawLine(centerX-START_SIZE, centerY-START_SIZE, centerX+START_SIZE, centerY+START_SIZE);
            g.drawLine(centerX-START_SIZE, centerY+START_SIZE, centerX+START_SIZE, centerY-START_SIZE);
            g.drawLine(centerX-START_SIZE, centerY, centerX+START_SIZE, centerY);
            g.drawLine(centerX, centerY-START_SIZE, centerX, centerY+START_SIZE);
        }
        
        // Draw the goal
        if( goalRow != -1)
        {
            centerX = leftX + MAZE_OFFSET + (2*goalCol+1)*SQUARE_SIZE;
            centerY = topY + MAZE_OFFSET + (2*goalRow+1)*SQUARE_SIZE;
            g.setColor(Color.green);
            g.drawOval(centerX-GOAL_SIZE, centerY-GOAL_SIZE, 2*GOAL_SIZE, 2*GOAL_SIZE);
            g.drawOval(centerX-PATH_MARK_SIZE, centerY-PATH_MARK_SIZE, 2*PATH_MARK_SIZE, 2*PATH_MARK_SIZE);
        }
        
        // Draw the last visited
        if( lastRowVisit != -1)
        {
            centerX = leftX + MAZE_OFFSET + (2*lastColVisit+1)*SQUARE_SIZE;
            centerY = topY + MAZE_OFFSET + (2*lastRowVisit+1)*SQUARE_SIZE;
            g.setColor(Color.red);
            g.fillOval(centerX-PATH_MARK_SIZE, centerY-PATH_MARK_SIZE, 2*PATH_MARK_SIZE, 2*PATH_MARK_SIZE);

        }

    }
        
}
