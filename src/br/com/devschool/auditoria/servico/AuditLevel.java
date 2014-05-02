package br.com.devschool.auditoria.servico;

import java.util.logging.Level;

public class AuditLevel extends Level {

	private static final long serialVersionUID = -3117203685117132776L;

	/**
	 * Value of audit level. This value is lesser than WARN_INT and higher than
	 * INFO_INT
	 */
	public static final int AUDIT_INT = 10000;

	public static final AuditLevel AUDIT = new AuditLevel(7, "AUDIT");

	protected AuditLevel(int syslogEquivalent, String levelStr) {
		super(levelStr, syslogEquivalent);
	}

	public static AuditLevel toLevel(int val, Level defaultLevel) {
		return AUDIT;
	}

	public static AuditLevel toLevel(String sArg, Level defaultLevel) {
		return AUDIT;
	}
}
