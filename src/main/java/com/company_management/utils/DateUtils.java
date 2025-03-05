package com.company_management.utils;

import lombok.experimental.UtilityClass;

import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@UtilityClass
public class DateUtils {
    private static ZoneId localZoneId = ZoneId.of("Asia/Ho_Chi_Minh");
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("d/M/yyyy");
    private static final SimpleDateFormat INPUT_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static LocalDate convertToLocalDateViaInstant(Date dateToConvert) {
        if (dateToConvert == null) {
            return null;
        }
        return dateToConvert.toInstant().atZone(localZoneId).toLocalDate();
    }

    public static LocalDateTime convertToLocalDateTime(Date dateToConvert) {
        if (dateToConvert == null) {
            return null;
        }
        return dateToConvert.toInstant().atZone(localZoneId).toLocalDateTime();
    }


    public static Date convertToDate(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return null;
        }

        return Date.from(localDateTime.atZone(localZoneId).toInstant());
    }

    public static Date convertToDate(LocalDate localDateTime) {
        if (localDateTime == null) {
            return null;
        }

        return Date.from(localDateTime.atStartOfDay(localZoneId).toInstant());
    }

    public static Date plusMonths(Date input, long month) {
        LocalDateTime localIn = convertToLocalDateTime(input);
        return convertToDate(localIn.plusMonths(month));
    }

    public static Date getNow() {
        LocalDateTime now = LocalDateTime.now(localZoneId);
        return convertToDate(now);
    }

    /**
     * Chuyển đổi Date thành chuỗi định dạng yyyy-MM-dd
     *
     * @param dateToConvert Đối tượng Date cần chuyển đổi
     * @return Chuỗi định dạng yyyy-MM-dd hoặc null nếu dateToConvert là null
     */
    public static String convertDateToString(Date dateToConvert) {
        if (dateToConvert == null) {
            return null;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(dateToConvert);
    }

    /**
     * Chuyển đổi Date thành chuỗi
     *
     * @param dateToConvert Đối tượng Date cần chuyển đổi
     * @param format        Định dạng chuỗi cần chuyển đổi
     * @return Chuỗi định dạng hoặc null
     */
    public static String convertDateToString(Date dateToConvert, String format) {
        if (dateToConvert == null) {
            return null;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(dateToConvert);
    }

    public static String convertDateToStringAndFormat(Date dateToConvert, String format) {
        if (dateToConvert == null) {
            return "";
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(dateToConvert);
    }

    public static String convertDateToStringAndFormat(Date dateToConvert) {
        if (dateToConvert == null) {
            return "";
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return dateFormat.format(dateToConvert);
    }

    public String getFirstDayOfMonth(YearMonth startMonth) {
        // Convert YearMonth to LocalDate
        LocalDate firstDayOfMonth = startMonth.atDay(1);
        // Convert LocalDate to Date
        Date date = Date.from(firstDayOfMonth.atStartOfDay(ZoneId.systemDefault()).toInstant());
        // Create a SimpleDateFormat to format the date as "yyyy-MM-dd"
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        // Format and return the first day of the month
        return sdf.format(date);
    }

    public String getLastDayOfMonth(YearMonth startMonth) {
        // Convert YearMonth to LocalDate
        LocalDate lastDayOfMonth = startMonth.atEndOfMonth();
        // Convert LocalDate to Date
        Date date = Date.from(lastDayOfMonth.atStartOfDay(ZoneId.systemDefault()).toInstant());
        // Create a SimpleDateFormat to format the date as "yyyy-MM-dd"
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        // Format and return the last day of the month
        return sdf.format(date);
    }

    public int getMonth(YearMonth startMonth) {
        // Get the month from YearMonth (1-based)
        return startMonth.getMonthValue();
    }

    public int getYear(YearMonth startMonth) {
        // Get the year from YearMonth
        return startMonth.getYear();
    }

    public int getMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.MONTH) + 1; // Calendar.MONTH is zero-based
    }

    public int getYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.YEAR);
    }

    public int getDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    public static Date expireDate(Date startDate, int monthsToAdd) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        calendar.add(Calendar.MONTH, monthsToAdd);
        return calendar.getTime();
    }

    public static int getDayOfWeek(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    public static Date formatToDate(Integer year, Integer month, Integer day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1); // Calendar.MONTH is zero-based
        calendar.set(Calendar.DAY_OF_MONTH, day);
        return calendar.getTime();
    }

    public static boolean isWeekend(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        return dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY;
    }

    public List<Integer> countDaysBetween(Date startTime, Date endTime) {
        List<Integer> dayList = new ArrayList<>();

        // Sử dụng Calendar để xử lý ngày tháng, đảm bảo bỏ giờ, phút, giây
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startTime);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(endTime);
        endCalendar.set(Calendar.HOUR_OF_DAY, 0);
        endCalendar.set(Calendar.MINUTE, 0);
        endCalendar.set(Calendar.SECOND, 0);
        endCalendar.set(Calendar.MILLISECOND, 0);

        // Duyệt qua từng ngày từ startTime đến endTime (tính cả endTime)
        while (!calendar.after(endCalendar)) {
            dayList.add(calendar.get(Calendar.DAY_OF_MONTH)); // Lưu ngày (ngày trong tháng)
            calendar.add(Calendar.DAY_OF_MONTH, 1); // Tăng ngày lên 1
        }

        return dayList;
    }

    public List<Integer> countDaysBetweenExcludingWeekends(Date startTime, Date endTime) {
        List<Integer> dayList = new ArrayList<>();

        // Sử dụng Calendar để xử lý ngày tháng, đảm bảo bỏ giờ, phút, giây
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startTime);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(endTime);
        endCalendar.set(Calendar.HOUR_OF_DAY, 0);
        endCalendar.set(Calendar.MINUTE, 0);
        endCalendar.set(Calendar.SECOND, 0);
        endCalendar.set(Calendar.MILLISECOND, 0);

        // Duyệt qua từng ngày từ startTime đến endTime (tính cả endTime)
        while (!calendar.after(endCalendar)) {
            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
            if (dayOfWeek != Calendar.SATURDAY && dayOfWeek != Calendar.SUNDAY) {
                dayList.add(calendar.get(Calendar.DAY_OF_MONTH)); // Lưu ngày (ngày trong tháng)
            }
            calendar.add(Calendar.DAY_OF_MONTH, 1); // Tăng ngày lên 1
        }

        return dayList;
    }

    public static Date getDiffTime(Date start, Date end) {
        if (start == null || end == null) {
            return null;
        }
        long diffInMillis = end.getTime() - start.getTime();
        return new Date(resetToMidnight().getTime() + diffInMillis);
    }

    public static Date resetToMidnight() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());

        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTime();
    }

    public static Date stripTime(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    public boolean isOverToday(Date date) {
        return stripTime(date).compareTo(stripTime(getNow())) > 0;
    }

    public static Integer getDiffTimeByDay(Date start, Date end) {
        if (start == null || end == null) {
            return null;
        }
        return Math.toIntExact((end.getTime() - start.getTime()) / (24 * 60 * 60 * 1000));
    }

    public static LocalDate convertStringToLocalDate(String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(dateString, formatter);
    }

    public static LocalDate convertDateToLocalDate(Date date) {
        if (date == null) {
            return null;
        }
        return date.toInstant()
                .atZone(java.time.ZoneId.systemDefault())
                .toLocalDate();
    }

    public static LocalDate convertToLocalDate(Object date) {
        if (date instanceof String) {
            return convertStringToLocalDate((String) date);
        } else if (date instanceof Date) {
            return convertDateToLocalDate((Date) date);
        } else if (date instanceof LocalDate) {
            return (LocalDate) date;
        }
        return null;
    }


    public static boolean isOverlapTime(Date oldStartDate, Date oldEndDate, Date newStartDate, Date newEndDate) {
        LocalDate oldStartLocalDate = convertToLocalDate(oldStartDate);
        LocalDate oldEndLocalDate = convertToLocalDate(oldEndDate);
        LocalDate newStartLocalDate = convertToLocalDate(newStartDate);
        LocalDate newEndLocalDate = convertToLocalDate(newEndDate);
        //        Case 1: Tất cả giá trị đều khác null
        if (newEndLocalDate != null && oldEndLocalDate != null) {
//         Check ngày bắt đầu và kết thúc của hợp đồng mới chạm nhau hoặc nằm trong khoảng ngày bắt đầu và kết thúc của hợp đồng cũ
            if ((newStartLocalDate.isAfter(oldStartLocalDate) || newStartLocalDate.equals(oldStartLocalDate)) &&
                    (newStartLocalDate.isBefore(oldEndLocalDate) || newStartLocalDate.equals(oldEndLocalDate)) ||
                    (newEndLocalDate.isAfter(oldStartLocalDate) || newEndLocalDate.equals(oldStartLocalDate)) &&
                            (newEndLocalDate.isBefore(oldEndLocalDate) || newEndLocalDate.equals(oldEndLocalDate))
            ) {
                return true;
            }
//            Check khoảng cũ nằm trong khoảng mới
            return newStartLocalDate.isBefore(oldStartLocalDate) && newEndLocalDate.isAfter(oldEndLocalDate);
        }
        /*Case 2: expiryDate = null, kiểm tra chỉ với effectiveDate
       Vì expiryDate = null (vô hạn), chỉ cần kiểm tra xem effectiveDate có xảy ra = or trước decisionExpiryDate*/

        else if (newEndLocalDate == null && oldEndLocalDate != null) {
            return newStartLocalDate.equals(oldEndLocalDate) || newStartLocalDate.isBefore(oldEndLocalDate);

        }
       /*Case 3: decisionExpiryDate = null, kiểm tra chỉ với expiryDate
       Nếu decisionExpiryDate = null, chỉ cần kiểm tra xem decisionEffectiveDate có xảy ra trước hoặc trùng với expiryDate của đề nghị*/
        else if (newEndLocalDate != null && oldEndLocalDate == null) {
            return newEndLocalDate.equals(oldStartLocalDate) || newEndLocalDate.isAfter(oldStartLocalDate);
        }

       /* Case 4: Cả expiryDate và calculatedExpiryDate đều null
        Nếu cả expiryDate và calculatedExpiryDate đều là null, điều đó có nghĩa là cả hai đều vô hạn, và vì vậy chắc chắn sẽ trùng thời gian*/
        else if (newEndLocalDate == null && oldEndLocalDate == null) {
            return true;
        }
        return false;
    }

    public static Integer getDiffTimeByMonth(Date start, Date end) {
        if (start == null || end == null) {
            return null;
        }
        LocalDate startDate = start.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate endDate = end.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        return Math.toIntExact(ChronoUnit.MONTHS.between(startDate.withDayOfMonth(1), endDate.withDayOfMonth(1)));
    }

    public static LocalDate convertMonthCodeToEndDate(String monthCode) {
        // Tách năm và tháng từ monthCode
        if (!isValidMonthCode(monthCode)) {
            return null;
        }
        String year = monthCode.trim().substring(0, 4);
        String month = monthCode.trim().substring(4, 6);

        // Tạo đối tượng YearMonth
        YearMonth yearMonth = YearMonth.of(Integer.parseInt(year), Integer.parseInt(month));

        // Lấy ngày cuối cùng của tháng
        LocalDate endOfMonth = yearMonth.atEndOfMonth();

        // Định dạng ngày theo định dạng "MM/yyyy"
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yyyy");
        return endOfMonth;
    }

    public static LocalDate convertMonthCodeToStartDate(String monthCode) {
        if (!isValidMonthCode(monthCode)) {
            return null;
        }

        // Tách năm và tháng từ chuỗi input
        String year = monthCode.substring(0, 4);
        String month = monthCode.substring(4, 6);

        // Tạo LocalDate cho ngày đầu tiên của tháng
        LocalDate firstDay = LocalDate.of(Integer.parseInt(year), Integer.parseInt(month), 1);

        // Định dạng LocalDate thành chuỗi "dd/MM/yyyy"
        return firstDay;
    }

    public static boolean isValidMonthCode(String monthCode) {
        // Biểu thức chính quy để kiểm tra định dạng YYYYMM
        String regex = "^\\d{4}(0[1-9]|1[0-2])$";
        return monthCode.matches(regex);
    }

}

