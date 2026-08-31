package de.devloper.pickelMaintenance.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TimeUtil {

    private static final Pattern TIME_PATTERN = Pattern.compile("(?:(\\d+)d)?\\.?(?:(\\d+)h)?\\.?(?:(\\d+)min)?\\.?(?:(\\d+)s)?");

    public static long parseDurationToMillis(String input) {
        if (input == null || input.isEmpty() || input.equals("0")) return 0;

        Matcher matcher = TIME_PATTERN.matcher(input);
        if (!matcher.matches()) return -1;

        long days = parseGroup(matcher.group(1));
        long hours = parseGroup(matcher.group(2));
        long minutes = parseGroup(matcher.group(3));
        long seconds = parseGroup(matcher.group(4));

        return ((days * 24 + hours) * 60 + minutes) * 60 + seconds;
    }

    private static long parseGroup(String group) {
        return group == null ? 0 : Long.parseLong(group);
    }

    public static String formatRemainingTime(long endTimeMillis) {
        if (endTimeMillis <= 0) return "Unbekannt";

        long remainingSeconds = (endTimeMillis - System.currentTimeMillis()) / 1000;
        if (remainingSeconds <= 0) return "Gleich fertig";

        long days = remainingSeconds / (24 * 3600);
        remainingSeconds %= (24 * 3600);
        long hours = remainingSeconds / 3600;
        remainingSeconds %= 3600;
        long minutes = remainingSeconds / 60;
        long seconds = remainingSeconds % 60;

        StringBuilder sb = new StringBuilder();
        if (days > 0) sb.append(days).append("d.");
        if (hours > 0) sb.append(hours).append("h.");
        if (minutes > 0) sb.append(minutes).append("min.");
        if (seconds > 0 || sb.length() == 0) sb.append(seconds).append("s");

        if (sb.charAt(sb.length() - 1) == '.') {
            sb.deleteCharAt(sb.length() - 1);
        }

        return sb.toString();
    }
}