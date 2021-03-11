import annotation.EventKey
import annotation.EventName
import annotation.EventNameField
import java.lang.IllegalArgumentException
import java.lang.reflect.Method
import java.lang.reflect.Proxy
import kotlin.reflect.KClass

class Flast(private val eventRecordAdapter: EventRecordAdapter) {
    fun <T> create(eventRecord: Class<T>): T {
        return Proxy.newProxyInstance(
            eventRecord.classLoader,
            arrayOf(eventRecord),
        ) { _, method, args ->
            parseParameters(method, args).let {
                eventRecordAdapter.track(it.first, it.second)
            }
        } as T
    }

    fun <T : Any> create(eventRecord: KClass<T>) = create(eventRecord.java)

    fun parseParameters(method: Method, args: Array<Any>): Pair<String, Map<String, Any>> {
        val eventName = method.annotations.find { it is EventName }
        if (eventName == null) {
            throw IllegalArgumentException("Event record must contains @EventName annotation")
        } else {
            eventName as EventName
        }
        var eventNameValue = ""
        val eventNameFields = method.parameterAnnotations
            .mapIndexed { index, annotations ->  index to annotations[0]}
            .filter { it.second is EventNameField }
            .map { Pair(it.first, it.second as EventNameField) }
        eventNameFields.forEach { field ->
            val replacement = eventName.value.replace(Regex.fromLiteral("{${field.second.value}}"), "${args[field.first]}")
            eventNameValue = replacement
        }

        val properties = method.parameterAnnotations
            .mapIndexed { index, annotations -> index to annotations[0] }
            .filter { it.second is EventKey }
            .map { Pair(it.first, it.second as EventKey) }
            .map {
                val key = it.second.value
                val value = args[it.first]
                key to value
            }.toMap()

        return Pair(eventNameValue, properties)
    }
}