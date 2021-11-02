package app.suprsend.android

class AndroidMutationHandler : MutationHandler {
    var flush = false
    override fun isFlushing(): Boolean = flush

    override fun setFlushing(value: Boolean) {
        flush = value
    }

}
