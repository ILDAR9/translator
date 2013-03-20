package dict.translators;

public class TranslatorThread extends Thread {
	private final Translator dict;
	private final String en;
	private String translateRus;

	public TranslatorThread(Translator dict, String en) {
		this.dict = dict;
		this.en = en;
	}

	public void run() {
		translateRus = dict.translate(en);
	}

	public String getTranslate() {
		return translateRus;
	}
}
