package dict.translators;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import dict.Messages;
import dict.exceptions.language.LanguageSyntaxException;
import dict.translators.cache.TranslatorCache;

public class TranslatorWeb implements Translator {
	private final static String API = "http://translate.yandex.net/api/v1/tr/translate?lang=en-ru&text=";
	private final static Logger logger = LoggerFactory.getLogger(TranslatorWeb.class);
	private TranslatorCache wayToDictBD;

	// constructor returns object which can collect new words into Cache
	public TranslatorWeb(TranslatorCache wayToDictBD) {
		this.wayToDictBD = wayToDictBD;
	}

	// returns simple web translator
	public TranslatorWeb() {
	}

	public String translate(String en) {
		String rus = null;
		try {
			URLConnection con = (new URL(API + en)).openConnection();
			InputStream input = con.getInputStream();
			DocumentBuilderFactory document = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = document.newDocumentBuilder();
			Document doc = builder.parse(input);
			input.close();
			NodeList nodeList = doc.getChildNodes();
			if (nodeList != null) {
				for (int i = 0; i < nodeList.getLength(); i++) {
					Node node = nodeList.item(i);
					if ("Translation".equals(node.getNodeName())) {
						NodeList mainContent = node.getChildNodes();
						if (mainContent != null) {
							for (int j = 0; j < mainContent.getLength(); j++) {
								Node ans = mainContent.item(j);
								if ("text".equals(ans.getNodeName())) {
									rus = ans.getTextContent();
								}
							}
						}
					}
				}
				if (wayToDictBD != null)
					insertNewWord(en, rus);
			}
		} catch (UnknownHostException ex) {
			System.out.println("There is no Internet connection, pleas repair this problem.");
			logger.error("{} {} get connection, please repair Internet connection [{}]", ex.getClass(),
					Messages.logCaughtException, API, en, ex);
		} catch (MalformedURLException ex) {
			logger.error("{} {} parse url: {} {}", ex.getClass(), Messages.logCaughtException, API, en, ex);
		} catch (IOException ex) {
			logger.error("{} {} parse xml request for word {}", ex.getClass(), Messages.logCaughtException, en, ex);
		} catch (ParserConfigurationException ex) {
			logger.error("{} {} parse xml response for word {}", Messages.logCaughtException, ex.getClass(), en, ex);
		} catch (SAXException ex) {
			logger.error("{} {} parse xml response for word {}", Messages.logCaughtException, ex.getClass(), en, ex);
		}
		return rus;
	}

	private void insertNewWord(String en, String rus) {
		if (rus == null || rus.equals(en) || rus.isEmpty())
			return;
		try {
			wayToDictBD.addWord(en, rus);
		} catch (LanguageSyntaxException ex) {
			logger.error("{} {} insert new word to dictionary-BD: en={} : rus={}", ex.getClass(),
					Messages.logCaughtException, en, rus, ex);
		}
	}
}
