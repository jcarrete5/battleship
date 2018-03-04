package me.jcarrete.battleship.common.logging;

import java.io.PrintWriter;
import java.io.StringWriter;

abstract class Formatter extends java.util.logging.Formatter {

	String throwableToString(Throwable throwable) {
		if (throwable != null) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			throwable.printStackTrace(pw);
			pw.close();
			return sw.toString();
		}
		return "";
	}
}
