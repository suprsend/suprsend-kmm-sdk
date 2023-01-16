package app.suprsend

expect class Platform() {
    val platform: String
}

expect class Information() {
    fun getDefaultProperties(): String
}
