package br.com.devschool.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

public class StackTraceUtil {

	public static String getStackTrace(Throwable aThrowable) {
		
		final Writer writer = new StringWriter();
		final PrintWriter printWriter = new PrintWriter(writer);
		aThrowable.printStackTrace(printWriter);
		return writer.toString().substring(0, 4000);
	}

}
