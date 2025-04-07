package application;

import java.util.Objects;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class CellRect extends Rectangle {
	
    public CellRect(int x, int y) {
        super(x, y, Size.CELL_RECT_SIDE, Size.CELL_RECT_SIDE);

        this.setFill(Color.BLACK);
        this.setArcWidth(Size.CELL_RECT_ARC);
        this.setArcHeight(Size.CELL_RECT_ARC);
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
