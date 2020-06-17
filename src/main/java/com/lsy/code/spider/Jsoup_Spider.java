package com.lsy.code.spider;

import java.util.HashMap;
import java.util.Map;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.alibaba.fastjson.JSON;

public class Jsoup_Spider {
	public static void main(String[] args) throws Exception {
		String url = "http://www.stats.gov.cn/tjsj/tjbz/tjyqhdmhcxhfdm/2018/";
		Map<String, Map<String, Object>> map = new HashMap<String, Map<String,Object>>();
		Connection connect = Jsoup.connect(url);
		Document doc = connect.get();
		Elements elements = doc.select(".provincetr a");
		for (Element element : elements) {
			String href = element.attr("href");
			String provinceName = element.text();
			parseCityName(map,provinceName,element.baseUri()+href);
		}
		System.out.println(JSON.toJSONString(map));
	}

	private static void parseCityName(Map<String, Map<String, Object>> map, String provinceName, String url) throws Exception {
		try {
			Map<String, Object> cityMap = new HashMap<String, Object>();
			Connection connect = Jsoup.connect(url);
			Document doc = connect.get();
			Elements elements = doc.select(".citytr");
			for (Element element : elements) {
				Elements tdElements = element.getElementsByTag("td");
				Elements aElements0 = tdElements.get(0).getElementsByTag("a");
				Elements aElements1 = tdElements.get(1).getElementsByTag("a");
				String href = aElements0.attr("href");
				String code = aElements0.text();
				String cityName = aElements1.text();
				cityMap.put(cityName, code);
			}
			map.put(provinceName, cityMap);
		} catch (Exception e) {
			parseCityName(map, provinceName, url);
		}

	}
}
