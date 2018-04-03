import java.io.*;

public class Problem12_11 {

	public static void main(String[] args) {
		if (args.length < 2) {
			System.out.println("Usage: java Problem12_11 <pattern> <filename>");
			return;
		}

		final String removeText = args[0];
		final String filename = args[1];
		File oldFile = new File(filename);
		File newFile;
		try {
			newFile = File.createTempFile("p12_11", null);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}

		try (BufferedReader in = new BufferedReader(new FileReader(oldFile));
		     BufferedWriter out = new BufferedWriter(new FileWriter(newFile))) {

			String line = in.readLine();
			while (line != null) {
				out.write(line.replace(removeText, ""));
				out.newLine();
				line = in.readLine();
			}

			oldFile.delete();
			newFile.renameTo(new File("p12_11.txt"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
