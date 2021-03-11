import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class FlastTest {
    private lateinit var fakeEventRecordAdapter: FakeEventRecordAdapter
    private lateinit var flast: Flast
    private lateinit var eventRecord: EventRecord

    @BeforeTest
    fun setup() {
        fakeEventRecordAdapter = FakeEventRecordAdapter()
        flast = Flast(fakeEventRecordAdapter)
        eventRecord = flast.create(EventRecord::class.java)
    }

    @Test
    fun test_trackAnEvent() {
        eventRecord.event001("Login", "TestUser", 9, false)

        assertEquals("Visit Login", fakeEventRecordAdapter.eventName)
        assertEquals("TestUser", fakeEventRecordAdapter.properties["username"])
        assertEquals(9, fakeEventRecordAdapter.properties["count"])
        assertEquals(false, fakeEventRecordAdapter.properties["is paid member"])
    }
}