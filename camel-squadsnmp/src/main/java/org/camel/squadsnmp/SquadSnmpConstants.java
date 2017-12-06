package org.camel.squadsnmp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SquadSnmpConstants {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public static final String ACTION = "ACTION";
	public static final String ACTION_REGISTER = "REGISTER";
	public static final String ACTION_DELETE = "DELETE";
	public static final String ACTION_DELETE_ALL = "DELETE_ALL";
	public static final String SNMP_OID = "SNMP_OID";
	public static final String SNMP_VALUE = "SNMP_VALUE";
}
