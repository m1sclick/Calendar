import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.pdf.draw.VerticalPositionMark;
import java.util.ArrayList;

public class PDFCalendar {

    /**
     * The resulting PDF file.
     */
    public static final String RESULT = "calendar.pdf";
    /**
     * The language code for the calendar
     */
    public static final String LANGUAGE = "uk";
    /**
     * Path to the resources.
     */
    public static final String RESOURCE = "resources/%tm.jpg";

    /**
     * A font that is used in the calendar
     */
    protected Font normal;
    /**
     * A font that is used in the calendar
     */
    protected Font small;
    /**
     * A font that is used in the calendar
     */
    protected Font bold;
    /**
     * The year for which we want to create a calendar
     */
    protected int year;
    protected ArrayList<Date> dates = new ArrayList<>();
    protected ArrayList<Date> holidays = new ArrayList<>();

    /**
     * Initializes the fonts and collections.
     *
     * @param year
     * @param dates
     * @throws DocumentException
     * @throws IOException
     */
    public PDFCalendar(int year, ArrayList<Date> dates) throws DocumentException, IOException {
        this.year = year;
        this.dates = dates;
        this.holidays = (ArrayList<Date>) dates.clone();
        Locale locale = new Locale(LANGUAGE);
        // fonts
        BaseFont bf_normal
                = BaseFont.createFont("c://windows/fonts/arial.ttf",
                        "cp1251", BaseFont.EMBEDDED);
        normal = new Font(bf_normal, 16);
        small = new Font(bf_normal, 8);
        BaseFont bf_bold
                = BaseFont.createFont("c://windows/fonts/arialbd.ttf",
                        "cp1251", BaseFont.EMBEDDED);
        bold = new Font(bf_bold, 14);
        createPdf(RESULT, locale);
    }

    /**
     * Creates a PDF document.
     *
     * @param filename the path to the new PDF document
     * @param locale Locale in case you want to create a Calendar in another
     * language
     * @throws DocumentException
     * @throws IOException
     */
    public void createPdf(String filename, Locale locale) throws IOException, DocumentException {
        // step 1
        Document document = new Document(PageSize.A4.rotate());
        // step 2
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(RESULT));
        // step 3
        document.open();
        // step 4
        PdfPTable table;
        Calendar calendar = new GregorianCalendar(year, 0, 1);
        PdfContentByte canvas = writer.getDirectContent();
        // Loop over the months
        for (int month = 0; month < 12; month++) {
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            // draw the background
            drawImage(canvas, calendar);
            // create a table with 7 columns
            table = new PdfPTable(7);
            table.setTotalWidth(504);
            // add the name of the month
            table.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
            table.addCell(getMonthCell(calendar, locale));
            int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
            int day = 1;
            int position = 2;
            // add empty cells
            while (position != calendar.get(Calendar.DAY_OF_WEEK)) {
                position = (position % 7) + 1;
                table.addCell("");
            }
            // add cells for each day
            while (day <= daysInMonth) {
                table.addCell(getDayCell(calendar, locale));
                day++;
                calendar.set(Calendar.DAY_OF_MONTH, day);
            }
            // complete the table
            table.completeRow();
            // write the table to an absolute position
            float height = table.getTotalHeight() + 18;
            table.writeSelectedRows(0, -1, 169, height, canvas);
            
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            table = new PdfPTable(1);
            table.setTotalWidth(300);
            table.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
            int i = 0;
            while (i < holidays.size()) {
                if (calendar.get(Calendar.MONTH)+1 == holidays.get(i).getMonth()) {
                    calendar.set(Calendar.DAY_OF_MONTH, holidays.get(i).getDay());
                    table.addCell(getHolidaysCell(locale, calendar, i)); 
                }
                i++;
            }
            // complete the table
            table.completeRow();
            // write the table to an absolute position
            table.writeSelectedRows(0, -1, 50, height + table.getTotalHeight() + 18, canvas);
            
            document.newPage();
        }
        // step 5
        document.close();
    }

    /**
     * Draws the image of the month to the calendar.
     *
     * @param canvas the direct content layer
     * @param calendar the month (to know which picture to use)
     * @throws DocumentException
     * @throws IOException
     */
    public void drawImage(PdfContentByte canvas, Calendar calendar) throws DocumentException, IOException {
        // get the image
        Image img = Image.getInstance(String.format(RESOURCE, calendar));
        img.scaleToFit(PageSize.A4.getHeight(), PageSize.A4.getWidth());
        img.setAbsolutePosition(
                (PageSize.A4.getHeight() - img.getScaledWidth()) / 2,
                (PageSize.A4.getWidth() - img.getScaledHeight()) / 2);
        canvas.addImage(img);
        // add metadata
        canvas.saveState();
        canvas.setCMYKColorFill(0x00, 0x00, 0x00, 0x80);
        canvas.restoreState();
    }

    /**
     * Creates a PdfPCell with the name of the month
     *
     * @param calendar a date
     * @param locale a locale
     * @return a PdfPCell with rowspan 7, containing the name of the month
     */
    public PdfPCell getMonthCell(Calendar calendar, Locale locale) {
        PdfPCell cell = new PdfPCell();
        cell.setColspan(7);
        cell.setBackgroundColor(BaseColor.WHITE);
        cell.setUseDescender(true);
        String year = String.format(locale, "%1$tY", calendar);
        String month = goodUkrainian(String.format(locale, "%1$tB", calendar));
        String text = month + " " + year;
        Paragraph p = new Paragraph(text, bold);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        return cell;
    }

    /**
     * Creates a PdfPCell for a specific day
     *
     * @param calendar a date
     * @param locale a locale
     * @return a PdfPCell
     */
    public PdfPCell getDayCell(Calendar calendar, Locale locale) {
        PdfPCell cell = new PdfPCell();
        cell.setPadding(3);
        // set the background color, based on the type of day
        if (isSpecialDay(calendar)) {
            cell.setBackgroundColor(BaseColor.YELLOW);
        } else if (isHoliday(calendar)) {
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        } else {
            cell.setBackgroundColor(BaseColor.WHITE);
        }
        // set the content in the language of the locale
        Chunk chunk = new Chunk(String.format(locale, "%1$ta", calendar), small);
        chunk.setTextRise(8);
        // a paragraph with the day
        Paragraph p = new Paragraph(chunk);
        // a separator
        p.add(new Chunk(new VerticalPositionMark()));
        // and the number of the day
        p.add(new Chunk(String.format("%1$te", calendar), normal));
        cell.addElement(p);
        return cell;
    }

    /**
     * Creates a PdfPCell with the name of the month
     *
     * @param index
     * @return a PdfPCell with rowspan 7, containing the name of the month
     */
    public PdfPCell getHolidaysCell(Locale locale, Calendar calendar, int index) {
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(BaseColor.WHITE);
        cell.setUseDescender(true);
        Date holiday = holidays.get(index);
        String text = String.format(locale, "%1$te %1$tB", calendar) + " - " + holiday.getName();
        Paragraph p = new Paragraph(text, bold);
        p.setAlignment(Element.ALIGN_LEFT);
        cell.addElement(p);
        return cell;
    }

    /**
     * Returns true if the date was holiday (holidays).
     *
     * @param calendar a date
     * @return true for holidays
     */
    public boolean isHoliday(Calendar calendar) {
        if ((calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) || (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)) {
            return true;
        }
        return false;
    }

    /**
     * Returns true if the date was found in a list with special days
     * (holidays).
     *
     * @param calendar a date
     * @return true for special days
     */
    public boolean isSpecialDay(Calendar calendar) {
        for (int i = 0; i < dates.size(); i++) {
            if ((dates.get(i).getMonth() == calendar.get(Calendar.MONTH) + 1) && (dates.get(i).getDay() == calendar.get(Calendar.DAY_OF_MONTH))) {
                dates.remove(i);
                return true;
            }
        }
        return false;
    }
    
    public String goodUkrainian(String s) {
        char[] c = s.toCharArray();
        String buff = "";
        if (c[c.length-1] == "я".toCharArray()[0]) {
            buff = s.substring(0, c.length-2);
            buff += "ень";
        } else if (c[c.length-1] == "о".toCharArray()[0]) {
            buff = s.substring(0, c.length-3);
            buff += "ий";
        } else if (c[c.length-1] == "а".toCharArray()[0]){
            buff = s.substring(0, c.length-1);
        }
        return buff.substring(0, 1).toUpperCase() + buff.substring(1);
    }
}
