package app.suprsend.android.user

internal interface UserRepositoryContract {
    fun identify(uniqueId: String)
    fun getIdentity(): String
}
