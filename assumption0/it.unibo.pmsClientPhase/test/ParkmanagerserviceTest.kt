import org.junit.After
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*

class ParkmanagerserviceTest {

    @Before
    fun setUp() {
        for (i in 1..6){
            Slotnum.uptadeSlotnum(i, false)
        }
    }

    @After
    fun tearDown() {
    }

    @Test
    fun getInitialState() {
    }

    @Test
    fun slotnumOccupied() {
        assertEquals(0, Slotnum.getSlotnum())

    }
    @Test
    fun slot5Free() {
        Slotnum.uptadeSlotnum(5, true)
        assertEquals(5, Slotnum.getSlotnum())
    }
}