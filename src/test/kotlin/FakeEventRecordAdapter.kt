class FakeEventRecordAdapter: EventRecordAdapter {
    var eventName: String = ""
        private set
    var properties: Map<String, Any> = emptyMap()
        private set

    override fun track(event: String, properties: Map<String, Any>) {
        eventName = event
        this.properties = properties
    }
}