import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Problem12_13 {

	public static void main(String[] args) {
		if (args.length < 1) {
			System.out.println("Usage: java Problem12_13 <filename>");
			return;
		}

		final String filename = args[0];
		File file = new File(filename);

		try (BufferedReader in = new BufferedReader(new FileReader(file))) {
			int lines = 0;
			int words = 0;
			int chars = 0;

			String line = in.readLine();
			while (line != null) {
				if (!line.equals("")) {
					chars += line.length();
					words += line.trim().split("\\s+").length;
				}
				lines++;
				line = in.readLine();
			}

			System.out.println(String.format("'%s' contains %d lines, %d words, and %d characters",
					filename, lines, words, chars));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
