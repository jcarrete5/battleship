public class Problem8_7 {

	public static void main(String[] args) {
		final double[][] points = {{-1, 0, 3}, {-1, -1, -1}, {4, 1, 1},
				{2, 0.5, 9}, {3.5, 2, -1}, {3, 1.5, 3}, {-1.5, 4, 2},
				{5.5, 4, -0.5}};

		double[] p1 = null, p2 = null;
		double shortest = Double.MAX_VALUE;
		for (int i = 0; i < points.length; i++) {
			for (int j = i + 1; j < points.length; j++) {
				double dist = dist(points[i], points[j]);
				if (dist < shortest) {
					shortest = dist;
					p1 = points[i];
					p2 = points[j];
				}
			}
		}

		System.out.println("The closest two points are " + pointToString(p1) + " and " + pointToString(p2));
	}

	private static double dist(double[] p1, double[] p2) {
		return Math.sqrt(Math.pow(p2[0] - p1[0], 2) + Math.pow(p2[1] - p1[1], 2) + Math.pow(p2[2] - p1[2], 2));
	}

	private static String pointToString(double[] p) {
		if (p == null) return null;
		return "(" + p[0] + ", " + p[1] + ", " + p[2] + ")";
	}
}
