package eva.monopoly.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystemNotFoundException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Collections;

import org.apache.commons.io.IOUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import eva.monopoly.Monopoly;

public class ResourceReaderUtil {
	private static final Gson GSON = new GsonBuilder().setLenient().create();

	public static <T> T getObjectAsJsonFromFile(Path path, Class<T> targetClass) throws IOException {
		InputStream stream = Files.newInputStream(path, StandardOpenOption.READ);
		String jsonTxt = IOUtils.toString(stream, "UTF-8");
		return GSON.fromJson(jsonTxt, targetClass);
	}

	public static Path getResourcePath(String path) throws URISyntaxException, IOException {
		URI jarURI = Monopoly.class.getProtectionDomain().getCodeSource().getLocation().toURI();
		Path jarPath = Paths.get(jarURI);
		if (Files.isRegularFile(jarPath)) {
			try (final FileSystem fs = getZipFileSystem(jarURI)) {
				return fs.getPath(path);
			}
		} else {
			return jarPath.resolve(path);
		}
	}

	private static FileSystem getZipFileSystem(final URI uri) throws IOException {
		final URI nUri = "jar".equals(uri.getScheme()) ? uri : URI.create("jar:" + uri.toString());
		return getFileSystem(nUri);
	}

	private static FileSystem getFileSystem(final URI uri) throws IOException {
		try {
			return FileSystems.getFileSystem(uri);
		} catch (FileSystemNotFoundException e) {
			return FileSystems.newFileSystem(uri, Collections.emptyMap());
		}
	}
}