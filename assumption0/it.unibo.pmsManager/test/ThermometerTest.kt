import JFlex.Options.time
import connQak.connQakBase
import connQak.connQakCoap
import org.junit.After
import org.junit.Before
import org.junit.Test
import connQak.connQakTcp
import it.unibo.kactor.ApplMessage
import org.junit.Assert.*
import org.tigris.subversion.javahl.NodeKind.none
import it.unibo.coap.CoapSupport
class ThermometerTest {

    @Before
    fun setUp() {


    }

    @After
    fun tearDown() {
    }

    @Test
    fun getInitialState() {
    }

    @Test
    fun fanStop() {


        val con = connQakTcp()
        val msg = ApplMessage("temperature", "event", "tester", "none","temperature(30)","2")
        con.createConnection()
        con.emit( msg)
             Thread.sleep(2000)
        val  coapSupport = CoapSupport("coap://localhost:8050", "ctxpm/fan")
      //  val coapSupport= connQakCoap()
     //   coapSupport.createConnection()
        con.emit( ApplMessage("temperature", "event", "tester", "none","temperature(30)","2"))

      //  coapSupport.updateResource()
       assertEquals("fanstop", coapSupport.readResource())

    }
    @Test
    fun fanWork() {


        val con = connQakTcp()
        val msg = ApplMessage("temperature", "event", "tester", "none","temperature(40)","2")
        con.createConnection()
        con.emit( msg)
        Thread.sleep(2000)
        val  coapSupport = CoapSupport("coap://localhost:8050", "ctxpm/fan")
       // val coapSupport= connQakCoap()
       // coapSupport.createConnection()
        con.emit( ApplMessage("temperature", "event", "tester", "none","temperature(30)","2"))

        //  coapSupport.updateResource()
         assertEquals("fanwork", coapSupport.readResource())

    }
}