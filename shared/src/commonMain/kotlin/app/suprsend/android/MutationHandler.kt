package app.suprsend.android

interface MutationHandler {
    fun isFlushing():Boolean
    fun setFlushing(value:Boolean)
}