/**
 * 
 */
package seedu.task.model.task;
import seedu.task.commons.exceptions.IllegalValueException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
/**
 * @author Daniel Mullen and Jacob Levy
 *
 */
public class TaskDate {
	
	private List<Date> dates;
    private SimpleDateFormat formatter;
    public final String OUTPUT_FORMAT = " dd/mm/yy";
	
    private int day;
    private int month;
    private int year;
    private int day2 = 0;
    private int month2 = 0;
    private int year2 = 0;
    public final String value;
    private final String DATE_DELIMITER = "/";
    private final String DATE_DELIMITER2 = "-";
    private final int DAY_ARRAY_INDEX = 0;
    private final int MONTH_ARRAY_INDEX = 1;
    private final int YEAR_ARRAY_INDEX = 2;
    private final int DAY2_ARRAY_INDEX = 3;
    private final int MONTH2_ARRAY_INDEX = 4;
    private final int YEAR2_ARRAY_INDEX = 5;

    private static final int DAY_START_INDEX = 0;
    private static final int DAY_END_INDEX = 2;
    private static final int MONTH_START_INDEX = 2;
    private static final int MONTH_END_INDEX = 4;
    private static final int YEAR_START_INDEX = 4;
    private static final int YEAR_END_INDEX = 6;

    private static final int DAY2_START_INDEX = 7;
    private static final int DAY2_END_INDEX = 9;
    private static final int MONTH2_START_INDEX = 9;
    private static final int MONTH2_END_INDEX = 11;
    private static final int YEAR2_START_INDEX = 11;
    private static final int YEAR2_END_INDEX = 13;

    public static final String MESSAGE_INVALID_DATE_FORMAT = "Invaid date, try ddmmyy-ddmmyy ";

    
    public TaskDate(String input) throws IllegalValueException {

	value = input.trim();
	try{
		int[] dateArray = dateFormatConverter(input);
		if (input.length() == 6) {
		    setDay(dateArray[DAY_ARRAY_INDEX]);
		    setMonth(dateArray[MONTH_ARRAY_INDEX]);
		    setYear(dateArray[YEAR_ARRAY_INDEX]);
		}
		if (input.length() == 13) {
		    setDay(dateArray[DAY_ARRAY_INDEX]);
		    setMonth(dateArray[MONTH_ARRAY_INDEX]);
		    setYear(dateArray[YEAR_ARRAY_INDEX]);
		    setDay2(dateArray[DAY2_ARRAY_INDEX]);
		    setMonth2(dateArray[MONTH2_ARRAY_INDEX]);
		    setYear2(dateArray[YEAR2_ARRAY_INDEX]);
		}
	}catch(Exception e){
	    formatter = new SimpleDateFormat (OUTPUT_FORMAT);
	    NattyParser natty = new NattyParser();
	    dates = natty.parse(input);
	    	
	    if(dates == null)
	    	throw new IllegalValueException(MESSAGE_INVALID_DATE_FORMAT);
	}
	

    }

    public void setDay(int day) {
	if (day > 0 && day <= 31) {
	    this.day = day;
	} else {
	    throw new IllegalArgumentException("Invalid day");
	}
    }

    public void setMonth(int month) {
	if (month > 0 && month <= 12) {
	    this.month = month;
	} else {
	    throw new IllegalArgumentException("Invalid month");
	}
    }

    public void setYear(int year) {
	if (year > 0) {
	    this.year = year;
	} else {
	    throw new IllegalArgumentException("Invalid year");
	}
    }

    public void setDay2(int day) {
	if (day > 0 && day <= 31) {
	    this.day2 = day;
	} else {
	    throw new IllegalArgumentException("Invalid day");
	}
    }

    public void setMonth2(int month) {
	if (month > 0 && month <= 12) {
	    this.month2 = month;
	} else {
	    throw new IllegalArgumentException("Invalid month");
	}
    }

    public void setYear2(int year) {
	if (year > 0) {
	    this.year2 = year;
	} else {
	    throw new IllegalArgumentException("Invalid year");
	}
    }


    public String toString() {
    if(dates == null){
    	if (day2 == 0 && month2 == 0 && year2 == 0) {
    	    return day + DATE_DELIMITER + month + DATE_DELIMITER + year;
    	}
    	return day + DATE_DELIMITER + month + DATE_DELIMITER + year + " " + DATE_DELIMITER2 + " " + day2
    		+ DATE_DELIMITER + month2 + DATE_DELIMITER + year2;
    }else{
    	if(dates.size() == 1)
        	return formatter.format(dates.get(0));
    	else
    		return formatter.format(dates.get(0)) +" - "+ formatter.format(dates.get(1));
    	
    }
    }

    public static int[] dateFormatConverter(String date) {
	if (date.length() == 6) {
	    int day = Integer.parseInt(date.substring(DAY_START_INDEX, DAY_END_INDEX));
	    int month = Integer.parseInt(date.substring(MONTH_START_INDEX, MONTH_END_INDEX));
	    int year = Integer.parseInt(date.substring(YEAR_START_INDEX, YEAR_END_INDEX));
	    int[] returnArray = { day, month, year };
	    return returnArray;
	}
	if (date.length() == 13) {
	    int day = Integer.parseInt(date.substring(DAY_START_INDEX, DAY_END_INDEX));
	    int month = Integer.parseInt(date.substring(MONTH_START_INDEX, MONTH_END_INDEX));
	    int year = Integer.parseInt(date.substring(YEAR_START_INDEX, YEAR_END_INDEX));
	    int day2 = Integer.parseInt(date.substring(DAY2_START_INDEX, DAY2_END_INDEX));
	    int month2 = Integer.parseInt(date.substring(MONTH2_START_INDEX, MONTH2_END_INDEX));
	    int year2 = Integer.parseInt(date.substring(YEAR2_START_INDEX, YEAR2_END_INDEX));
	    int[] returnArray = { day, month, year, day2, month2, year2 };
	    return returnArray;
	} else {
	    throw new IllegalArgumentException("Invalid date format");
	}

    }

    public int compareTo(TaskDate other) {
	if (this.year > other.year) {
	    return 1;
	} else if (this.year < other.year) {
	    return -1;
	}
	if (this.month > other.month) {
	    return 1;
	} else if (this.month < other.month) {
	    return -1;
	}
	if (this.day > other.day) {
	    return 1;
	} else if (this.day < other.day) {
	    return -1;
	}
	return 0;
    }
}
