package com.nutricampus.app;

import com.nutricampus.app.utils.Conversor;

import org.junit.Test;

import java.util.Calendar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by kellison on 20/07/17.
 */
public class ConversorTesteUnitario {
    @Test
    public void booleanToInt() throws Exception {

        assertEquals(0, Conversor.booleanToInt(false));
        assertEquals(1, Conversor.booleanToInt(true));
        assertFalse(1 == Conversor.booleanToInt(false));
    }

    @Test
    public void intToBoolean() throws Exception {

        assertTrue(Conversor.intToBoolean(1));
        assertFalse(Conversor.intToBoolean(0));
        assertFalse(Conversor.intToBoolean(2));
    }

    @Test
    public void stringToBoolean() throws Exception {
        assertTrue(Conversor.StringToBoolean("true"));
        assertFalse(Conversor.StringToBoolean("false"));
    }

    @Test
    public void stringToDate() throws Exception {
        Calendar cal = Calendar.getInstance();
        String data1 = "10/08/1992";

        cal.setTimeInMillis(Conversor.StringToDate(data1).getTime());

        int month = cal.get(Calendar.MONTH);

        assertEquals(month, 7);
    }

    @Test
    public void dataFormatada() throws Exception {
        Calendar cal = Calendar.getInstance();

        cal.set(2015, 10, 22);
        assertTrue(("22/11/2015".equals(Conversor.dataFormatada(cal))));

        cal.set(2000, 0, 02);
        assertTrue(("02/01/2000".equals(Conversor.dataFormatada(cal))));
    }

    @Test
    public void mesParaNumero() throws Exception {

        assertEquals(0, Conversor.mesStringParaInt("janeiro"));
        assertEquals(0, Conversor.mesStringParaInt("JAneirO"));
        assertEquals(12, Conversor.mesStringParaInt("stringqualquer"));
    }

}