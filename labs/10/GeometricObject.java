import java.util.Date;

public abstract class GeometricObject {

	private String color;
	private boolean filled;
	private Date dateCreated;

	public GeometricObject() {
		this("white", false);
	}

	public GeometricObject(String color, boolean filled) {
		this.dateCreated = new Date();
		this.color = color;
		this.filled = filled;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public boolean isFilled() {
		return filled;
	}

	public void setFilled(boolean filled) {
		this.filled = filled;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public abstract double getArea();

	public abstract double getPerimeter();

	@Override
	public String toString() {
		return String.format("GeometricObject: {color: %s, filled: %b, dateCreated: %s}",
				getColor(), isFilled(), getDateCreated());
	}
}
