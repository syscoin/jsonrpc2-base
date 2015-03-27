package com.thetransactioncompany.jsonrpc2;

import java.util.Map;

import junit.framework.TestCase;

import com.thetransactioncompany.jsonrpc2.JSONRPC2Parser;
import com.thetransactioncompany.jsonrpc2.JSONRPC2ParseException;

import net.minidev.json.JSONAware;

/**
 * Tests the JSON-RPC 2.0 request class.
 */
public class JSONRPC2ResponseTest extends TestCase {
    public class Bean {
        protected String test = "foo";

        public String getTest() {
            return this.test;
        }
    }

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

    public void testSetResultJsonAware()
        throws
            JSONRPC2ParseException
    {
        JSONRPC2Parser parser = new JSONRPC2Parser();

        JSONAware result = new JSONAware() {
            public String toJSONString() {
                return "\"dummy\"";
            }
        };

		JSONRPC2Response response = new JSONRPC2Response(result, "id");
        // we need to check serialized form
        response = parser.parseJSONRPC2Response(response.toJSONString());

		assertEquals("dummy", response.getResult());
    }

    public void testSetResultBean()
        throws
            JSONRPC2ParseException
    {
        JSONRPC2Parser parser = new JSONRPC2Parser();

        Object result = new JSONRPC2ResponseTest.Bean();

		JSONRPC2Response response = new JSONRPC2Response(result, "id");
        // we need to check serialized form
        response = parser.parseJSONRPC2Response(response.toJSONString());

        result = response.getResult();
        assertTrue(result instanceof Map);
        assertTrue(((Map) result).containsKey("test"));
		assertEquals("foo", ((Map) result).get("test"));
    }
}
