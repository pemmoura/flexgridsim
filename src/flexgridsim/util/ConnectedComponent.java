package flexgridsim.util;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.HashMap;

import flexgridsim.Slot;

/**
 * Use row-by-row labeling algorithm to label connected components The algorithm
 * makes two passes over the image: one pass to record equivalences and assign
 * temporary labels and the second to replace each temporary label by the label
 * of its equivalence class. [Reference] Linda G. Shapiro, Computer Vision:
 * Theory and Applications. (3.4 Connected Components Labeling) Rosenfeld and
 * Pfaltz (1966)
 */
public class ConnectedComponent {
	final int MAX_LABELS = 80000;
	int next_label = 1;

	/**
	 * @param image
	 * @return a map of lists of pixels
	 */
	public HashMap<Integer, ArrayList<Slot>> listOfRegions(boolean[][] image) {
		HashMap<Integer, ArrayList<Slot>> res = new HashMap<Integer, ArrayList<Slot>>();
		// System.out.println("Image " +image.length+","+image[0].length);
		int resMatrix[][] = componentLabeling(image);
		// System.out.println("Res " +resMatrix.length+","+resMatrix[0].length);
		for (int i = 0; i < resMatrix.length; i++) {
			for (int j = 0; j < resMatrix[i].length; j++) {
				if (resMatrix[i][j] != 1) {
					if (!res.containsKey(resMatrix[i][j])) {
						res.put(resMatrix[i][j], new ArrayList<Slot>());
					}
					res.get(resMatrix[i][j]).add(new Slot(i, j));
				}
			}
		}
		return res;
	}

	/**
	 * @param image
	 * @return matrix containing labels
	 */
	public int[][] componentLabeling(boolean[][] image) {
		int rows = image.length;
		int columns = image[0].length;
		int[] integerImage = new int[image.length * image[0].length];
		for (int i = 0; i < image.length; i++) {
			for (int j = 0; j < image[0].length; j++) {
				if (image[i][j] == true) {
					integerImage[i * image[i].length + j] = 0;
				} else {
					integerImage[i * image[i].length + j] = 1;
				}
			}
		}
		int res[] = compactLabeling(integerImage, new Dimension(rows, columns), true);
		int resMatrix[][] = new int[rows][columns];
		int j = 0;
		int k = 0;
		for (int i = 0; i < res.length; i++) {
			resMatrix[k][j] = res[i];
			j++;
			if (j == columns) {
				k++;
				j = 0;
			}

		}
		return resMatrix;
	}

	/**
	 * label and re-arrange the labels to make the numbers of label continous
	 * 
	 * @param image
	 * @param d
	 * 
	 * @param zeroAsBg
	 *            Leaving label 0 untouched
	 * @return the labels
	 */
	public int[] compactLabeling(int[] image, Dimension d, boolean zeroAsBg) {
		// label first
		int[] label = labeling(image, d, zeroAsBg);
		int[] stat = new int[next_label + 1];
		for (int i = 0; i < image.length; i++) {
			if (label[i] > next_label)
				System.err.println("bigger label than next_label found!");
			stat[label[i]]++;
		}

		stat[0] = 0; // label 0 will be mapped to 0
						// whether 0 is background or not
		int j = 1;
		for (int i = 1; i < stat.length; i++) {
			if (stat[i] != 0)
				stat[i] = j++;
		}

		// System.out.println("From " + next_label + " to " + (j - 1) + "
		// regions");
		next_label = j - 1;
		for (int i = 0; i < image.length; i++)
			label[i] = stat[label[i]];
		return label;
	}

	/**
	 * @return the max label in the labeling process. the range of labels is
	 *         [0..max_label]
	 */
	public int getMaxLabel() {
		return next_label;
	}

	/**
	 * Label the connect components If label 0 is background, then label 0 is
	 * untouched; If not, label 0 may be reassigned [Requires] 0 is treated as
	 * background
	 * 
	 * @param image
	 *            data
	 * @param d
	 *            dimension of the data
	 * @param zeroAsBg
	 *            label 0 is treated as background, so be ignored
	 * @return the labels
	 */
	public int[] labeling(int[] image, Dimension d, boolean zeroAsBg) {
		int w = d.width, h = d.height;
		int[] rst = new int[w * h];
		int[] parent = new int[MAX_LABELS];
		int[] labels = new int[MAX_LABELS];
		// region label starts from 1;
		// this is required as union-find data structure
		int next_region = 1;
		for (int y = 0; y < h; ++y) {
			for (int x = 0; x < w; ++x) {
				if (image[y * w + x] == 0 && zeroAsBg)
					continue;
				int k = 0;
				boolean connected = false;
				// if connected to the left
				if (x > 0 && image[y * w + x - 1] == image[y * w + x]) {
					k = rst[y * w + x - 1];
					connected = true;
				}
				// if connected to the up
				if (y > 0 && image[(y - 1) * w + x] == image[y * w + x]
						&& (connected = false || image[(y - 1) * w + x] < k)) {
					k = rst[(y - 1) * w + x];
					connected = true;
				}
				if (!connected) {
					k = next_region;
					next_region++;
				}

				if (k >= MAX_LABELS) {
					System.err.println("maximum number of labels reached. " + "increase MAX_LABELS and recompile.");
					System.exit(1);
				}
				rst[y * w + x] = k;
				// if connected, but with different label, then do union
				if (x > 0 && image[y * w + x - 1] == image[y * w + x] && rst[y * w + x - 1] != k)
					uf_union(k, rst[y * w + x - 1], parent);
				if (y > 0 && image[(y - 1) * w + x] == image[y * w + x] && rst[(y - 1) * w + x] != k)
					uf_union(k, rst[(y - 1) * w + x], parent);
			}
		}

		// Begin the second pass. Assign the new labels
		// if 0 is reserved for background, then the first available label is 1
		next_label = 1;
		for (int i = 0; i < w * h; i++) {
			if (image[i] != 0 || !zeroAsBg) {
				rst[i] = uf_find(rst[i], parent, labels);
				// The labels are from 1, if label 0 should be considered, then
				// all the label should minus 1
				if (!zeroAsBg)
					rst[i]--;
			}
		}
		next_label--; // next_label records the max label
		if (!zeroAsBg)
			next_label--;

		// System.out.println(next_label + " regions");

		return rst;
	}

	void uf_union(int x, int y, int[] parent) {
		while (parent[x] > 0)
			x = parent[x];
		while (parent[y] > 0)
			y = parent[y];
		if (x != y) {
			if (x < y)
				parent[x] = y;
			else
				parent[y] = x;
		}
	}

	/**
	 * This function is called to return the root label Returned label starts
	 * from 1 because label array is inited to 0 as first [Effects] label array
	 * records the new label for every root
	 */
	int uf_find(int x, int[] parent, int[] label)

	{
		while (parent[x] > 0)
			x = parent[x];
		if (label[x] == 0)
			label[x] = next_label++;
		return label[x];
	}
}