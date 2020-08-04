import org.junit.Before;
import org.junit.Test;

import ir.ac.kntu.Technical.Other.Other.Helper;
import saman.zamani.persiandate.PersianDate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class HelperTest {
    private Helper helper;

    @Before
    public void preInitialize() {
        helper = Helper.getInstance();
    }

    @Test
    public void toCamelCase() {
        String str = "hello mohammad, i am fine.";
        assertEquals("Hello Mohammad, I Am Fine.", helper.toCamelCase(str));
        assertNull(helper.toCamelCase(null));
        assertEquals("", helper.toCamelCase(""));
    }

    @Test
    public void getOneDigitOrNon() {
        assertEquals("1.22", helper.getOneDigitOrNon(1.223, false));
        assertEquals("123.46", helper.getOneDigitOrNon(123456, true));
    }

    @Test
    public void testGetOneDigitOrNon() {
        assertEquals("1.23", helper.getOneDigitOrNon(1.23f, false));
        assertEquals("123.45", helper.getOneDigitOrNon(123452f, true));
    }

    @Test
    public void isRightName() {
        assertFalse(helper.isRightName(null));
        assertFalse(helper.isRightName(""));
        assertFalse(helper.isRightName("%محمد-"));
        assertTrue(helper.isRightName("محمد"));
    }

    @Test
    public void isRightPhone() {
        assertFalse(helper.isRightPhone(null));
        assertFalse(helper.isRightPhone(""));
        assertFalse(helper.isRightPhone("9"));
        assertFalse(helper.isRightPhone("912345678"));
        assertFalse(helper.isRightPhone("91234 5678"));
        assertTrue(helper.isRightPhone("9123456789"));
    }

    @Test
    public void toLowerCase() {
        assertEquals("", helper.toLowerCase(null));
        assertEquals("hello", helper.toLowerCase("HeLLO"));
    }

    @Test
    public void getShamsiDateFromString() {
        PersianDate persianDate = new PersianDate();
        persianDate.setShDay(22);
        persianDate.setShMonth(11);
        persianDate.setShYear(1397);
        persianDate.setHour(15);
        persianDate.setMinute(19);
        try {
            PersianDate persianDate1 = helper.getShamsiDateFromString("1397/11/22 15:19", "yyyy/MM/dd HH:mm");
            assertThrows(Exception.class, () -> helper.getShamsiDateFromString("1397+/11/22 15:19", "yyyy/MM/dd HH:mm"));
            assertEquals(persianDate.getShYear(), persianDate1.getShYear());
            assertEquals(persianDate.getShMonth(), persianDate1.getShMonth());
            assertEquals(persianDate.getShDay(), persianDate1.getShDay());
            assertEquals(persianDate.getHour(), persianDate1.getHour());
            assertEquals(persianDate.getMinute(), persianDate1.getMinute());
        } catch (Exception e) {
        }
    }

    @Test
    public void getCostCeilOf() {
        assertEquals(123125, helper.getCostCeilOf(123124.23));
    }

    @Test
    public void get2DigitsOfDigit() {
        assertEquals("-1", helper.get2DigitsOfDigit(-5));
        assertEquals("06", helper.get2DigitsOfDigit(6));
        assertEquals("16", helper.get2DigitsOfDigit(16));
    }

    @Test
    public void stringToPersianDateTime() {
        PersianDate persianDate = helper.stringToPersianDateTime("1397/08/22 14:26");
        assertNull(helper.stringToPersianDateTime("1397/08/22 14:s26"));
        assertEquals(1397, persianDate.getShYear());
        assertEquals(8, persianDate.getShMonth());
        assertEquals(22, persianDate.getShDay());
        assertEquals(14, persianDate.getHour());
        assertEquals(26, persianDate.getMinute());
    }

    @Test
    public void getMinuteFromSecond() {
        assertEquals(14, helper.getMinuteFromSecond(14 * 60 * 1000));
        assertEquals(-1, helper.getMinuteFromSecond(-14 * 60 * 1000));
    }

    @Test
    public void getSecondFromSeconds() {
        assertEquals(21, helper.getSecondFromSeconds(21 * 1000));
        assertEquals(1, helper.getSecondFromSeconds(61 * 1000));
        assertEquals(-1, helper.getSecondFromSeconds(-14 * 1000));
    }

    @Test
    public void generateRandomNumber() {
        int ranNum = helper.generateRandomNumber(10, 500);
        assertTrue(ranNum >= 10);
        assertTrue(ranNum <= 500);
    }

    @Test
    public void containsNonPersianLanguage() {
        assertFalse(helper.containsNonPersianLanguage(null));
        assertFalse(helper.containsNonPersianLanguage("بله"));
        assertTrue(helper.containsNonPersianLanguage("yes"));
        assertTrue(helper.containsNonPersianLanguage("yesبله"));
    }

    @Test
    public void isInteger() {
        assertFalse(helper.isInteger(null, 10));
        assertFalse(helper.isInteger("", 10));
        assertFalse(helper.isInteger("1234", -5));
        assertTrue(helper.isInteger("1234", 10));
    }

    @Test
    public void isPrime() {
        assertFalse(helper.isPrime(-17));
        assertFalse(helper.isPrime(0));
        assertFalse(helper.isPrime(4));
        assertTrue(helper.isPrime(17));
        assertTrue(helper.isPrime(2));
    }
}
