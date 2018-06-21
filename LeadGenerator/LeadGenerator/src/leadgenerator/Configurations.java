/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package leadgenerator;

/**
 *
 * @author NDBDEV
 */
public class Configurations {

    public static String searchKeyPhrase = "colombo colts cricket club administration";

    //Regular Expressions for search key patterns
    public static final String REGEX_NAME = "[A-Z]([a-z]*) [A-Z]([a-z]*)";
    public static final String REGEX_EMAIL = "(?:[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?\\.)+[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-zA-Z0-9-]*[a-zA-Z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";
    public static final String REGEX_PHONE = "(0|94)[1-9]\\d{8}";

    // Fake USER_AGENT for the web server to think the robot is a normal web browser.
    public static final String USER_AGENT = "Mozilla/5.0 (compatible; Googlebot/2.1";
    // public static final String USER_AGENT= "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/13.0.782.112 Safari/535.1";
    // public static final String USER_AGENT ="Mozilla/5.0 (Windows; U; Windows NT 6.1; en-US)";
    
    // link depth
    public static int linkDepthForNames=1;
    public static int linkDepthForEmails=1;
    public static int linkDepthForPhone=1;
    
    // results depth
    public static int googleURLSearchResultEnd=10;
    public static int googleURLEmailResultEnd=100;
    public static int googleURLPhoneResultEnd=100;
}
