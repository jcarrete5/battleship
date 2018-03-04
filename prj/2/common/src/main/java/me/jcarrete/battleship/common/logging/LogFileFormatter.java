package me.jcarrete.battleship.common.logging;

import java.lang.management.ManagementFactory;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.LogRecord;

public class LogFileFormatter extends Formatter {

	private static final DateFormat df = new SimpleDateFormat("[yyyy.MM.dd HH:mm:ss:SSS]");

	private final Date logTime = new Date();

	@Override
	public String format(LogRecord record) {
		String threadName = ManagementFactory.getThreadMXBean()
				.getThreadInfo(record.getThreadID()).getThreadName();
		logTime.setTime(record.getMillis());

		StringBuilder formatted = new StringBuilder();
		formatted
				.append(df.format(logTime))
				.append(' ').append('[')
				.append(threadName)
				.append('/')
				.append(record.getLevel().getLocalizedName())
				.append(']').append(' ').append('[')
				.append(record.getSourceClassName())
				.append('#')
				.append(record.getSourceMethodName())
				.append(']').append(':').append(' ')
				.append(formatMessage(record))
				.append('\n')
				.append(throwableToString(record.getThrown()));
		return formatted.toString();
	}
}
