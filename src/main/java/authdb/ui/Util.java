package authdb.ui;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

public class Util {

	private Util() { }

	public static Set<String> stringAsSet(String string) {
		Set<String> result = new HashSet<String>();
		StringTokenizer st = new StringTokenizer(string);
		while (st.hasMoreTokens()) {
			result.add(st.nextToken());
		}
		return result;
	}

	public static String setAsString(Collection<String> collection) {
		StringBuffer result = new StringBuffer();
		for (String s : collection) {
			result.append(s);
			result.append(" ");
		}
		int length = result.length();
		if (length > 1) {
			result.deleteCharAt(result.length() - 1);
		}
		return result.toString();
	}
}
