package hr.fer.zemris.java.gallery.rest.util;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.servlet.ServletContext;

import hr.fer.zemris.java.gallery.init.ServletContextProvider;

/**
 * Razred koji služi kao biblioteka za skaliranje i dohvat slika iz memorije.
 * Ovom razredu ne mogu se stvarati primjerci. Razred nudi sljedeće dvije
 * metode:
 * <ul>
 * <li>{@link #scaleImage(File, File)}</li>
 * <li>{@link #getImage(String)}</li>
 * </ul>
 * 
 * @author Davor Češljaš
 */
public class ImageUtil {

	/** Konstanta koja predstavlja širinu smanjene slike */
	private static final int THUMBNAIL_WIDTH = 150;

	/** Konstanta koja predstavlja visinu smanjene slike */
	private static final int THUMBNAIL_HEIGHT = 150;

	/**
	 * Privatni konstruktor koji služi tome da se primjerci ovog razreda ne mogu
	 * stvarati izvan samog razreda.
	 */
	private ImageUtil() {
	}

	/**
	 * Metoda koja se koristi za učitavanje slike čija je apstraktna
	 * reprezentacija poslana kao parametar <b>pictureFile</b>. Metoda nakon što
	 * učita sliku skalira sliku na veličinu {@value #THUMBNAIL_WIDTH} x
	 * {@value #THUMBNAIL_HEIGHT}. Potom se slika sprema na lokaciju predanu kao
	 * parametar <b>thumbnailFile</b>. Metoda po završetku smanjenu sliku vraća
	 * kao primjerak razreda {@link BufferedImage}
	 *
	 * @param pictureFile
	 *            apstraktna reprezentacija slike koja se umanjuje
	 * @param thumbnailFile
	 *            apstraktna reprezentacija lokakcije na koju se sprema umanjena
	 *            slika
	 * @return primjerak razreda {@link BufferedImage} koji predstavlja umanjenu
	 *         sliku
	 * @throws IOException
	 *             Ukoliko nije moguće pisati na lokaciju zadanu sa
	 *             <b>thumbnailFile</b> ili čitati sa lokacije
	 *             <b>pictureFile</b>
	 */
	public static BufferedImage scaleImage(File pictureFile, File thumbnailFile) throws IOException {
		BufferedImage picture = ImageIO.read(pictureFile);

		BufferedImage thumbnail = new BufferedImage(THUMBNAIL_WIDTH, THUMBNAIL_HEIGHT, BufferedImage.TYPE_3BYTE_BGR);
		Graphics2D g = thumbnail.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		g.drawImage(picture, 0, 0, THUMBNAIL_WIDTH, THUMBNAIL_HEIGHT, null);
		g.dispose();

		ImageIO.write(thumbnail, getExtension(thumbnailFile), thumbnailFile);

		return thumbnail;
	}

	/**
	 * Pomoćna metoda koja se koristi za parsiranje ekstenzije datoteke predane
	 * kao parametar <b>file</b>
	 *
	 * @param file
	 *            datoteka čija se ekstenzija traži
	 * @return ekstenziju datoteke
	 * @throws IndexOutOfBoundsException
	 *             Ukoliko nije moguće pronaći ekstenziju za datoteku, jer ona
	 *             ne postoji
	 */
	private static String getExtension(File file) {
		String fileName = file.getName();
		return fileName.substring(fileName.lastIndexOf('.') + 1);
	}

	/**
	 * Metoda koja se koristi za dohvat slike iz memorije, a koja se nalazi na
	 * lokaciji <b>filePath</b>. Metoda sliku vraća kao primjerak razreda
	 * {@link BufferedImage}
	 *
	 * @param filePath
	 *            apstraktna reprezentacija slike u memoriji
	 * @return primjerak razreda {@link BufferedImage} koji predstavlja učitanu
	 *         sliku
	 * @throws IOException
	 *             Ukoliko sliku na lokaciji <b>filePath</b> nije moguće učitati
	 */
	public static BufferedImage getImage(String filePath) throws IOException {
		ServletContext context = ServletContextProvider.getContext();

		return ImageIO.read(new File(context.getRealPath(filePath)));
	}
}
