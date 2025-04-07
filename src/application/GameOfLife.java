package application;

import java.util.HashSet;
import java.util.Set;


import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class GameOfLife extends Application {
	
    private long lastUpdateTime = 0;
    private final double FPS = 2;  // Target FPS
    private final long NANOS_PER_UPDATE = 1_000_000_000 / (long) FPS;  // Time per frame in nanoseconds

	int width = 800;
	int height = 800;
	int rectangleSize = 50;
	
	boolean allowUserInput = true;
	
	Set<Rectangle> liveCells = new HashSet<>();
	
	public void clickCells(MouseEvent event,Set<Rectangle> liveCells, Pane pane) {		
        // Get click coordinates
        double x = event.getX();
        double y = event.getY();

        final int adjustedX = (int) (x/rectangleSize)*rectangleSize;
        final int adjustedY = (int) (y/rectangleSize)*rectangleSize;
        
        //remove a cell (square) if a there is a living cell already on that position
        pane.getChildren().removeIf(node -> node instanceof Rectangle && ((Rectangle) node).getX() == adjustedX && ((Rectangle) node).getY() == adjustedY);
        boolean addRectangle = !(liveCells.removeIf(rect -> rect.getX() == adjustedX && rect.getY() == adjustedY));
        
        if (addRectangle) {
            // Create a square at the click location
            Rectangle rect = new Rectangle(adjustedX, adjustedY, rectangleSize, rectangleSize);
            
            rect.setFill(Color.BLACK);
            rect.setArcWidth(20);
            rect.setArcHeight(20);
            
            liveCells.add(rect);
            pane.getChildren().add(rect);        	
        }
	}
	
    @Override
    public void start(Stage primaryStage) {  	
        // Create a pane
        Pane pane = new Pane();
        //create Scene
        Scene scene = new Scene(pane, width, height);
      
        // Adding and removing live cells from user input
        pane.setOnMouseClicked((MouseEvent event) -> {
        		clickCells(event, liveCells, pane);
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
