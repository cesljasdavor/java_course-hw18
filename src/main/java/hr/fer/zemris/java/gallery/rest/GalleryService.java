package hr.fer.zemris.java.gallery.rest;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.function.Supplier;

import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.google.gson.Gson;

import hr.fer.zemris.java.gallery.init.ServletContextProvider;
import hr.fer.zemris.java.gallery.loader.GalleryLoader;
import hr.fer.zemris.java.gallery.model.GalleryEntry;
import hr.fer.zemris.java.gallery.rest.util.ImageUtil;

/**
 * Razred koji predstavlja <a href=
 * "https://en.wikipedia.org/wiki/Representational_state_transfer">RESTful</a>
 * servis za galeriju slika. Primjerci ovog razreda nude iduće metode za dohvat
 * opisa slika i samih slika:
 * 
 * <ul>
 * <li>{@link #getTags()}</li>
 * <li>{@link #getEntriesForTag(String)}</li>
 * <li>{@link #getPicture(String)}</li>
 * <li>{@link #getThumbnail(String)}</li>
 * </ul>
 * 
 * @author Davor Češljaš
 */
@Path("/gallery_service")
public class GalleryService {

	/** Konstanta koja predstavlja direktorij umanjenih slika u memoriji */
	private static final String THUMBNAILS_DIRECTORY = "WEB-INF/thumbnails/";

	/** Konstanta koja predstavlja direktorij slika u memoriji */
	private static final String PICTURES_DIRECTORY = "WEB-INF/pictures/";

	/**
	 * Članska varijabla koja predstavlja primjerak razreda {@link Gson} za
	 * pretvorbu podataka u JSON format
	 */
	private static final Gson gson = new Gson();

	/**
	 * Metoda koja dohvaća sve postojeće oznake unutar aplikacije. Metoda posao
	 * dohvata delegira metodi {@link GalleryLoader#loadTags()}
	 *
	 * @return primjerak razreda {@link Response} koji modelira sve postojeće
	 *         oznake (format je JSON)
	 */
	@Path("/tags")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getTags() {
		return (createResponse(() -> gson.toJson(GalleryLoader.loadTags())));
	}

	/**
	 * Metoda koja dohvaća sve postojeće primjerke razreda {@link GalleryEntry}
	 * koji sadrže oznaku <b>tag</b>. Metoda posao dohvata delegira metodi
	 * {@link GalleryLoader#loadEntriesForTag(String)}
	 *
	 * @param tag
	 *            oznaka koja se pretražuje
	 * @return primjerak razreda {@link Response} koji modelira sve pronađene
	 *         opisnike slika (format je JSON)
	 */
	@Path("{tag}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getEntriesForTag(@PathParam("tag") String tag) {
		return (createResponse(() -> gson.toJson(GalleryLoader.loadEntriesForTag(tag))));
	}

	/**
	 * Metoda koja dohvaća sliku koja ima naziv <b>picture</b>. Ova slika je
	 * nativne veličine
	 *
	 * @param picture
	 *            naziv slike koja je zatražena
	 * @return sliku modeliranu primjerkom razreda {@link Response} koja je
	 *         zatražena
	 */
	@Path("/picture/{picture}")
	@GET
	@Produces({ "image/png", "image/jpg" })
	public Response getPicture(@PathParam("picture") String picture) {
		return createResponse(() -> ImageUtil.getImage(PICTURES_DIRECTORY + picture));
	}

	/**
	 * Metoda koja dohvaća sliku koja ima naziv <b>thumbnail</b>. Ova slika je
	 * umanjena na veličinu 150x150. Ukoliko umanjena slika pod ovim nazivom ne
	 * postoji, a ista postoji u nativnoj veličini, tada se slika u nativnoj
	 * valičini umanjuje te sprema kako bi prilikom ponovnog poziva ove metode
	 * sa istim argumentom bila dostupna.
	 *
	 * @param thumbnail
	 *            naziv umanjene slike koja je zatražena
	 * @return umanjenu sliku modeliranu primjerkom razreda {@link Response}
	 *         koja je zatražena
	 */
	@Path("/thumbnail/{thumbnail}")
	@GET
	@Produces({ "image/png", "image/jpg" })
	public Response getThumbnail(@PathParam("thumbnail") String thumbnail) {
		ServletContext context = ServletContextProvider.getContext();

		File thumbnailDir = new File(context.getRealPath(THUMBNAILS_DIRECTORY));
		if (!thumbnailDir.exists()) {
			thumbnailDir.mkdirs();
		}

		String thumbnailPath = THUMBNAILS_DIRECTORY + thumbnail;
		File toFile = new File(context.getRealPath(thumbnailPath));
		if (!toFile.exists()) {
			File fromFile = new File(context.getRealPath(PICTURES_DIRECTORY + thumbnail));

			return createResponse(() -> ImageUtil.scaleImage(fromFile, toFile));
		}

		return createResponse(() -> ImageUtil.getImage(thumbnailPath));
	}

	/**
	 * Pomoćna metoda koja prima strategiju predstavljenu sučeljem
	 * {@link DataSupplier} te koristeći rezultat izvođenja metode
	 * {@link DataSupplier#get()} stvara prikladni primjerak razreda
	 * {@link Response} koji modelira odgovor.
	 *
	 * @param <T>
	 *            tip kojim je parametrizirana ova metoda
	 * @param supplier
	 *            primjerak sučelja {@link DataSupplier}
	 * @return rezultat izvođenja metode {@link DataSupplier#get()} modeliran
	 *         primjerkom razreda {@link Response}
	 */
	private <T> Response createResponse(DataSupplier<T> supplier) {
		try {
			return Response.status(Status.OK).entity(supplier.get()).encoding(StandardCharsets.UTF_8.name()).build();
		} catch (Exception e) {
			return Response.status(Status.NOT_FOUND).build();
		}
	}

	/**
	 * Pomoćno sučelje koja po svojoj semantici odgovara sučelju
	 * {@link Supplier}. Jedina razlika je ta što ovdje metoda {@link #get()}
	 * može izazvati iznimku koja je primjerak razreda {@link IOException}
	 *
	 * @param <T>
	 *            tip kojim je parametrizirano ovo sučelje
	 *            
	 * @author Davor Češljaš
	 */
	private interface DataSupplier<T> {

		/**
		 * Metoda koja dohvaća rezultat.
		 * 
		 *
		 * @return rezultat
		 * @throws IOException
		 *             Ukoliko izvođenje ove metode generira ovu iznimku
		 * @see Supplier#get()
		 */
		T get() throws IOException;
	}
}
