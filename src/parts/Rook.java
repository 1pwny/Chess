package parts;

public class Rook implements Piece {

	public int column, row;
	public char color;
	
	public Rook(int c, int r) {
		column = c;
		row = r;
		
		color = (r == 7) ? 'b' : 'w';
	}
	
	@Override
	public boolean threatens(int r, int c, Square[][] board) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean moveTo(int r, int c, Square[][] board) {
		// TODO Auto-generated method stub
		return false;
	}

	public String toString() {
		return color + "R";
	}
}
