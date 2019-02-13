package com.forixusa.android.utils;

import java.text.SimpleDateFormat;

public class GlobalConstants {

	public static final SimpleDateFormat DISPLAY_DATE_PARSER = new SimpleDateFormat("MMMM dd");
	public static String EULA_KEY = "";
	public static String EULA_PREFIX = "eula_";
	public static final String FACEBOOK_APP_ID = "194920670544051";
	
	public static final String FEED_URL_LIST[] = new String[] { "http://www.droppingtimber.com/feed.xml",
			"http://feeds.feedburner.com/sportsblogs/stumptownfooty.xml",
			"http://portlandtimbersmls.blogspot.com/feeds/posts/default", "http://blog.oregonlive.com/timbers/atom.xml" };

	// kickaxe/photo_add.php
	public static final String GET_COMMENT_LIST_URL = "http://forixdev.forix.net:8080/forixintranet/kickaxe/comment_list.php";
	public static final String PHOTO_GALLERY_URL = "http://forixdev.forix.net:8080/forixintranet/kickaxe/photo_list.php";
	public static final String POST_COMMENT_URL = "http://forixdev.forix.net:8080/forixintranet/kickaxe/comment_add.php";
	public static final String POST_PHOTO_LIST_URL = "http://forixdev.forix.net:8080/forixintranet/kickaxe/photo_add.php";

	public static final SimpleDateFormat SERVICE_ATOM_DATE_PARSER = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
	public static final SimpleDateFormat SERVICE_ATOM_DATE_PARSER_2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
	public static final SimpleDateFormat SERVICE_DATE_PARSER = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z");

}
