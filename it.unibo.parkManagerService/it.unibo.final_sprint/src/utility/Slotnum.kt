 object Slotnum {
    var indoorfree = true
    var slotStateFree = booleanArrayOf(true, false, true, true, true, true)

    /* public Slotnum() {
    }
*/
     fun uptadeSlotnum( num: Int, bool: Boolean  ){
         slotStateFree[num-1]= bool
     }
	 fun slotfree(num : Int): Boolean {
     
		   return slotStateFree[num - 1] 

    }
    fun getSlotnum(): Int {
        var slot = 0
        for (i in 1..6) {
            if (slotStateFree[i - 1] == true) {
                slot = i
                break
            }
        }
        return slot
    }

	 
	fun findSlot( TOKENID: String) : Int{		
		 var slot =Character.getNumericValue( TOKENID.get(0))
		 return slot
		}
	 
	 fun generateTOKENID(SLOTNUM: Int): String{
		 var TIMESTAMP = System.currentTimeMillis().toString()
		 var TOKENID =	SLOTNUM.toString().plus(TIMESTAMP)

		return TOKENID
		}

}