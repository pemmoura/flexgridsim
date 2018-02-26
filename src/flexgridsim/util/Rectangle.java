package flexgridsim.util;

/**
 * @author pedrom
 *
 */
public class Rectangle {
	private int rowStart;
	private int colStart;
	private int rowEnd;
	private int colEnd;
	
	/**
	 * @param rowStart
	 * @param colStart
	 * @param rowEnd
	 * @param colEnd
	 */
	public Rectangle(int rowStart, int colStart, int rowEnd, int colEnd) {
		super();
		this.rowStart = rowStart;
		this.colStart = colStart;
		this.rowEnd = rowEnd;
		this.colEnd = colEnd;
	}


	/**
	 * @return starting row of the rectangle
	 */
	public int getRowStart() {
		return rowStart;
	}


	/**
	 * @return starting column of the rectangle
	 */
	public int getColStart() {
		return colStart;
	}


	/**
	 * @return ending row of the rectangle
	 */
	public int getRowEnd() {
		return rowEnd;
	}


	/**
	 * @return ending column of the rectangle
	 */
	public int getColEnd() {
		return colEnd;
	}


	/**
	 * @return width of the reclangle
	 */
	public int getWidth() {
		return colEnd-colStart+1;
	}

	/**
	 * @return height of the reclangle
	 */
	public int getHeight() {
		return rowEnd-rowStart+1;
	}
	
	/**
	 * @return area of the reclangle
	 */
	public int getSize() {
		return getWidth()*getHeight();
	}

	/**
	 * @param r the potential subrectangle
	 * @return true if this rectangle contains r; false otherwise
	 */
	public boolean contains(Rectangle r){
		if (colStart<=r.colStart && colEnd>=r.colEnd && rowStart<=r.getRowStart() && rowEnd>=r.getRowEnd())
			return true;
		else 
			return false;
	}
	
	@Override
	public String toString() {
		return "Rectangle [size" + getWidth()*getHeight() + " row=" + rowStart + ", col=" + colStart + ", rowEnd=" + rowEnd + ", colEnd=" + colEnd + "]";
	}
	
	
	
}
