package com.trihydro.cvpt;


import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * Provides some simple parse routines to extract sepecific values from 
 * request/response content or header strings.
 *
 */
public class TestUtilityParse
{

	/**
	 * Utility routine to extract the record id from the location URL
	 * The location URL is expected to have the form:
	 *       "/some/path/123"
	 * The path may contain numbers, but only the digits at the end of
	 * the URL are considered the record id
	 *
	 * @param location - URL in response header location format
	 * @param Message for exception if parse of the id is not successful
	 *
	 */
	public static String getIdFromHeaderLocation(String location, String exceptionMessage) throws Exception
	{
		Pattern p = Pattern.compile("\\d+$"); //get digits from end of url string 
		Matcher m = p.matcher(location);
		if (!m.find())
		{
			throw new Exception(exceptionMessage);
		}
		return m.group();
	}

	/**
	 * Utility routine to extract the record id from the content
	 * The id value is assumed to be the first series of digits 
	 * following the given field name, in standard JSON format:
	 *      {"fieldName":123, "someOtherField":"aValue"} 
	 *
	 * @param content - response body content
	 * @param field - the field name of the id 
	 * @param Message for exception if the parse of the id is not successful
	 */
	public static String getIdFromBodyContent(String content, String field, String exceptionMessage) throws Exception
	{
		String regex = field + "\"\\s*:\\s*(\\d+)"; //get digits following field name
		Pattern p = Pattern.compile(regex);  
		Matcher m = p.matcher(content);
		if (!m.find())
		{
			throw new Exception(exceptionMessage);
		}
		return m.group(1); //return matched digits group
	}
}