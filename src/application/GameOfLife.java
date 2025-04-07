package application;

import java.util.HashSet;
import java.util.Set;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class GameOfLife extends Application {
	
    private long lastUpdateTime = 0;
    private final double FPS = 2;  // Target FPS
    private final long NANOS_PER_UPDATE = 1_000_000_000 / (long) FPS;  // Time per frame in nanoseconds

	private final int width = Size.WINDOW_WIDTH;
	private final int height = Size.WINDOW_HEIGHT;
	private final int rectSize = Size.CELL_RECT_SIDE;
	
	boolean allowUserInput = true;
	
	Set<CellRect> liveCells = new HashSet<>();
	
	public void clickCells(MouseEvent event,Set<CellRect> liveCells, Pane pane) {		
        //Get click coordinates
        double x = event.getX();
        double y = event.getY();

        final int adjustedX = (int) (x/rectSize)*rectSize;
        final int adjustedY = (int) (y/rectSize)*rectSize;
        
        //remove a cell (square) if a there is a living cell already on that position
        pane.getChildren().removeIf(node -> node instanceof CellRect && ((CellRect) node).getX() == adjustedX && ((CellRect) node).getY() == adjustedY);
        boolean addCellRect = !(liveCells.removeIf(rect -> rect.getX() == adjustedX && rect.getY() == adjustedY));
        
        if (addCellRect) {
            // Create a square at the click location
            CellRect rect = new CellRect(adjustedX, adjustedY);
      
            liveCells.add(rect);
            pane.getChildren().add(rect);        	
        }
	}
	
	public Set<CellRect> cellsToDie(Set<CellRect> liveCells) {	
		
		Set<CellRect> cellsToDie = new HashSet<>();
		for (CellRect cell : liveCells ) {
			int neighboors = numOfliveCellsAround(liveCells, (int) cell.getX(), (int) cell.getY());			
			if (neighboors < 2 || neighboors >3) {
				cellsToDie.add(cell);
			}	
		}
		return cellsToDie;
	}
	
	public int numOfliveCellsAround(Set<CellRect>  liveCells, int x, int y) {
		int neighboors = 0;
		//calculate number of Moore neighbors	
		for (CellRect cell : liveCells ) {
			if (cell.getX() == x-rectSize && cell.getY() == y-rectSize) neighboors++;
			if (cell.getX() == x-rectSize && cell.getY() == y) neighboors++;
			if (cell.getX() == x-rectSize && cell.getY() == y+rectSize) neighboors++;
		
			if (cell.getX() == x && cell.getY() == y-rectSize) neighboors++;
			if (cell.getX() == x && cell.getY() == y+rectSize) neighboors++;
						
			if (cell.getX() == x+rectSize && cell.getY() == y-rectSize) neighboors++;
			if (cell.getX() == x+rectSize && cell.getY() == y) neighboors++;
			if (cell.getX() == x+rectSize && cell.getY() == y+rectSize) neighboors++;
		}
		return neighboors;
	}
	
	public Set<CellRect> generateNewCells(Set<CellRect> liveCells, Pane pane) {
		Set<CellRect> newBornCells = new HashSet<>();
		
		for (CellRect cell : liveCells) {		
			int x = (int) cell.getX();
			int y = (int) cell.getY();
			
			//mn stands for Moore Neighboors
			int[][] mn = {{x-rectSize,y-rectSize},{x-rectSize,y},{x-rectSize,y+rectSize},  {x,y-rectSize},{x,y-rectSize},  {x+rectSize,y-rectSize},{x+rectSize,y},{x-rectSize,y+rectSize}}; 
			
			for (int[] coordinates :mn) {				
				int neighboors = numOfliveCellsAround(liveCells, coordinates[0], coordinates[1]);
				
				if (neighboors == 3) {
					CellRect rect = new CellRect(coordinates[0],coordinates[1]);
									
					//create new cell only if there is not already live cell on those coordinates and if is not an new cell created already
					if (! (liveCells.contains(rect) || newBornCells.contains(rect))) {
						newBornCells.add(rect);
					}
				}	
			}
		}
		return newBornCells;
	}
	
    @Override
    public void start(Stage primaryStage) {  	
        // Create a pane
        Pane pane = new Pane();
        //create Scene
        Scene scene = new Scene(pane, width, height);
      
        // Adding and removing live cells from user input
        pane.setOnMouseClicked((MouseEvent event) -> {
        	if (allowUserInput) {
        		clickCells(event, liveCells, pane);
        	}
        });
        
        //start the game by pressing SPACE
        scene.setOnKeyPressed(e -> {
            if( e.getCode() == KeyCode.SPACE) {
            	System.out.println("ENTER PRESSED; disable user input");
            	allowUserInput = false;
            }
        });
        
        // Game loop with fixed FPS using AnimationTimer
        AnimationTimer gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (now - lastUpdateTime >= NANOS_PER_UPDATE) {
                	
                    // Update the game state and render the new frame
                	if (!allowUserInput) {
                		//evaluate which live cells will not survive
                		Set<CellRect> cellsToDie = cellsToDie(liveCells);
                		//evalute which cells should become alive
                		Set<CellRect> newBornCells = generateNewCells(liveCells, pane);
                		
                		//remove dead cells
                		liveCells.removeAll(cellsToDie);
                        pane.getChildren().removeAll(cellsToDie);
                		
                        //add new born cells 
                        liveCells.addAll(newBornCells);
                        pane.getChildren().addAll(newBornCells);
                	}
                    // Update last update time
                    lastUpdateTime = now;
                }
            }
        };
                
        // Start the game loop
        gameLoop.start();
        
        primaryStage.setTitle("Game of Life");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
