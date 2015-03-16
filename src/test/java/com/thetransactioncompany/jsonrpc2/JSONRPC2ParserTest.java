package com.thetransactioncompany.jsonrpc2;


import junit.framework.TestCase;


/**
 * Tests the JSON-RPC 2.0 message parser.
 *
 * @author Vladimir Dzhuvinov
 */
public class JSONRPC2ParserTest extends TestCase {


	public void testParseRequest()
		throws Exception {

		String json = "{\"jsonrpc\": \"2.0\", \"method\": \"subtract\", \"params\": [42, 23], \"id\": 1}";

		JSONRPC2Parser parser = new JSONRPC2Parser();

		JSONRPC2Request request = parser.parseJSONRPC2Request(json);
		assertEquals("subtract", request.getMethod());
		assertEquals(JSONRPC2ParamsType.ARRAY, request.getParamsType());
		assertEquals(42l, request.getPositionalParams().get(0));
		assertEquals(23l, request.getPositionalParams().get(1));
		assertEquals(2, request.getPositionalParams().size());
		assertEquals(1l, request.getID());
	}


	public void testParseRequestWithTrailingWhiteSpace()
		throws Exception {

		String json = "{\"jsonrpc\": \"2.0\", \"method\": \"subtract\", \"params\": [42, 23], \"id\": 1}   ";

		JSONRPC2Parser parser = new JSONRPC2Parser();

		JSONRPC2Request request = parser.parseJSONRPC2Request(json);
		assertEquals("subtract", request.getMethod());
		assertEquals(JSONRPC2ParamsType.ARRAY, request.getParamsType());
		assertEquals(42l, request.getPositionalParams().get(0));
		assertEquals(23l, request.getPositionalParams().get(1));
		assertEquals(2, request.getPositionalParams().size());
		assertEquals(1l, request.getID());
	}
}
