package com.disney.qa.common.comparator;

public class FieldsComparator {

	public static int compareFields(String name, String s1, String s2) {
		int res = 0;
		if (isStringEmpty(s2) && isStringEmpty(s1)) {
			res = 0;
		} else if (isStringEmpty(s1) && !isStringEmpty(s2)) {
			res = -1;
		} else if (isStringEmpty(s2) && !isStringEmpty(s1)) {
			res = 1;
		} else {
			res = s1.compareTo(s2);
		}
		doLogging(name, s1, s2, res == 0);
		return res;
	}

	public static int compareFieldsIgnoreCase(String name, String s1, String s2) {
		return compareFields(name, s1 == null ? null : s1.toLowerCase(), s2 == null ? null : s2.toLowerCase());
	}

	public static int compareFields(String name, Boolean b1, Boolean b2) {
		int res = 0;
		if (b1 == null && b2 != null) {
			res = -1;
		}
		else if (b1 != null && b2 == null) {
			res = 1;
		}
		else if (b1 == null && b2 == null) {
			res = 0;
		}
		else {
			res = b1.compareTo(b2);
		}
		doLogging(name, b1, b2, res == 0);
		return res;
	}

	protected static void doLogging(String name, Object str1, Object str2,
			boolean equal) {
//		if (equal) {
//			logger.info(new StringBuilder("Equal parameters - ").append(name)
//					.append(" \"").append(str1).append("\" = \"").append(str2)
//					.append("\"").toString());
//
//		} else {
//			logger.info(new StringBuilder("Different parameters - ")
//					.append(name).append(" \"").append(str1).append("\" != \"")
//					.append(str2).append("\"").toString());
//		}
	}

	protected static boolean isStringEmpty(String str) {
		return null == str || str.isEmpty();
	}
}
