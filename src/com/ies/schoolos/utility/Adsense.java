package com.ies.schoolos.utility;

public class Adsense {
	private static StringBuilder ADSENSE = new StringBuilder();

	public static String getAdsenseUrl(){
		ADSENSE.append("<script async src='//pagead2.googlesyndication.com/pagead/js/adsbygoogle.js'></script>");
		ADSENSE.append("<!-- SchoolOS Touch -->");
		ADSENSE.append("<ins class='adsbygoogle' style='display:block' data-ad-client='ca-pub-9152752877294115' data-ad-slot='4603021980' data-ad-format='auto'></ins>");
		ADSENSE.append("<script>");
		ADSENSE.append("<ins class='adsbygoogle' style='display:block' data-ad-client='ca-pub-9152752877294115' data-ad-slot='4603021980' data-ad-format='auto'></ins>");
		ADSENSE.append("<script>");
		ADSENSE.append("(adsbygoogle = window.adsbygoogle || []).push({});");
		ADSENSE.append("</script>");
		return ADSENSE.toString();
	}		
			
			
			
			
}
