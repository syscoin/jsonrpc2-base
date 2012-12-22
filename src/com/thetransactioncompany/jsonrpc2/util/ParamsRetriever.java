package com.thetransactioncompany.jsonrpc2.util;


import com.thetransactioncompany.jsonrpc2.JSONRPC2Error;


/**
 * The base abstract class for the JSON-RPC 2.0 parameter retrievers.
 *
 * @author Vladimir Dzhuvinov
 * @version $version$ (2012-12-21)
 */
public abstract class ParamsRetriever {

	
	/**
	 * Returns the parameter count.
	 *
	 * @return The number of parameters.
	 */
	public abstract int size();


	/**
	 * Throws a {@code JSONRPC2Error.INVALID_PARAMS} exception if the input
	 * string doesn't match a value in the specified string array.
	 *
	 * <p>This method is intended to check a string against a set of
	 * acceptable values.
	 *
	 * @param input       The string to check.
	 * @param enumStrings The acceptable string values. Must not be 
	 *                    {@code null}.
	 * @param ignoreCase  {@code true} for a case insensitive match.
	 *
	 * @return The matching string value.
	 *
	 * @throws JSONRPC2Error With proper code and message if the input
	 *                       string didn't match.
	 */
	protected static String ensureEnumString(final String input, 
		                                 final String[] enumStrings, 
		                                 final boolean ignoreCase)
		throws JSONRPC2Error {
	
		for (final String en: enumStrings) {
		
			if (ignoreCase) {
				if (en.equalsIgnoreCase(input))
					return en;
			}
			else {
				if (en.equals(input))
					return en;
			}
		}
		
		// No match -> raise error
		throw JSONRPC2Error.INVALID_PARAMS;
	}
	
	
	/**
	 * Throws a {@code JSONRPC2Error.INVALID_PARAMS} exception if the input
	 * string doesn't match a constant name in the specified enumeration
	 * class.
	 *
	 * <p>This method is intended to check a string against a set of
	 * acceptable values.
	 *
	 * @param input      The string to check.
	 * @param enumClass  The enumeration class specifying the acceptable 
	 *                   constant names. Must not be {@code null}.
	 * @param ignoreCase {@code true} for a case insensitive match.
	 *
	 * @return The matching enumeration constant.
	 *
	 * @throws JSONRPC2Error With proper code and message if the input
	 *                       string didn't match.
	 */
	protected static <T extends Enum<T>> T ensureEnumString(final String input, 
		                                                final Class<T> enumClass, 
		                                                final boolean ignoreCase)
		throws JSONRPC2Error {
		
		for (T en: enumClass.getEnumConstants()) {
		
			if (ignoreCase) {
				if (en.toString().equalsIgnoreCase(input))
					return en;
			}
			else {
				if (en.toString().equals(input))
					return en;
			}
		}
		
		// No match -> raise error
		throw JSONRPC2Error.INVALID_PARAMS;
	}
}
