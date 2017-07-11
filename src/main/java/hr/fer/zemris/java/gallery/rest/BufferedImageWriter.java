package hr.fer.zemris.java.gallery.rest;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.imageio.ImageIO;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

/**
 * Razred koji implementira sučelje {@link MessageBodyWriter}. Primjerci ovog
 * razreda koriste se za pripremu slike modelirane razredom
 * {@link BufferedImage} za slanje preko toka podataka.
 * 
 * @see MessageBodyWriter
 * 
 * @author Davor Češljaš
 */
@Produces({ "image/png", "image/jpg" })
@Provider
public class BufferedImageWriter implements MessageBodyWriter<BufferedImage> {

	@Override
	public long getSize(BufferedImage t, Class<?> type, Type genericType, Annotation[] annotations,
			MediaType mediaType) {
		// deprecated
		return -1;
	}

	@Override
	public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		return type == BufferedImage.class;
	}

	@Override
	public void writeTo(BufferedImage image, Class<?> type, Type genericType, Annotation[] annotations,
			MediaType mediaType, MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream)
			throws IOException, WebApplicationException {
		ImageIO.write(image, mediaType.getSubtype(), entityStream);
	}

}
