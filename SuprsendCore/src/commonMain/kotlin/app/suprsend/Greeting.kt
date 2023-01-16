package app.suprsend

class Greeting {
    fun greeting(): String {
        return "Hello, ${Platform().platform}!"
    }
}
