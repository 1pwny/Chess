package parts;

import java.util.ArrayList;

public class Queen extends Piece {

	private int column, row;
	private char color;

	public Queen(int c, int r) {
		column = c;
		row = r;

		color = (r == 7) ? 'b' : 'w';
	}
	public Queen(int c, int r, char co) {
		column = c;
		row = r;

		color = co;
	}

	@Override
	public boolean equals(Object obj) {

	    if (obj == null)
	    	return false;

	    if (obj == this)
	    	return true;

	    if (!(obj instanceof Queen))
	    	return false;

	    Queen o = (Queen) obj;
	    return this.row == o.row && this.column == o.column;
	}

	@Override
	public boolean threatens(int c, int r, Board b) {

		if(c == column && r == row)
			return false;

		//System.out.println("Checking if a queen at " + brak(column, row) + " threatens " + brak(c,r));
		
		int updown = PosOrNeg(r - row);
		int leftright = PosOrNeg(c - column);

		int tempc = column + leftright;
		int tempr = row + updown;
		
		for(int delta = 1; b.onBoard(tempc,tempr); delta++) {

			//System.out.println("Currently at " + brak(tempc, tempr));
			
			if(c == tempc && r == tempr)
				return true;

			if(b.filled(tempc, tempr))
				return false;

			tempc += leftright;
			tempr += updown;

		}

		return false;
	}

	//helper method for threatens
	public int PosOrNeg(int n) {
		if(n < 0)
			return -1;
		if(n == 0)
			return 0;
		return 1;
	}

	public String toString() {
		return getColor() + "Q";
	}
	@Override
	public void setRow(int r) {
		// TODO Auto-generated method stub
		row = r;
	}
	@Override
	public void setColumn(int c) {
		// TODO Auto-generated method stub
		column = c;
	}
	public char getColor() {
		return color;
	}
	@Override
	public int getRow() {
		// TODO Auto-generated method stub
		return row;
	}
	@Override
	public int getColumn() {
		// TODO Auto-generated method stub
		return column;
	}

	public static String brak(int a, int b) {
		return "[" + a + "," + b + "]";
	}
	
	@Override
	public ArrayList<Square> getAllMoves(Board b) {
		ArrayList<Square> moves = new ArrayList<Square>();

		//up
		for(int r = row + 1; r < 8; r++) {
			moves.add(new Square(column, r));

			if(b.filled(column, r))
				break;
		}

		//up-right
		for(int delta = 1; b.onBoard(column + delta, row + delta); delta++) {
			moves.add(new Square(column + delta, row + delta));

			if(b.filled(column + delta, row + delta))
				break;
		}

		//right
		for(int c = column + 1; c < 8; c++) {
			moves.add(new Square(c, row));

			if(b.filled(c, row))
				break;
		}

		//down-right
		for(int delta = 1; b.onBoard(column + delta, row - delta); delta++) {
			moves.add(new Square(column + delta, row - delta));

			if(b.filled(column + delta, row - delta))
				break;
		}

		//down
		for(int r = row - 1; r >= 0; r--) {
			moves.add(new Square(column, r));

			if(b.filled(column, r))
				break;
		}

		//down-left
		for(int delta = 1; b.onBoard(column - delta, row - delta); delta++) {
			moves.add(new Square(column - delta, row - delta));

			if(b.filled(column - delta, row - delta))
				break;
		}

		//left
		for(int c = column - 1; c >= 0; c--) {
			moves.add(new Square(c, row));

			if(b.filled(c, row))
				break;
		}

		//up-left
		for(int delta = 1; b.onBoard(column - delta, row + delta); delta++) {
			moves.add(new Square(column - delta, row + delta));

			if(b.filled(column - delta, row + delta))
				break;
		}

		return moves;
	}
}
