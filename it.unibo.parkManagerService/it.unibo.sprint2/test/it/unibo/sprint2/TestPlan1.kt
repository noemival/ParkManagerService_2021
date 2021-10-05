package it.unibo.sprint2
import org.junit.Assert.*
import org.junit.Test
import org.junit.Before
import org.junit.Assert.*
import Slotnum
class TestPlan1 {
	
    @Before
    fun setUp() {
        for (i in 1..6){
            Slotnum.uptadeSlotnum(i, false)
        }
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
	
    @Test
	fun checkSlotNumEqualsCarSlot(){
		var slotNum =  1
		var tokenID = Slotnum.generateTOKENID(slotNum)
		var carSlot = Slotnum.findSlot(tokenID)
		assertEquals(carSlot, slotNum)
	}
}
