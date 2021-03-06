package parts;

import java.util.ArrayList;
import java.util.List;
import java.util.function.*;

/**
 * 
 * @author Anand Raju
 * @author Sammy Berger
 *
 *
 * The board class is an 8x8 array of squares that hold all of the pieces. 
 * 
 * Keeps track of all the pieces that are currently in play, 
 * including which Pawn is able to be taken in an en passant manuver
 */

public class Board {

	public Square[][] board;
	public ArrayList<Piece> black_pieces;
	public ArrayList<Piece> white_pieces;

	public Pawn en_passant = null;
	public King black_king;
	public King white_king;

	/**
	 * Constructor for the board class, loads an 8x8 board with all black and white pieces
	 */
	public Board() {

		board = new Square[8][8];
		black_pieces = new ArrayList<Piece>();
		white_pieces = new ArrayList<Piece>();

		for(int column = 0; column < 8; column++)
			for(int row = 0; row < 8; row++) {
				board[column][row] = new Square(column,row);

				if(row < 2 || row > 5) {
					// there should be pieces in this square
					// [0,0] is bottom left, [7,7] is top right

					if(row == 1 || row == 6) {
						//we have a pawn!
						Pawn p = new Pawn(column, row);
						addPiecePlay(p.getColumn(), p.getRow(), p);

					} else {
						//we have some other piece
						switch(column) {

						case 0: case 7:
							//rook
							Rook r = new Rook(column, row);
							addPiecePlay(r.getColumn(), r.getRow(), r);
							break;

						case 1: case 6:
							//knight
							Knight n = new Knight(column, row);
							addPiecePlay(n.getColumn(), n.getRow(), n);
							break;

						case 2: case 5:
							//bishop
							Bishop b = new Bishop(column, row);
							addPiecePlay(b.getColumn(), b.getRow(), b);
							break;

						case 3:
							//YASSS QUEEN
							Queen q = new Queen(column, row);
							addPiecePlay(q.getColumn(), q.getRow(), q);
							break;

						case 4:
							//THE KING
							King k = new King(column, row);
							addPiecePlay(k.getColumn(), k.getRow(), k);
							break;
						}
					}
				}
			}
	}

	
	/*
	public static Board blankBoard() {
		Board b = new Board();
		b.board = new Square[8][8];
		b.black_pieces = new ArrayList<Piece>();
		b.white_pieces = new ArrayList<Piece>();
		b.black_king = null;
		b.white_king = null;
		
		for(int c = 0; c < 8; c++)
			for(int r = 0; r < 8; r++)
				b.board[c][r] = new Square(c,r);
		
		return b;
	} */
	
	public static String brak(int a, int b) {
		return "[" + a + "," + b + "]";
	}
	
	/**
	 * 
	 * @param c
	 * @param r
	 * @param p
	 * 
	 * Adds a new piece to the board that is in play
	 */
	public void addPiecePlay(int c, int r, Piece p) {

		if(p == null)
			return;
		
		board[c][r].putPiece(p);

		if(p.getColor() == 'w') {

			if(p instanceof King)
				white_king = (King)p;

			white_pieces.add(p);

		}

		else if(p.getColor() == 'b') {

			if(p instanceof King)
				black_king = (King)p;

			black_pieces.add(p);

		}

	}
	
	/**
	 * 
	 * 
	 * @param p
	 * 
	 * Overloaded method of addPiecePlay
	 */
	public void addPiecePlay(Piece p) {
		addPiecePlay(p.getColumn(), p.getRow(), p);
	}
	
	/**
	 * 
	 * @param s
	 * @return Returns square based on String rank and file input.
	 */
	public Square getTileAt(String s) {

		int row = s.charAt(0) - 'a';
		int col = Character.getNumericValue(s.charAt(1)) - 1;

		if(onBoard(row,col))
			return board[row][col];
		
		/*maybe we should just throw an error?*/
		int boundrow = 0, boundcol = 0;
		if(row > 7)
			boundrow = 7;
		if(col > 7)
			boundcol = 7;
		
		return board[boundrow][boundcol];

	}
	
	
	/**
	 * 
	 * @param s
	 * @return Returns tile based on Square
	 */
	public Square getTileAt(Square s) {
		return getTileAt(s.column, s.row);
	}
	
	
	/**
	 * 
	 * @param c
	 * @param r
	 * @return Returns tile based on Column and Row
	 */
	public Square getTileAt(int c, int r) {

		return board[c][r];

	}
	

	/**
	 * 
	 * @param oldspot
	 * @param newspot
	 * @param promote
	 * 
	 * Moves a Piece on the board from one spot or another based on S, if moving a pawn to end of enemy line, 
	 * uses String promote to determine pawn promotion
	 */
	public void movePiece(Square oldspot, Square newspot, String promote) {

		Piece piece = oldspot.removePiece();

		if(newspot.filled) {

			Piece captured = newspot.removePiece();

			if(captured.getColor() == 'w')
				white_pieces.remove(captured);

			else
				black_pieces.remove(captured);
		}
		
		int prom = (piece.getColor() == 'w') ? 7 : 0;
		
		if(piece instanceof Pawn && newspot.row == prom) {
			
			Promotion(promote, newspot.column, newspot.row);
		}
		
		else {
			
			newspot.putPiece(piece);
		}
	}
	
	/**
	 * 
	 * @param oc
	 * @param or
	 * @param nc
	 * @param nr
	 * 
	 * Overloaded method of movePiece that takes in only rows and columns
	 */
	public void movePiece(int oc, int or, int nc, int nr) {
		
		Piece piece = board[oc][or].removePiece();
		//System.out.println("Moving Piece: " + piece + " to: " + board[nc][nr]);
		
		if(piece != null) {
			
			if(board[nc][nr].filled) {
	
				Piece captured = board[nc][nr].removePiece();
	
				if(captured.getColor() == 'w')
					white_pieces.remove(captured);
	
				else
					black_pieces.remove(captured);
			}
	
			board[nc][nr].putPiece(piece);
		
		}
		
	}
	
	/**
	 * 
	 * @param oldspot
	 * @param newspot
	 * @param promote
	 * @return Returns true if board is able to physically move a piece into a spot
	 */
	public boolean tryMove(Square oldspot, Square newspot, String promote) {
		if(!transientmove(oldspot, newspot)) {
			System.out.println("failed the transient move");
			return false;
		}
		
		if(!oldspot.p.moveTo(newspot.column, newspot.row, this)) {
			System.out.println("moveTo failed");
			return false;
		}
		
		movePiece(oldspot, newspot, promote);
		
		return true;
	}
	public boolean tryMove(int oc, int or, int nc, int nr) {
		
		if(!transientmove(oc, or, nc, nr))
			return false;
		
		if(!(board[oc][or].p.moveTo(nc, nr, this)))
			return false;
		
		movePiece(oc, or, nc, nr);
		
		return true;
	}
	
	//just check if a move is possible, don't actually do it
	public boolean transientmove(Piece p, Square s) {
		//kings have their own unique movement stuff that actually already checks for all this
		//so just use their method!
		if(p instanceof King)
			return ((King) p).transientMoveTo(s.column, s.row, this);
		
		/* Steps:
		 * 1 - save the old piece and en passant
		 * 2 - check if you can actually move there
		 * 3 - move there (temporarily)
		 * 4 - check for opposing check
		 * 5 - undo move new piece
		 * 6 - replace old piece and en passant
		 * 7 - return findings
		 */
		
		boolean hm = true;
		if(p instanceof Pawn)
			hm = ((Pawn) p).hasmoved;
		if(p instanceof Rook)
			hm = ((Rook) p).hasmoved;
		
		
		//1 - save the old piece
		Piece prev = s.p;
		Pawn ep = en_passant;
		
		//2 - check if you can actually move there
		if(!p.moveTo(s.column, s.row, this))
			return false;
		
		//3 - move there (temporarily)
		int oldc = p.getColumn(), oldr = p.getRow();
		movePiece(p.getColumn(), p.getRow(), s.column, s.row);
		
		//4 - check for opposing check
		boolean ret = !inCheck(p.getColor());
		
		//System.out.println("In check after " + p + " to " + s.pos() + ": " + inCheck(p.color));
		
		//5 - undo move new piece
		movePiece(s.column, s.row, oldc, oldr);
		if(!hm) {
			if(p instanceof Pawn)
				((Pawn) p).hasmoved = hm;
			else if(p instanceof Rook)
				((Rook) p).hasmoved = hm;
		}
		
		//6 - replace old piece and en passant
		addPiecePlay(s.column, s.row, prev);
		en_passant = ep;
		
		//7 - return findings
		return ret;
		
	}
	public boolean transientmove(int oc, int or, int nc, int nr) {
		return transientmove(board[oc][or].p, board[nc][nr]);
	}
	public boolean transientmove(Square os, Square ns) {
		return transientmove(os.p, ns);
	}
	
	public void Promotion(String promote, int column, int row) {
		
		switch(promote.toLowerCase()) {

			case "rook":
				//rook
				Rook rook = new Rook(column, row);
				addPiecePlay(rook.getColumn(), rook.getRow(), rook);
				break;
	
			case "knight":
				//knight
				Knight n = new Knight(column, row);
				addPiecePlay(n.getColumn(), n.getRow(), n);
				break;
	
			case "bishop":
				//bishop
				Bishop b = new Bishop(column, row);
				addPiecePlay(b.getColumn(), b.getRow(), b);
				break;
	
			default:
				//YASSS QUEEN
				Queen q = new Queen(column, row);
				addPiecePlay(q.getColumn(), q.getRow(), q);
				break;
	
		}
		
	}
	
	// where you determine if there's checkmate or not;
	public boolean resolve_check(List<Piece> checks, King k) {

			ArrayList<Square> king_spots = k.getAllMoves(this);

			if(king_spots.size() > 0)
				return false;

			System.out.println("got here");
			
			//with double check, the only possible way to escape is for the king move to a safe spot.
			if(checks.size() < 2) {
				
				List<Piece> ally = new ArrayList<Piece>();
				List<Piece> enemy = new ArrayList<Piece>();
				
				if(k.getColor() == 'w') {
					
					ally = white_pieces;
					ally.remove(white_king);
					enemy = black_pieces;
					enemy.remove(black_king);
				}
				
				else {
					
					ally = black_pieces;
					ally.remove(black_king);
					enemy = white_pieces;
					enemy.remove(white_king);
				}
				
				Piece threat = checks.get(0);
				List<Piece> eliminate_check = threatens_spot(ally, checks.get(0).getColumn(), checks.get(0).getRow());
				List<Piece> blocks_check = filter(ally, p -> p.canBlockPiece(threat, (Piece)k, this));

				if(!eliminate_check.isEmpty() || !blocks_check.isEmpty())
					return false;
			}

			return true;
		}
	public boolean canMove(char c) {
		ArrayList<Square> allPieces = new ArrayList<Square>();
		
		for(Piece p: ((c == 'w') ? white_pieces : black_pieces)) {
			allPieces.add(new Square(p.getColumn(), p.getRow()));
		}
		
		//System.out.println(allPieces.size());
		
		for(Square loc: allPieces) {
			Piece p = board[loc.column][loc.row].p;
			
			if(p == null) {
				//System.out.println(loc.pos());
				continue;
			}
			
			for(Square s: p.getAllMoves(this))
				if(transientmove(p,s)) {
					//System.out.println(p + " to " + s.pos());
					return true;
				}
		}
		
		
		
		return false;
	}
	
	//methods to find what pieces threaten a certain spot
	public static <T> List<T> filter(List<T> list, Predicate<T> p){

		List<T> result = new ArrayList<T>();

		for(T t: list) {

			if(p.test(t))
				result.add(t);
		}

		return result;
	}
	public List<Piece> threatens_spot(List<Piece> list, int c, int r) {
		// TODO Auto-generated method stub
		return filter(list, p -> p.threatens(c, r, this));
	}
	
	
	/** Minor methods that can be reused often to save lines of code **/
	
	//checks if a color is in check, or if a general piece of a color in a square would be threatened
	public boolean inCheck(char c) {
		King k = (c == 'w') ? white_king : black_king;
		
		return threatened(k.getColumn(), k.getRow(), c);
	}
	public boolean threatened(int c, int r, char color) {

		if(color == 'w') {
			for(Piece p: black_pieces)
				if(p.threatens(c, r, this))
					return true;
		}

		if(color == 'b') {
			for(Piece p: white_pieces)
				if(p.threatens(c, r, this))
					return true;
		}
		
		return false;
	}
	
	//to check if there's a piece at a given spot on the board
	public boolean filled(int c, int r) {
		return board[c][r].filled;
	}
	public boolean filled(Square s) {
		return board[s.column][s.row].filled;
	}

	//checks if a given row/column is on the board
	public boolean onBoard(int c, int r) {
		return (c >= 0) && (c < 8) && (r >= 0) && (r < 8);
	}
	public boolean onBoard(Square s) {
		return onBoard(s.column, s.row);
	}

	//checks the color of a given square
	public char colorAt(int c, int r) {
		if(!filled(c,r))
			return 'n';
		return board[c][r].p.getColor();
	}
	
	//gets you the piece at a location
	public Piece pieceAt(int c, int r) {
		return board[c][r].p;
	}
	
	//returns the string of the board
	public String toString() {
		String ret = "";

		for(int r = 7; r >= 0; r--) {
			for(int c = 0; c < 8; c++) {
				ret += board[c][r] + " ";
			}
			ret += (r+1) + "\n";
		}
		
		for(char c = 'a'; c <= 'h'; c++) {
			ret += " " + c;
			
			if(c != 'h')
				ret += " ";
		}
		
		return ret;
	}
	public void printBoard() {
		System.out.println(toString() + "\n");
	}


	/* public static void main(String[] args) {

		Board b = blankBoard();
		
		b.addPiecePlay(new King(3,3,'b'));
		b.addPiecePlay(new Rook(2,2,'w'));
		b.addPiecePlay(new Rook(4,4,'w'));
		b.addPiecePlay(new Rook(2,4,'w'));
		
		b.tryMove(3, 3, 4, 3);
		
		boolean inCheck = b.inCheck('b');
		boolean cantMove = !b.canMove('b');
		
		b.printBoard();
		if(cantMove && inCheck) {
			System.out.println("Checkmate");
		}
		else if(cantMove && !inCheck) {
			System.out.println("Stalemate");
		}
		else if(!cantMove && inCheck) {
			System.out.println("Check");
		} else {
			System.out.println("Not in check and can move?");
		}
	} */
}
