package cn.chf.lightjob.dal.base;

public class UnderlineHumpUtil {
    public UnderlineHumpUtil() {
    }

    public static String UnderlineToHump(String para) {
        StringBuilder result = new StringBuilder();
        String[] a = para.split("_");
        String[] var3 = a;
        int var4 = a.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            String s = var3[var5];
            if (result.length() == 0) {
                result.append(s.toLowerCase());
            } else {
                result.append(s.substring(0, 1).toUpperCase());
                result.append(s.substring(1).toLowerCase());
            }
        }

        return result.toString();
    }

    public static String HumpToUnderline(String para) {
        StringBuilder sb = new StringBuilder(para);
        int temp = 0;

        for(int i = 0; i < para.length(); ++i) {
            if (i > 0 && Character.isUpperCase(para.charAt(i))) {
                sb.insert(i + temp, "_");
                ++temp;
            }
        }

        return sb.toString().toLowerCase();
    }
}
