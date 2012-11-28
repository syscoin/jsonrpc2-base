package com.thetransactioncompany.jsonrpc2.util;


import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import com.thetransactioncompany.jsonrpc2.*;


/**
 * JUnit tests for the parameter retriever classes.
 *
 * @author Vladimir Dzhuvinov
 * @version $version$ (2012-11-28)
 */
public class Test extends TestCase {

	
	public void testPositionalParams() {
	
		// Create new request with positional parameters
		String method = "rpc.test";
		int id = 0;
		
		String param0 = "John Smith";
		int param1 = 25;
		boolean param2 = true;
		double param3 = 3.1415;
		Object param4 = null;
		
		List param5 = new LinkedList();
		int param5_0 = 0;
		int param5_1 = 1;
		int param5_2 = 2;
		int param5_3 = 3;
		int param5_4 = 4;
		param5.add(param5_0);
		param5.add(param5_1);
		param5.add(param5_2);
		param5.add(param5_3);
		param5.add(param5_4);
		
		String param6 = "one";
		String[] param6enums = {"one", "two", "three"};
		
		int nonExistingPosition = 7;
		
		List params = new LinkedList();
		params.add(param0);
		params.add(param1);
		params.add(param2);
		params.add(param3);
		params.add(param4);
		params.add(param5);
		params.add(param6);
		int size = params.size();
		
		// Create request from parsed back JSON string
		JSONRPC2Request request = new JSONRPC2Request(method, params, id);
		String json = request.toString();
		
		try {
			request = JSONRPC2Request.parse(json);
		
		} catch (JSONRPC2ParseException e) {
			fail("JSON-RPC 2.0 Request parse exception: " + e.getMessage());
		}
		
		// Create new positional params instance
		params = (List)request.getParams();
		PositionalParamsRetriever pp = new PositionalParamsRetriever(params);

		assertTrue(pp.getParams() instanceof List);
		
		// Check params size
		assertEquals(size, params.size());
		
		// No exception must be thrown here
		try {
			for (int i=0; i < size; i++)
				pp.ensureParam(i);
		
		} catch (JSONRPC2Error e) {
			fail("Missing positional parameter: " + e.getMessage());
		}
		
		// Force exception
		try {
			pp.ensureParam(nonExistingPosition);
			fail("Failed to raise exception on non-existing position");
		} catch (JSONRPC2Error e) {
			// ok
		}
		
		
		// Check parameter test
		assertTrue(pp.hasParam(0));
		assertTrue(pp.hasParam(1));
		assertTrue(pp.hasParam(2));
		assertTrue(pp.hasParam(3));
		assertTrue(pp.hasParam(4));
		assertTrue(pp.hasParam(5));
		assertTrue(pp.hasParam(6));
		assertFalse(pp.hasParam(nonExistingPosition));
	
		// Compare parameters
		try {
			assertEquals(param0, pp.getString(0));
			assertEquals(param1, pp.getInt(1));
			assertEquals(param2, pp.getBoolean(2));
			assertEquals(param3, pp.getDouble(3));
			assertEquals(param4, pp.get(4));
			
			// List
			List ints = pp.getList(5);
			assertEquals(param5_0, ((Number)ints.get(0)).intValue());
			assertEquals(param5_1, ((Number)ints.get(1)).intValue());
			assertEquals(param5_2, ((Number)ints.get(2)).intValue());
			assertEquals(param5_3, ((Number)ints.get(3)).intValue());
			assertEquals(param5_4, ((Number)ints.get(4)).intValue());
			
			assertEquals(param6, pp.getEnumString(6, param6enums));
			
		} catch (JSONRPC2Error e) {
			fail("Comparison failed: " + e.getMessage());
		}
		
		// Test optional parameters
		try {
			
			assertEquals("abc", pp.getOptString(nonExistingPosition, "abc"));
			assertEquals(true, pp.getOptBoolean(nonExistingPosition, true));
			assertEquals(100, pp.getOptInt(nonExistingPosition, 100));
			assertEquals(3.1415, pp.getOptDouble(nonExistingPosition, 3.1415));
		
		} catch (JSONRPC2Error e) {
			fail("Invalid optional parameter: " + e.getMessage());
		}
	}


	public void testNamedParams() {
	
		// Create new request with named parameters
		String method = "rpc.test";
		int id = 0;
		
		String param0 = "John Smith";
		int param1 = 25;
		boolean param2 = true;
		double param3 = 3.1415;
		Object param4 = null;
		
		List param5 = new LinkedList();
		int param5_0 = 0;
		int param5_1 = 1;
		int param5_2 = 2;
		int param5_3 = 3;
		int param5_4 = 4;
		param5.add(param5_0);
		param5.add(param5_1);
		param5.add(param5_2);
		param5.add(param5_3);
		param5.add(param5_4);
		
		String param6 = "one";
		String[] param6enum = {"one", "two", "three"};
		
		Map params = new HashMap();
		params.put("param0", param0);
		params.put("param1", param1);
		params.put("param2", param2);
		params.put("param3", param3);
		params.put("param4", param4);
		params.put("param5", param5);
		params.put("param6", param6);
		
		
		int size = params.size();
		
		String badParamName = "non-existing-name";
		
		
		// Create request from parsed back JSON string
		JSONRPC2Request request = new JSONRPC2Request(method, params, id);
		String json = request.toString();
		
		try {
			request = JSONRPC2Request.parse(json);
		
		} catch (JSONRPC2ParseException e) {
			fail("JSON-RPC 2.0 Request parse exception: " + e.getMessage());
		}
		
		// Create new named params instance
		params = (Map)request.getParams();
		NamedParamsRetriever np = new NamedParamsRetriever(params);

		assertTrue(np.getParams() instanceof Map);
		
		// Check params size
		assertEquals(size, np.size());
		
		// No exception must be thrown here
		try {
			for (int i=0; i < size; i++)
				np.ensureParam("param" + i);
			
		} catch (JSONRPC2Error e) {
			fail(e.getMessage());
		}

		// Force exception
		try {
			np.ensureParam(badParamName);
			
		} catch (JSONRPC2Error e) {
			assertTrue(true);
		}
		
		// Check parameter test
		assertTrue(np.hasParam("param0"));
		assertTrue(np.hasParam("param1"));
		assertTrue(np.hasParam("param2"));
		assertTrue(np.hasParam("param3"));
		assertTrue(np.hasParam("param4"));
		assertTrue(np.hasParam("param5"));
		assertTrue(np.hasParam("param6"));
		assertFalse(np.hasParam(badParamName));
		
		
		// Compare parameters
		try {
			assertEquals(param0, np.getString("param0"));
			assertEquals(param1, np.getInt("param1"));
			assertEquals(param2, np.getBoolean("param2"));
			assertEquals(param3, np.getDouble("param3"));
			assertEquals(param4, np.get("param4"));
			
			// List
			List ints = np.getList("param5");
			assertEquals(param5_0, ((Number)ints.get(0)).intValue());
			assertEquals(param5_1, ((Number)ints.get(1)).intValue());
			assertEquals(param5_2, ((Number)ints.get(2)).intValue());
			assertEquals(param5_3, ((Number)ints.get(3)).intValue());
			assertEquals(param5_4, ((Number)ints.get(4)).intValue());
			
			assertEquals(param6, np.getEnumString("param6", param6enum));
		
		} catch (JSONRPC2Error e) {
			fail(e.getMessage());
		}
		
		
		// Test optional parameters
		try {
			assertEquals("abc", np.getOptString(badParamName, "abc"));
			assertEquals(true, np.getOptBoolean(badParamName, true));
			assertEquals(100, np.getOptInt(badParamName, 100));
			assertEquals(3.1415, np.getOptDouble(badParamName, 3.1415));
		
		} catch (JSONRPC2Error e) {
			fail(e.getMessage());
		}
		
		// Test get names method
		String[] names = null;
		
		try {
			names = np.getNames();
			
		} catch (Exception e) {
			fail("Unexpected getNames() exception: " + e.getMessage());
		}
		
		assertEquals(size, names.length);
		
		for (int i=0; i < names.length; i++)
			assertEquals("param" + i, names[i]);
		
		
		// Test check names method
		try {
			np.ensureParams(names);
			
		} catch (JSONRPC2Error e) {
			fail("Unexpected ensureParams() exception: " + e.getMessage());
		}
		
		try {
			// modify the last name
			names[size-1] = "bad name";
			np.ensureParams(names);
			
		} catch (JSONRPC2Error e) {
			assertTrue(true);
		}
		
		try {
			// name outside the list
			names = new String[size-1];
			
			for (int i=0; i< size-1; i++)
				names[i] = "param" + i;
			
			np.ensureParams(names);
			
		} catch (JSONRPC2Error e) {
			assertTrue(true);
		}
	}
	
	
	public void testEnsureEnumString() {
	
		String[] enumValues = {"ONE", "TWO", "THREE"};
		
		boolean ignoreCase = false;
		
		// test observe case correct
		try {
			ignoreCase = false;
			ParamsRetriever.ensureEnumString("ONE", enumValues, ignoreCase);
			
		} catch (JSONRPC2Error e) {
			fail("Unexpected ensureEnumString() exception: " + e.getMessage());
		}
		
		
		// test observe case incorrect
		try {
			ignoreCase = false;
			ParamsRetriever.ensureEnumString("one", enumValues, ignoreCase);
			
		} catch (JSONRPC2Error e) {
			assertTrue(true);
		}
		
		// test ignore case correct 1
		try {
			ignoreCase = true;
			ParamsRetriever.ensureEnumString("ONE", enumValues, ignoreCase);
			
		} catch (JSONRPC2Error e) {
			fail("Unexpected ensureEnumString() exception: " + e.getMessage());
		}
		
		// test ignore case correct 2
		try {
			ignoreCase = true;
			ParamsRetriever.ensureEnumString("one", enumValues, ignoreCase);
			
		} catch (JSONRPC2Error e) {
			fail("Unexpected ensureEnumString() exception: " + e.getMessage());
		}
		
		// test ignore case incorrect
		try {
			ignoreCase = true;
			ParamsRetriever.ensureEnumString("four", enumValues, ignoreCase);
			
		} catch (JSONRPC2Error e) {
			assertTrue(true);
		}
	}
	
	
	public void testEnsureEnumString2() {
	
		// test observe case correct
		
		boolean ignoreCase = false;
		
		try {
			ParamsRetriever.ensureEnumString("MONDAY", TestEnumDay.class, ignoreCase);
		
		} catch (JSONRPC2Error e) {
			fail("Unexpected ensureEnumString() exception: " + e.getMessage());
		}
		
		
		// test observe case incorrect
		
		try {
			ParamsRetriever.ensureEnumString("SOMEDAY", TestEnumDay.class, ignoreCase);
			fail("Failed to raise exception");
			
		} catch (JSONRPC2Error e) {
			// ok
		}
		
		
		// test ignore case correct
		
		ignoreCase = true;
		
		try {
			ParamsRetriever.ensureEnumString("monday", TestEnumDay.class, ignoreCase);
		
		} catch (JSONRPC2Error e) {
			fail("Unexpected ensureEnumString() exception: " + e.getMessage());
		}
		
		// test observe case incorrect
		
		try {
			ParamsRetriever.ensureEnumString("SOMEDAY", TestEnumDay.class, ignoreCase);
			fail("Failed to raise exception");
			
		} catch (JSONRPC2Error e) {
			// ok
		}
	}
	
	
	public void testGetEnumPositional() {
	
		List l = new LinkedList();
		l.add("MONDAY");
		l.add("tuesday");
		l.add("SOMEDAY");
		
		PositionalParamsRetriever r = new PositionalParamsRetriever(l);
		
		TestEnumDay day = null;
		
		try {
			day = r.getEnum(0, TestEnumDay.class);
		
		} catch (JSONRPC2Error e) {
			fail(e.getMessage());
		}
		
		assertEquals(day.toString(), "MONDAY");
		
		boolean ignoreCase = true;
		
		try {
			day = r.getEnum(1, TestEnumDay.class, ignoreCase);
		
		} catch (JSONRPC2Error e) {
			fail(e.getMessage());
		}
		
		assertEquals(day.toString(), "TUESDAY");
		
		try {
			day = r.getEnum(2, TestEnumDay.class);
			fail("Failed to raise exception");
			
		} catch (JSONRPC2Error e) {
			// ok
		}
	}
	
	
	public void testGetEnumNamed() {
	
		Map m = new HashMap();
		m.put("Montag", "MONDAY");
		m.put("Diestag", "tuesday");
		m.put("Eintag", "SOMEDAY");
		
		NamedParamsRetriever r = new NamedParamsRetriever(m);
		
		TestEnumDay day = null;
		
		try {
			day = r.getEnum("Montag", TestEnumDay.class);
		
		} catch (JSONRPC2Error e) {
			fail(e.getMessage());
		}
		
		assertEquals(day.toString(), "MONDAY");
		
		boolean ignoreCase = true;
		
		try {
			day = r.getEnum("Diestag", TestEnumDay.class, ignoreCase);
		
		} catch (JSONRPC2Error e) {
			fail(e.getMessage());
		}
		
		assertEquals(day.toString(), "TUESDAY");
		
		try {
			day = r.getEnum("Eintag", TestEnumDay.class);
			fail("Failed to raise exception");
			
		} catch (JSONRPC2Error e) {
			// ok
		}
	}
	
	
	public void testEnsurePositional() {
	
		List l = new LinkedList();
		l.add("one");
		l.add("two");
		l.add("three");
		
		PositionalParamsRetriever r = new PositionalParamsRetriever(l);
		
		try {
			r.ensureParam(0);
			r.ensureParam(1);
			r.ensureParam(2);
		} catch (JSONRPC2Error e) {
			fail(e.getMessage());
		}
		
		try {
			r.ensureParam(3);
			fail("Failed to raise exception");
		} catch (JSONRPC2Error e) {
			// ok
		}
	}
	
	
	public void testEnsureNamed() {
	
		Map m = new HashMap();
		m.put("one", 1);
		m.put("two", 2);
		m.put("three", 3);
		
		NamedParamsRetriever r = new NamedParamsRetriever(m);
		
		try {
			r.ensureParam("one");
			r.ensureParam("two");
			r.ensureParam("three");
		} catch (JSONRPC2Error e) {
			fail(e.getMessage());
		}
		
		try {
			r.ensureParam("four");
			fail("Failed to raise exception");
		} catch (JSONRPC2Error e) {
			// ok
		}
	}
	
	
	public void testEnsurePositionalTyped() {
	
		List l = new LinkedList();
		l.add("one");
		l.add(2);
		l.add(null);
		
		PositionalParamsRetriever r = new PositionalParamsRetriever(l);
		
		try {
			r.ensureParam(0, String.class);
			r.ensureParam(1, Integer.class);
		} catch (JSONRPC2Error e) {
			fail(e.getMessage());
		}
		
		try {
			r.ensureParam(2, Double.class);
			fail("Failed to raise exception");
		} catch (JSONRPC2Error e) {
			// ok
		}
	}
	
	
	public void testEnsureNamedTyped() {
	
		Map m = new HashMap();
		m.put("one", "eins");
		m.put("two", 2);
		m.put("three", null);
		
		NamedParamsRetriever r = new NamedParamsRetriever(m);
		
		try {
			r.ensureParam("one", String.class);
			r.ensureParam("two", Integer.class);
		} catch (JSONRPC2Error e) {
			fail(e.getMessage());
		}
		
		try {
			r.ensureParam("three", Double.class);
			fail("Failed to raise exception");
		} catch (JSONRPC2Error e) {
			// ok
		}
	}
	
	
	public void testPositionalNull() {
	
		List l = new LinkedList();
		l.add(null);
		
		PositionalParamsRetriever r = new PositionalParamsRetriever(l);
		
		boolean allowNull = true;
		
		assertTrue(r.hasParam(0));
		
		try {
			r.get(0, String.class, allowNull);
			r.getOpt(0, String.class, allowNull, "HELLO");
			r.getString(0, allowNull);
			r.getOptString(0, allowNull, "HELLO");
			r.getList(0, allowNull);
			r.getOptList(0, allowNull, new LinkedList());
			r.getMap(0, allowNull);
			r.getOptMap(0, allowNull, new HashMap());
		
		} catch (JSONRPC2Error e) {
			fail(e.getMessage());
		}
		
		allowNull = false;
		
		try {
			r.get(0, String.class, allowNull);
			fail("Failed to raise exception");
		} catch (JSONRPC2Error e) {
			// ok
		}
		
		try {
			r.getOpt(0, String.class, allowNull, "HELLO");
			fail("Failed to raise exception");
		} catch (JSONRPC2Error e) {
			// ok
		}
		
		try {
			r.getString(0, allowNull);
			fail("Failed to raise exception");
		} catch (JSONRPC2Error e) {
			// ok
		}
		
		try {
			r.getOptString(0, allowNull, "HELLO");
			fail("Failed to raise exception");
		} catch (JSONRPC2Error e) {
			// ok
		}
		
		try {
			r.getList(0, allowNull);
			fail("Failed to raise exception");
		} catch (JSONRPC2Error e) {
			// ok
		}
		
		try {
			r.getOptList(0, allowNull, new LinkedList());
			fail("Failed to raise exception");
		} catch (JSONRPC2Error e) {
			// ok
		}
		
		try {
			r.getMap(0, allowNull);
			fail("Failed to raise exception");
		} catch (JSONRPC2Error e) {
			// ok
		}
		
		try {
			r.getOptMap(0, allowNull, new HashMap());
			fail("Failed to raise exception");
		} catch (JSONRPC2Error e) {
			// ok
		}
	
	}
	
	
	public void testNamedNull() {
	
		Map m = new HashMap();
		m.put("one", null);
		
		NamedParamsRetriever r = new NamedParamsRetriever(m);
		
		boolean allowNull = true;
		
		assertTrue(r.hasParam("one"));
		
		try {
			r.get("one", String.class, allowNull);
			r.getOpt("one", String.class, allowNull, "HELLO");
			r.getString("one", allowNull);
			r.getOptString("one", allowNull, "HELLO");
			r.getList("one", allowNull);
			r.getOptList("one", allowNull, new LinkedList());
			r.getMap("one", allowNull);
			r.getOptMap("one", allowNull, new HashMap());
		
		} catch (JSONRPC2Error e) {
			fail(e.getMessage());
		}
		
		allowNull = false;
		
		try {
			r.get("one", String.class, allowNull);
			fail("Failed to raise exception");
		} catch (JSONRPC2Error e) {
			// ok
		}
		
		try {
			r.getOpt("one", String.class, allowNull, "HELLO");
			fail("Failed to raise exception");
		} catch (JSONRPC2Error e) {
			// ok
		}
		
		try {
			r.getString("one", allowNull);
			fail("Failed to raise exception");
		} catch (JSONRPC2Error e) {
			// ok
		}
		
		try {
			r.getOptString("one", allowNull, "HELLO");
			fail("Failed to raise exception");
		} catch (JSONRPC2Error e) {
			// ok
		}
		
		try {
			r.getList("one", allowNull);
			fail("Failed to raise exception");
		} catch (JSONRPC2Error e) {
			// ok
		}
		
		try {
			r.getOptList("one", allowNull, new LinkedList());
			fail("Failed to raise exception");
		} catch (JSONRPC2Error e) {
			// ok
		}
		
		try {
			r.getMap("one", allowNull);
			fail("Failed to raise exception");
		} catch (JSONRPC2Error e) {
			// ok
		}
		
		try {
			r.getOptMap("one", allowNull, new HashMap());
			fail("Failed to raise exception");
		} catch (JSONRPC2Error e) {
			// ok
		}
	}


	public void testJSONRPC2ErrorMessagesNamed() {


		Map<String,Object> params = new HashMap<String,Object>();

		NamedParamsRetriever r = new NamedParamsRetriever(params);


		try {
			r.getString("one");

			fail("Failed to raise exception");
		
		} catch (JSONRPC2Error e) {

			assertTrue(JSONRPC2Error.INVALID_PARAMS.equals(e));

			assertEquals("Invalid parameters: Missing one parameter(s)", e.getMessage());
		}


		params.put("one", null);

		r = new NamedParamsRetriever(params);

		try {
			r.ensureParam("one", String.class, false);

			fail("Failed to raise exception");

		} catch (JSONRPC2Error e) {

			assertTrue(JSONRPC2Error.INVALID_PARAMS.equals(e));

			assertEquals("Invalid parameters: Parameter one must not be null", e.getMessage());
		}


		params.put("two", 2);

		r = new NamedParamsRetriever(params);

		try {
			r.getString("two");

			fail("Failed to raise exception");
		
		} catch (JSONRPC2Error e) {

			assertTrue(JSONRPC2Error.INVALID_PARAMS.equals(e));

			assertEquals("Invalid parameters: Parameter two has an unexpected JSON type", e.getMessage());
		}
	}


	public void testJSONRPC2ErrorMessagesPositional() {


		List<Object> params = new LinkedList<Object>();

		PositionalParamsRetriever r = new PositionalParamsRetriever(params);


		try {
			r.getString(0);

			fail("Failed to raise exception");
		
		} catch (JSONRPC2Error e) {

			assertTrue(JSONRPC2Error.INVALID_PARAMS.equals(e));

			assertEquals("Invalid parameters: Missing parameter at position 0", e.getMessage());
		}


		params.add(null);

		r = new PositionalParamsRetriever(params);

		try {
			r.ensureParam(0, String.class, false);

			fail("Failed to raise exception");

		} catch (JSONRPC2Error e) {

			assertTrue(JSONRPC2Error.INVALID_PARAMS.equals(e));

			assertEquals("Invalid parameters: Parameter at position 0 must not be null", e.getMessage());
		}


		params.add(2);

		r = new PositionalParamsRetriever(params);

		try {
			r.getString(1);

			fail("Failed to raise exception");
		
		} catch (JSONRPC2Error e) {

			assertTrue(JSONRPC2Error.INVALID_PARAMS.equals(e));

			assertEquals("Invalid parameters: Parameter at position 1 has an unexpected JSON type", e.getMessage());
		}
	}
}
