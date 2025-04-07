package application;

import java.util.Objects;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class CellRect extends Rectangle {
	
	static private final int rectSize= 50;
	static private final int cubeArc= 25;
	
    public CellRect(int x, int y) {
        super(x, y, rectSize, rectSize);

        this.setFill(Color.BLACK);
        this.setArcWidth(cubeArc);
        this.setArcHeight(cubeArc);
    }

    // Override equals to compare based on position
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CellRect)) return false;
        
        CellRect that = (CellRect) o;
        return this.getX() == that.getX() && this.getY() == that.getY();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getX(), getY());
    }
}
