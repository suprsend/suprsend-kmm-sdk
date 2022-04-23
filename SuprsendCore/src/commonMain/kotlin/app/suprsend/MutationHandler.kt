package app.suprsend

interface MutationHandler {
    fun isFlushing(): Boolean
    fun setFlushing(value: Boolean)
}
