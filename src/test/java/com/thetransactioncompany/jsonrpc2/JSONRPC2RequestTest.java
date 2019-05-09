package com.thetransactioncompany.jsonrpc2;


import junit.framework.TestCase;


/**
 * Tests the JSON-RPC 2.0 request class.
 */
public class JSONRPC2RequestTest extends TestCase {


	public void testSetIDFromToString() {

		Object id = new Object(){
			@Override
			public String toString() {
				return "id";
			}
		};

		JSONRPC2Request request = new JSONRPC2Request("echo", id);

		assertEquals("id", request.getID());
	}
}
