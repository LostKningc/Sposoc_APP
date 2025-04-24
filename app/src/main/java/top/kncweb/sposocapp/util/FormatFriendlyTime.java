package top.kncweb.sposocapp.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public  class FormatFriendlyTime {

    /**
     * 将时间戳转换为友好的时间格式
     *
     * @param rawTime 原始时间字符串，格式为 "yyyy-MM-dd HH:mm:ss"
     * @return 友好的时间字符串
     */
    public static String formatFriendlyTime(String rawTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date;
        try {
            date = sdf.parse(rawTime);
        } catch (Exception e) {
            return "";
        }

        Calendar now = Calendar.getInstance();
        Calendar time = Calendar.getInstance();
        time.setTime(date);

        if (now.get(Calendar.YEAR) == time.get(Calendar.YEAR)) {
            int diffDay = now.get(Calendar.DAY_OF_YEAR) - time.get(Calendar.DAY_OF_YEAR);
            if (diffDay == 0) {
                return new SimpleDateFormat("HH:mm", Locale.getDefault()).format(date); // 今天
            } else if (diffDay == 1) {
                return "昨天";
            } else if (diffDay >= 2 && diffDay <= 6) {
                String[] weekDays = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
                int dayOfWeek = time.get(Calendar.DAY_OF_WEEK);
                return weekDays[dayOfWeek - 1];
            } else {
                return new SimpleDateFormat("MM-dd", Locale.getDefault()).format(date); // 今年更早
            }
        } else {
            return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(date); // 跨年
        }
    }
}
