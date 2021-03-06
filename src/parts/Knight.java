package parts;

import java.util.ArrayList;

public class Knight extends Piece {

	private int column, row;
	private char color;

	public Knight(int c, int r) {
		column = c;
		row = r;

		color = (r == 7) ? 'b' : 'w';
	}

	@Override
	public boolean equals(Object obj) {

	    if (obj == null)
	    	return false;

	    if (obj == this)
	    	return true;

	    if (!(obj instanceof Knight))
	    	return false;

	    Knight o = (Knight) obj;
	    return this.row == o.row && this.column == o.column;
	}

	@Override
	public boolean threatens(int c, int r, Board b) {
		// TODO Auto-generated method stub

		if(c == column || r == row) {
			return false;
		}


		int r_mod_1 = (r > row) ? 2 : -2;
		int r_mod_2 = (r_mod_1 > 0) ? -1 : 1;

		int c_mod_1 = (c > column) ? 1 : -1;
		int c_mod_2 = c_mod_1;


		int rindex_1 = row + r_mod_1;
		int cindex_1 = column + c_mod_1;

		int rindex_2 = rindex_1 + r_mod_2;
		int cindex_2 = cindex_1 + c_mod_2;

		if((rindex_1 >= 0 && rindex_1 < 8) && (cindex_1 >= 0 && cindex_1 < 8)){

			if(r == rindex_1 && c == cindex_1)
				return true;
		}

		if(rindex_2 >= 0 && rindex_2 < 8 && cindex_2 >= 0 && cindex_2 < 8){

			if(r == rindex_2 && c == cindex_2)
				return true;
		}

		return false;
	}

	public String toString() {
		return getColor() + "N";
	}

	@Override
	public ArrayList<Square> getAllMoves(Board b) {
		// TODO Auto-generated method stub
		ArrayList<Square> moves = new ArrayList<Square>();
		Pawn o_pas = b.en_passant;

		for(int col = 0; col < 8; col++) {

			for(int ro = 0; ro < 8; ro++) {

				if(moveTo(col, ro, b)) {
					b.en_passant = o_pas;
					moves.add(b.board[col][ro]);
				}
			}
		}

		return moves;
	}

	@Override
	public void setRow(int r) {
		row = r;
	}
	@Override
	public void setColumn(int c) {
		column = c;
	}
	public char getColor() {
		return color;
	}
	public int getRow() {
		return row;
	}
	public int getColumn() {
		return column;
	}
}
