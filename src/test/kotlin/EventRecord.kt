import annotation.EventKey
import annotation.EventName
import annotation.EventNameField

interface EventRecord {
    @EventName("Visit {page}")
    fun event001(
        @EventNameField("page") page: String,
        @EventKey("username") uname: String,
        @EventKey("count") counter: Int,
        @EventKey("is paid member") isPaidMember: Boolean
    )
}