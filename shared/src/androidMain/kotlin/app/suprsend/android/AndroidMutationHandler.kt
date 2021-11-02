package app.suprsend.android

class AndroidMutationHandler : MutationHandler {
    var flush = false
    override var isFlushing: Boolean = flush
}
