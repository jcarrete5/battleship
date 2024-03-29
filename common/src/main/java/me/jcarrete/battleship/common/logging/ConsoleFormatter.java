package me.jcarrete.battleship.common.logging;

import java.lang.management.ManagementFactory;
import java.util.logging.LogRecord;

public class ConsoleFormatter extends Formatter {

	@Override
	public String format(LogRecord record) {
		String threadName = ManagementFactory.getThreadMXBean().getThreadInfo(record.getThreadID()).getThreadName();

		StringBuilder formatted = new StringBuilder();
		formatted
				.append('[')
				.append(threadName)
				.append('/')
				.append(record.getLevel().getLocalizedName())
				.append(']').append(':').append(' ')
				.append(formatMessage(record))
				.append(System.lineSeparator())
				.append(throwableToString(record.getThrown()));

		return formatted.toString();
	}
}
