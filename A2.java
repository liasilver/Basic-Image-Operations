package a2;

import java.awt.Color;
import java.io.File;
import java.net.URI;
import java.net.URL;

public class A2 {
	/**
	 * The original image
	 */
	private static Picture orig;

	/**
	 * The image viewer class
	 */
	private static A2Viewer viewer;

	/**
	 * Returns a 300x200 image containing the Queen's flag (without the crown).
	 * 
	 * @return an image containing the Queen's flag
	 */
	public static Picture flag() {
		Picture img = new Picture(300, 200);
		int w = img.width();
		int h = img.height();

		// set the pixels in the blue stripe
		Color blue = new Color(0, 48, 95);
		for (int col = 0; col < w / 3; col++) {
			for (int row = 0; row < h - 1; row++) {
				img.set(col, row, blue);
			}
		}

		// set the pixels in the yellow stripe
		Color yellow = new Color(255, 189, 17);
		for (int col = w / 3; col < 2 * w / 3; col++) {
			for (int row = 0; row < h - 1; row++) {
				img.set(col, row, yellow);
			}
		}

		// set the pixels in the red stripe
		Color red = new Color(185, 17, 55);
		for (int col = 2 * w / 3; col < w; col++) {
			for (int row = 0; row < h - 1; row++) {
				img.set(col, row, red);
			}
		}
		return img;
	}

	/**
	 * Copies the original image
	 * 
	 * @param original picture p
	 * @return the copy of original image
	 */
	public static Picture copy(Picture p) {
		Picture result = new Picture(p.width(), p.height());

		int w = result.width();
		int h = result.height();

		for (int col = 0; col < w; col++) {
			for (int row = 0; row < h; row++) {
				Color p_color = new Color(p.getRGB(col, row));
				result.set(col, row, p_color);
			}
		}
		return result;
	}

	/**
	 * Creates a blue border around original image
	 * 
	 * @param original picture p
	 * @param desired  border width border_width
	 * @return image with a blue border
	 */

	public static Picture border(Picture p, int border_width) {
		Picture img = new Picture(p.width(), p.height());

		int w = img.width();
		int h = img.height();

		for (int col = 0; col < w; col++) {
			for (int row = 0; row < h; row++) {
				Color p_color = new Color(p.getRGB(col, row));
				img.set(col, row, p_color);

				// if col/row falls in border width color blue
				if (col < border_width - 1 || col > w - border_width || row < border_width - 1
						|| row > h - border_width) {
					img.set(col, row, Color.BLUE);
				}
			}
		}

		return img;
	}

	/**
	 * Converts a picture to grayscale
	 * 
	 * @param original picture p
	 * @return grayscale version of image
	 */
	public static Picture to_gray(Picture p) {
		Picture img = new Picture(p.width(), p.height());

		int w = img.width();
		int h = img.height();

		for (int col = 0; col < w; col++) {
			for (int row = 0; row < h; row++) {
				Color p_color = new Color(p.getRGB(col, row));
				int r = p_color.getRed();
				int g = p_color.getGreen();
				int b = p_color.getBlue();
				int gray = (int) (Math.round(0.2989 * r + 0.5870 * g + 0.1140 * b));
				Color grayscale = new Color(gray, gray, gray);

				img.set(col, row, grayscale);
			}
		}

		return img;
	}

	/**
	 * Converts image to binary colors
	 * 
	 * @param original picture p
	 * @param first    color c1
	 * @param second   color c2
	 * @return image with 2 colors
	 */
	public static Picture to_binary(Picture p, Color c1, Color c2) {
		Picture img = new Picture(p.width(), p.height());

		int w = img.width();
		int h = img.height();

		// convert to grayscale
		for (int col = 0; col < w; col++) {
			for (int row = 0; row < h; row++) {
				Color p_color = new Color(p.getRGB(col, row));
				int r = p_color.getRed();
				int g = p_color.getGreen();
				int b = p_color.getBlue();
				int gray = (int) (Math.round(0.2989 * r + 0.5870 * g + 0.1140 * b));
				Color grayscale = new Color(gray, gray, gray);

				img.set(col, row, grayscale);
			}
		}

		// average colors in grayscale and choose c1 or c2
		for (int col = 0; col < w; col++) {
			for (int row = 0; row < h; row++) {

				Color p_color = new Color(p.getRGB(col, row));
				int r = p_color.getRed();
				int g = p_color.getGreen();
				int b = p_color.getBlue();
				int gray = (int) (Math.round(0.2989 * r + 0.5870 * g + 0.1140 * b));

				if (gray < 128) {
					img.set(col, row, c1);
				} else {
					img.set(col, row, c2);
				}
			}
		}

		return img;
	}

	/**
	 * Vertically flips original image
	 * 
	 * @param original picture p
	 * @return vertically flipped image
	 */
	public static Picture flip_vert(Picture p) {
		Picture img = new Picture(p.width(), p.height());

		int w = img.width();
		int h = img.height();

		for (int col = 0; col < w; col++) {
			for (int row = 0; row < h; row++) {
				Color p_color = new Color(p.getRGB(col, row));
				img.set(col, h - row - 1, p_color);
			}
		}
		return img;
	}

	/**
	 * Rotates an image 90 deg clockwise
	 * 
	 * @param original picture p
	 * @return image rotated
	 */
	public static Picture rotate_r(Picture p) {
		Picture img = new Picture(p.height(), p.width());

		int w = img.width();
		int h = img.height();

		for (int col = 0; col < p.width(); col++) {
			for (int row = 0; row < p.height(); row++) {
				Color p_color = new Color(p.getRGB(col, row));
				img.set(w - row - 1, col, p_color);
			}
		}

		return img;
	}

	/**
	 * Corrects red eyes in images
	 * 
	 * @param original picture p
	 * @return original picture without red eyes
	 */
	public static Picture red_eye(Picture p) {
		Picture img = new Picture(p.width(), p.height());

		int w = img.width();
		int h = img.height();

		Color black = new Color(0, 0, 0);

		for (int col = 0; col < w; col++) {
			for (int row = 0; row < h; row++) {

				Color p_color = new Color(p.getRGB(col, row));
				int r = p_color.getRed();
				int g = p_color.getGreen();
				int b = p_color.getBlue();

				// if pixel is red
				if (g < 76 && b < 200 && r > 110) {
					img.set(col, row, black);
				} else {
					img.set(col, row, p_color);
				}

			}
		}

		return img;
	}

	/**
	 * Blurs an image with box-blur
	 * 
	 * @param original picture p
	 * @param radius   of box
	 * @return blurred image
	 */
	public static Picture blur(Picture p, int radius) {
		Picture img = new Picture(p.width(), p.height());

		int w = img.width();
		int h = img.height();

		for (int col = 0; col < w; col++) {
			for (int row = 0; row < h; row++) {

				int r = 0;
				int g = 0;
				int b = 0;

				// default start/stop values
				int col2_start = col - radius;
				int col2_stop = col + radius * 2 + 1;
				int row2_start = row - radius;
				int row2_stop = row + radius * 2 + 1;

				// if box goes past any boundaries, change start/stop
				if (col - radius < 0) {
					col2_start = 0;
				} else if (col + radius * 2 + 1 >= w) {
					col2_stop = w;
				}
				if (row - radius < 0) {
					row2_start = 0;
				} else if (row + radius * 2 + 1 >= h) {
					row2_stop = h;
				}

				int area = 0;

				// loop through box surrounding pixel & save RGB values
				for (int col2 = col2_start; col2 < col2_stop; col2++) {
					for (int row2 = row2_start; row2 < row2_stop; row2++) {
						Color p_color = new Color(p.getRGB(col2, row2));
						r += p_color.getRed();
						g += p_color.getGreen();
						b += p_color.getBlue();
						area++;
					}
				}
				Color avg_color = new Color((int) r / (area), (int) g / (area), (int) b / (area));
				img.set(col, row, avg_color);
			}
		}
		return img;
	}

	/**
	 * A2Viewer class calls this method when a menu item is selected. This method
	 * computes a new image and then asks the viewer to display the computed image.
	 * 
	 * @param op the operation selected in the viewer
	 */
	public static void processImage(String op) {

		switch (op) {
		case A2Viewer.FLAG:
			// create a new image by copying the original image
			Picture p = A2.flag();
			A2.viewer.setComputed(p);
			break;
		case A2Viewer.COPY:
			// create a new image by copying the original image
			p = A2.copy(A2.orig);
			A2.viewer.setComputed(p);
			break;
		case A2Viewer.BORDER_1:
			// create a new image by adding a border of width 1 to the original image
			p = A2.border(A2.orig, 1);
			A2.viewer.setComputed(p);
			break;
		case A2Viewer.BORDER_5:
			// create a new image by adding a border of width 5 the original image
			p = A2.border(A2.orig, 5);
			A2.viewer.setComputed(p);

			break;
		case A2Viewer.BORDER_10:
			// create a new image by adding a border of width 10 the original image
			p = A2.border(A2.orig, 10);
			A2.viewer.setComputed(p);

			break;
		case A2Viewer.TO_GRAY:
			// create a new image by converting the original image to grayscale
			p = A2.to_gray(A2.orig);
			A2.viewer.setComputed(p);

			break;
		case A2Viewer.TO_BINARY:
			// create a new image by converting the original image to black and white
			p = A2.to_binary(A2.orig, Color.BLACK, Color.WHITE);
			A2.viewer.setComputed(p);

			break;
		case A2Viewer.FLIP_VERTICAL:
			// create a new image by flipping the original image vertically
			p = A2.flip_vert(A2.orig);
			A2.viewer.setComputed(p);

			break;
		case A2Viewer.ROTATE_RIGHT:
			// create a new image by rotating the original image to the right by 90 degrees
			p = A2.rotate_r(A2.orig);
			A2.viewer.setComputed(p);

			break;
		case A2Viewer.RED_EYE:
			// create a new image by removing the redeye effect in the original image
			p = A2.red_eye(A2.orig);
			A2.viewer.setComputed(p);

			break;
		case A2Viewer.BLUR_1:
			// create a new image by blurring the original image with a box blur of radius 1
			p = A2.blur(A2.orig, 1);
			A2.viewer.setComputed(p);
			break;
		case A2Viewer.BLUR_3:
			// create a new image by blurring the original image with a box blur of radius 3
			p = A2.blur(A2.orig, 3);
			A2.viewer.setComputed(p);
			break;
		case A2Viewer.BLUR_5:
			// create a new image by blurring the original image with a box blur of radius 5
			p = A2.blur(A2.orig, 5);
			A2.viewer.setComputed(p);

			break;
		default:
			// do nothing
		}
	}

	/**
	 * Starting point of the program. Students can comment/uncomment which image to
	 * use when testing their program.
	 * 
	 * @param args not used
	 */
	public static void main(String[] args) {
		A2.viewer = new A2Viewer();
		A2.viewer.setVisible(true);

		URL img;
		// uncomment one of the next two lines to choose which test image to use (person
		// or cat)
		img = A2.class.getResource("redeye-400x300.jpg");
		// img = A2.class.getResource("cat.jpg");

		try {
			URI uri = new URI(img.toString());
			A2.orig = new Picture(new File(uri.getPath()));
			A2.viewer.setOriginal(A2.orig);
		} catch (Exception x) {

		}
	}

}
