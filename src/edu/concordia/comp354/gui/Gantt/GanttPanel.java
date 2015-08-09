package edu.concordia.comp354.gui.Gantt;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;
import de.jollyday.Holiday;
import de.jollyday.HolidayManager;
import de.jollyday.ManagerParameters;
import edu.concordia.comp354.gui.ActivityEntry;
import net.objectlab.kit.datecalc.common.DateCalculator;
import net.objectlab.kit.datecalc.common.DefaultHolidayCalendar;
import net.objectlab.kit.datecalc.common.HolidayCalendar;
import net.objectlab.kit.datecalc.common.HolidayHandlerType;
import net.objectlab.kit.datecalc.jdk8.LocalDateKitCalculatorsFactory;

import java.awt.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

/**
 * Created by joao on 15.07.26.
 */
public class GanttPanel extends mxGraphComponent {
    private final ActivityEntry activityEntry;

    public GanttPanel(ActivityEntry activityEntry, mxGraph graph) {
        super(graph);
        this.activityEntry = activityEntry;
    }

    @Override
    protected void paintBackground(Graphics g) {
        super.paintBackground(g);
        Graphics2D g2 = (Graphics2D) g;

        String country = Locale.getDefault().getCountry();

        HolidayManager.setManagerCachingEnabled(true);
        HolidayManager manager = HolidayManager.getInstance(ManagerParameters.create("Canada"));


// create or get the Holidays
        final Set<Holiday> holidays = manager.getHolidays(2015);

// fill dates into set of LocalDate
        Set<LocalDate> holidayDates = new HashSet<LocalDate>();
        for (Holiday h : holidays) {
            holidayDates.add(h.getDate());
        }

        DateTimeFormatter localFormatter = DateTimeFormatter.ofLocalizedDate(
                FormatStyle.MEDIUM).withLocale(Locale.getDefault());

// create the HolidayCalendar ASSUMING that the set covers 2015!
        final HolidayCalendar<LocalDate> calendar = new DefaultHolidayCalendar<LocalDate>
                (holidayDates, LocalDate.parse("2015-01-01"), LocalDate.parse("2025-12-31"));

// register the holidays, any calculator with name "Canada"
// asked from now on will receive an IMMUTABLE reference to this calendar
        LocalDateKitCalculatorsFactory.getDefaultInstance().registerHolidays("Canada", calendar);

// ask for a LocalDate calculator for "Canada"
// (even if a new set of holidays is registered, this one calculator is not affected
        DateCalculator<LocalDate> dateCalculator = LocalDateKitCalculatorsFactory.getDefaultInstance()
                .getDateCalculator("Canada", HolidayHandlerType.FORWARD);


        LocalDate date = activityEntry.getCurrentProject().getStart_date();

        dateCalculator.setStartDate(date);

        if (dateCalculator.getCurrentBusinessDate().equals(date)) {
            //  set to first work day of  week
            while (dateCalculator.isCurrentDateNonWorking()) {
                dateCalculator.setCurrentBusinessDate(dateCalculator.getCurrentBusinessDate().plusDays(-1));
            }
            dateCalculator.setCurrentBusinessDate(dateCalculator.getCurrentBusinessDate().plusDays(1));
        }

        int x = 0;
        int y = 0;
        int zoom = ActivityEntry.X_SCALE;

        String[] days = "S M T W T F S".split(" ");

        int numOfVisibleDays = getWidth() / ActivityEntry.X_SCALE;

        final int DAYS_PER_WEEK = 7;

        //  find first Sunday before start date

        LocalDate curDate = dateCalculator.getStartDate();
        while (curDate.getDayOfWeek() != DayOfWeek.SUNDAY) {
            curDate = curDate.plusDays(-1);
        }
        Color oldColor = g.getColor();
        g.setColor(new Color(245, 245, 245));
        g2.fillRect(0, y * zoom - 2 + 1, getWidth(), zoom * 2 - 5);
        g.setColor(oldColor);

        int numOfVisibleWeeks = numOfVisibleDays / DAYS_PER_WEEK + 1;
        for (int i = 0; i < numOfVisibleWeeks; i++) {
            g2.drawString(curDate.format(DateTimeFormatter.ofPattern("dd MMM yy")), i * ActivityEntry.X_SCALE * DAYS_PER_WEEK + 6, y * zoom + zoom / 2 + 1);
            curDate = curDate.plusDays(DAYS_PER_WEEK);
        }


        oldColor = g.getColor();
        g.setColor(new Color(245, 245, 245));
        g2.fillRect(0, y * zoom + zoom / 2 + 1, ActivityEntry.X_SCALE, getHeight());
        for (int i = 1; i < numOfVisibleWeeks; i++) {
            g2.fillRect(i * ActivityEntry.X_SCALE * DAYS_PER_WEEK - ActivityEntry.X_SCALE, y * zoom + zoom / 2 + 4 + 1, 2 * ActivityEntry.X_SCALE, getHeight());
            g.setColor(Color.LIGHT_GRAY);
            g2.drawLine(i * ActivityEntry.X_SCALE * DAYS_PER_WEEK, y * zoom + zoom / 2 + 4 + 1, i * ActivityEntry.X_SCALE * DAYS_PER_WEEK + 1, getHeight());
            g.setColor(new Color(245, 245, 245));
        }
        g.setColor(oldColor);

        g.setColor(Color.LIGHT_GRAY);
        g2.drawLine(x * zoom + 6, y * zoom + zoom / 2 + 3 + 1, getWidth(), y * zoom + zoom / 2 + 3 + 1);
        g.setColor(Color.GRAY);


        for (int i = 0; i < numOfVisibleDays; i++) {

            g2.drawString(days[i % days.length], i * ActivityEntry.X_SCALE + 6, y * zoom + zoom / 2 + 19 + 1);
        }
        g2.drawLine(x * zoom + 6, y * zoom + zoom / 2 + 3 + 20 + 1, getWidth(), y * zoom + zoom / 2 + 3 + 20 + 1);
    }
}
