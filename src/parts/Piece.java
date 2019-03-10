package parts;

import java.util.ArrayList;

public interface Piece {

	public int row = 0, column = 0;
	public char color = 'w'; //w for white, b for black
	
	public boolean threatens(int r, int c, Square[][] board);
	
	public boolean moveTo(int r, int c, Square[][] board);
	
}
