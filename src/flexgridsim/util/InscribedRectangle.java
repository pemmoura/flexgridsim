package flexgridsim.util;

import java.util.ArrayList;

/**
 * @author pedrom
 *
 */
public class InscribedRectangle {

	int GetSize(int width, int height) {
		return (width * height);
	}

	private int[][] calculateSquares(int height, int width, boolean mask[][]) {
		int[][] squares = new int[height][width];

		int row;
		int col;

		// process bottom boundary of the mask
		row = (height - 1);

		for (col = 0; col < width; col++) {
			if (mask[row][col]) {
				squares[row][col] = 1;
			}
		}

		// process right boundary of the mask
		col = (width - 1);

		for (row = 0; row < height; row++) {
			if (mask[row][col]) {
				squares[row][col] = 1;
			}
		}

		// process internal pixels of the mask
		for (row = (height - 2); row >= 0; row--) {
			for (col = (width - 2); col >= 0; col--) {
				if (mask[row][col]) {
					int a = squares[row][col + 1];
					int b = squares[row + 1][col];
					int c = squares[row + 1][col + 1];

					squares[row][col] = (Math.min(Math.min(a, b), c) + 1);
				}
			}
		}
		return squares;
	}

	/**
	 * @param height 
	 * @param width
	 * @param mask mask of the elements
	 * @return list of rectangles
	 */
	public ArrayList<Rectangle> calculateRectangles(int height, int width, boolean mask[][]) {
		int[][] squares = calculateSquares(height, width, mask);

		int[][] sizes = new int[height][width];
		int square;
		int maxSquare = 0;

		for (int row = 0; row < height; row++) {
			for (int col = 0; col < width; col++) {
				square = squares[row][col];

				sizes[row][col] = GetSize(square, square);

				if (square > maxSquare) {
					maxSquare = square;
				}
			}
		}

		// find largest rectangles with width >= height
		int[] height2width = new int[maxSquare + 1];

		int[][] widths = new int[height][width];
		int[][] heights = new int[height][width];

		int maxSize;
		int rectWidth;
		int rectHeight;
		int size;

		for (int row = 0; row < height; row++) {
			for (int s = 0; s <= maxSquare; s++) {
				height2width[s] = 0;
			}

			for (int col = (width - 1); col >= 0; col--) {
				square = squares[row][col];
				if (square > 0) {
					maxSize = sizes[row][col];

					for (rectHeight = square; rectHeight > 0; rectHeight--) {
						rectWidth = height2width[rectHeight];
						rectWidth = Math.max(rectWidth + 1, square);
						height2width[rectHeight] = rectWidth;
						size = GetSize(rectWidth, rectHeight);
						if (size >= maxSize) {
							maxSize = size;
							widths[row][col] = rectWidth;
							heights[row][col] = rectHeight;
						}
					}

					sizes[row][col] = maxSize;
				}

				for (int s = (square + 1); s <= maxSquare; s++) {
					// widths larger that 'square' will not be available
					height2width[s] = 0;
				}
			}
		}

		// find largest rectangles with width < height
		int[] width2height = new int[maxSquare + 1];

		for (int col = 0; col < width; col++) {
			for (int s = 0; s <= maxSquare; s++) {
				width2height[s] = 0;
			}

			for (int row = (height - 1); row >= 0; row--) {
				square = squares[row][col];

				if (square > 0) {
					maxSize = sizes[row][col];

					for (rectWidth = square; rectWidth > 0; rectWidth--) {
						rectHeight = width2height[rectWidth];
						rectHeight = Math.max(rectHeight + 1, square);
						width2height[rectWidth] = rectHeight;
						size = GetSize(rectWidth, rectHeight);

						if (size > maxSize) {
							maxSize = size;
							widths[row][col] = rectWidth;
							heights[row][col] = rectHeight;
						}
					}

					sizes[row][col] = maxSize;
				}

				for (int s = (square + 1); s <= maxSquare; s++) {
					// heights larger that 'square' will not be available
					width2height[s] = 0;
				}
			}
		}

		// find the largest rectangle
		ArrayList<Rectangle> rectangles = new ArrayList<Rectangle>();
		for (int row = 0; row < height; row++) {
			for (int col = 0; col < width; col++) {
				size = sizes[row][col];
				int widthx = col+widths[row][col]-1;
				int heightx = row+heights[row][col]-1;
				if (size>0){
					Rectangle r = new Rectangle(row, col, heightx, widthx);
					boolean add = true;
					for (int i = 0; i < rectangles.size(); i++) {
						if (rectangles.get(i).contains(r)){
							add = false;
						}
						if (r.contains(rectangles.get(i))){
							rectangles.remove(i);
						}
					}
					if (add)
						rectangles.add(r);
					else 
						System.out.print("");
				}
			}
		}

		return rectangles;
	}
	
	
}
