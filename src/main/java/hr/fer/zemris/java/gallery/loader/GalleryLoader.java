package hr.fer.zemris.java.gallery.loader;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.ServletContext;

import hr.fer.zemris.java.gallery.init.ServletContextProvider;
import hr.fer.zemris.java.gallery.model.GalleryEntry;

/**
 * Razred koji se koristi za učitavanje datoteke descriptor.txt te njezino
 * parsiranje. Ovaj razred je biblioteka te se njegovi primjerci ne mogu
 * kreirati.
 * 
 * Razred nudi svega dvije metode:
 * <ul>
 * <li>{@link #loadTags()}</li>
 * <li>{@link #loadEntriesForTag(String)}</li>
 * </ul>
 * 
 * @author Davor Češljaš
 */
public class GalleryLoader {

	/**
	 * Konstanta koja predstavlja relativnu putanju do descriptor.txt datoteke
	 * unutar aplikacije
	 */
	private static final String DESCRIPTOR_LOCATION = "WEB-INF/descriptor.txt";

	/**
	 * Konastanta koja predstavlja regularni izraz po kojem se razdvajaju oznake
	 * iz datoteke
	 */
	private static final String TAGS_SPLIT_REGEX = "\\s*,\\s*";

	/**
	 * Konstanta koja predstavlja količinu linija potrebnih za izradu jedong
	 * primjerka razreda {@link GalleryEntry}
	 */
	private static final int LINES_PER_ENTRY = 3;

	/**
	 * Privatni konstruktor koji služi tome da se primjerci ovog razreda ne mogu
	 * stvarati izvan samog razreda.
	 */
	private GalleryLoader() {
	}

	/**
	 * Metoda koja se koristi za učitavanje svih postojećih oznaka iz datoteke
	 * {@value #DESCRIPTOR_LOCATION}. Metoda parsirane oznake vraća kroz
	 * {@link Set} primjeraka razreda {@link String}, gdje svaki primjerak tog
	 * razreda predstavlja jednu oznaku
	 *
	 * @return {@link Set} primjeraka razreda {@link String}, gdje svaki
	 *         primjerak tog razreda predstavlja jednu oznaku
	 * @throws IOException
	 *             Ukoliko nije moguće pročitati datoteku
	 *             {@value #DESCRIPTOR_LOCATION}
	 */
	public static Set<String> loadTags() throws IOException {
		List<String> descriptorLines = loadDescriptorLines();

		Set<String> tags = new TreeSet<>();
		for (int i = 2, len = descriptorLines.size(); i < len; i += LINES_PER_ENTRY) {
			tags.addAll(parseTags(descriptorLines.get(i)));
		}

		return tags;
	}

	/**
	 * Metoda koja za predani argument <b>tag</b> dohvaća sve primjerke razreda
	 * {@link GalleryEntry} koji sadrže tu oznaku. Metoda parsira datoteku
	 * {@value #DESCRIPTOR_LOCATION} u kojima se nalaze potrebne informacije za
	 * stvaranje primjeraka razreda {@link GalleryEntry}.
	 *
	 * @param tag
	 *            oznaka čiji se primjerci razreda {@link GalleryEntry} traže
	 * @return {@link Set} primjeraka razreda {@link GalleryEntry} koji imaju
	 *         oznaku <b>tag</b>
	 * @throws IOException
	 *             Ukoliko nije moguće pročitati datoteku
	 *             {@value #DESCRIPTOR_LOCATION}
	 */
	public static Set<GalleryEntry> loadEntriesForTag(String tag) throws IOException {
		List<String> descriptorLines = loadDescriptorLines();

		Set<GalleryEntry> entries = new HashSet<>();
		tag = tag.toUpperCase();
		for (int i = 0, len = descriptorLines.size(); i < len; i += LINES_PER_ENTRY) {
			Set<String> tags = parseTags(descriptorLines.get(i + 2));
			if (!tags.contains(tag)) {
				continue;
			}

			entries.add(new GalleryEntry(descriptorLines.get(i), descriptorLines.get(i + 1), tags));
		}

		return entries;
	}

	/**
	 * Pomoćna metoda koja se koristi za parsiranje svih oznaka iz redka
	 * <b>tagLine</b> unutar datoteke {@value #DESCRIPTOR_LOCATION}.
	 *
	 * @param tagLine
	 *            redak datoteke {@value #DESCRIPTOR_LOCATION} unutar kojeg se
	 *            nalaze oznake
	 * @return {@link Set} parsiranih oznaka
	 */
	private static Set<String> parseTags(String tagLine) {
		Set<String> tags = new HashSet<>();
		for (String tag : tagLine.split(TAGS_SPLIT_REGEX)) {
			tags.add(tag.toUpperCase());
		}

		return tags;
	}

	/**
	 * Pomoćna metoda koja se koristi za učitavanje sadržaja datoteke
	 * {@value #DESCRIPTOR_LOCATION}. Metoda vraća {@link List} redaka datoteke.
	 *
	 * @return {@link List} redaka datoteke
	 * @throws IOException
	 *             Ukoliko nije moguće pročitati datoteku
	 *             {@value #DESCRIPTOR_LOCATION}
	 */
	private static List<String> loadDescriptorLines() throws IOException {
		ServletContext context = ServletContextProvider.getContext();

		return Files.readAllLines(Paths.get(context.getRealPath(DESCRIPTOR_LOCATION)), StandardCharsets.UTF_8);
	}
}
