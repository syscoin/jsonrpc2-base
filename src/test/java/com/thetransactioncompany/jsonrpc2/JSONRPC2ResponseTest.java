package com.thetransactioncompany.jsonrpc2;


import junit.framework.TestCase;


/**
 * Tests the JSON-RPC 2.0 request class.
 */
public class JSONRPC2ResponseTest extends TestCase {


	public void testSetIDFromToString() {

		Object id = new Object(){
			@Override
			public String toString() {
				return "id";
			}
		};

		JSONRPC2Response response = new JSONRPC2Response(id);

		assertEquals("id", response.getID());
	}
}
