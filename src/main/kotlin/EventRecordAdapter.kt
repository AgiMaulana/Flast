interface EventRecordAdapter {
    fun track(event: String, properties: Map<String, Any>)
}