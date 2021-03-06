package app.suprsend.android

expect class Platform() {
    val platform: String
}

expect class Information() {
    fun getDefaultProperties(): String
    fun isFlushing(): Boolean
    fun setFlushing(value: Boolean)
}
